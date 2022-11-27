package net.aelang.runtime;

import net.aelang.Pair;
import net.aelang.SolverError;
import net.aelang.ast.*;
import net.aelang.runtime.elements.Element;
import net.aelang.runtime.elements.Function;
import net.aelang.runtime.elements.Variable;

import java.util.List;

public class Solver {

    private boolean allowSubstitution = false;
    private List<SolvableNode> substitutes;

    public double solve(SolvableNode node) throws SolverError {
        if (node instanceof BinaryNode castNode)
            return solveBinary(castNode);

        if (node instanceof ImmediateNode castNode)
            return solveImmediate(castNode);

        if (node instanceof VariableCallNode castNode)
            return solveVariableCall(castNode);

        if (node instanceof PlaceholderAtomNode castNode)
            if (allowSubstitution)
                return solvePlaceholder(castNode);
            else throw new SolverError("Placeholders are only allowed when defining a function.");

        if (node instanceof ComplexAccessNode castNode)
            return solveComplexAccess(castNode);

        return 0.0;
    }

    private double solveBinary(BinaryNode node) throws SolverError {
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

    private double solveVariableCall(VariableCallNode node) throws SolverError {
        PersistentEnvironment env = PersistentEnvironment.getInstance();
        Pair<Class<?>, Element> el = env.findElement(node.id());

        if (el == null)
            throw new SolverError("The element \"" + node.id() + "\" is not declared.");
        if (el.key() != Variable.class)
            throw new SolverError("The element \"" + node.id() + "\" is not a variable.");

        Variable variable = (Variable) el.val();
        double variableValue = solve(variable.getValue());

        return variableValue;
    }

    private double solvePlaceholder(PlaceholderAtomNode node) throws SolverError {
        if (node.ref() < 0 || node.ref() >= this.substitutes.size())
            throw new SolverError("The placeholder reference number is out of bounds with index " + node.ref() + " and " + substitutes.size() + " available placeholders.");

        double substitutionValue = solve(substitutes.get(node.ref()));

        return substitutionValue;
    }

    private double solveFunctionCall(FunctionCallNode node) throws SolverError {
        PersistentEnvironment env = PersistentEnvironment.getInstance();
        Pair<Class<?>, Element> el = env.findElement(node.id());

        if (el == null)
            throw new SolverError("The element \"" + node.id() + "\" is not declared.");
        if (el.key() != Variable.class)
            throw new SolverError("The element \"" + node.id() + "\" is not a function.");

        Function function = (Function) el.val();

        if (function.getParameters() != node.params().size())
            throw new SolverError("The function \"" + node.id() + "\" expects " + function.getParameters() + " parameter(s), but " + node.params().size() + " parameter(s) have been passed.");

        this.allowSubstitution = true;

        this.substitutes = node.params();
        double functionValue = solve(function.getExpression());

        this.allowSubstitution = false;

        return functionValue;
    }

    private double solveComplexAccess(ComplexAccessNode node) {
        // TODO Find a matching instance, access/solve the field, return the substitution value
    }

}
