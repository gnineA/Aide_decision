
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modelling.Constraint;
import modelling.Variable;

/**
 * Solveur MAC dont l'ordre des variables et des valeurs est dicté par des heuristiques.
 */
public class HeuristicMACSolver extends AbstractSolver {

    private ArcConsistency arcConsistency;
    private VariableHeuristic variableHeuristic;
    private ValueHeuristic valueHeuristic;

    public HeuristicMACSolver(Set<Variable> variables, Set<Constraint> constraints,
            VariableHeuristic variableHeuristic, ValueHeuristic valueHeuristic) {
        super(variables, constraints);
        this.arcConsistency = new ArcConsistency(constraints);
        this.variableHeuristic = variableHeuristic;
        this.valueHeuristic = valueHeuristic;
    }

    @Override
    public Map<Variable, Object> solve() {
        Map<Variable, Set<Object>> domains = initialDomains();
        if (!arcConsistency.ac1(domains)) {
            return null;
        }
        return search(new HashMap<>(), new HashSet<>(variables), domains);
    }

    private Map<Variable, Object> search(Map<Variable, Object> partial, Set<Variable> unassigned,
            Map<Variable, Set<Object>> domains) {
        if (unassigned.isEmpty()) {
            return partial;
        }
        // L'heuristique choisit la variable, puis l'ordre de ses valeurs
        Variable var = variableHeuristic.best(unassigned, domains);
        Set<Variable> rest = new HashSet<>(unassigned);
        rest.remove(var);
        List<Object> values = valueHeuristic.ordering(var, domains.get(var));

        for (Object value : values) {
            Map<Variable, Object> next = new HashMap<>(partial);
            next.put(var, value);
            if (isConsistent(next)) {
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
