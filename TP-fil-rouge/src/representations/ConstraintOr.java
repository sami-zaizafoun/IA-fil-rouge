
package representations;

import java.util.Map;

/**
 * this class is an OR combination between two constraints
 *
 */
public class ConstraintOr extends ConstraintBool implements Constraint {

    /**
     * Builds an OR constraint
     * @param c1 the first constraint
     * @param c2 the second constraint
     */
    public ConstraintOr(Constraint c1, Constraint c2) {
        super(c1, c2);
    }

    /**
     * Test if the first constraint OR the second constraint is satisfied by a car
     * @param voiture the car
     * @return test result
     */
    @Override
    public boolean isSatisfiedBy(Map<Variable, String> voiture) {
        return (this.c1.isSatisfiedBy(voiture) || this.c2.isSatisfiedBy(voiture));
    }

    /**
     * Get the string's separator of OR : "||"
     * @return the string's separator 
     */
    @Override
    public String getSeparator() {
        return " || ";
    }
}
