package planning;

import java.util.Map;

import modelling.Variable;

/**
 But basique : spécifié par une instanciation partielle des variables.
 */
public class BasicGoal implements Goal {

    private Map<Variable, Object> instanciation;

    public BasicGoal(Map<Variable, Object> instanciation) {
        this.instanciation = instanciation;
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> state) {
        for (Variable v : instanciation.keySet()) {
            if (!state.containsKey(v)) {
                return false;
            }
            if (!state.get(v).equals(instanciation.get(v))) {
                return false;
            }
        }
        return true;
    }
}
