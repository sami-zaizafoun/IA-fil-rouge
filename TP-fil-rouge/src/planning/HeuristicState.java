
package planning;

/**
 * Interface of heuristics
 *
 */
public interface HeuristicState {

    /**
     * Calculates the heuristic value between an initial state and a target state
     * @param currentState the initial state
     * @param goal the target state
     * @return the value of the heuristic
     */
    public int heuristicValue(State currentState, State goal);

}
