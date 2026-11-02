
import java.util.List;
import java.util.Set;

import modelling.Variable;

/**
 * Heuristique d'ordre d'essai des valeurs d'une variable.
 */
public interface ValueHeuristic {

    /** Retourne les valeurs du domaine, la meilleure en premier. */
    List<Object> ordering(Variable variable, Set<Object> domain);
}
