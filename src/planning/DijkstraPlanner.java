package planning;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import modelling.Variable;

/**
 Planificateur de type Dijkstra : retourne un plan de coût minimal
 (les coûts des actions doivent être positifs ou nuls).
 */
public class DijkstraPlanner implements Planner {

    /** Un nœud de la file de priorité : un état et le coût du chemin trouvé jusqu'à lui. */
    private static class Noeud implements Comparable<Noeud> {
        Map<Variable, Object> etat;
        int cout;

        Noeud(Map<Variable, Object> etat, int cout) {
            this.etat = etat;
            this.cout = cout;
        }

        @Override
        public int compareTo(Noeud autre) {
            return Integer.compare(cout, autre.cout);
        }
    }

    private Map<Variable, Object> initialState;
    private Set<Action> actions;
    private Goal goal;

    // Sonde : nombre de nœuds explorés
    private boolean countActivated = false;
    private int nodeCount = 0;

    public DijkstraPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
    }

    @Override
    public List<Action> plan() {
        nodeCount = 0;
        // Meilleur coût connu pour chaque état, avec son parent et l'action qui y mène
        Map<Map<Variable, Object>, Integer> meilleurCout = new HashMap<>();
        Map<Map<Variable, Object>, Map<Variable, Object>> parent = new HashMap<>();
        Map<Map<Variable, Object>, Action> actionMenant = new HashMap<>();
        PriorityQueue<Noeud> file = new PriorityQueue<>();

        meilleurCout.put(initialState, 0);
        file.add(new Noeud(initialState, 0));

        while (!file.isEmpty()) {
            Noeud noeud = file.poll();
            // Nœud périmé : on a trouvé mieux depuis son insertion dans la file
            if (noeud.cout > meilleurCout.get(noeud.etat)) {
                continue;
            }
            if (countActivated) {
                nodeCount++;
            }
            if (goal.isSatisfiedBy(noeud.etat)) {
                return buildPlan(noeud.etat, parent, actionMenant);
            }
            for (Action a : actions) {
                if (a.isApplicable(noeud.etat)) {
                    Map<Variable, Object> suivant = a.successor(noeud.etat);
                    int nouveauCout = noeud.cout + a.getCost();
                    Integer ancienCout = meilleurCout.get(suivant);
                    if (ancienCout == null || nouveauCout < ancienCout) {
                        meilleurCout.put(suivant, nouveauCout);
                        parent.put(suivant, noeud.etat);
                        actionMenant.put(suivant, a);
                        file.add(new Noeud(suivant, nouveauCout));
                    }
                }
            }
        }
        return null; // pas de plan
    }

    /** Remonte de l'état but jusqu'à l'état initial pour reconstruire le plan. */
    private List<Action> buildPlan(Map<Variable, Object> but,
            Map<Map<Variable, Object>, Map<Variable, Object>> parent,
            Map<Map<Variable, Object>, Action> actionMenant) {
        LinkedList<Action> plan = new LinkedList<>();
        Map<Variable, Object> courant = but;
        while (actionMenant.containsKey(courant)) {
            plan.addFirst(actionMenant.get(courant));
            courant = parent.get(courant);
        }
        return plan;
    }

    @Override
    public Map<Variable, Object> getInitialState() {
        return initialState;
    }

    @Override
    public Set<Action> getActions() {
        return actions;
    }

    @Override
    public Goal getGoal() {
        return goal;
    }

    /** Active ou désactive le comptage des nœuds explorés (désactivé par défaut). */
    public void activateNodeCount(boolean activate) {
        countActivated = activate;
    }

    /** Nombre de nœuds explorés lors de la dernière exécution de plan(). */
    public int getNodeCount() {
        return nodeCount;
    }
}
