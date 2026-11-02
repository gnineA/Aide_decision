
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modelling.Constraint;
import modelling.Variable;

/**
 * Solveur par retour arrière (backtrack) : on affecte les variables une par une
 * et on revient en arrière dès qu'une contrainte est violée.
 */
public class BacktrackSolver extends AbstractSolver {

    public BacktrackSolver(Set<Variable> variables, Set<Constraint> constraints) {
        super(variables, constraints);
    }

    @Override
    public Map<Variable, Object> solve() {
        return search(new HashMap<>(), new ArrayList<>(variables));
    }

    /**
     * Étend la solution partielle en affectant les variables non instanciées.
     * Retourne une solution complète, ou null si c'est impossible.
     */
    private Map<Variable, Object> search(Map<Variable, Object> partial, List<Variable> unassigned) {
        if (unassigned.isEmpty()) {
            return partial; // toutes les variables sont affectées
        }
        Variable var = unassigned.get(0);
        List<Variable> rest = unassigned.subList(1, unassigned.size());
        for (Object value : var.getDomain()) {
            Map<Variable, Object> next = new HashMap<>(partial);
            next.put(var, value);
            if (isConsistent(next)) {
                Map<Variable, Object> result = search(next, rest);
                if (result != null) {
                    return result;
                }
            }
        }
        return null; // aucune valeur ne convient pour var
    }
}
