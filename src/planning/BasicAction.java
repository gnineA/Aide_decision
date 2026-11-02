package planning;

import java.util.HashMap;
import java.util.Map;

import modelling.Variable;

/**
 * Action basique : précondition et effet sont des instanciations partielles.
 */
public class BasicAction implements Action {

    private Map<Variable, Object> precondition;
    private Map<Variable, Object> effect;
    private int cost;

    public BasicAction(Map<Variable, Object> precondition, Map<Variable, Object> effect, int cost) {
        this.precondition = precondition;
        this.effect = effect;
        this.cost = cost;
    }

    @Override
    public boolean isApplicable(Map<Variable, Object> state) {
        // Toutes les variables de la précondition doivent avoir la bonne valeur dans l'état
        for (Variable v : precondition.keySet()) {
            if (!state.containsKey(v)) {
                return false;
            }
            if (!state.get(v).equals(precondition.get(v))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<Variable, Object> successor(Map<Variable, Object> state) {
        Map<Variable, Object> nouvelEtat = new HashMap<>(state); // copie de l'état
        nouvelEtat.putAll(effect); // les variables de l'effet prennent la valeur prescrite
        return nouvelEtat;
    }

    @Override
    public int getCost() {
        return cost;
    }
}
