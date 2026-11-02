package planning;
import java.util.List;
import java.util.Map;
import java.util.Set;
import modelling.Variable;

public interface Planner {
    /** Retourne un plan, ou null s'il n'y en a pas. */
    List<Action> plan();
    Map<Variable, Object> getInitialState();
    Set<Action> getActions();
    Goal getGoal();
}
