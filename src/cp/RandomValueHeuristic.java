package cp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import modelling.Variable;

/**
 * Ordonne les valeurs du domaine au hasard.
 */
public class RandomValueHeuristic implements ValueHeuristic {

    private Random random;

    public RandomValueHeuristic(Random random) {
        this.random = random;
    }

    @Override
    public List<Object> ordering(Variable variable, Set<Object> domain) {
        List<Object> values = new ArrayList<>(domain);
        Collections.shuffle(values, random); // mélange uniforme
        return values;
    }
}
