package modelling;
import java.util.*;

public class Implication implements Constraint{
    private Variable v1 ;
    private Set<Object> s1;
      private Variable v2 ;
    private Set<Object> s2;
  
 
    public  Implication ( Variable v1,Set<Object> s1,Variable v2,Set<Object> s2){
        this.v1 = v1;
        this.s1 = s1;
        this.v2 = v2;
        this.s2 = s2;

    }

    @Override
    public Set<Variable> getScope(){
        Set<Variable> scope = new HashSet<>();
        scope.add(v1);
        scope.add(v2);
        return scope;
    }

   @Override
    public boolean isSatisfiedBy(Map<Variable, Object> instanciation) {
        if (!instanciation.containsKey(v1) || !instanciation.containsKey(v2)) {
            throw new IllegalArgumentException("Une variable de la contrainte n'est pas affectée");
        }
        Object valeur1 = instanciation.get(v1);
        Object valeur2 = instanciation.get(v2);
        if (!s1.contains(valeur1)) {
            return true; // la condition de départ est fausse : l'implication est vraie
        }
        return s2.contains(valeur2);
    }

}
