
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import modelling.Constraint;
import modelling.Variable;

/**
 * Filtrage des domaines par cohérence de nœud (contraintes unaires)
 * et cohérence d'arc (contraintes binaires).
 */
public class ArcConsistency {

    private Set<Constraint> constraints;
    private List<Constraint> unaryConstraints = new ArrayList<>();
    private List<Constraint> binaryConstraints = new ArrayList<>();

    public ArcConsistency(Set<Constraint> constraints) {
        this.constraints = constraints;
        for (Constraint c : constraints) {
            int taille = c.getScope().size();
            if (taille == 1) {
                unaryConstraints.add(c);
            } else if (taille == 2) {
                binaryConstraints.add(c);
            } else {
                throw new IllegalArgumentException("Ni unaire ni binaire");
            }
        }
    }

    /**
     * Supprime (en place) des domaines les valeurs qui violent une contrainte unaire.
     * Retourne false si un domaine est vide, true sinon.
     */
    public boolean enforceNodeConsistency(Map<Variable, Set<Object>> domains) {
        for (Constraint c : unaryConstraints) {
            Variable var = c.getScope().iterator().next();
            Set<Object> domain = domains.get(var);
            // On stocke les valeurs à supprimer à part, pour ne pas modifier le domaine
            // pendant qu'on le parcourt
            Set<Object> toRemove = new HashSet<>();
            for (Object value : domain) {
                Map<Variable, Object> assignment = new HashMap<>();
                assignment.put(var, value);
                if (!c.isSatisfiedBy(assignment)) {
                    toRemove.add(value);
                }
            }
            domain.removeAll(toRemove);
        }
        for (Set<Object> domain : domains.values()) {
            if (domain.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Supprime (en place) de d1 les valeurs de v1 qui n'ont aucun support dans d2
     * pour les contraintes portant sur v1 et v2.
     * Retourne true si au moins une valeur a été supprimée.
     */
    public boolean revise(Variable v1, Set<Object> d1, Variable v2, Set<Object> d2) {
        // Contraintes portant sur v1 et v2
        List<Constraint> relevant = new ArrayList<>();
        for (Constraint c : binaryConstraints) {
            Set<Variable> scope = c.getScope();
            if (scope.contains(v1) && scope.contains(v2)) {
                relevant.add(c);
            }
        }

        Set<Object> toRemove = new HashSet<>();
        for (Object x : d1) {
            boolean supported = false;
            for (Object y : d2) {
                Map<Variable, Object> assignment = new HashMap<>();
                assignment.put(v1, x);
                assignment.put(v2, y);
                if (satisfiesAll(relevant, assignment)) {
                    supported = true;
                    break; // un support suffit
                }
            }
            if (!supported) {
                toRemove.add(x);
            }
        }
        d1.removeAll(toRemove);
        return !toRemove.isEmpty();
    }

    private boolean satisfiesAll(List<Constraint> list, Map<Variable, Object> assignment) {
        for (Constraint c : list) {
            if (!c.isSatisfiedBy(assignment)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Algorithme AC1 : cohérence de nœud, puis révision de tous les arcs jusqu'à stabilité.
     * Les domaines sont modifiés en place. Retourne false si un domaine est vidé.
     * Le filtrage va jusqu'au bout même après un domaine vide : la valeur de retour est
     * calculée à la fin, sur tous les domaines.
     */
    public boolean ac1(Map<Variable, Set<Object>> domains) {
        enforceNodeConsistency(domains);
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Constraint c : binaryConstraints) {
                Iterator<Variable> it = c.getScope().iterator();
                Variable v1 = it.next();
                Variable v2 = it.next();
                // Les deux sens de l'arc
                if (revise(v1, domains.get(v1), v2, domains.get(v2))) {
                    changed = true;
                }
                if (revise(v2, domains.get(v2), v1, domains.get(v1))) {
                    changed = true;
                }
            }
        }
        for (Set<Object> domain : domains.values()) {
            if (domain.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
