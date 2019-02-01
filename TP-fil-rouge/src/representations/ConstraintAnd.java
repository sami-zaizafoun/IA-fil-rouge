
package representations;

import java.util.Map;

/**
 * This class is an AND combination between two constraints
 *
 */
public class ConstraintAnd extends ConstraintBool implements Constraint {

    /**
     * Build an AND constraint
     * @param c1 the first constraint
     * @param c2 the second constraint
     */
    public ConstraintAnd(Constraint c1, Constraint c2) {
        super(c1, c2);
    }

    /**
     * Test if the first constraint AND the second constraint are satisfied by a car
     * @param voiture the car
     * @return test result
     */
    @Override
    public boolean isSatisfiedBy(Map<Variable, String> voiture) {
        return (this.c1.isSatisfiedBy(voiture) && this.c2.isSatisfiedBy(voiture));
    }

    /**
     * Get the string separator of AND : "&#038;&#038;"
     * @return the string's separator
     */
    @Override
    public String getSeparator() {
        return " && ";
    }
}
