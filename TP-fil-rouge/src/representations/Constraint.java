
package representations;

import java.util.Map;
import java.util.Set;

/**
 * A constraint is a requirement of the variable's values
 *
 */
public interface Constraint {

    /**
     * Getter method of constraint's scope
     * @return the scope of constraint
     */
    public Set<Variable> getScope();

    /**
     * Test if the constraint is satisfied by a car
     * @param voiture the car
     * @return test result
     */
    public boolean isSatisfiedBy(Map<Variable,String> voiture);

    /**
     * Filters the domain's variables
     * @param voiture a car for the filtering test
     * @param domaines variables and its copied domain for filtering
     * @return true if a filtering occures
     */
    public boolean filtrer(Map<Variable, String> voiture, Map<Variable, Set<String>> domaines);

}
