
package diagnosis;

import java.util.*;
import representations.*;
import ppc.*;
import examples.*;

/**
 * Tests the diagnoser
 *
 */
public class Main {
    public static void main(String[] agrs) {

        Examples ex = new Examples();

        // variables
        Set<Variable> variables = ex.getVariables();

        // constraints
        Constraint c1 = ex.getExemple1();
        Constraint c2 = ex.getExemple2();
        Constraint c3 = ex.getExemple3();
        Constraint c4 = ex.getExemple4();

        Set<Constraint> constraints = new HashSet<>();
        constraints.add(c1);
        constraints.add(c2);
        constraints.add(c3);
        constraints.add(c4);

        // build diagnoser
        HeuristicVariable heuristic = new DomainMinHeuristic();

        Backtracking back = new Backtracking(variables, constraints, heuristic);

        Diagnoser diagnoser = new Diagnoser(back);

        // choices
        Map<Variable, String> choices = new HashMap<>();
        choices.put(ex.getVariableWithName("couleur_capot"), "rouge");
        choices.put(ex.getVariableWithName("couleur_droite"), "noir");
        choices.put(ex.getVariableWithName("couleur_hayon"), "rouge");

        System.out.println("\nConstraints : \n" + back.getStringConstraints());

        System.out.println("Choices :");
        for (Variable var : choices.keySet()) {
            System.out.println(var + " = " + choices.get(var));
        }

        // choice to test
        Variable variable = ex.getVariableWithName("couleur_gauche");
        String value = "noir";

        System.out.println("\nTest with the next choice : " + variable + "=" + value);

        if (diagnoser.isExplanation(choices, variable, value)) {
            // if the choice is an explanation
            Map<Variable, String> explication = diagnoser.findMinimalInclusionExplanation(choices, variable, value);
            // droit=noir
            System.out.println("\nminimal inclusion explication : " + explication);


            Set<Map<Variable, String>> set = diagnoser.findAllExplanations(choices, variable, value);
            // droit=noir , droit=noir and hayon=rouge , capot=rouge and droit=noir , capot=rouge and hayon=rouge and droit=noir
            System.out.println("\nall explanations : " + set);

            // droit=noir
            Map<Variable, String> minC = diagnoser.findMinimalCardinalExplanation(choices, variable, value);
            System.out.println("\nminimal cardinal explanation : " + minC);
        } else {
            // no probleme
            System.out.println("\nIsn't an explanation");
        }
    }
}
