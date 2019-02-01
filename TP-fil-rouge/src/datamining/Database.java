
package datamining;

import java.util.*;
import representations.*;

/**
 * A database contains all variables with their value, for examples :
 * {droit=noir, hayon=blanc, ... , toit=blanc}, 
 * {gauche=rouge, capot=noir,... , toit ouvrant=true}, etc...
 * 
 */
public class Database {

    private List<Variable> listVariables;
    private List<Map<Variable, String>> listTransactions;

    /**
     * Build a insatance of Database
     * @param listVariables list of variables in the database
     * @param listTransactions list of transactions of database
     */
    public Database(List<Variable> listVariables, List<Map<Variable, String>> listTransactions) {
        this.listVariables = listVariables;
        this.listTransactions = listTransactions;
    }
    
    /**
     * Build a boolean database about this database
     * @return the boolean database builded
     */
    public BooleanDatabase toBooleanDatabase() {
        // all motifs possible present in the database
        // use a set fto have just one of each item
        Set<Item> motifs = new HashSet<>();
        List<Map<Item, String>> booleanTransactions = new ArrayList<>();
        Item itemTemp;
        Map<Item, String> transactionTemp;
        for (Map<Variable, String> transaction : this.listTransactions) {
            // for all transactions
            transactionTemp = new HashMap<>();
            for (Variable var : transaction.keySet()) {
                // for variables of the transaction
                for (String value : var.getDomaine()) {
                    // for all values of the variable's domain
                    // build the item with the variable and a value of its doamin
                    itemTemp = new Item(var, value);
                    if (transaction.get(var).equals(value)) {
                        // if the "variable=value" is in the transaction
                        // put the item with "1"
                        motifs.add(itemTemp);
                        transactionTemp.put(itemTemp, "1");
                    } else {
                        // put the item with "0"
                        transactionTemp.put(itemTemp, "0");
                    }
                }
            }
            // add the transaction in the boolean form in the list of transactions
            booleanTransactions.add(transactionTemp);
        }
        // return a boolean database with the list of motifs ans the list of boolean
        // transactions
        return new BooleanDatabase(new ArrayList<>(motifs), booleanTransactions);
    }
}
