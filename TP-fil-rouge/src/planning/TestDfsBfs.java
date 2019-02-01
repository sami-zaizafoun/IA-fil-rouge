package planning;

import java.util.*;
import examples.AssemblyLine;

/**
 * Tests both recursive and iterative dfs and iterative bfs
 *
 */
public class TestDfsBfs {
    public static void main(String[] args) {

        AssemblyLine assembly = new AssemblyLine(false);

        Set<Action> actions = assembly.getActions();

        State goal = assembly.generateGoal();

        PlanningProblem problem = new PlanningProblem(
                assembly.getInitState(),
                goal,
                actions
        );

        System.out.println("Une voiture a au départ un chassis, sinon elle est considéré comme rien");
        System.out.println("\nVoiture de départ : \n" + problem.getInitState());
        System.out.println("\nVoiture d'arrivée : \n" + problem.getGoal());

        System.out.println("\nDFS recursif, nombre d'actions : " + problem.dfs().size());
        System.out.println("Nombre de noeuds parcourus : " + problem.getNbNode());

        System.out.println("\nDFS iteratif, nombre d'actions : " + problem.dfsIter().size());
        System.out.println("Nombre de noeuds parcourus : " + problem.getNbNode());

        System.out.println("\nBFS iteratif, nombre d'actions : " + problem.bfs().size());
        System.out.println("Nombre de noeuds parcourus : " + problem.getNbNode());
    }
}
