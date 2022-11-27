package net.aelang.runtime.elements;

import net.aelang.ast.Node;

import java.util.HashMap;

public class Instance extends Element {

    private HashMap<String, Node> values;
    private String function;

    public Instance(String id, String function, HashMap<String, Node> values) {
        super(id);
        this.function = function;
        this.values = values;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public HashMap<String, Node> getValues() {
        return values;
    }

    public void setValues(HashMap<String, Node> values) {
        this.values = values;
    }

}
