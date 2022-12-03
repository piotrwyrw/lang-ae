package net.aelang.runtime;

import net.aelang.Pair;
import net.aelang.ast.*;
import net.aelang.exception.RuntimeError;
import net.aelang.runtime.elements.*;

public class Runtime {

    private StatementSequenceNode root;

    public Runtime(StatementSequenceNode program) {
        this.root = program;
    }

    public void execute() {
        for (Node node : root.nodes()) {
            Pair<Boolean, Double> exec = executeWrapper(node);
            if (exec == null)
                continue;
            if (exec.key())
                System.out.println(exec.val());
        }
    }

    private Pair<Boolean, Double> executeWrapper(Node n) {
        Pair<Boolean, Double> exec = null;
        try {
            exec = executeAny(n);
        } catch (RuntimeError e) {
            System.out.println("[Runtime Error] " + e.getMessage());
        }
        return exec;
    }

    private Pair<Boolean, Double> executeAny(Node node) throws RuntimeError {
        if (node instanceof SolvableNode castNode)
            return new Pair<>(true, expression(castNode));

        if (node instanceof FunctionDefinitionNode castNode) {
            functionDefinition(castNode);
            return defaultReturn();
        }

        if (node instanceof UndefineNode castNode) {
            undefine(castNode);
            return defaultReturn();
        }

        if (node instanceof NoteNode castNode) {
            note(castNode);
            return defaultReturn();
        }

        if (node instanceof VariableDeclarationNode castNode) {
            variableDeclaration(castNode);
            return defaultReturn();
        }

        if (node instanceof AssignmentNode castNode) {
            variableAssignment(castNode);
            return defaultReturn();
        }

        if (node instanceof InstantiationNode castNode) {
            instantiation(castNode);
            return defaultReturn();
        }

        if (node instanceof ComplexDefinitionNode castNode) {
            complexDefinition(castNode);
            return defaultReturn();
        }

        if (node instanceof LoadNode castNode) {
            throw new RuntimeError("This node is not supported yet.");
            // TODO Add 'ld' support
        }

        if (node instanceof InfoNode castNode) {
            info(castNode);
            return defaultReturn();
        }

        if (node instanceof ComplexAssignmentNode castNode) {
            complexAssign(castNode);
            return defaultReturn();
        }

        return null;
    }

    private Pair<Boolean, Double> defaultReturn() {
        return new Pair<>(false, 0.0);
    }

    private double expression(SolvableNode node) {
        return new Solver().solverFrontend(node);
    }

    private void functionDefinition(FunctionDefinitionNode node) throws RuntimeError {
        boolean state = PersistentEnvironment.getInstance().putElement(Function.from(node));
        if (!state)
            throw new RuntimeError("Element already defined \"" + node.id() + "\"");;
    }

    private void variableDeclaration(VariableDeclarationNode node) throws RuntimeError {
        boolean state = PersistentEnvironment.getInstance().putElement(Variable.from(node));
        if (!state)
            throw new RuntimeError("Element already defined \"" + node.id() + "\"");
    }

    private void variableAssignment(AssignmentNode node) throws RuntimeError {
        Pair<Class<?>, Element> el = PersistentEnvironment.getInstance().findElement(node.id());

        if (el == null)
            throw new RuntimeError("The element \"" + node.id() + "\" is not defined.");

        if (el.key() != Variable.class)
            throw new RuntimeError("The element \"" + node.id() + "\" is not a variable.");

        Variable v = ((Variable) el.val());
        v.setValue(new ImmediateNode(new Solver().solverFrontend(node.expr())));
    }

    private void undefine(UndefineNode node) throws RuntimeError {
        boolean state = PersistentEnvironment.getInstance().deleteElement(node.id());
        if (!state && !node.silent())
            throw new RuntimeError("The element \"" + node.id() + "\" is not defined.");
    }

    private void note(NoteNode node) {
        System.out.println(node.str());
    }

    private void info(InfoNode node) throws RuntimeError {
        PersistentEnvironment env = PersistentEnvironment.getInstance();
        Pair<Class<?>, Element> el = env.findElement(node.id());
        if (el == null)
            throw new RuntimeError("The element \"" + node.id() + "\" is not defined.");
        String cname = Element.classNameOf(el.val());
        if (cname.equalsIgnoreCase("unknown"))
            throw new RuntimeError("The element \"" + node.id() + "\" is of an unknown datatype.");
        System.out.print(node.id() + " (" + cname + ")");
        if (cname.equalsIgnoreCase("function"))
            System.out.println(" :: " + ((Function)el.val()).getParameters() + " parameter(s)");
        else
            System.out.println();
    }

    private void instantiation(InstantiationNode node) throws RuntimeError {
        boolean state = PersistentEnvironment.getInstance().putElement(Instance.from(node));
        if (!state)
            throw new RuntimeError("Element already defined \"" + node.getId() + "\"");
    }

    private void complexDefinition(ComplexDefinitionNode node) throws RuntimeError {
        boolean state = PersistentEnvironment.getInstance().putElement(Complex.from(node));
        if (!state)
            throw new RuntimeError("Element already defined \"" + node.getId() + "\"");
    }

    private void complexAssign(ComplexAssignmentNode node) throws RuntimeError {
        Pair<Class<?>, Element> el = PersistentEnvironment.getInstance().findElement(node.getId());

        if (el == null)
            throw new RuntimeError("The element \"" + node.getId() + "\" is not defined.");

        if (el.key() != Instance.class)
            throw new RuntimeError("The element \"" + node.getId() + "\" is not an instance of a complex type.");

        Instance i = ((Instance) el.val());

        if (i.getFieldValue(node.getField()) == null)
            throw new RuntimeError("The instance \"" + node.getId() + "\" doesn't contain a field named \"" + node.getField() + "\".");

        i.setFieldValue(node.getField(), node.getExpr());
    }

}
