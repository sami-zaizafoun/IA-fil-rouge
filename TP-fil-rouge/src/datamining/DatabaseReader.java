
package datamining;

import java.io.*;
import java.util.*;
import representations.*;

/**
 * DatabaseReader read a database like this and build a instance of Database
 * var1;var2;var3;
 * value1;value2;value3;
 * value4;value5;value6;
 *          .
 *          .
 *          .
 */
public class DatabaseReader {

    private Set<Variable> variables;

    /**
     * Build an insatance of DatabaseReader
     * @param variables the set of variables
     */
    public DatabaseReader(Set<Variable> variables) {
        this.variables = variables;
    }
    
    /**
     * Build a database after reader a file database
     * @param filename the file name of the database
     * @return the database builded
     * @throws IOException if the filename isn't correct
     */
    public Database importDB(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader (filename));
        Database database = this.readDB(reader);
        reader.close();
        return database;
    }
    
    /**
     * Read a BufferedReader of a database and build a database
     * @param in the bufferedReader of a database file
     * @return the Database builded
     * @throws IOException if a syntax error is related in the database
     */
    private Database readDB(BufferedReader in) throws IOException {
        String variableLine = in.readLine();
        List<Variable> orderedVariables = this.readVariables(variableLine);
        List<Map<Variable, String>> transactions = this.readTransactions(in, orderedVariables);
        return new Database(orderedVariables, transactions);
    }

    /**
     * Read a line and build a list of variable with the string on this line
     * @param line the line to will read
     * @return a list of variables with the name present on the line
     * @throws IOException if a name on the line no corresponding to no variables
     */
    private List<Variable> readVariables(String line) throws IOException {
        List<Variable> variables = new ArrayList<>();
        // split line with the separator ";"
        String[] variableNames = line.split(";");
        for (String variableName : variableNames) {
            // for all variable name of the split
            boolean found = false;
            for (Variable var : this.variables) {
                // for all variables know
                if (var.getName().equals(variableName)) {
                    // if the name of the split corresponding to the variable's name
                    variables.add(var);
                    found = true;
                    break;
                }
            }
            if (!found) {
                // if no variables have the name variableName
                throw new IOException("Unknown variable name: " + variableName);
            }
        }
        return variables;
    }

    /**
     * Read a bufferedReader without variable's name and build a list of transactions
     * @param in the bufferedReader
     * @param orderedVariables the ordered variables to the same order of the values will be assign
     * in the transaction
     * @return the list of transactions builded
     * @throws IOException if the number of values on a line is different to the
     * number of variables in the database
     */
    private List<Map<Variable, String>> readTransactions(
            BufferedReader in, List<Variable> orderedVariables
        )  throws IOException {

        List<Map<Variable, String>> transactions = new ArrayList<>();
        String line;
        int lineNb = 1;
        while ((line = in.readLine()) != null) {
            // while isn't the end of the file
            // split the line with the separator ";"
            String[] values = line.split(";");
            if (values.length != orderedVariables.size()) {
                // if the number of values on a line is different to the number of variables
                throw new IOException("Wrong number of fields on line " + lineNb);
            }
            Map<Variable, String> transaction = new HashMap<>();
            for (int i = 0; i < values.length; i++) {
                // put variable[i] with the value[i] of the line 
                transaction.put(orderedVariables.get(i), values[i]);
            }
            transactions.add(transaction);
            lineNb += 1;
        }
        return transactions;
    }
}
