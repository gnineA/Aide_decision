package cp;

import java.util.Map;
import java.util.Set;

import modelling.Constraint;
import modelling.Variable;

/**
 * Choisit la variable apparaissant dans le plus (ou le moins) de contraintes.
 */
public class NbConstraintsVariableHeuristic implements VariableHeuristic {

    private Set<Constraint> constraints;
    private boolean preferMore; // true : le plus de contraintes ; false : le moins

    public NbConstraintsVariableHeuristic(Set<Constraint> constraints, boolean preferMore) {
        this.constraints = constraints;
        this.preferMore = preferMore;
    }

    @Override
    public Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains) {
        Variable best = null;
        int bestCount = 0;
        for (Variable v : variables) {
            int count = countConstraints(v);
            boolean better = (best == null)
                    || (preferMore && count > bestCount)
                    || (!preferMore && count < bestCount);
            if (better) {
                best = v;
                bestCount = count;
            }
        }
        return best;
    }

    /** Nombre de contraintes dont le scope contient la variable. */
    private int countConstraints(Variable v) {
        int count = 0;
        for (Constraint c : constraints) {
            if (c.getScope().contains(v)) {
                count++;
            }
        }
        return count;
    }
}
