package planning;

import java.util.Map;
import modelling.Variable;

public interface Action {
    boolean isApplicable(Map<Variable, Object> state);
    Map<Variable, Object> successor(Map<Variable, Object> state);
    int getCost();
    
}

