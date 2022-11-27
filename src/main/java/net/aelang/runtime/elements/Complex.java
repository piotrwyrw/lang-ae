package net.aelang.runtime.elements;

public class Complex extends Element {

    private String[] fields;

    public Complex(String id, String[] fields) {
        super(id);
        this.fields = fields;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }
}
