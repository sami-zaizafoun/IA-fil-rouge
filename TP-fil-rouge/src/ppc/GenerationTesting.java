package ppc;

import java.util.*;
import representations.*;
import examples.Examples;

/**
 * Generates random solutions cars of the csp.
 *
 */
public class GenerationTesting {

    private Set<Variable> variables;
    private Set<Constraint> constraints;

    /**
     * Constructs an instance of GenerationTesting with a collection of variables
     * and of constraints to respect for the generation
     * @param variables collection of variables
     * @param constraints Set of constraints used for the PPC.
     */
    public GenerationTesting(Set<Variable> variables, Set<Constraint> constraints){
        this.variables = variables;
        this.constraints = constraints;
    }

    /**
     * Method called for generating a series of tests.
     * @return True if a solution has been found, false otherwise.
     */
    public boolean generate_and_test(){
        Map<Variable,String> voiture = generateCar();
        Iterator<Constraint> iter = this.constraints.iterator();
        while(iter.hasNext()){
            if(!iter.next().isSatisfiedBy(voiture)){
                return false;
            }
        }
        return true;
    }

    /**
     * Generate a new random car.
     * @return Map representing the car.
     */
    public Map<Variable,String> generateCar(){
        Map<Variable,String> voiture = new HashMap<>();
        Iterator<Variable> iter = this.variables.iterator();
        while(iter.hasNext()){
            Variable var = iter.next();
            voiture.put(var, getElement(var.getDomaine()));
        }
        return voiture;
    }

    /**
     * Get an element of the variable's domain, to assign it to it.
     * @param domain All possible values of the variable.
     * @return A random String value, to assign to the variable.
     */
    public String getElement(Set<String> domain){
        int size = domain.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for(String value : domain) {
            if (i == item)
                return value;
            i++;
        }
        return null;
    }
}
