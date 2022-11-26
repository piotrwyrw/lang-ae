package net.aelang;

import net.aelang.ast.Node;

public class Variable {

    private String id;
    private Node value;

    public Variable(String id, Node value) {
        this.id = id;
        this.value = value;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Node value() {
        return value;
    }

    public void setValue(Node value) {
        this.value = value;
    }

}
