
package planning;

import java.util.*;
import representations.*;

/**
 * Represents an Action applied to a car
 *
 */
public class Action {

    private Set<Rule> preconditions;
    private int cost;

    /**
     * Build an instance of Action
     * @param preconditions a set of rules to apply
     * @param cost the cost of the action's application
     */
    public Action(Set<Rule> preconditions, int cost){
        this.preconditions = preconditions;
        this.cost = cost;
    }

    /**
     * Getter method of the preconditions
     * @return the preconditions of the action
     */
    public Set<Rule> getPreconditions() {
        return this.preconditions;
    }

    /**
     * Getter method of the action's cost
     * @return the cost of the action
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * Test if the action is applicable on a state
     * @param state state
     * @return true if the action is applicable
     */
    public boolean isApplicable(State state) {
        for (Rule rule : this.preconditions) {
            // for all rules in the precondistions
            if (state.satisfies(rule.getPremisse()) && !state.satisfies(rule.getConclusion())) {
                // if the state satisfies the premisse of the rule and
                // the state doesn't satisfy the conclusion (if one or more value(s)
                // of the car's variables doesn't correspond to the conclusion's value
                return true;
            }
        }
        return false;
    }

    /**
     * Applies the action to the state
     * @param state a copy of the result of the application of the action
     * @return a state copy with the applied action
     */
    public State apply(State state) {
        // create a copy of the state
        State copyState = state.getCopy();
        if (this.isApplicable(copyState)) {
            for (Rule rule : this.preconditions) {
                // for all rules in the preconditions
                if (copyState.satisfies(rule.getPremisse())) {
                    // if the copyState satisfies the premisse of the rule
                    for (Variable var : rule.getConclusion().keySet()) {
                        // for all variables in the conclusion
                        // modification of the value in the copyState's car
                        // car[variable] = rule's_conclusion[var]
                        copyState.getVoiture().put(var, rule.getConclusion().get(var));
                    }
                }
            }
        }
        return copyState;
    }

    /**
     * toString of an action is the toString of all rules
     * @return String representation of the action
     */
    @Override
    public String toString() {
        String ch = "";
        for (Rule rule : this.preconditions) {
            ch += rule.toString() + "\n";
        }
        return ch;
    }
}
