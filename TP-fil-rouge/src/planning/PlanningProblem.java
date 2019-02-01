
package planning;

import java.util.*;

/* RESULTS OF TESTS

Tests with 3 colors in the component_color :
- dfs :
nodes : 594, 469 and 153
number of actions : 468, 593 and 152

- dfs iterative :
nodes : 191, 11, 41
number of actions : 35, 113 and 11

- bfs :
nodes : 2124, 1962 and 81
number of actions : 10, 10 and 5
__________________________________________

Tests with 5 colors in the component_color :
- dfs :
nodes : 3216, 2774, 2419 and StackOverflowError
number of actions : 2418, 2773, 3115 and StackOverflowError

- dfs iterative :
nodes : 230, 5, 491, 1922, 2242
number of actions : 5, 309, 1133, 1280, 145

- bfs :
nodes : 18884, 16159, 637, 17091, 16578
number of actions : 10, 5, 10, 10, 10
___________________________________________

Tests with 8 colors in the component_color :
- dfs : StackOverflowError

- dfs iterative : we interrupt the program after 30 minutes because it takes too long...

- bfs : we interrupt the program after 30 minutes because it takes too long too...
___________________________________________

Conclusion :
The dfs method with a few selected colors finds a solution faster but it's not the
best solution, there are too many actions in the plan and with 5 to 8 colors, we obtain a
StackOverflowError or it's infinit. Therefore dfs isn't a good algorithm to find a solution.
As well, the bfs with a few selected colors finds a solution, not quickly but it finds
only the actions that are needed between many nodes visited. With 8 colors, the algorithm
takes too long to find a solution. Therefore bfs isn't a good algorithm to find a solution.
*/


/**
 * PlanningProblem contains the DFS and BFS methods
 *
 */
public class PlanningProblem {

    protected State initialState;
    protected State goal;
    public Set<Action> actions;
    /** counter of node */
    protected int nbNode;

    /**
     * Build an instance of PlanningProblem
     * @param initialState the initial state of the problem
     * @param goal the target state
     * @param actions set of possible actions
     */
    public PlanningProblem(State initialState, State goal, Set<Action> actions) {
        this.initialState = initialState;
        this.goal = goal;
        this.actions = actions;
        this.nbNode = 0;
    }

    /**
     * Getter method of the number of nodes
     * @return the number of node
     */
    public int getNbNode() {
        return this.nbNode;
    }

    /**
     * Increments the number of nodes
     */
    public void upNbNode() {
        this.nbNode += 1;
    }

    /**
     * Initializes the number of nodes to 0
     */
    public void initNbNode() {
        this.nbNode = 0;
    }

    /**
     * Getter of the initialState
     * @return the initial state of the problem
     */
    public State getInitState() {
        return this.initialState;
    }

    /**
     * Getter of the goal
     * @return the goal of the problem
     */
    public State getGoal() {
        return this.goal;
    }

    /**
     * Function to call the DFS recursive method with the right arguments
     * @return the plan of action to execute to go to the target state
     */
    public Stack<Action> dfs() {
        // initialize the number of nodes
        this.initNbNode();
        return this.dfs(this.initialState, new Stack<>(), new HashSet<>());
    }

    /**
     * DFS recursive method
     * @param state the state of the beginning
     * @param plan the plan to built
     * @param closed the set of states visited
     * @return the plan of actions to execute to go to the target state
     */
    public Stack<Action> dfs(State state, Stack<Action> plan,
            Set<State> closed) {

        this.upNbNode();
        if (state.satisfies(this.goal.getVoiture())) {
            // reverse the plan
            Collections.reverse(plan);
            return plan;
        } else {
            for (Action action : this.actions) {
                // for all actions in the set of actions of the problem
                if (action.isApplicable(state)) {
                    // if the action is applicable to the state
                    // apply the action to build a new state
                    State next = action.apply(state);
                    if (!closed.contains(next)) {
                        // if the new state isn't already visited
                        // add to the plan
                        plan.push(action);
                        // add to the set of visited states
                        closed.add(next);
                        // recusivity with the new state
                        Stack<Action> subplan = dfs(next, plan, closed);
                        if (!subplan.isEmpty()) {
                            // if the recursivity returns an empty plan
                            return subplan;
                        } else {
                            // remove the new state
                            plan.pop();
                        }
                    }
                }
            }
            // empty plan
            return plan;
        }
    }

    /**
     * DFS iterative method
     * @return the plan of actions to execute to go to the target state
     */
    public Queue<Action> dfsIter() {
        // initialize the counter of nodes
        this.initNbNode();
        // map of (son, father)
        Map<State, State> father = new HashMap<>();
        // map of the plan state and the actions to go to this state
        Map<State, Action> plan = new HashMap<>();
        // set of visited states
        Set<State> closed = new HashSet<>();
        // stack of states to visit
        Stack<State> open = new Stack<>();
        // add the initial state to the open
        open.push(this.initialState);
        // the initial state doesn't have a father
        father.put(this.initialState, null);
        while (!open.isEmpty()) {
            // increment the counter of nodes
            this.upNbNode();
            // take the state at the top of the stack
            State state = open.pop();
            // add the state to visited states
            closed.add(state);
            for (Action action : this.actions) {
                // for all actions in the set of actions of the problem
                if (action.isApplicable(state)) {
                    // if the action is applicable
                    // apply the action to build a new state
                    State next = action.apply(state);
                    if (!closed.contains(next) && !open.contains(next)) {
                        // if the new state isn't already visited and isn't in the open
                        // add the state before applying the action to the father of the
                        // new state
                        father.put(next, state);
                        // add the new state in the plan with the action applied
                        plan.put(next, action);
                        if (next.satisfies(this.goal.getVoiture())) {
                            // if the new state satisfies the goal
                            // use the function satisfies of state because
                            // it satisfies the same premisse that satisfies a car
                            return this.getBfsPlan(father, plan, next);
                        } else {
                            // add the new state to the open
                            open.add(next);
                        }
                    }
                }
            }
        }
        // no solution found
        return null;
    }

    /**
     * BFS iterative method
     * @return the plan of action to execute to go to the target state
     */
    public Queue<Action> bfs() {
        // initialize the counter of node
        this.initNbNode();
        // map of (son, father)
        Map<State, State> father = new HashMap<>();
        // map of the plan state and action to go to this state
        Map<State, Action> plan = new HashMap<>();
        // set of visited states
        Set<State> closed = new HashSet<>();
        // Queue of states to visit
        Queue<State> open = new LinkedList<>();
        // add the initial state to the open
        open.add(this.initialState);
        // the initial state doesn't have a father
        father.put(this.initialState, null);
        while (!open.isEmpty()) {
            // increment the counter of nodes
            this.upNbNode();
            // dequeue of the open
            State state = open.remove();
            // add the state to visited states
            closed.add(state);
            for (Action action : this.actions) {
                // for all actions in the set of actions of the problem
                if (action.isApplicable(state)) {
                    // if the action is applicable
                    // apply the action to build a new state
                    State next = action.apply(state);
                    if (!closed.contains(next) && !open.contains(next)) {
                        // if the new state isn't already visited and isn't in the open
                        // add the state before applying the action to the father of the
                        // new state
                        father.put(next, state);
                        // add the new state in the plan with the action applied
                        plan.put(next, action);
                        if (next.satisfies(this.goal.getVoiture())) {
                            // if the new state satisfies the goal
                            // use the function satisfies of state because
                            // it satisfies the same premisse that satisfies a car
                            return this.getBfsPlan(father, plan, next);
                        } else {
                            // add the new state to the open
                            open.add(next);
                        }
                    }
                }
            }
        }
        // no solution found
        return null;
    }

    /**
     * Build the plan of actions to go to the initialState of the goal
     * @param father map of (son, father)
     * @param actions the plan of actions
     * @param goal the target state
     * @return the plan of actions to go to the target state
     */
    public Queue<Action> getBfsPlan(Map<State, State> father,
            Map<State, Action> actions, State goal) {

        // initialize the plan
        Queue<Action> plan = new LinkedList<>();
        // get the action to go to the goal
        Action action = actions.get(goal);
        do {
            // add the action to the plan
            plan.add(action);
            // goal will be the father of the goal
            goal = father.get(goal);
            // action is the action to go to the goal
            action = actions.get(goal);
        } while (goal != null && action != null); // if there is goal && action
        // reverse the plan
        Collections.reverse((LinkedList<Action>) plan);
        return plan;
    }
}
