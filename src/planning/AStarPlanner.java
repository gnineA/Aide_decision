package planning;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import modelling.Variable;

/**
 * Planificateur A* : retourne un plan de coût minimal si l'heuristique est admissible.
 */
public class AStarPlanner implements Planner {

    private Map<Variable, Object> initialState;
    private Set<Action> actions;
    private Goal goal;
    private Heuristic heuristic;

    // Sonde : nombre de nœuds explorés
    private boolean countActivated = false;
    private int nodeCount = 0;
    
    /** Un nœud de la file : état, coût g du chemin trouvé, et priorité f = g + h. */
    private static class Noeud implements Comparable<Noeud> {
        Map<Variable, Object> etat;
        int g;
        float f;

        Noeud(Map<Variable, Object> etat, int g, float f) {
            this.etat = etat;
            this.g = g;
            this.f = f;
        }

        @Override
        public int compareTo(Noeud autre) {
            return Float.compare(f, autre.f);
        }
    }

  
    

    public AStarPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal,
            Heuristic heuristic) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
        this.heuristic = heuristic;
    }

    @Override
    public List<Action> plan() {
        nodeCount = 0;
        Map<Map<Variable, Object>, Integer> meilleurCout = new HashMap<>();
        Map<Map<Variable, Object>, Map<Variable, Object>> parent = new HashMap<>();
        Map<Map<Variable, Object>, Action> actionMenant = new HashMap<>();
        PriorityQueue<Noeud> file = new PriorityQueue<>();

        meilleurCout.put(initialState, 0);
        file.add(new Noeud(initialState, 0, heuristic.estimate(initialState)));

        while (!file.isEmpty()) {
            Noeud noeud = file.poll();
            // Nœud périmé : un meilleur chemin vers cet état a été trouvé depuis
            if (noeud.g > meilleurCout.get(noeud.etat)) {
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
                    int nouveauG = noeud.g + a.getCost();
                    Integer ancienG = meilleurCout.get(suivant);
                    if (ancienG == null || nouveauG < ancienG) {
                        meilleurCout.put(suivant, nouveauG);
                        parent.put(suivant, noeud.etat);
                        actionMenant.put(suivant, a);
                        file.add(new Noeud(suivant, nouveauG, nouveauG + heuristic.estimate(suivant)));
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
