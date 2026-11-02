import cptests.AbstractSolverTests;
import cptests.ArcConsistencyTests;
import cptests.BacktrackSolverTests;
import cptests.DomainSizeVariableHeuristicTests;
import cptests.HeuristicMACSolverTests;
import cptests.MACSolverTests;
import cptests.NbConstraintsVariableHeuristicTests;
import cptests.RandomValueHeuristicTests;


/**
 * Lance les tests fournis
 */
public class Tests {

    public static void main(String[] args) {
        boolean ok = true;
        ok = ok && AbstractSolverTests.testIsConsistent();
        ok = ok && BacktrackSolverTests.testSolve();
        ok = ok && ArcConsistencyTests.testEnforceNodeConsistency();
        ok = ok && ArcConsistencyTests.testRevise();
        ok = ok && ArcConsistencyTests.testAC1();
        ok = ok && MACSolverTests.testSolve();
        ok = ok && HeuristicMACSolverTests.testSolve();
        ok = ok && NbConstraintsVariableHeuristicTests.testBest();
        ok = ok && DomainSizeVariableHeuristicTests.testBest();
        ok = ok && RandomValueHeuristicTests.testOrdering();

        // Tests génériques de solveurs (interface Solver)
        ok = ok && new SolverTests(
                (vars, constraints) -> new BacktrackSolver(vars, constraints)).testSolve();
        ok = ok && new SolverTests(
                (vars, constraints) -> new MACSolver(vars, constraints)).testSolve();
        ok = ok && new SolverTests(
                (vars, constraints) -> new HeuristicMACSolver(vars, constraints,
                        new DomainSizeVariableHeuristic(false),
                        new RandomValueHeuristic(new Random()))).testSolve();
        System.out.println(ok ? "All tests OK" : "At least one test KO");
    }
}
