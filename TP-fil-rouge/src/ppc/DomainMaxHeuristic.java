
package ppc;

import java.util.*;
import representations.*;

/**
 * This heuristic gives the size of the domain, this heuristic is not effective for
 * filtering with a small ammount of colors because all the variables has the same domain
 * in the beginning and the variable that'll take the heuristic won't be necessarily advantageous
 */
public class DomainMaxHeuristic implements HeuristicVariable {

    /**
     * Calculates the size of the variable's domain
     * @param constraints list of constraints
     * @param var the variable
     * @param domaine the domain of the variable possibly filtered
     * @return the value of the variable
     */
    @Override
    public int heuristicValue(Set<Constraint> constraints, Variable var, Set<String> domaine) {
        return domaine.size();
    }

    /**
     * String name of the heuristic
     * @return the heuristic's name
     */
    @Override
    public String toString() {
        return "Domain max heuristic";
    }
}
