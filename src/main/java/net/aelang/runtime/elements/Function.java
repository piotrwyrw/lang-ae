package net.aelang.runtime.elements;

import net.aelang.ast.FunctionDefinitionNode;
import net.aelang.ast.Node;

public class Function extends Element {

    private int parameters;
    private Node expression;

    public Function(String id, int parameters, Node expression) {
        super(id);
        this.parameters = parameters;
        this.expression = expression;
    }

    public static Function from(FunctionDefinitionNode node) {
        return new Function(node.id(), node.params(), node.expr());
    }

    public int getParameters() {
        return parameters;
    }

    public void setParameters(int parameters) {
        this.parameters = parameters;
    }

    public Node getExpression() {
        return expression;
    }

    public void setExpression(Node expression) {
        this.expression = expression;
    }
}
