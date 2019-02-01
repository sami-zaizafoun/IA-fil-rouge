
package representations;

import java.util.*;

/**
 * The incompatibility is the rule : not(a=4 and b=5 and c=0 and ...)
 *
 */
public class IncompatibilityConstraint extends Rule {

    /**
     * Builds an instance of incompatibility
     * @param scope all variables in constraint
     * @param premisse the incompatibility
     */
    public IncompatibilityConstraint(Set<Variable> scope, Map<Variable, String> premisse) {
        super(scope, premisse, null);
    }

    /**
     * Filters the domain's variables
     * not(a=1 and b=3 and ...) = a&#033;=1 or b&#033;=3 or ..., it's like filtering
     * a conclusion with an equality test set to false (inequality)
     * @param voiture a car for the filtering test
     * @param domaines variables and its copied domain for filtering
     * @return true if a filter occures
     * @see Rule#filterWithPart
     */
    @Override
    public boolean filtrer(Map<Variable, String> voiture, Map<Variable, Set<String>> domaines) {
        return this.filterWithPart(voiture, domaines, this.getPremisse(), false);
    }
}
