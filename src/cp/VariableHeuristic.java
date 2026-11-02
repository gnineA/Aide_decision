import java.util.Map;
import java.util.Set;

import modelling.Variable;

/**
 * Heuristique de choix de la prochaine variable à affecter.
 */
public interface VariableHeuristic {

    /** Retourne la meilleure variable de l'ensemble, selon l'heuristique. */
    Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains);
}
