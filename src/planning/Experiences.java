package planning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modelling.Variable;

/**
 * Exercice 12 : compare le nombre de nœuds explorés par les différents planificateurs
 * sur un même problème : aller d'un coin à l'autre d'une grille avec des murs.
 * Une case est un couple (x, y) ; les actions déplacent d'une case (coût 1).
 */
public class Experiences {

    /** Heuristique : distance de Manhattan jusqu'à la case but (admissible ici). */
    private static class Manhattan implements Heuristic {
        private Variable x;
        private Variable y;
        private int butX;
        private int butY;

        Manhattan(Variable x, Variable y, int butX, int butY) {
            this.x = x;
            this.y = y;
            this.butX = butX;
            this.butY = butY;
        }

        @Override
        public float estimate(Map<Variable, Object> state) {
            int px = (Integer) state.get(x);
            int py = (Integer) state.get(y);
            return Math.abs(butX - px) + Math.abs(butY - py);
        }
    }

    /** Heuristique nulle (A* se comporte alors comme Dijkstra). */
    private static class Nulle implements Heuristic {
        @Override
        public float estimate(Map<Variable, Object> state) {
            return 0;
        }
    }

    public static void main(String[] args) {
        int taille = 10;

        Set<Object> domaine = new HashSet<>();
        for (int i = 0; i < taille; i++) {
            domaine.add(i);
        }
        Variable x = new Variable("x", domaine);
        Variable y = new Variable("y", domaine);

        // Cases murées : un mur vertical avec une ouverture en bas
        boolean[][] mur = new boolean[taille][taille];
        for (int j = 1; j < taille; j++) {
            mur[5][j] = true;
        }

        // Création des actions : pour chaque case libre et chaque direction
        Set<Action> actions = new HashSet<>();
        int[] dx = { 1, -1, 0, 0 };
        int[] dy = { 0, 0, 1, -1 };
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (mur[i][j]) {
                    continue;
                }
                for (int d = 0; d < 4; d++) {
                    int ni = i + dx[d];
                    int nj = j + dy[d];
                    if (ni >= 0 && ni < taille && nj >= 0 && nj < taille && !mur[ni][nj]) {
                        Map<Variable, Object> pre = new HashMap<>();
                        pre.put(x, i);
                        pre.put(y, j);
                        Map<Variable, Object> eff = new HashMap<>();
                        eff.put(x, ni);
                        eff.put(y, nj);
                        actions.add(new BasicAction(pre, eff, 1));
                    }
                }
            }
        }

        Map<Variable, Object> init = new HashMap<>();
        init.put(x, 0);
        init.put(y, taille - 1);

        Map<Variable, Object> but = new HashMap<>();
        but.put(x, taille - 1);
        but.put(y, taille - 1);
        Goal goal = new BasicGoal(but);

        // Planificateurs
        DFSPlanner dfs = new DFSPlanner(init, actions, goal);
        BFSPlanner bfs = new BFSPlanner(init, actions, goal);
        DijkstraPlanner dijkstra = new DijkstraPlanner(init, actions, goal);
        AStarPlanner astarNul = new AStarPlanner(init, actions, goal, new Nulle());
        AStarPlanner astarManhattan = new AStarPlanner(init, actions, goal,
                new Manhattan(x, y, taille - 1, taille - 1));

        dfs.activateNodeCount(true);
        bfs.activateNodeCount(true);
        dijkstra.activateNodeCount(true);
        astarNul.activateNodeCount(true);
        astarManhattan.activateNodeCount(true);

        System.out.println("Grille " + taille + "x" + taille + ", " + actions.size() + " actions");
        List<Action> p;

        p = dfs.plan();
        System.out.println("DFS              : plan de " + p.size() + " actions, " + dfs.getNodeCount() + " noeuds explores");
        p = bfs.plan();
        System.out.println("BFS              : plan de " + p.size() + " actions, " + bfs.getNodeCount() + " noeuds explores");
        p = dijkstra.plan();
        System.out.println("Dijkstra         : plan de " + p.size() + " actions, " + dijkstra.getNodeCount() + " noeuds explores");
        p = astarNul.plan();
        System.out.println("A* (h = 0)       : plan de " + p.size() + " actions, " + astarNul.getNodeCount() + " noeuds explores");
        p = astarManhattan.plan();
        System.out.println("A* (Manhattan)   : plan de " + p.size() + " actions, " + astarManhattan.getNodeCount() + " noeuds explores");
    }
}
