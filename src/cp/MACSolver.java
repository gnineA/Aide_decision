
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modelling.Constraint;
import modelling.Variable;

/**
 * Solveur MAC (Maintaining Arc Consistency) : retour arrière, avec filtrage des domaines
 * par cohérence d'arc (AC1) avant la recherche et après chaque affectation.
 */
public class MACSolver extends AbstractSolver {

    private ArcConsistency arcConsistency;

    public MACSolver(Set<Variable> variables, Set<Constraint> constraints) {
        super(variables, constraints);
        arcConsistency = new ArcConsistency(constraints);
    }

    @Override
    public Map<Variable, Object> solve() {
        Map<Variable, Set<Object>> domains = initialDomains();
        if (!arcConsistency.ac1(domains)) {
            return null;
        }
        return search(new HashMap<>(), new ArrayList<>(variables), domains);
    }

    private Map<Variable, Object> search(Map<Variable, Object> partial, List<Variable> unassigned,
            Map<Variable, Set<Object>> domains) {
        if (unassigned.isEmpty()) {
            return partial;
        }
        Variable var = unassigned.get(0);
        List<Variable> rest = unassigned.subList(1, unassigned.size());
        // On parcourt une copie du domaine filtré de var
        for (Object value : new ArrayList<>(domains.get(var))) {
            Map<Variable, Object> next = new HashMap<>(partial);
            next.put(var, value);
            if (isConsistent(next)) {
                // Nouveaux domaines : var n'a plus que la valeur choisie, puis filtrage
                Map<Variable, Set<Object>> newDomains = copyDomains(domains);
                Set<Object> single = new HashSet<>();
                single.add(value);
                newDomains.put(var, single);
                if (arcConsistency.ac1(newDomains)) {
                    Map<Variable, Object> result = search(next, rest, newDomains);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }
}
