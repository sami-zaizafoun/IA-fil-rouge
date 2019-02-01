
package representations;

import java.util.Set;

/**
 * A variable is represented by a name and a domain
 *
 */
public class Variable implements Comparable<Variable> {

    private String name;
    private Set<String> domaine;

    /**
     * Construct an instance of Variable
     * @param name the variable's name
     * @param domaine all of the varible's possible values
     */
    public Variable(String name, Set<String> domaine) {
        this.name = name;
        this.domaine = domaine;
    }

    /**
     * Getter method of the variable's name
     * @return the variable's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method of the variable's domain
     * @return the variable's domain
     */
    public Set<String> getDomaine(){
        return this.domaine;
    }

    /**
     * The equals test
     * @param o the other object of the comparison
     * @return true if the variable and the object have the same name
     */
    @Override
    public boolean equals(Object o) {
        if(this==o) {
          return true;
        } else {
            if(o instanceof Variable) {
                return this.name.equals(((Variable) o).getName());
            } else {
                return false;
            }
        }
    }

    /**
     * Hash function of the variable's object with the variable's name
     * @return the hashcode
     */
    @Override
    public int hashCode(){
      int result = 17;
      result = 31 * this.name.hashCode();
      return result;
    }

    /**
     * The reprensentation of a variable's name
     * @return the string of the variable's name
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Compares the given variable's name with this
     * @param variable the variable that we want to compare with this
     */
    @Override
    public int compareTo(Variable variable) {
        return this.name.compareTo(variable.getName());
    }
}
