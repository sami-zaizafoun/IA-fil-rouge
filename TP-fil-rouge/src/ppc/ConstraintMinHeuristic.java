
package ppc;

import java.util.*;
import representations.*;

/**
 * This heuristic gives the opposite value of the ConstraintMaxHeuristic, the
 * max value of a list of variables will be the min value with this heuristic
 */
public class ConstraintMinHeuristic implements HeuristicVariable {


    /**
     * Calculates the opposite of the occurence this of the variable in constraints
     * @param constraints list of constraints
     * @param var the variable
     * @param domaine the domain of the variable possibly filtered
     * @return the value of the variable
     * @see ConstraintMaxHeuristic#heuristicValue
     */
    @Override
    public int heuristicValue(Set<Constraint> constraints, Variable var, Set<String> domaine) {
        HeuristicVariable constraintMax = new ConstraintMaxHeuristic();
        // the min value is the opposite of the max value
        return -constraintMax.heuristicValue(constraints, var, domaine);
    }

    /**
     * String name of the heuristic
     * @return the heuristic's name 
     */
    @Override
    public String toString() {
        return "Constraint min heuristic";
    }
}
