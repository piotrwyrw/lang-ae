package net.aelang.runtime.elements;

import net.aelang.ast.ComplexDefinitionNode;

public class Complex extends Element {

    private String[] fields;

    public Complex(String id, String[] fields) {
        super(id);
        this.fields = fields;
    }

    public static Complex from(ComplexDefinitionNode node) {
        return new Complex(node.getId(), node.getFields().toArray(new String[0]));
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }
}
