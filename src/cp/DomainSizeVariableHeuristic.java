package cp;

import java.util.Map;
import java.util.Set;

import modelling.Variable;

/**
 * Choisit la variable ayant le plus grand (ou le plus petit) domaine.
 */
public class DomainSizeVariableHeuristic implements VariableHeuristic {

    private boolean preferLarger; // true : plus grand domaine ; false : plus petit

    public DomainSizeVariableHeuristic(boolean preferLarger) {
        this.preferLarger = preferLarger;
    }

    @Override
    public Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains) {
        Variable best = null;
        int bestSize = 0;
        for (Variable v : variables) {
            int size = domains.get(v).size();
            boolean better = (best == null)
                    || (preferLarger && size > bestSize)
                    || (!preferLarger && size < bestSize);
            if (better) {
                best = v;
                bestSize = size;
            }
        }
        return best;
    }
}
