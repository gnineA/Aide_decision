package planning;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modelling.Variable;

/**
  Planificateur par recherche en profondeur d'abord (avec mémorisation des états visités).
  Le plan retourné n'est pas forcément le plus court.
 */
public class DFSPlanner implements Planner {

    private Map<Variable, Object> initialState;
    private Set<Action> actions;
    private Goal goal;

    // Sonde : nombre de nœuds explorés
    private boolean countActivated = false;
    private int nodeCount = 0;

    public DFSPlanner(Map<Variable, Object> initialState, Set<Action> actions, Goal goal) {
        this.initialState = initialState;
        this.actions = actions;
        this.goal = goal;
    }

    @Override
    public List<Action> plan() {
        nodeCount = 0;
        Set<Map<Variable, Object>> visited = new HashSet<>();
        return search(initialState, visited);
    }

    /** Méthode récursive : cherche un plan depuis l'état donné. */
    private List<Action> search(Map<Variable, Object> state, Set<Map<Variable, Object>> visited) {
        visited.add(state);
        if (countActivated) {
            nodeCount++;
        }
        if (goal.isSatisfiedBy(state)) {
            return new LinkedList<Action>(); // plan vide
        }
        for (Action a : actions) {
            if (a.isApplicable(state)) {
                Map<Variable, Object> suivant = a.successor(state);
                if (!visited.contains(suivant)) {
                    LinkedList<Action> reste = (LinkedList<Action>) search(suivant, visited);
                    if (reste != null) {
                        reste.addFirst(a);
                        return reste;
                    }
                }
            }
        }
        return null; // aucun plan depuis cet état
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
