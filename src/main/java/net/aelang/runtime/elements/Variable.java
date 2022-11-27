package net.aelang.runtime.elements;

import net.aelang.ast.Node;
import net.aelang.ast.VariableDeclarationNode;

public class Variable extends Element {

    private Node value;

    public Variable(String id, Node value) {
        super(id);
        this.value = value;
    }

    public static Variable from(VariableDeclarationNode node) {
        return new Variable(node.id(), node.def());
    }

    public Node getValue() {
        return value;
    }

    public void setValue(Node value) {
        this.value = value;
    }

}
