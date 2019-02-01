
package representations;

import java.util.*;
import examples.*;

/**
 * A test class that tests the satisfaction of contraintes
 *
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Examples ex = new Examples();

        List<Map<Variable, String>> voitures = new ArrayList<>();
        voitures.add(ex.getVoiture1());
        voitures.add(ex.getVoiture2());
        voitures.add(ex.getVoiture3());
        voitures.add(ex.getVoiture4());

        List<Constraint> constrains = new ArrayList<>();
        constrains.add(ex.getExemple1());
        constrains.add(ex.getExemple2());
        constrains.add(ex.getExemple3());
        constrains.add(ex.getExemple4());

        // display of contraintes
        System.out.println("\nContraintes :");
        int n = 1;
        for (Constraint c : constrains) {
            System.out.println("c" + n + " : " + c);
            n++;
        }

        // display of the cars and their satisfaction with each constraint
        for (Map<Variable, String> voiture : voitures) {
            System.out.println("\n-----------------------------");
            System.out.println("Voiture : " + voiture + "\n");
            n = 1;
            System.out.println("Satisfaction : ");
            for (Constraint c : constrains) {
                System.out.println("c" + n + " : " + c.isSatisfiedBy(voiture));
                n++;
            }
        }

    }
}
