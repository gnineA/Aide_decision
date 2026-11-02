package modelling;
import java.util.*;




public class Demo {
    public static void main(String[] args) {
      
        Set<Object> domainXY = new HashSet<>();
        domainXY.add(1);
        domainXY.add(2);
        domainXY.add(3);
        Variable x = new Variable("x", domainXY);
        Variable y = new Variable("y", domainXY);
        BooleanVariable b = new BooleanVariable("b");



        // contraintes
        DifferenceConstraint diff = new DifferenceConstraint(x, y);
        Set<Object> s1 = new HashSet<>();
        s1.add(1);
        s1.add(2);
        Set<Object> s2 = new HashSet<>();
        s2.add(true);
        Implication impl = new Implication(x, s1, b, s2);
        Set<Object> allowed = new HashSet<>();
        allowed.add(1);
        allowed.add(3);
        UnaryConstraint unary = new UnaryConstraint(x, allowed);
        // Instanciations
        Map<Variable, Object> inst1 = new HashMap<>();
        inst1.put(x, 1);
        inst1.put(y, 2);
        inst1.put(b, true);
        Map<Variable, Object> inst2 = new HashMap<>();
        inst2.put(x, 1);
        inst2.put(y, 1);
        inst2.put(b, false);
        Map<Variable, Object> inst3 = new HashMap<>();
        inst3.put(x, 3);
        inst3.put(y, 2);
        inst3.put(b, false);
        Map<Variable, Object> inst4 = new HashMap<>();
        inst4.put(x, 2);
        inst4.put(y, 3);
        inst4.put(b, false);
        // Test DifferenceConstraint
        System.out.println("=== DifferenceConstraint (x != y) ===");
        System.out.println("inst1 (x=1, y=2): " + diff.isSatisfiedBy(inst1)); // true
        System.out.println("inst2 (x=1, y=1): " + diff.isSatisfiedBy(inst2)); // false
        // Test Implication
        System.out.println("\n=== Implication (x in {1,2} => b in {true}) ===");
        System.out.println("inst1 (x=1, b=true): " + impl.isSatisfiedBy(inst1)); // true
        System.out.println("inst2 (x=1, b=false): " + impl.isSatisfiedBy(inst2)); // false
        System.out.println("inst3 (x=3, b=false): " + impl.isSatisfiedBy(inst3)); // true
        // Test UnaryConstraint
        System.out.println("\n=== UnaryConstraint (x in {1,3}) ===");
        System.out.println("inst1 (x=1): " + unary.isSatisfiedBy(inst1)); // true
        System.out.println("inst4 (x=2): " + unary.isSatisfiedBy(inst4)); // false
        // Test equals
        System.out.println("\n=== Equals test ===");
        Variable b2 = new BooleanVariable("b");
        Variable b3 = new Variable("b", Set.of(true, false));
        System.out.println("BooleanVariable b equals BooleanVariable b2: " + b.equals(b2));
        System.out.println("BooleanVariable b equals Variable b3: " + b.equals(b3));
        System.out.println("hashCodes equal: " + (b.hashCode() == b2.hashCode()));
    }
}