package net.aelang.runtime;

import net.aelang.Pair;
import net.aelang.ast.*;
import net.aelang.exception.RuntimeError;
import net.aelang.runtime.elements.Element;
import net.aelang.runtime.elements.Function;

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
            node(castNode);
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
            throw new RuntimeError("This element is already defined \"" + node.id() + "\"");;
    }

    private void undefine(UndefineNode node) throws RuntimeError {
        boolean state = PersistentEnvironment.getInstance().deleteElement(node.id());
        if (!state && !node.silent())
            throw new RuntimeError("The element \"" + node.id() + "\" is not defined.");
    }

    private void node(NoteNode node) {
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
        System.out.println(node.id() + " (" + cname + ")");
    }

}
