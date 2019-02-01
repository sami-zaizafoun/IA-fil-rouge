
package representations;

import java.util.*;

/**
 * This class represents all the values with equal constraints
 *
 */
public class AllEqualConstraint extends AllCompareConstraint {

    /**
     * Builds an instance of AllEqualConstraint
     * @param variables vairables of constraint
     */
    public AllEqualConstraint(Set<Variable> variables){
        super(variables);
    }

    /**
     * Test if all variables are equal in a car
     * @param voiture the car
     * @return test result
     */
    @Override
    public boolean isSatisfiedBy(Map<Variable, String> voiture) {
        if (voiture.isEmpty() || voiture.size()==1) {
            // if the car is empty or if there's one variable, it satisfies the
            // contraint
            return true;
        } else {
            String values = null;
            String currentValue = null;
            for (Variable var : this.variables) {
                // for all the variables
                // we recover the value in the car
                currentValue = voiture.get(var);
                if (currentValue != null) {
                    // if the variable is defined in the car
                    if (values == null) {
                        // if the value of the equality is not yet affected
                        // we add the value to the variable in the car
                        values = currentValue;
                    } else {
                        // otherwise the valus of the equality is already affected
                        if (!values.equals(currentValue)) {
                            // if the value doesn't correspond to the variable's value
                            // in the car then it's not equal. Therefore we stop and
                            // return false
                            return false;
                        }
                    }
                } else {
                    // if the variable is not defined, we return false
                    return true;
                }
            } // end for
            return true;
        }
    }

    /**
     * Get the string separator of this constraint
     * @return the string's separator
     */
    @Override
    public String getSeparator() {
        return " = ";
    }

    /**
     * Filters the domain's variables
     * @param voiture a car for the filtering test
     * @param domaines variables and its copied domain for filtering
     * @return true if filtering occures
     */
    @Override
    public boolean filtrer(Map<Variable, String> voiture, Map<Variable, Set<String>> domaines) {
        boolean isFilter = false; // boolean to test if there is a filtering
        String value = null; // value of variable of the allEqual
        for (Variable var : this.variables) {
            // for all variables in constraint's variables
            if (value == null && voiture.get(var) != null) {
                // if the value isn't assigned and the car doesn't have this variable
                value = voiture.get(var);
                break;
            }
        }
        if (value != null) {
            // if a value isn't assigned, there are no variables of this constraint in the car
            for (Variable var : this.variables) {
                // for all variables in the constraint's variables
                if (domaines.containsKey(var)) {
                    // if the variable isn't in the car
                    Set<String> copyDom = new HashSet<>(domaines.get(var)); // copy of the variable's domain
                    for (String str : domaines.get(var)) {
                        // for all the values of variables (that aren't in the car) Set of Values domain of the map
                        if (!str.equals(value)) {
                            // if the value isn't equal to the value found at the beggining of this function
                            // we remove it and assign true to the boolean of the filtering test
                            copyDom.remove(str);
                            isFilter = true;
                        }
                    }
                    // add variable and the domain filter or not filter
                    domaines.put(var, copyDom);
                }
            }
        }
        return isFilter;
    }
}
