
package datamining;

import representations.*;

/**
 * A item represent a pair Variable/value
 * 
 */
public class Item implements Comparable<Item> {

    private Variable variable;
    private String value;

    /**
     * Build a item with a variable and its value
     * @param var the variable
     * @param value the value of the variable
     */
    public Item(Variable var, String value) {
        this.variable = var;
        this.value = value;
    }

    /**
     * Getter of the variable
     * @return the variable of the item
     */
    public Variable getVariable() {
        return this.variable;
    }

    /**
     * Getter of the value
     * @return the value of the item
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Test if the item is equals to an other item
     * @param other the other item
     * @return true if the other item is equals to this item
     */
    public boolean isEquals(Item other) {
        Variable var = other.getVariable();
        String value = other.getValue();
        // two item are equals if there are the same variable and value
        return this.variable.equals(var) && this.value.equals(value);
    }

    /**
     * Test if the item is equals to an other object
     * @param o the other object
     * @return true if the other object is the same item to this item
     */
    @Override
    public boolean equals(Object o) {
        if (this==o) {
            return true;
        } else {
            if (o instanceof Item) {
                return this.isEquals(((Item)o));
            } else {
                return false;
            }
        }
    }

    /**
     * HashCode fonction of a item
     * @return the hashCode of item
     */
    @Override
    public int hashCode() {
        return this.variable.hashCode() * this.value.hashCode();
    }
    
    /**
     * Compare this item to an other item with the comparation of their variable
     * @param other the other item
     * @return the comparation of this item's variable and the other item's variable
     */
    @Override
    public int compareTo(Item other) {
        return this.variable.compareTo(other.getVariable());
    }

    /**
     * Build a String representation of an item
     * @return the string representation of the item
     */
    @Override
    public String toString() {
        return this.variable + "=" + this.value;
    }
}
