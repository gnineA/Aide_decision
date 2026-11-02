package planning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import modelling.Variable;

/**
  Planificateur par recherche en largeur d'abord : retourne un plan ayant le moins d'actions.
 */
public class BFSPlanner implements Planner {

    private Map<Variable, Object> initialState;
    private Set<Action> actions;
    private Goal goal;

    // Sonde : nombre de nœuds explorés
    private boolean countActivated = false;
    private int nodeCount = 0;

    public BFSPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
    }

    @Override
    public List<Action> plan() {
        nodeCount = 0;
        // Pour chaque état découvert : son parent et l'action qui y mène
        Map<Map<Variable, Object>, Map<Variable, Object>> parent = new HashMap<>();
        Map<Map<Variable, Object>, Action> actionMenant = new HashMap<>();
        Set<Map<Variable, Object>> visited = new HashSet<>();
        Queue<Map<Variable, Object>> file = new LinkedList<>();

        visited.add(initialState);
        file.add(initialState);

        while (!file.isEmpty()) {
            Map<Variable, Object> state = file.poll();
            if (countActivated) {
                nodeCount++;
            }
            if (goal.isSatisfiedBy(state)) {
                return buildPlan(state, parent, actionMenant);
            }
            for (Action a : actions) {
                if (a.isApplicable(state)) {
                    Map<Variable, Object> suivant = a.successor(state);
                    if (!visited.contains(suivant)) {
                        visited.add(suivant);
                        parent.put(suivant, state);
                        actionMenant.put(suivant, a);
                        file.add(suivant);
                    }
                }
            }
        }
        return null; // pas de plan
    }

    /** Remonte des états but jusqu'à l'état initial pour reconstruire le plan. */
    private List<Action> buildPlan(Map<Variable, Object> but,Map<Map<Variable, Object>, Map<Variable, Object>> parent,Map<Map<Variable, Object>, Action> actionMenant) {
        
        LinkedList<Action> plan = new LinkedList<>();
        Map<Variable, Object> courant = but;
        while (actionMenant.containsKey(courant)) { // l'état initial n'a pas d'action
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
