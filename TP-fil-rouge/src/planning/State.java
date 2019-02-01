
package planning;

import java.util.*;
import representations.*;

/**
 * Represents a state of a car
 *
 */
public class State implements Comparable<State> {

    private Map<Variable, String> voiture;
    private Integer distance;

    /**
     * Builds an instance of State
     * @param voiture the map of the car
     * @param distance the distance to the target car
     */
    public State(Map<Variable, String> voiture, Integer distance) {
        this.voiture = voiture;
        this.distance = distance;
    }

    /**
     * Builds an instance of State with a distance to max value possible
     * @param voiture the map of the car
     */
    public State(Map<Variable, String> voiture) {
        this(voiture, Integer.MAX_VALUE);
    }

    /**
     * Getter method of the car
     * @return the map of the car
     */
    public Map<Variable, String> getVoiture() {
        return this.voiture;
    }

    /**
     * Getter method of the state's distance
     * @return the distance of the state
     */
    public int getDistance() {
        return this.distance;
    }

    /**
     * Setter method of the state's distance
     * @param distance the value to set
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Test if the car satisfies a premisse of Rule
     * @param partialState the premisse of rule
     * @return true if the part is satisfied by the car
     * @see Rule#isPartSatisfied
     */
    public boolean satisfies(Map<Variable, String> partialState) {
        // we use the static function isPartSatisfied definied in the class Rule
        // like a premisse : testpart = true
        return Rule.isPartSatisfied(this.voiture, partialState, true);
    }

    /**
     * Creates a copy of this state
     * @return the copy of this state
     */
    public State getCopy() {
        return new State(new HashMap<>(this.voiture), this.distance);
    }

    /**
     * HashCode function of a State it's the hasCode of the car
     * @return the hashCode of a state
     */
    @Override
    public int hashCode() {
        return this.voiture.hashCode();
    }

    /**
     * Equals of the State, two states are equals if their cars are equals
     * @param o the other element for the test
     * @return true if states are the same car
     */
    @Override
    public boolean equals(Object o) {
        if(this==o) {
          return true;
        } else {
            if(o instanceof State) {
                // return the test of equality between their car
                return this.voiture.equals(((State) o).getVoiture());
            } else {
                return false;
            }
        }
    }

    /**
     * CompareTo of a State, the comparison of distances of two states
     * @param other the other State for the comparison
     * @return the result of the comparison of their distance
     */
    @Override
    public int compareTo(State other) {
        return this.distance.compareTo(other.getDistance());
    }

    /**
     * toString of the state
     * @return the String representation of a state
     */
    @Override
    public String toString() {
        String ch = "";
        for (Variable var : this.voiture.keySet()) {
            // for all variable in the car
            // add String like "variable = value \n"
            ch += var + " = " + this.voiture.get(var) + "\n";
        }
        return ch;
    }
}
