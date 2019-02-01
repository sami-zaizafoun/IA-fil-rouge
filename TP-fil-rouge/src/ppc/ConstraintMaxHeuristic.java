
package ppc;

import java.util.*;
import representations.*;

/**
 * This heuristic counts the occurence of the variable in constraints
 *
 */
public class ConstraintMaxHeuristic implements HeuristicVariable {

    /**
     * Calculates the occurence this of the variable in constraints
     * @param constraints list of constraints
     * @param var the variable
     * @param domaine the domain of the variable possibly filtered
     * @return the value of the variable
     */
    @Override
    public int heuristicValue(Set<Constraint> constraints, Variable var, Set<String> domaine) {
        int cpt = 0;
        for(Constraint c : constraints) {
            // for all constraints
            if (c.getScope().contains(var)) {
                // if variable is in the constraint's scope
                cpt += 1;
            }
        }
        return cpt;
    }

    /**
     * String name of the heuristic 
     * @return the heuristic's name
     */
    @Override
    public String toString() {
        return "Constraint max heuristic";
    }
}
