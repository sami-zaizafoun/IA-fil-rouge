
package ppc;

import java.util.*;
import examples.*;
import representations.*;

/**
 * Allows backtrack to be tested with or without filtering
 *
 */
public class Main {
    public static void main(String[] args) {

        Examples ex = new Examples();

        // variables
        Set<Variable> variables = ex.getVariables();

        // constraints
        Constraint c1 = ex.getExemple1();
        Constraint c2 = ex.getExemple2();
        Constraint c3 = ex.getExemple3();
        Constraint c4 = ex.getExemple4();
        Constraint c5 = ex.getExemple5();
        Constraint c6 = ex.getExemple6();

        Set<Constraint> constraints = new HashSet<>();
        constraints.add(c1);
        constraints.add(c2);
        constraints.add(c3);
        constraints.add(c4);
        //constraints.add(c5);
        //constraints.add(c6);

        HeuristicVariable heuristic = new DomainMinHeuristic();
        Backtracking back = new Backtracking(variables, constraints, heuristic);
        System.out.println("\nContraintes : \n" + back.getStringConstraints());

        Map<Variable, String> voiture1 = back.solution();
        Map<Variable, String> voiture2 = back.solutionFilter();
        System.out.println("backtrack avec premiere solution sans filtrage trouvée :\n" + voiture1);
        System.out.println("\nbacktrack avec premiere solution avec filtrage trouvée :\n" + voiture2);

        Set<Map<Variable, String>> sols = back.solutions();

        if (sols != null) {
            System.out.println("\n\\/ SOLUTIONS TROUVEES SANS FILTRAGE \\/\n");
            System.out.println("Nombre de solutions : " + sols.size());
            System.out.println("Heuristic utilisée : " + back.getHeuristic());
            System.out.println("noeuds parcourus : " + back.getNbNode());
        } else {
            System.out.println("Aucune solutions trouvées");
        }

        Set<Map<Variable, String>> sols2 = back.solutionsFilter();

        if (sols2 != null) {
            System.out.println("\n\\/ SOLUTIONS TROUVEES AVEC FILTRAGE \\/\n");
            System.out.println("Nombre de solutions : " + sols2.size());
            System.out.println("Heuristic utilisée : " + back.getHeuristic());
            System.out.println("noeuds parcourus : " + back.getNbNode());
            System.out.println("\nVoitures trouvées : \n");

            for (Map<Variable, String> car : sols2) {
                System.out.println(car);
            }
        } else {
            System.out.println("Aucune solutions trouvées");
        }

    }
}
