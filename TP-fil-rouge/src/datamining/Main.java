
package datamining;

import java.io.*;
import java.util.*;
import representations.*;
import examples.Examples;
import java.security.InvalidParameterException;

/**
 * classe qui test la generation de regles et de motifs frequents
 * 
 */
public class Main {
    public static void main(String[] args) throws InvalidParameterException {

        // if the number of parameters is invalid
        if (args.length != 3) {
            throw new InvalidParameterException("Need 3 parameters (file name of database, minfr, minconf). You gave " + args.length + " parameters.");
        }

        String filename = args[0];
        int minfr;
        double minconf;

        // try to convert the parameters of minfr and minconf
        try {
            minfr = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("the second parameter must be an integer, you gave : " + args[1]);
        }
        try {
            minconf = Double.parseDouble(args[2]);
        } catch (NumberFormatException e2) {
            throw new InvalidParameterException("the third parameter must be a float, you gave : " + args[2]);
        }

        Examples ex = new Examples();
        DatabaseReader data = new DatabaseReader(ex.getVariables());
        Database database = null;

        try {
            database = data.importDB(filename);
        } catch(IOException excep) {
            excep.printStackTrace();
        }

        BooleanDatabase bdb = database.toBooleanDatabase();

        FrequentItemsetMiner frim = new FrequentItemsetMiner(bdb);

        Map<Set<Item>, Integer> itemsets = frim.frequentItemsets(minfr);
        AssociationRuleMiner arm = new AssociationRuleMiner(itemsets);
        Set<Rule> rules = arm.getAssociationsRules(minfr, minconf);
        
        Set<Set<Item>> closed = arm.getClosed();
        
        System.out.println("Nombre de motifs frequents : " + itemsets.size());
        System.out.println("Nombre de motifs fermes frequents : " + closed.size());
        
        System.out.println("\n" + rules.size() + " rules :");

        for (Rule rule1 : rules) {
            System.out.println("\n" + rule1);
        }
    }
}
