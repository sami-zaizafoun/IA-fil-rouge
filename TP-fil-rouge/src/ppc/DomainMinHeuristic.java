
package ppc;

import java.util.*;
import representations.*;

/**
 * This heuristic gives the opposite of the variable's size domain, the minimum
 * in the list of variables is the opposite of the maximum value
 */
public class DomainMinHeuristic implements HeuristicVariable {

    /**
     * Calculates the size of the variable's domain
     * @param constraints list of constraints
     * @param var the variable
     * @param domaine the domain of the variable possibly filtered
     * @return the value of the variable
     * @see DomainMaxHeuristic#heuristicValue
     */
    @Override
    public int heuristicValue(Set<Constraint> constraints, Variable var, Set<String> domaine) {
        return -domaine.size();
    }

    /**
     * String name of the heuristic
     * @return the heuristic's name 
     */
    @Override
    public String toString() {
        return "Domain min heuristic";
    }
}
