
package modelling;
import java.util.*;
public class UnaryConstraint implements Constraint{
      private Variable v;
      private  Set<Object> s;

      public UnaryConstraint (Variable v ,Set<Object> s){
        this.v =v;
        this.s = s;

      }

    @Override
    public Set<Variable> getScope(){
            Set<Variable> scope = new HashSet<>();
            scope.add(v);
            return scope;
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> instanciation) {
        if (!instanciation.containsKey(v)) {
            throw new IllegalArgumentException("La variable de la contrainte n'est pas affectée");
        }
        return s.contains(instanciation.get(v));
    }
}