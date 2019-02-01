
package representations;

import java.util.*;

/**
 * This abstract classe is a boolean combination between two constraints (And or OR)
 *
 */
public abstract class ConstraintBool implements Constraint {

    protected Constraint c1;
    protected Constraint c2;

    /**
     * builds an instance of the boolean's constraint
     * @param c1 the first constraint
     * @param c2 the second constraint
     */
    public ConstraintBool(Constraint c1, Constraint c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    /**
     * Getter method of the scope's union
     * @return the scope's union
     */
    @Override
    public Set<Variable> getScope() {
        Set<Variable> scopeFinal = new HashSet<>();
        scopeFinal.addAll(this.c1.getScope());
        scopeFinal.addAll(this.c2.getScope());
        return scopeFinal;
    }

    /**
     * Test if the first constraint AND/OR the second constraint are/is satisfied by a car
     * @param voiture the car
     * @return test result
     */
    @Override
    public abstract boolean isSatisfiedBy(Map<Variable, String> voiture);

    /**
     * Getter method of the string's separator for AND and OR
     * @return the string separator
     */
    public abstract String getSeparator();

    /**
     * Build a string representation of the boolean's constraint
     * @return the string representation
     */
    @Override
    public String toString() {
        return "(" + this.c1 + ")" + this.getSeparator() + "(" + this.c2 + ")";
    }

    /**
     * Test if all the variables of constraint are definied in the car
     * @param voiture the car to be tested
     * @param constraint test's constraint
     * @return true if all the variables are definied in the car
     */
    public boolean allVariablesAssigned(Map<Variable, String> voiture, Constraint constraint) {
        Set<Variable> scopeConstraint = constraint.getScope();
        for (Variable var : scopeConstraint) {
            if (voiture.get(var) == null) {
                // unassigned variables
                return false;
            }
        }
        return true;
    }

    /**
     * Filters the domain's variables
     * @param voiture a car for the filtering test
     * @param domaines variables and its copied domain for filtering
     * @return true if filtering occures
     */
    @Override
    public boolean filtrer(Map<Variable, String> voiture, Map<Variable, Set<String>> domaines) {
        boolean res = false;
        boolean c1Assigned = this.allVariablesAssigned(voiture, this.c1);
        boolean c2Assigned = this.allVariablesAssigned(voiture, this.c2);

        if (!c1Assigned && !this.c2.isSatisfiedBy(voiture)) {
            // if there's at least one unassigned variable in the first constraint
            // and the second constraint is not satisfied, we do a filtering of
            // the first constraint
            res = res || this.c1.filtrer(voiture, domaines);
        }

        if (!c2Assigned && !this.c1.isSatisfiedBy(voiture)) {
            // same thing for the filtering of the second constraint
            res = res || this.c2.filtrer(voiture, domaines);
        }

        return res;
    }
}
