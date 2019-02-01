
package examples;

import java.util.*;
import planning.*;
import representations.*;

/**
 * Simulation of an assembly line
 *
 */
public class AssemblyLine {

    private Map<Variable, String> voiture;
    private Map<String, Variable> mapVar;
    private Set<String> domainBooleans;
    private Set<String> domainColors;
    private Set<String> elementsBool;
    private Set<String> componentsColor;
    private Set<Variable> HAS_ELEMENT;
    private Set<Variable> ELEMENT_COLOR;

    /**
     * Build all the lists and maps needed with a domain colors in the domain color
     */
    public AssemblyLine() {
        this(true);
    }

    /**
     * Build all the lists and maps needed
     * @param manyColors true if we want to have 8 colors in the domain of colors or false if we want
     * to have 3 colors
     */
    public AssemblyLine(boolean manyColors) {
        // set of all the names of variables with a boolean domain
        this.elementsBool = new HashSet<>(Arrays.asList(
                "HAS_CHASSIS", "HAS_FRONT_LEFT_WHEEL", "HAS_FRONT_RIGHT_WHEEL",
                "HAS_REAR_LEFT_WHEEL", "HAS_REAR_RIGHT_WHEEL", "HAS_BODY"
        ));

        // boolean's domain
        this.domainBooleans = new HashSet<>(Arrays.asList(
                "TRUE", "FALSE"
        ));

        // set of all the names of variables with a color domain
        this.componentsColor = new HashSet<>(Arrays.asList(
                "FRONT_COLOR", "LEFT_COLOR", "REAR_COLOR", "RIGHT_COLOR", "ROOF_COLOR",
                "FRONT_LEFT_WHEEL_COLOR", "FRONT_RIGHT_WHEEL_COLOR", "REAR_LEFT_WHEEL_COLOR",
                "REAR_RIGHT_WHEEL_COLOR", "BODY_COLOR"
        ));


        // colors' domain
        if (manyColors) {
            this.domainColors = new HashSet<>(Arrays.asList(
            "GRAY", "BLACK", "WHITE", "RED", "BLUE", "GREEN", "ORANGE", "YELLOW"
            ));
        } else {
            this.domainColors = new HashSet<>(Arrays.asList(
            "GRAY", "BLACK", "RED"
            ));
        }

        // set of variables with a boolean domain
        this.HAS_ELEMENT = new HashSet<>();

        // map to get a variable with its name
        this.mapVar = new HashMap<>();

        for (String varBoolean : this.elementsBool) {
            // for all names of boolean components
            // build a variable with the name and the boolean domain
            Variable var = new Variable(varBoolean, domainBooleans);
            // add the variable to the set of variable's boolean
            this.HAS_ELEMENT.add(var);
            // put the names of the variables and the variables on the map
            this.mapVar.put(varBoolean, var);
        }

        // set of the variable with colors domain
        this.ELEMENT_COLOR = new HashSet<>();

        for (String component : this.componentsColor) {
            // for all the names of components with colors domain
            // build a variable with the name and the colors domain
            Variable var = new Variable(component, this.domainColors);
            // add the variable to the set of variables colors domain
            this.ELEMENT_COLOR.add(var);
            // put the names of variables and the variable on the map
            this.mapVar.put(component, var);
        }

        // initial car
        this.voiture = this.initializeCar("GRAY");
    }

    /**
     * Builds a state with the car of this class
     * @return the state built
     */
    public State getInitState() {
        return new State(this.voiture);
    }

    /**
     * Builds a random state with a car with one random color
     * @return the state built
     */
    public State generateGoal() {
        return new State(this.generateCar());
    }

    /**
     * Builds all actions possbile of a problem
     * @return a set of all actions
     */
    public Set<Action> getActions() {
        Set<Action> actions = new HashSet<>();
        for (String color : this.domainColors) {
            // for all colors add all paints possible
            actions.add(this.paintRear(color));
            actions.add(this.paintFront(color));
            actions.add(this.paintLeft(color));
            actions.add(this.paintRight(color));
            actions.add(this.paintRoof(color));
        }

        for (String comp : this.elementsBool) {
            // for all component add all unique installations possible
            actions.add(this.installPiece(comp));
        }
        // add the action of placing two wheels
        actions.add(this.installRightWheel());
        actions.add(this.installLeftWheel());
        actions.add(this.installFrontWheel());
        actions.add(this.installRearWheel());


        return actions;
    }

    /**
     * Choose a string value in a set and return it
     * @param setValues the set of values
     * @return the value chosen
     */
    public String choiceValue(Set<String> setValues) {
        Random r = new Random();
        // a random value between 0 and the set's size
        int valueRandom = r.nextInt(setValues.size());
        int i = 0;
        for (String value : setValues) {
            // for all value in the set
            if (i++ == valueRandom) {
                // if this value's position (not the position in the set)
                // corresponds to the counter, return the value
                return value;
            }
        }
        // never returns null
        return null;
    }

    /**
     * Builds a car with all colors to colorInitial and all pieces to FALSE except
     * the chassis
     * @param colorInitial the initial colors of all the pieces
     * @return an initialized car
     */
    public Map<Variable, String> initializeCar(String colorInitial) {
        Map<Variable, String> voiture = new HashMap<>();
        for (String varBoolean : this.elementsBool) {
            // for all names of boolean components
            //set the value of variables to FALSE in the car
            voiture.put(this.mapVar.get(varBoolean), "FALSE");
        }
        // a car has at least a chassis or else it has nothing
        voiture.put(this.mapVar.get("HAS_CHASSIS"), "TRUE");
        for (String component : this.componentsColor) {
            // for all components with a color domain
            // initialized in the car, turn this component to GRAY
            voiture.put(this.mapVar.get(component), colorInitial);
        }
        return voiture;
    }

    /**
     * Create a random car
     * @return the built car
     */
    public Map<Variable, String> generateCar() {
        // make a copy of the initial car to the beginning
        Map<Variable, String> voiture = new HashMap<>(this.voiture);
        // get a random color
        String colorSelect = choiceValue(this.domainColors);
        for (Variable var : this.HAS_ELEMENT) {
            // for all variables in the copy, set all boolean variables to TRUE
            voiture.put(var, "TRUE");
        }
        for (Variable var : this.ELEMENT_COLOR) {
            // for all variables in the copy, set all color variables to the color chosen
            voiture.put(var, colorSelect);
        }
        return voiture;
    }

    /**
     * Builds a part of a rule (premisse or conclusion)
     * @param varStr the string names of variables
     * @param values the values of each variable or the value to assign to each
     * variable
     * @return the built part of a rule
     */
    public Map<Variable, String> buildMap(ArrayList<String> varStr, ArrayList<String> values) {
        Map<Variable, String> map = new HashMap<>();
        for (int i = 0; i<varStr.size(); i++) {
            map.put(
                    // the variable's name
                    this.mapVar.get(varStr.get(i)),
                    // the modulo if there is one value gift else the value in
                    // the same position as the name of variable
                    values.get(Math.floorMod(i, values.size()))
            );
        }
        return map;
    }

    /**
     * Build the action, install the piece if there is a chassis
     * @param piece the piece to install
     * @return the built action
     */
    public Action installPiece(String piece) {
        Set<Rule> setRules = new HashSet<>();

        // the list of variables engaged in the premisse
        ArrayList<String> varStrP1 = new ArrayList<>(Arrays.asList(
                "HAS_CHASSIS"
        ));
        // the value assigned to the variable
        ArrayList<String> valeursP1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the premisse
        Map<Variable, String> premisse1 = buildMap(varStrP1, valeursP1);

        // the list variables engaged in the conclusion
        ArrayList<String> varStrC1 = new ArrayList<>(Arrays.asList(
                piece
        ));
        // the value assigned to the variable
        ArrayList<String> valeursC1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the conclusion
        Map<Variable, String> conclusion1 = buildMap(varStrC1, valeursC1);

        // add the rule to the set of rules
        setRules.add(new Rule(premisse1,conclusion1));

        // the cost of the action to install one piece is 2
        return new Action(setRules, 2);
    }

    /**
     * Paint the roof and the body if there is a body
     * @param color the color of the paint
     * @see AssemblyLine#buildMap
     * @return the built action
     */
    public Action paintRoof(String color) {
        Set<Rule> setRules = new HashSet<>();

        // the list of names of variables engaged in the premisse
        ArrayList<String> varStrP1 = new ArrayList<>(Arrays.asList(
                "HAS_BODY"
        ));

        // the value assigned to the variable
        ArrayList<String> valeursP1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the premisse
        Map<Variable, String> premisse1 = buildMap(varStrP1, valeursP1);

        // the list of variables engaged in the conclusion
        ArrayList<String> varStrC1 = new ArrayList<>(Arrays.asList(
                "ROOF_COLOR", "BODY_COLOR"
        ));

        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursC1 = new ArrayList<>(Arrays.asList(color));

        // build the conclusion
        Map<Variable, String> conclusion1 = buildMap(varStrC1, valeursC1);

        // add the rule to the set of rules
        setRules.add(new Rule(premisse1,conclusion1));

        // the cost of the action to paint two pieces is 1
        return new Action(setRules, 1);
    }

    /**
     * Paint the rear and the roof if there is a body
     * @param color the color of the paint
     * @see AssemblyLine#buildMap
     * @return the built action
     */
    public Action paintRear(String color) {
        Set<Rule> setRules = new HashSet<>();

        // the list of variables engaged in the premisse
        ArrayList<String> varStrP1 = new ArrayList<>(Arrays.asList(
                "HAS_BODY"
        ));
        // the value assigned to the variable
        ArrayList<String> valeursP1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the premisse
        Map<Variable, String> premisse1 = buildMap(varStrP1, valeursP1);

        // the list of variables engaged in the conclusion
        ArrayList<String> varStrC1 = new ArrayList<>(Arrays.asList(
                "REAR_COLOR","ROOF_COLOR"
        ));
        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursC1 = new ArrayList<>(Arrays.asList(color));

        // build the conclusion
        Map<Variable, String> conclusion1 = buildMap(varStrC1, valeursC1);

        // add the rule to the set of rules
        setRules.add(new Rule(premisse1,conclusion1));

        // the cost of the action to paint two pieces is 1
        return new Action(setRules, 1);
    }

    /**
     * Paint the roof and the front if there are a chassis and a body
     * @param color the color of the paint
     * @see AssemblyLine#buildMap
     * @return the builded action
     */
    public Action paintFront(String color) {
        Set<Rule> setRules = new HashSet<>();

        // the list of variables engaged in the premisse
        ArrayList<String> varStrP1 = new ArrayList<>(Arrays.asList(
                "HAS_CHASSIS", "HAS_BODY"
        ));
        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursP1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the premisse
        Map<Variable, String> premisse1 = buildMap(varStrP1, valeursP1);

        // the list of variables engaged in the conclusion
        ArrayList<String> varStrC1 = new ArrayList<>(Arrays.asList(
                "ROOF_COLOR", "FRONT_COLOR"
        ));
        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursC1 = new ArrayList<>(Arrays.asList(color));

        // build the conclusion
        Map<Variable, String> conclusion1 = buildMap(varStrC1, valeursC1);

        // add the rule to the set of rules
        setRules.add(new Rule(premisse1,conclusion1));

        // the cost of the action to paint the two pieces is 1
        return new Action(setRules, 1);
    }

    /**
     * Paint the roof, front and rear left wheel and the left if there's a
     * chassis, a body, a front and a rear left wheel
     * @param color the color of the paint
     * @see AssemblyLine#buildMap
     * @return the builded action
     */
    public Action paintLeft(String color) {
        Set<Rule> setRules = new HashSet<>();

        // the list of variables engaged in the premisse
        ArrayList<String> varStrP1 = new ArrayList<>(Arrays.asList(
                "HAS_CHASSIS", "HAS_BODY", "HAS_FRONT_LEFT_WHEEL", "HAS_REAR_LEFT_WHEEL"
        ));
        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursP1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the premisse
        Map<Variable, String> premisse1 = buildMap(varStrP1, valeursP1);

        // the list of variables engaged in the conclusion
        ArrayList<String> varStrC1 = new ArrayList<>(Arrays.asList(
                "ROOF_COLOR", "FRONT_LEFT_WHEEL_COLOR", "REAR_LEFT_WHEEL_COLOR", "LEFT_COLOR"
        ));
        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursC1 = new ArrayList<>(Arrays.asList(color));

        // build the conclusion
        Map<Variable, String> conclusion1 = buildMap(varStrC1, valeursC1);

        // add the rule to the set of rules
        setRules.add(new Rule(premisse1,conclusion1));

        // the cost of the action to paint two or more pieces is 1
        return new Action(setRules, 1);
    }

    /**
     * Paint the roof, front and rear right wheel and the right if there is a
     * chassis, a body, a front and a rear right wheel
     * @param color the color of the paint
     * @see AssemblyLine#buildMap
     * @return the builded action
     */
    public Action paintRight(String color) {
        Set<Rule> setRules = new HashSet<>();

        // the list of variables engaged in the premisse
        ArrayList<String> varStrP1 = new ArrayList<>(Arrays.asList(
                "HAS_CHASSIS", "HAS_BODY", "HAS_FRONT_RIGHT_WHEEL", "HAS_REAR_RIGHT_WHEEL"
        ));
        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursP1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the premisse
        Map<Variable, String> premisse1 = buildMap(varStrP1, valeursP1);

        // the list of variables engaged in the conclusion
        ArrayList<String> varStrC1 = new ArrayList<>(Arrays.asList(
                "ROOF_COLOR", "FRONT_RIGHT_WHEEL_COLOR", "REAR_RIGHT_WHEEL_COLOR", "RIGHT_COLOR"
        ));
        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursC1 = new ArrayList<>(Arrays.asList(color));

        // build the conclusion
        Map<Variable, String> conclusion1 = buildMap(varStrC1, valeursC1);

        // add the rule to the set of rules
        setRules.add(new Rule(premisse1,conclusion1));

        // the cost of the action to paint two or more pieces is 1
        return new Action(setRules, 1);
    }

    /**
     * Install the right wheels
     * @see AssemblyLine#buildMap
     * @return the builded action
     */
    public Action installRightWheel() {
        Set<Rule> setRules = new HashSet<>();

        // the list of variables engaged in the premisse
        ArrayList<String> varStrP1 = new ArrayList<>(Arrays.asList(
                "HAS_CHASSIS"
        ));
        // the value assigned to the variables
        ArrayList<String> valeursP1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the premisse
        Map<Variable, String> premisse1 = buildMap(varStrP1, valeursP1);

        // the list of variables engaged in the conclusion
        ArrayList<String> varStrC1 = new ArrayList<>(Arrays.asList(
                "HAS_FRONT_RIGHT_WHEEL", "HAS_REAR_RIGHT_WHEEL"
        ));
        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursC1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the conclusion
        Map<Variable, String> conclusion1 = buildMap(varStrC1, valeursC1);

        // add the rule to the set of rules
        setRules.add(new Rule(premisse1,conclusion1));

        // the cost of the action to paint two or more pieces is 1
        return new Action(setRules, 1);
    }

    /**
     * Install the left wheels
     * @see AssemblyLine#buildMap
     * @return the builded action
     */
    public Action installLeftWheel() {
        Set<Rule> setRules = new HashSet<>();

        // the list of variables engaged in the premisse
        ArrayList<String> varStrP1 = new ArrayList<>(Arrays.asList(
                "HAS_CHASSIS"
        ));
        // the value assigned to the variables
        ArrayList<String> valeursP1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the premisse
        Map<Variable, String> premisse1 = buildMap(varStrP1, valeursP1);

        // the list of variables engaged in the conclusion
        ArrayList<String> varStrC1 = new ArrayList<>(Arrays.asList(
                "HAS_FRONT_LEFT_WHEEL", "HAS_REAR_LEFT_WHEEL"
        ));
        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursC1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the conclusion
        Map<Variable, String> conclusion1 = buildMap(varStrC1, valeursC1);

        // add the rule to the set of rules
        setRules.add(new Rule(premisse1,conclusion1));

        // the cost of the action to paint two or more pieces is 1
        return new Action(setRules, 1);
    }

    /**
     * Install the front wheels
     * @see AssemblyLine#buildMap
     * @return the builded action
     */
    public Action installFrontWheel() {
        Set<Rule> setRules = new HashSet<>();

        // the list of variables engaged in the premisse
        ArrayList<String> varStrP1 = new ArrayList<>(Arrays.asList(
                "HAS_CHASSIS"
        ));
        // the value assigned to the variables
        ArrayList<String> valeursP1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the premisse
        Map<Variable, String> premisse1 = buildMap(varStrP1, valeursP1);

        // the list of variables engaged in the conclusion
        ArrayList<String> varStrC1 = new ArrayList<>(Arrays.asList(
                "HAS_FRONT_LEFT_WHEEL", "HAS_FRONT_RIGHT_WHEEL"
        ));
        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursC1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the conclusion
        Map<Variable, String> conclusion1 = buildMap(varStrC1, valeursC1);

        // add the rule to the set of rules
        setRules.add(new Rule(premisse1,conclusion1));

        // the cost of the action to paint two or more pieces is 1
        return new Action(setRules, 1);
    }

    /**
     * Install the rear wheels
     * @see AssemblyLine#buildMap
     * @return the builded action
     */
    public Action installRearWheel() {
        Set<Rule> setRules = new HashSet<>();

        // the list of variables engaged in the premisse
        ArrayList<String> varStrP1 = new ArrayList<>(Arrays.asList(
                "HAS_CHASSIS"
        ));
        // the value assigned to the variables
        ArrayList<String> valeursP1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the premisse
        Map<Variable, String> premisse1 = buildMap(varStrP1, valeursP1);

        // the list of variables engaged in the conclusion
        ArrayList<String> varStrC1 = new ArrayList<>(Arrays.asList(
                "HAS_REAR_LEFT_WHEEL", "HAS_REAR_RIGHT_WHEEL"
        ));
        // the value assigned to the variables. Here, there is one value in the
        // list, it will be assigned to all the variables with the modulo
        ArrayList<String> valeursC1 = new ArrayList<>(Arrays.asList("TRUE"));

        // build the conclusion
        Map<Variable, String> conclusion1 = buildMap(varStrC1, valeursC1);

        // add the rule to the set of rules
        setRules.add(new Rule(premisse1,conclusion1));

        // the cost of the action to paint two or more pieces is 1
        return new Action(setRules, 1);
    }
}
