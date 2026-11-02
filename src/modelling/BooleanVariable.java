package modelling;
import java.util.*;

/**                                                                             
  Une variable booléenne : son domaine est {true, false}.                      
 equals et hashCode sont hérités de Variable (ils ne dépendent que du nom).   
 */

public class BooleanVariable extends Variable {

    public BooleanVariable(String name){
        super(name ,CreateBooleanDomain());
    }

    private static Set<Object> CreateBooleanDomain(){
        Set<Object> domain = new HashSet<>();
        domain.add(true);
        domain.add(false);
        return domain;
        
    }
}