
package datamining;

import java.util.*;
import representations.*;

/**
 * This class find the frequent and confiant rules of frequent motifs 
 * 
 */
public class AssociationRuleMiner {

    private Map<Set<Item>, Integer> motifSets;

    /**
     * Build an instance of AssociationRuleMiner
     * @param motifSets the map of motif with its frequence
     */
    public AssociationRuleMiner(Map<Set<Item>, Integer> motifSets) {
        this.motifSets = motifSets;
    }

    /**
     * Calculates all frequent and confiant rules of the set of motifs
     * @param minfr the minimum of frequence
     * @param minconf the minimum of confiance
     * @return a set of frequent and confiant rule
     */
    public Set<Rule> getAssociationsRules(int minfr, double minconf) {
        Set<Rule> rules = new HashSet<>();
        // find the closed motifs of the motifSet
        Set<Set<Item>> closed = this.getClosed(this.motifSets);

        for(Set<Item> motif : closed) {
            // for all motif in the closed motifs
            Set<Rule> rulesGenerated = this.generateRules(motif);
            for (Rule rule : rulesGenerated) {
                // for all rule generated
                if (this.isConfiance(rule, minconf) && this.isFrequence(rule, minfr)) {
                    // if the frequence and the confiance of the rule are more than the
                    // minimum of frequence and confiance
                    rules.add(rule);
                }
            }
        }
        return rules;
    }

    /**
     * Test if the confiance of the rule are superior than a confiance
     * @param rule the rule
     * @param minconf the confiance of the test
     * @return true if the confiance of the rule is superior than the confiance
     */
    public boolean isConfiance(Rule rule, double minconf) {
        return this.confiance(rule) >= minconf;
    }
    
    /**
     * Test if the frequence of the rule are superior than a frequence
     * @param rule the rule
     * @param minfr the frequence of the test
     * @return true if the frequence of the rule is superior than the frequence 
     */
    public boolean isFrequence(Rule rule, int minfr) {
        return this.frequence(rule) >= minfr;
    }
    
    /**
     * Calculates the confiance of a rule
     * @param rule the rule
     * @return the value of the confiance of the rule in [0,1]
     */
    public double confiance(Rule rule) {
        double v1 = this.frequence(rule);
        double v2 = this.frequence(this.createMotifOfRulePart(rule.getPremisse()));
        return v1 / v2;
    }

    /**
     * Build a motif of a part of a rule
     * @param partOfRule the part of rule (premisse of conclusion)
     * @return the motif builded
     */
    public Set<Item> createMotifOfRulePart(Map<Variable, String> partOfRule) {
        Set<Item> motif = new HashSet<>();
        for (Variable var : partOfRule.keySet()) {
            // for all variable of the premisse
            // add item "variable=value" in the motif 
            motif.add(new Item(var, partOfRule.get(var)));
        }
        return motif;
    }

    /**
     * Calculates the frequence of a rule
     * @param rule the rule 
     * @return the occurence of the rule
     */
    public int frequence(Rule rule) {
        Map<Variable, String> scope = new HashMap<>(rule.getPremisse());
        scope.putAll(rule.getConclusion());
        // use createMotifOfRulePart with the union of the premisse and the conclusion
        Set<Item> motif = this.createMotifOfRulePart(scope);
        return this.frequence(motif);
    }
    
    /**
     * Calculates the frequence of a motif
     * @param motif the motif of the calcul
     * @return the value of the frequence of the motif in the motifSet
     */
    public int frequence(Set<Item> motif) {
        return this.motifSets.get(motif);
    }

    /**
     * Find the closed motifs in a map of motif and their frequence
     * @param mapMotifs the map of motif
     * @return a set of closed motifs
     */
    public Set<Set<Item>> getClosed(Map<Set<Item>, Integer> mapMotifs) {
        Set<Set<Item>> closed = new HashSet<>(mapMotifs.keySet());
        for (Set<Item> motif1 : mapMotifs.keySet()) {
            for (Set<Item> motif2 : mapMotifs.keySet()) {
                if (motif1.size() < motif2.size() && this.isInclude(motif1, motif2) && this.haveSameFrequence(motif1, motif2)) {
                    // if the motif1 is include in motif2 and they are the same frequence
                    // the motif1 isn't a closed motif
                    closed.remove(motif1);
                }
            }
        }
        return closed;
    }
    
    /**
     * Find the closed motifs in a map of motif of the class
     * @return a set of closed motifs
     */
    public Set<Set<Item>> getClosed() {
        return this.getClosed(this.motifSets);
    }

    /**
     * Test if a motif is include in a other motif
     * @param motif1 the first motif
     * @param motif2 the second motif
     * @return true if the motif1 is include in the motif2
     */
    public boolean isInclude(Set<Item> motif1, Set<Item> motif2) {
        if (motif1.size() <= motif2.size()) {
            // if the size of motif 1 is less of equals than the size of the motif2
            for (Item item : motif1) {
                // for all item in the motif1
                if (!motif2.contains(item)) {
                    // if the item isn't in the motif2
                    return false;
                }
            }
            // if all item of the motif1 is in the motif2
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Test if the motif1 and the motif2 have the same frequence
     * @param motif1 the fisrt motif
     * @param motif2 the second motif
     * @return true if the motif1 and the motif2 have the same frequence
     */
    public boolean haveSameFrequence(Set<Item> motif1, Set<Item> motif2) {
        return this.frequence(motif1) == this.frequence(motif2);
    }

    /**
     * Generate all subsets of a motif
     * @param motif the motif
     * @return a set of all subsets of the motif 
     */
    public Set<Set<Item>> generateSubSets(Set<Item> motif) {
        Set<Set<Item>> subSets = new HashSet<>();
        Set<Set<Item>> subSetPrec = new HashSet<>();
        // add the empty set
        subSetPrec.add(new HashSet<>());
        Set<Set<Item>> subSetRes;
        Set<Item> motifRes;
        for (int k = 1; k<motif.size(); k++) {
            // for size 1 to motif size, generates the subsets with the length k
            subSetRes = new HashSet<>();
            for (Set<Item> subMotif : new HashSet<>(subSetPrec)) {
                // for all previous subsets
                for (Item item : motif) {
                    // for all item of the motif
                    if (subMotif.isEmpty() || item.compareTo(Collections.max(subMotif)) > 0) {
                        // if the previous subset if the empty set or if the item
                        // is superior of the max of th previous subset
                        // add the motif with a new item
                        motifRes = new HashSet<>(subMotif);
                        motifRes.add(item);
                        subSetRes.add(motifRes);
                    }
                }
            }
            // modif the previous subsets and add it in the set of all subsets
            subSetPrec = new HashSet<>(subSetRes);
            subSets.addAll(subSetPrec);
        }
        // add the motif in the subsets
        subSets.add(new HashSet<>(motif));
        return subSets;
    }

    /**
     * Build a motif : {motif1}\{motif2}
     * @param motif1 the first motif
     * @param motif2 the second motif
     * @return the motif {motif1}\{motif2}
     */
    public Set<Item> motifPrivateOfMotif(Set<Item> motif1, Set<Item> motif2) {
        Set<Item> res = new HashSet<>();
        for (Item item : motif1) {
            // for all item in the motif1
            if (!motif2.contains(item)) {
                // if the motif2 not contains the item
                // add the item to the motif
                res.add(item);
            }
        }
        return res;
    }
    
    /**
     * Build a map variable/value with a motif
     * @param motif the motif
     * @return a map of variable/value
     */
    public Map<Variable, String> buildMap(Set<Item> motif) {
        Map<Variable, String> part = new HashMap<>();
        for (Item item : motif) {
            // for all item in the motif
            // put its variable with its value in the map
            part.put(item.getVariable(), item.getValue());
        }
        return part;
    }

    /**
     * Build a rule with two motifs (one for the premisse and the second for the
     * conclusion)
     * @param motifPremisse the motif of the premisse
     * @param motifConclusion the motif of the conclusion
     * @return the rule builded
     */
    public Rule buildRule(Set<Item> motifPremisse, Set<Item> motifConclusion) {
        Map<Variable, String> premisse = this.buildMap(motifPremisse);
        Map<Variable, String> conclusion = this.buildMap(motifConclusion);
        return new Rule(premisse, conclusion);
    }

    /**
     * Generates rules on a motif
     * @param motif the motif
     * @return the set of rules generated
     */
    public Set<Rule> generateRules(Set<Item> motif) {
        Set<Rule> rules = new HashSet<>();
        Set<Set<Item>> subSetsOfMotif = this.generateSubSets(motif);
        Set<Item> complementMotif;
        for (Set<Item> subMotif : subSetsOfMotif) {
            // for all subsets of the motif
            if (subMotif.size() < motif.size()) {
                // if the subset isn't the motif
                // complementMotif is {motif}\{subMotif} it will be the conclusion
                // of the rule
                complementMotif = this.motifPrivateOfMotif(motif, subMotif);
                // build rule subMotif -> complementMotif
                Rule rule = this.buildRule(subMotif, complementMotif);
                rules.add(rule);
            }
        }
        return rules;
    }
}
