
package ppc;

import java.util.*;
import representations.*;

/**
 * This classe is the backtracking search methode
 *
 */
public class Backtracking {

    private Set<Variable> variables;
    private Set<Constraint> constraints;
    private HeuristicVariable heuristic;
    private int nbNode = 0;

    /**
     * Builds an instance of backtracking
     * @param variables all the variables possibles
     * @param constraints constraints of the problem
     * @param heuristic the heuristic of backtracking
     */
    public Backtracking(Set<Variable> variables, Set<Constraint> constraints, HeuristicVariable heuristic){
        this.constraints = constraints;
        this.variables = variables;
        this.heuristic = heuristic;
    }

    /**
     * Getter method of variables of backtracking
     * @return the set of variables of backtracking
     */
    public Set<Variable> getVariables() {
      return this.variables;
    }

    /**
     * Add a variable in the set of variables
     * @param variable the variable to add
     */
    public void addVariable(Variable variable) {
        this.variables.add(variable);
    }

    /**
     * Remove a variable in the set of variables
     * @param variable the variable to remove
     */
    public void removeVariable(Variable variable) {
        this.variables.remove(variable);
    }

    /**
     * Creates a String representation of constraints in backtrack
     * @return the string representation of constraints
     */
    public String getStringConstraints() {
        String ch = "";
        int i = 1;
        // prints all constraints implicated in backtracking
        for(Constraint c : this.constraints) {
            ch += "c" + i + " : " + c + "\n";
            i++;
        }
        return ch;
    }

    /**
     * Getter method of heuristic of backtracking
     * @return the heuristic of backtracking
     */
    public HeuristicVariable getHeuristic() {
        return this.heuristic;
    }

    /**
     * Setter method of heuristic of backtracking
     * @param heuristic the heuristic you want to use
     */
    public void setHeuristic(HeuristicVariable heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Getter method of the number of nodes traveled in backtracking algorithm
     * @return the number of nodes
     */
    public int getNbNode() {
        return this.nbNode;
    }

    /**
     * Chooses a variable not definied in the car according
     * to the heuristic of backtracking
     * @param voiture the car
     * @param mapDom the map of variable and its domain
     * @return one of variables not definied in the car with the best
     * heuristic value
     */
    public Variable choiceVar(Map<Variable, String> voiture, Map<Variable, Set<String>> mapDom) {
        // initialize the variable not assigned and its occurence in the constraints
        // this will be the variable not assigned with the best occurence
        Variable varMax = null;
        int valueOcc = Integer.MIN_VALUE;
        int currentValue; // initialize the current value of occurence
        for (Variable var : mapDom.keySet()) {
            if (!voiture.containsKey(var)) {
                // if the variable isn't definied in the car
                if (varMax == null) {
                    // if the max variable isn't initialize with a variable
                    varMax = var;
                    valueOcc = this.heuristic.heuristicValue(this.constraints, var, mapDom.get(var));
                } else {
                    currentValue = this.heuristic.heuristicValue(this.constraints, var, mapDom.get(var));
                    if (currentValue > valueOcc) {
                        // if the value of the current variable's heuristic
                        // is better than the last maximum found, we replace it
                        valueOcc = currentValue;
                        varMax = var;
                    }
                }
            }
        }
        return varMax;
    }

    /**
     * Test if all the variables are in a car
     * @param voiture the car
     * @param mapVar map of all variables with its domain
     * @return the result of the test
     */
    public boolean isComplete(Map<Variable, String> voiture, Map<Variable, Set<String>> mapVar) {
        for(Variable var : mapVar.keySet()) {
            // for all variables in the map
            if (!voiture.containsKey(var)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Test if the car satisfies all constraints
     * @param voiture the car of the test
     * @return the result of the test
     */
    public boolean isCompatible(Map<Variable, String> voiture) {
        for(Constraint c : this.constraints) {
            if (!c.isSatisfiedBy(voiture)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Copy a map like (Variable, String)
     * @param map the map for the copy
     * @return a copy of the map
     */
    public Map<Variable, String> copyMap(Map<Variable, String> map) {
        return new HashMap<>(map);
    }

    /**
     * Copy a map like (Variable, Set(String))
     * @param map the map to be copied
     * @return a copy of the map
     */
    public Map<Variable, Set<String>> copyMapDomain(Map<Variable, Set<String>> map) {
        return new HashMap<>(map);
    }

    /**
     * Builds a map of the variable with a copy of its domain
     * @param setVar the set of variables you want to transform
     * @return the map built
     */
    public Map<Variable, Set<String>> transformToMap(Set<Variable> setVar) {
        Map<Variable, Set<String>> mapRes = new HashMap<>();
        for (Variable var : setVar) {
            // puts variables with its copied domain
            mapRes.put(var, new HashSet<>(var.getDomaine()));
        }
        return mapRes;
    }

    /**
     * filters the variables' domain
     * @param voiture the car to be filtered
     * @param mapDom the map of the variables' domain to be or not to be filtered
     * @return true if a filtering occures
     */
    public boolean filtering(Map<Variable, String> voiture, Map<Variable, Set<String>> mapDom) {
        for (Constraint c : this.constraints) {
            if (c.filtrer(voiture, mapDom)) {
                // if a filtering occures, return a recursivity or true to get
                // all the filtering and the boolean for the final return
                return filtering(voiture, mapDom) || true;
            }
        }
        return false;
    }

    /**
     * Get a solution of car without filtering
     * @return the first solution found or null if the solution doesn't exist
     */
    public Map<Variable, String> solution() {
        this.nbNode = 0;
        return backtracking(new HashMap<>(), transformToMap(this.variables));
    }

    /**
     * Get a set of all solution cars without filtering
     * @return a set of all solution cars or null if there aren't any solutions
     */
    public Set<Map<Variable, String>> solutions() {
        this.nbNode = 0;
        Set<Map<Variable, String>> solutions = new HashSet<>();
        backtrackingSols(solutions, new HashMap<>(), transformToMap(this.variables));
        return (solutions.isEmpty() ? null : solutions);
    }

    /**
     * Get a solution of the car with filtering
     * @return the first solution found or null if the solution doesn't exist
     */
    public Map<Variable, String> solutionFilter() {
        this.nbNode = 0;
        return backtrackingFilter(new HashMap<>(), transformToMap(this.variables));
    }

    /**
     * Get a set of all solution cars with filtering
     * @return a set of all solution cars or null if there aren't any solutions
     */
    public Set<Map<Variable, String>> solutionsFilter() {
        this.nbNode = 0;
        Set<Map<Variable, String>> solutions = new HashSet<>();
        backtrackingSolsFilter(solutions, new HashMap<>(), transformToMap(this.variables));
        return (solutions.isEmpty() ? null : solutions);
    }

    /**
     * Backtracking algorithm, returns the first solution found without filtering
     * @param voiture the car to be built
     * @param mapDom the map of variables with a copy of its domain
     * @return the first solution car found
     */
    public Map<Variable, String> backtracking(Map<Variable, String> voiture, Map<Variable, Set<String>> mapDom) {

        if (this.isComplete(voiture, mapDom)) {
            // return the solution
            return voiture;
        }
        // Chooses a variable not definied in the car
        Variable var = choiceVar(voiture, mapDom);
        Map<Variable, String> backVoiture;
        for (String value : var.getDomaine()) {
            // for all values in the variable's domain
            // add variable with the value in the car

            this.nbNode += 1; // node counter
            voiture.put(var, value);
            if (this.isCompatible(voiture)) {
                // if all constraints are satisfied by the car
                backVoiture = backtracking(voiture, mapDom);
                if (backVoiture != null) {
                    // if the recursivity finds a solution, return this solution
                    return backVoiture;
                }
            }
            // remove variable with the value in the car, it's a wrong value
            voiture.remove(var);
        }
        // solution not found
        return null;
    }

    /**
     * Backtracking algorithm, fill the set of cars with a copy of all solution
     * cars found without filtering
     * @param solutions the set will be filled
     * @param voiture the car built for the backtracking
     * @param mapDom mapDom the map of variables with a copy of its domain
     */
    public void backtrackingSols(Set<Map<Variable, String>> solutions,
                    Map<Variable, String> voiture, Map<Variable, Set<String>> mapDom) {

        if (this.isComplete(voiture, mapDom)) {
            // add the car in the set of solutions
            solutions.add(copyMap(voiture));
            // block the following of the function's call
            return;
        }
        // Chooses variable not definied in the car
        Variable var = choiceVar(voiture, mapDom);
        for (String value : var.getDomaine()) {
            // for all values in variable's domain
            // add variable with the value in the car

            this.nbNode += 1; // node counter
            voiture.put(var, value);
            if (this.isCompatible(voiture)) {
                // if all constraints are satisfied by the car
                backtrackingSols(solutions, copyMap(voiture), mapDom);
            }
            // remove the variable with the value in the car, it's a wrong value
            voiture.remove(var);
        }
    }

    /**
     * Backtracking algorithm, returns the first solution found with filtering
     * @param voiture the car to be built
     * @param mapDom the map of the variables with a copy of its domain
     * @return the first solution car found
     */
    public Map<Variable, String> backtrackingFilter(Map<Variable, String> voiture, Map<Variable, Set<String>> mapDom) {

        if (this.isComplete(voiture, mapDom)) {
            // returns the solution
            return copyMap(voiture);
        }
        // Chooses a variable not definied in the car
        Variable var = choiceVar(voiture, mapDom);
        Map<Variable, Set<String>> copyMapDomain;
        Map<Variable, String> backVoiture;
        for (String value : mapDom.get(var)) {
            // for all variable in filter domain
            // add variable with the value in the car

            this.nbNode += 1; // node counter
            voiture.put(var, value);
            if (this.isCompatible(voiture)) {
                // if all constraints are satisfied by the car
                copyMapDomain = copyMapDomain(mapDom);

                // reduce the domaine of variable to {value}
                Set<String> restrictedDom = new HashSet<>();
                restrictedDom.add(value);
                copyMapDomain.put(var, restrictedDom);

                // filtering of the variables' domain
                filtering(voiture, copyMapDomain);
                backVoiture = backtrackingFilter(copyMap(voiture), copyMapDomain);
                if (backVoiture != null) {
                    // if the recursivity finds a solution, return this solution
                    return backVoiture;
                }
            }
            // remove variable with the value in the car, it's a wrong value
            voiture.remove(var);
        }
        // solution not found
        return null;
    }

    /**
     * Backtracking algorithm, fill the set of cars with a copy of all
     * solution cars found with filtering
     * @param solutions the set will be fill
     * @param voiture the car built for the backtracking
     * @param mapDom mapDom the map of variable with a copy of its domain
     */
    public void backtrackingSolsFilter(Set<Map<Variable, String>> solutions,
                    Map<Variable, String> voiture, Map<Variable, Set<String>> mapDom) {

        if (this.isComplete(voiture, mapDom)) {
            // add the car in the set of solutions
            solutions.add(copyMap(voiture));
            // block the following of the function's call
            return;
        }
        // Chooses a variable not definied in the car
        Variable var = choiceVar(voiture, mapDom);
        Map<Variable, Set<String>> copyMapDomain;
        for (String value : mapDom.get(var)) {
            // for all variable in filter domain
            // add variable with the value in the car

            this.nbNode += 1; // node counter
            voiture.put(var, value);
            if (this.isCompatible(voiture)) {
                // if all constraints are satisfied by the car
                copyMapDomain = copyMapDomain(mapDom);

                // reduce the domaine of variable to {value}
                Set<String> restrictedDom = new HashSet<>();
                restrictedDom.add(value);
                copyMapDomain.put(var, restrictedDom);

                // filtering of variables' domain
                filtering(voiture, copyMapDomain);
                backtrackingSolsFilter(solutions, copyMap(voiture), copyMapDomain);
            }
        }
        // remove variable with the value in the car, it's a wrong value
        voiture.remove(var);
    }
}
