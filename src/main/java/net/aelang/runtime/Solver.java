package net.aelang.runtime;

import net.aelang.ast.BinaryNode;
import net.aelang.ast.ImmediateNode;
import net.aelang.ast.SolvableNode;

public class Solver {

    public double solve(SolvableNode node) {
        if (node instanceof BinaryNode castNode)
            return solveBinary(castNode);

        if (node instanceof ImmediateNode castNode)
            return solveImmediate(castNode);

        return 0.0;
    }

    private double solveBinary(BinaryNode node) {
        double left = solve(node.left());
        double right = solve(node.right());

        return switch (node.op()) {
            case ADD    -> left + right;
            case SUB    -> left - right;
            case MUL    -> left * right;
            case DIV    -> left / right;
            default     -> 0.0;
        };
    }

    private double solveImmediate(ImmediateNode node) {
        return node.value();
    }

}
