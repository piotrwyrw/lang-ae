package net.aelang.runtime.elements;

import net.aelang.ast.Node;
import net.aelang.ast.SolvableNode;
import net.aelang.ast.VariableDeclarationNode;

public class Variable extends Element {

    private SolvableNode value;

    public Variable(String id, SolvableNode value) {
        super(id);
        this.value = value;
    }

    public static Variable from(VariableDeclarationNode node) {
        return new Variable(node.id(), node.def());
    }

    public SolvableNode getValue() {
        return value;
    }

    public void setValue(SolvableNode value) {
        this.value = value;
    }

}
