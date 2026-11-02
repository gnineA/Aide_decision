package cp;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import modelling.Constraint;
import modelling.DifferenceConstraint;
import modelling.Variable;

/**
 * Classe exécutable : colorie une carte (Australie) avec 3 couleurs, avec les trois solveurs.
 */
public class Demo {

    public static void main(String[] args) {
        Set<Object> couleurs = new HashSet<>();
        couleurs.add("rouge");
        couleurs.add("vert");
        couleurs.add("bleu");

        Variable wa = new Variable("WA", couleurs);
        Variable nt = new Variable("NT", couleurs);
        Variable sa = new Variable("SA", couleurs);
        Variable q = new Variable("Q", couleurs);
        Variable nsw = new Variable("NSW", couleurs);
        Variable v = new Variable("V", couleurs);
        Variable t = new Variable("T", couleurs);

        Set<Variable> variables = new HashSet<>();
        variables.add(wa);
        variables.add(nt);
        variables.add(sa);
        variables.add(q);
        variables.add(nsw);
        variables.add(v);
        variables.add(t);

        // Deux régions voisines doivent avoir des couleurs différentes
        Set<Constraint> constraints = new HashSet<>();
        constraints.add(new DifferenceConstraint(wa, nt));
        constraints.add(new DifferenceConstraint(wa, sa));
        constraints.add(new DifferenceConstraint(nt, sa));
        constraints.add(new DifferenceConstraint(nt, q));
        constraints.add(new DifferenceConstraint(sa, q));
        constraints.add(new DifferenceConstraint(sa, nsw));
        constraints.add(new DifferenceConstraint(sa, v));
        constraints.add(new DifferenceConstraint(q, nsw));
        constraints.add(new DifferenceConstraint(nsw, v));

        Solver backtrack = new BacktrackSolver(variables, constraints);
        Solver mac = new MACSolver(variables, constraints);
        Solver heuristicMac = new HeuristicMACSolver(variables, constraints,
                new DomainSizeVariableHeuristic(false), new RandomValueHeuristic(new Random(42)));

        afficher("Backtrack", backtrack.solve());
        afficher("MAC", mac.solve());
        afficher("MAC avec heuristiques", heuristicMac.solve());
    }

    private static void afficher(String nom, Map<Variable, Object> solution) {
        System.out.println(nom + " : " + solution);
    }
}
