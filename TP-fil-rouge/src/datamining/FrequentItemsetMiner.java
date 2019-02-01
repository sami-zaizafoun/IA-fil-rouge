
package datamining;

import java.util.*;

/**
 * This class find the frequent items of a boolean database
 *
 */
public class FrequentItemsetMiner {

    public BooleanDatabase database;

    /**
     * Build an instance of FrequentItemsetMiner
     * @param database a boolean database
     */
    public FrequentItemsetMiner(BooleanDatabase database) {
        this.database = database;
    }

    /**
     * Build a map of the motifs and their frequence
     * @param minfr the minimum of the frequence you want
     * @return the map of frequence of motifs builded
     */
    public Map<Set<Item>, Integer> frequentItemsets(int minfr) {
        Map<Set<Item>, Integer> mapFrequent = new HashMap<>();
        // liste des items de la baseBooleenne
        List<Item> listItems = this.database.getListItems();
        List<Map<Item, String>> listTransactions = this.database.getListTransactions();
        Set<Set<Item>> motifs = null;
        // k is the size of motifs builded
        int k = 1;
        while (motifs==null || !motifs.isEmpty()) {
            // while there are no more combination possible
            if (k==1) {
                // build motifs with the size 1
                motifs = this.getSingletons(listItems);
            } else {
                // make a combination between all motifs of size k
                motifs = combinations(motifs);
            }
            // on retire les motifs qui ne sont pas frequents pour faire un 
            // elagage
            for (Set<Item> motif : new HashSet<>(motifs)) {
                // for all motifs of combination builded
                int frequence = this.frequence(listTransactions, motif);
                if (frequence >= minfr) {
                    // if the motif has a frequence more important than the minimum
                    // of frequence
                    mapFrequent.put(motif, frequence);
                } else {
                    // remove the motif to the combination
                    motifs.remove(motif);
                }
            }
            k ++;
        }
        return mapFrequent;
    }

    /**
     * Build a set of motifs with the size 1 of a list of item
     * @param listItem the list of item
     * @return the set of motifs with size 1
     */
    public Set<Set<Item>> getSingletons(List<Item> listItem) {
        Set<Set<Item>> singletons = new HashSet<>();
        Set<Item> motif;
        for (Item item : listItem) {
            // for all item of the list of items
            // add a motif with size 1 in the set
            motif = new HashSet<>();
            motif.add(item);
            singletons.add(motif);
        }
        return singletons;
    }

    /**
     * Creates all combinations of a set of motifs
     * @param setMotif the set of motifs
     * @return a set of all combination
     */
    public Set<Set<Item>> combinations(Set<Set<Item>> setMotif) {
        Set<Set<Item>> combinations = new HashSet<>();
        Set<Item> motifTemp;
        for (Set<Item> motif1 : setMotif) {
            for (Set<Item> motif2 : setMotif) {
                if (motif1 != motif2 && this.hasSameKFirstElements(motif1, motif2)) {
                    // if the motif1 and motif2 has the same K first elements
                    // do the combination between motifs
                    motifTemp = new HashSet<>();
                    motifTemp.addAll(motif1);
                    motifTemp.addAll(motif2);
                    combinations.add(motifTemp);
                }
            }
        }
        return combinations;
    }

    /**
     * Test if two motifs has the same k first element with k the minimum (size-1)
     * of motifs
     * @param motif1 the first motif
     * @param motif2 the second motif
     * @return true if they are the same k first elements
     */
    public boolean hasSameKFirstElements(Set<Item> motif1, Set<Item> motif2) {
        Iterator<Item> iter1 = motif1.iterator();
        Iterator<Item> iter2 = motif2.iterator();
        // take the minimum of size-1 of motifs to avoid error with iterator
        int k = Math.min(motif1.size() - 1, motif2.size() - 1);
        Item item1;
        Item item2;
        while (k > 0) {
            // take the i th element of each set
            item1 = iter1.next();
            item2 = iter2.next();
            if (!item1.equals(item2)) {
                // if the elements are differents
                return false;
            }
            k--;
        }
        return true;
    }

    /**
     * Calculates the frequence of a item in a list of transactions 
     * @param listTransactions the list of transactions
     * @param motif the motif
     * @return the number of occurence of the motif in the list of transactions
     */
    public int frequence(List<Map<Item, String>> listTransactions, Set<Item> motif) {
        int cpt = 0;
        Item itemTest = null;
        for (Map<Item, String> transaction : listTransactions) {
            // for all transactions
            boolean itemInTransaction = true;
            for (Item item : motif) {
                // for all item in the motif
                if (transaction.get(item).equals("0")) {
                    // if the item isn't in the transaction
                    itemInTransaction = false;
                    break;
                }
            }
            if (itemInTransaction) {
                // if all item of the motif are in the transaction
                cpt += 1;
            }
        }
        return cpt;
    }
}
