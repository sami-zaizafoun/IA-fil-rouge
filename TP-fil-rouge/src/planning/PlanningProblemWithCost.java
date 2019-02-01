
package planning;

import java.util.*;

/* RESULTS OF TESTS

Tests with 3 colors (actions are generated with colors in the class AssemblyLine,
more there are colors, more there are actions) :

- Dijkstra : 2332
- aStar SimpleHeuristic : 580
- weightAStar(5) SimpleHeuristic : 23
- aStar InformedHeuristic : 96
- weightAStar(5) InformedHeuristic : 9

______________________________________
Tests with 5 colors :

- Dijkstra : 24336
- aStar SimpleHeuristic : 2311
- weightAStar(5) SimpleHeuristic : 19
- aStar InformedHeuristic : 97
- weightAStar(5) InformedHeuristic : 10

____________________________________
Tests with 8 colors :

- Dijkstra : 221232
- aStar SimpleHeuristic : 7362
- weightAStar(5) SimpleHeuristic : 19
- aStar InformedHeuristic : 109
- weightAStar(5) InformedHeuristic : 10

_____________________________________
Conclusion :
The Dijkstra algorithm browses many more nodes than aStar, therefore there are way more possibilities,
which leads to a bigger difference in number between Dijkstra and aStar. With
weightAStar, the number of colors obviously has no influence on the number of
nodes visited. The informed heuristic is more efficient than the
simple heuristic.

*/

/**
 * PlanningProblemWithCost contains the Dijkstra and aStar algortihms
 * actions of the problem are now a cost
 */
public class PlanningProblemWithCost extends PlanningProblem {

    private HeuristicState heuristic;

    /**
     * Builds an instance of PlanningProblemWithCost with a heuristic
     * @param initialState the initial state of the problem
     * @param goal the target state
     * @param actions set of possible actions
     * @param heuristic the heuristic on states
     */
    public PlanningProblemWithCost(State initialState, State goal, Set<Action> actions, HeuristicState heuristic) {
        super(initialState, goal, actions);
        this.heuristic = heuristic;
    }

    /**
     * Setter method of the heuristic
     * @param heuristic the new heuristic you want to use
     */
    public void setHeuristic(HeuristicState heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Dijkstra algoritm
     * @return the plan of actions
     */
    public Stack<Action> dijkstra() {
        // initialize the counter of nodes
        this.initNbNode();
        // priority queue of the open states
        PriorityQueue<State> open = new PriorityQueue<>();
        // the map of states' distance
        Map<State, Integer> distance = new HashMap<>();
        // map of fathers (son, father)
        Map<State, State> father = new HashMap<>();
        // the state and the actions to do to get this state
        Map<State, Action> plan = new HashMap<>();
        // list of priority of goals found
        PriorityQueue<State> goals = new PriorityQueue<>();
        // initialises the distance of the initial state to 0
        this.initialState.setDistance(0);
        // add the initial state to the open states
        open.add(this.initialState);
        // the initial state doesn't have a father
        father.put(this.initialState, null);
        // the distance of the initial state is 0
        distance.put(this.initialState, 0);
        while (!open.isEmpty()) {
            // while there is a state in the list of open states
            // increment the counter of nodes
            this.upNbNode();
            // take the state with the minimal distance
            State state = open.remove();
            if (state.satisfies(this.goal.getVoiture())) {
                // if the state is a goal, we add it in the list of goals
                goals.add(state);
            }
            for (Action action : this.actions) {
                // for all actions possible of the problem
                if (action.isApplicable(state)) {
                    // if the action is applicable on the state
                    // apply this action to create a new state
                    State next = action.apply(state);
                    if (!distance.keySet().contains(next)) {
                        // if the new state doesn't have a distance, we add it with the
                        // max value possible to its distance
                        distance.put(next, Integer.MAX_VALUE);
                    }
                    if (distance.get(next) > distance.get(state) + action.getCost()) {
                        // if the distance of the new state is more than the distance
                        // of the state and the cost of the action

                        // set the distance of the new state to the distance of
                        // the state and the cost of the action
                        next.setDistance(distance.get(state) + action.getCost());
                        // add the distance of the new state to its new distance
                        distance.put(next, next.getDistance());
                        // add the state to the father of the new state
                        father.put(next, state);
                        // add the new state with its action in the plan
                        plan.put(next, action);
                        // add the new state to the list of open states
                        open.add(next);
                    }
                }
            }
        }
        // return the plan built
        return this.getDijkstraPlan(father, plan, goals);
    }

    /**
     * Builds the plan of the best path to go to the target state
     * @param father the map of fathers
     * @param actions the map of plan
     * @param goals the list of goals found
     * @return the plan of the best path
     */
    public Stack<Action> getDijkstraPlan(Map<State, State> father, Map<State, Action> actions, PriorityQueue<State> goals) {
        // initialize the plan
        Stack<Action> plan = new Stack<>();
        // get the target state with the minimal distance
        State goal = goals.remove();
        // initializes the action with the last action before the target state
        Action action = actions.get(goal);
        do {
            // add the action to the plan
            plan.push(action);
            // get the son of the state
            goal = father.get(goal);
            // get the action to go to this son
            action = actions.get(goal);
        } while (goal != null && action != null); // while there is a goal or an action
        // resverse the plan
        Collections.reverse(plan);
        return plan;
    }

    /**
     * aStar algorithm, A* it's the WA* with a weight=1
     * @return the best plan to go to the target state
     */
    public Queue<Action> aStar() {
        return this.weightedAStar(1);
    }

    /**
     * WA* algorithm, it's A* with a weight on the heuristic
     * @param weight the weight of the heuristic
     * @return the best plan to go to the target state
     */
    public Queue<Action> weightedAStar(int weight) {
        // initialize the counter of nodes
        this.initNbNode();
        // priority queue of the open states
        PriorityQueue<State> open = new PriorityQueue<>();
        // the map of states' distance
        Map<State, Integer> distance = new HashMap<>();
        // the state and the action do to get this state
        Map<State, Action> plan = new HashMap<>();
        // map of fathers (son, father)
        Map<State, State> father = new HashMap<>();
        // add the initial state to the open states
        open.add(this.initialState);
        // the initial state doesn't have a father
        father.put(this.initialState, null);
        // initialises the distance of the initial state to 0
        this.initialState.setDistance(0);
        // the distance of the initial state is 0
        distance.put(this.initialState, 0);
        while (!open.isEmpty()) {
            // while there is a state in the list of open states
            // increment the counter of nodes
            this.upNbNode();
            // takes the state with the minimal distance
            State state = open.remove();
            if (state.satisfies(this.goal.getVoiture())) {
                // if the state is the goal, return the build plan of the best path
                // use the function of buildPlan of the bfs
                return this.getBfsPlan(father, plan, state);
            } else {
                for (Action action : this.actions) {
                    // for all actions possible of the problem
                    if (action.isApplicable(state)) {
                        // if the action is applicable
                        // apply this action to create a new state
                        State next = action.apply(state);
                        if (!distance.keySet().contains(next)) {
                            // if the new state doesn't have a distance, we add it with the
                            // max value possible to its distance
                            distance.put(next, Integer.MAX_VALUE);
                        }
                        if (distance.get(next) > distance.get(state) + action.getCost()) {
                            // if the distance of the new state is more than the distance
                            // of the state and the cost of the action

                            // add the distance of the new state to distance of state
                            // and the action's cost
                            distance.put(next, distance.get(state) + action.getCost());
                            // set the distance of the new state to its distance and
                            // wieght * heuristic between the new state and the goal
                            next.setDistance(distance.get(next) + weight*this.heuristic.heuristicValue(next, this.goal));
                            // add the state to father of the new state
                            father.put(next, state);
                            // add the new state with its action in the plan
                            plan.put(next, action);
                            // add the new state to the list of open states
                            open.add(next);
                        }
                    }
                }
            }
        }
        // no solution found
        return null;
    }
}
