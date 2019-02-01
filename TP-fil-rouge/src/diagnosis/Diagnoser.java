
package diagnosis;

import java.util.*;
import representations.*;
import ppc.*;

/**
 * This class allows us to get an explanation for the non satisfaction of a
 * group of contraints
 */
public class Diagnoser {

    private Backtracking backtrack;
    private Map<Variable, Set<String>> variablesWithDomain;

    /**
     * Build an instance of Diagnoser
     * @param backtrack an instance of backtrack to find a solution for a given
     * car's state, it contains all the constraints and variables
     */
    public Diagnoser(Backtracking backtrack) {
        this.backtrack = backtrack;
        // use of the function transformToMap that allows us to get a map of variables
        // as well as  copy of their domain that we can reduce
        this.variablesWithDomain = this.backtrack.transformToMap(this.backtrack.getVariables());
    }

    /**
     * Create an instance of diagnoser with a collection of variables, a collection
     * of constraints and a heuristic on the variables to create a backtrack
     * @param variables collection of variables
     * @param constraints collection of contraints
     * @param heuristic the heuristic to use
     */
    public Diagnoser(Set<Variable> variables, Set<Constraint> constraints, HeuristicVariable heuristic) {
        this(new Backtracking(variables, constraints, heuristic));
    }

    /**
     * Create an instance of diagnoser with a collection of variables, a collection
     * of constraints that'll create a backtrack with a heuristic on the minimal domain
     * by default
     * @param variables collection of variables
     * @param constraints collection of contraints
     */
    public Diagnoser(Set<Variable> variables, Set<Constraint> constraints) {
        this(variables, constraints, new DomainMinHeuristic());
    }

    /**
     * Adds a variable in the map of variables with their copied domain and also
     * adds it in the collection of variables of the Backtracking instance
     * @param variable the variable to add
     */
    public void add(Variable variable) {
        this.backtrack.addVariable(variable);
        this.variablesWithDomain.put(variable, new HashSet<>(variable.getDomaine()));
    }

    /**
     * Deletes a variable in the map of variables and in the collection of
     * variables of backtrack
     * @param variable the variable to delete
     */
    public void remove(Variable variable) {
        this.backtrack.removeVariable(variable);
        this.variablesWithDomain.remove(variable);
    }

    /**
     * Test if a collection of given choices is an explanation with the addition of
     * an additional choice. The choices are an explanation if a solution that
     * satisfies all the constraints doesn't exist
     * @param choices collection of choices
     * @param variable the variable of the additional choice
     * @param value the value of the variable of the additional choice
     * @return true if the additional choice correponds with the other choices
     */
    public boolean isExplanation(Map<Variable, String> choices, Variable variable, String value) {
        Set<String> reduceDomain;
        Map<Variable, Set<String>> copyDomain = new HashMap<>(this.variablesWithDomain);
        Map<Variable, String> copyChoices = new HashMap<>(choices);
        for (Variable var : copyChoices.keySet()) {
            // for all choice in the set of choices
            // reduce the domain to the value assigned in the choices
            reduceDomain = new HashSet<>();
            reduceDomain.add(copyChoices.get(var));
            copyDomain.put(var, reduceDomain);
        }
        // add the new choice with the domain reduced to their value
        reduceDomain = new HashSet<>();
        reduceDomain.add(value);
        copyDomain.put(variable, reduceDomain);
        // add the new value in the choices copy
        copyChoices.put(variable, value);
        // try to find a solution
        Map<Variable, String> solution = this.backtrack.backtrackingFilter(copyChoices, copyDomain);
        // solution = null <=> no solution found
        return solution == null;
    }

    /**
     * Finds the minimal explanation for the inclusion of a variable with
     * a value in a set of predefined choices
     * @param choices collection of the predefined choices
     * @param variable the variable
     * @param value it's value
     * @return the minimal explanation for the inclusion
     */
    public Map<Variable, String> findMinimalInclusionExplanation(Map<Variable, String> choices, Variable variable, String value) {
        Map<Variable, String> explanation = new HashMap<>(choices);
        Map<Variable, String> ePrime;
        for(Variable var : choices.keySet()) {
            // for all the variables of choices
            // copy the current explanation and remove
            // the variable of the copy of the explanation
            ePrime = new HashMap<>(explanation);
            ePrime.remove(var);
            if (this.isExplanation(ePrime, variable, value)) {
                // if it's an explanation we replace the variable of the old
                // explanation with the new one found
                explanation = ePrime;
            }
        }
        return explanation;
    }

    /**
     * Find the minimal explanation in the sense of cardinality
     * @param choices collection of the predefined choices
     * @param variable the variable
     * @param value it's value
     * @return the minimal explanation in the sense of cardinality
     */
    public Map<Variable, String> findMinimalCardinalExplanation(Map<Variable, String> choices, Variable variable, String value) {
        // recover a set with 1 explanation which is the minimal explanation in
        // the sense of cardinality
        Set<Map<Variable, String>> solution = this.findExplanations(choices, variable, value, false);
        // recovery of the explanation, iter.next won't see another exception
        // because there will be at least one element in the set therefore it won't
        // be necessary to test if the set is empty
        Iterator<Map<Variable, String>> iter = solution.iterator();
        return iter.next();
    }

    /**
     * List all the explanations for a set of choices
     * @param choices collection of the predefined choices
     * @param variable the variable that we would like to add to the choices
     * @param value the value of the variable we want to add
     * @return all of the explanations
     */
    public Set<Map<Variable, String>> findAllExplanations(Map<Variable, String> choices, Variable variable, String value) {
        return this.findExplanations(choices, variable, value, true);
    }

    /**
     * enumerates all explanations or returns the minimal explanation in the
     * sense of cardinality according to the value of the Boolean
     * @param choices collection of the predefined choices
     * @param variable the variable that we would like to add to the choices
     * @param value the value of the variable we want to add
     * @param allExplanations true if we want to enumerate all explanations
     * and false if we just want to recover just a set with just the minimal
     * explanation in the sense of cardinality
     * @return the set of all explanations or a set with the minimal
     * explanation in the sense of cardinality
     */
    private Set<Map<Variable, String>> findExplanations(Map<Variable, String> choices, Variable variable, String value, boolean allExplanations) {
        // For this algorthin we start from the set of choices and for each choice
        // we will remove it to have an explanation of size choix.size()-1. On ne
        // we do not start from the empty set to go to the choice because we have a
        // monotonicity if A and B are separate, if A et B are not an explanation
        // it does not guarantee that AuB is not an explanation

        // the set that we will return
        Set<Map<Variable, String>> finalRes = new HashSet<>();
        // the explanations at the stage k
        Set<Map<Variable, String>> res = new HashSet<>();
        // the explanation at the stage k-1
        Set<Map<Variable, String>> resPrec;
        // variable that will serve as a copy of each explanation of the step k-1
        // to be able to generate those of the step k
        Map<Variable, String> copyPart;
        // initialization of the minimal explanation in the sense of
        // inclusion to the predefined choices that are an explanation
        Map<Variable, String> minimalExplication = new HashMap<>(choices);
        if (allExplanations) {
            finalRes.add(minimalExplication); // added explanation of maximum size
        }
        res.add(minimalExplication); // to generate the smaller explanation of size

        for (int size = choices.size(); size>0; size--) {
            // reverse course, we start from the choices to reach singletons
            //  we make a copy of k-1
            resPrec = new HashSet<>(res);
            res = new HashSet<>(); // initializes the variable k
            for (Map<Variable, String> part : resPrec) {
                // for all parts of k-1
                for (Variable var : part.keySet()) {
                    // for all variables in this part
                    // we make a copy of the part where we remove the variable
                    copyPart = new HashMap<>(part);
                    copyPart.remove(var);
                    if (this.isExplanation(copyPart, variable, value)) {
                        // if the built part is an explanation we add it
                        // throughout the floor k
                        res.add(copyPart);
                        if (!allExplanations && copyPart.size() < minimalExplication.size()) {
                            // if we want the method to give us the minimal
                            // explanation in the sense of cardinality we test
                            // if the explanation to a smaller size than the explanation
                            // that is affected, if so, we replace the old
                            // explanation with the new
                            minimalExplication = copyPart;
                        }
                    }
                } // end for var in part
            } // end for part in resPrec
            if (allExplanations) {
                // if we want all the explanations, add all the explanations
                // of the floor k found
                finalRes.addAll(res);
            }
        } // end for with variable size

        if (!allExplanations) {
            // if we want the minimal explanation we modify the return
            // variable and we put it to the empty set and we add to it
            // the minimal explanation
            finalRes.add(minimalExplication);
        }
        return finalRes;
    }
}
