package net.aelang.runtime.elements;

import net.aelang.ast.FunctionDefinitionNode;
import net.aelang.ast.Node;
import net.aelang.ast.SolvableNode;

public class Function extends Element {

    private int parameters;
    private SolvableNode expression;

    public Function(String id, int parameters, SolvableNode expression) {
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

    public SolvableNode getExpression() {
        return expression;
    }

    public void setExpression(SolvableNode expression) {
        this.expression = expression;
    }
}
