
package datamining;

import java.util.*;

/**
 * This class represent a boolean database, for example if there are 3 items
 * possibles A, B and C. An item present in a transaction are a value to 1 else 0.
 * Transactions possibles are {A=1, B=0, C=1}, {A=0, B=0, C=1}, {A=1, B=1, C=1}, etc...
 * 
 */
public class BooleanDatabase {

    private List<Item> listItems;
    private List<Map<Item, String>> listTransactions;
    
    /**
     * Build a instance of boolean database
     * @param listItems list of all item in the database
     * @param listTransactions all transactions of the database (all cars)
     */
    public BooleanDatabase(List<Item> listItems, List<Map<Item, String>> listTransactions) {
        this.listItems = listItems;
        this.listTransactions = listTransactions;
    }

    /**
     * Getter of the list of items
     * @return the list of items
     */
    public List<Item> getListItems() {
        return this.listItems;
    }

    /**
     * Getter of the list of transactions
     * @return the list of transactions
     */
    public List<Map<Item, String>> getListTransactions() {
        return this.listTransactions;
    }
}
