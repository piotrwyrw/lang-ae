package net.aelang.runtime.elements;

import net.aelang.Pair;
import net.aelang.ast.ImmediateNode;
import net.aelang.ast.InstantiationNode;
import net.aelang.ast.SolvableNode;
import net.aelang.exception.RuntimeError;
import net.aelang.runtime.PersistentEnvironment;

import java.util.HashMap;

public class Instance extends Element {

    private HashMap<String, SolvableNode> values;
    private String type;

    public Instance(String id, String type, HashMap<String, SolvableNode> values) {
        super(id);
        this.type = type;
        this.values = values;
    }

    public static Instance from(InstantiationNode node) throws RuntimeError {
        PersistentEnvironment env = PersistentEnvironment.getInstance();
        Pair<Class<?>, Element> el = env.findElement(node.getType());

        if (el == null)
            throw new RuntimeError("The referenced complex type does not exist \"" + node.getType() + "\"");

        if (el.key() != Complex.class)
            throw new RuntimeError("The element \"" + node.getType() + "\" is not a complex type.");

        Complex type = (Complex) el.val();
        HashMap<String, SolvableNode> fields = new HashMap<>();

        for (String field : type.getFields())
            fields.put(field, new ImmediateNode(0.0));

        return new Instance(node.getId(), node.getType(), fields);
    }

    public SolvableNode getFieldValue(String field) {
        return this.values.get(field);
    }

    public void setFieldValue(String field, SolvableNode node) {
        this.values.put(field, node);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, SolvableNode> getValues() {
        return values;
    }

    public void setValues(HashMap<String, SolvableNode> values) {
        this.values = values;
    }

}
