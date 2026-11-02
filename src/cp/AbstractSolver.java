

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import modelling.Constraint;
import modelling.Variable;

/**
 * Classe de base des solveurs : stocke les variables et les contraintes du problème.
 */
public abstract class AbstractSolver implements Solver {

    protected Set<Variable> variables;
    protected Set<Constraint> constraints;

    public AbstractSolver(Set<Variable> variables, Set<Constraint> constraints) {
        this.variables = variables;
        this.constraints = constraints;
    }

    /**
     * Cohérence locale : l'affectation partielle satisfait-elle toutes les contraintes
     * dont toutes les variables sont affectées ?
     */
    public boolean isConsistent(Map<Variable, Object> assignment) {
        for (Constraint c : constraints) {
            // On ne teste que les contraintes dont tout le scope est affecté
            if (assignment.keySet().containsAll(c.getScope())) {
                if (!c.isSatisfiedBy(assignment)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Crée les domaines initiaux (copies modifiables des domaines des variables). */
    protected Map<Variable, Set<Object>> initialDomains() {
        Map<Variable, Set<Object>> domains = new HashMap<>();
        for (Variable v : variables) {
            domains.put(v, new HashSet<>(v.getDomain()));
        }
        return domains;
    }

    /** Copie profonde d'un ensemble de domaines (chaque domaine est copié). */
    protected Map<Variable, Set<Object>> copyDomains(Map<Variable, Set<Object>> domains) {
        Map<Variable, Set<Object>> copy = new HashMap<>();
        for (Variable v : domains.keySet()) {
            copy.put(v, new HashSet<>(domains.get(v)));
        }
        return copy;
    }
}
