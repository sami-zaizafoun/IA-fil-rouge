
package representations;

import java.util.*;

/**
 * This class represents all the values with different constraints
 *
 */
public class AllDifferentConstraint extends AllCompareConstraint {

    /**
     * Builds an instance of AllDifferentConstraint
     * @param variables vairables of constraint
     */
    public AllDifferentConstraint(Set<Variable> variables){
        super(variables);
    }

    /**
     * Test if all the variables are different in a car
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
            // list of the values of each variable
            ArrayList<String> values = new ArrayList<>();
            String currentValue = null;
            for (Variable var : this.variables) {
                // for all variables
                // we recover the value in the car
                currentValue = voiture.get(var);
                if (currentValue != null) {
                    // if the the variable is defined in the car
                    if (values.contains(currentValue)) {
                        // if the value of the variable in the car is added to
                        // another variable, then it's present in the list therefore
                        // the constraint is not respected
                        return false;
                    }
                    // we add the value of the variable that is not already present
                    // in the list
                    values.add(currentValue);
                }
            }
            return true;
        }
    }

    /**
     * Get the string separator of this constraint
     * @return the string's separator
     */
    @Override
    public String getSeparator() {
        return " != ";
    }

    /**
     * Filters the domain's variables
     * @param voiture a car for the filtering test
     * @param domaines variables and its copied domain for filtering
     * @return true if a filtering occures
     */
    @Override
    public boolean filtrer(Map<Variable, String> voiture, Map<Variable, Set<String>> domaines) {
        boolean isFilter = false;
        Set<String> values = new HashSet<>();
        // recovery of all the values of the variables in the car
        for (Variable var : this.variables) {
            // for all the variables
            if (voiture.get(var) != null) {
                // si la variable est presente dans la voiture on l'ajoute
                // if the variable is defined in the car, we add it's value to the list of variables
                values.add(voiture.get(var));
            }
        }
        if (!values.isEmpty()) {
            // if the list of the values is not empty
            for (Variable var : this.variables) {
                // for all the variables
                if (domaines.containsKey(var)) {
                    // if the variable is in the domain
                    // we copy the domain
                    Set<String> copyDom = new HashSet<>(domaines.get(var));
                    for (String value : domaines.get(var)) {
                        // for all the values of the domain
                        if (values.contains(value)) {
                            // if the value is already added to another variable
                            // we remove it from the domain, therefore we filter
                            copyDom.remove(value);
                            isFilter = true;
                        }
                    }
                    // we add the variable with the filtrered domain or not
                    domaines.put(var, copyDom);
                }

            }
        }
        return isFilter;
    }
}
