package net.aelang;

import net.aelang.ast.*;
import net.aelang.functions.*;
import net.aelang.parse.Parser;

import java.io.*;
import java.util.*;

public class ReadEvalPrintLoop {


    private ArrayList<BuiltinFunction> builtinFunctions;
    private ArrayList<DefinitionNode> runtimeFunctions;
    private HashMap<String, Node> variables;
    private ArrayList<Pair<String, Boolean>> history;
    private boolean showAST = false;

    private Scanner scanner;


    public ReadEvalPrintLoop() {
        builtinFunctions = new ArrayList<>();
        runtimeFunctions = new ArrayList<>();
        variables = new HashMap<>();
        history = new ArrayList<>();

        builtinFunctions.add(new SinFunction());
        builtinFunctions.add(new CosFunction());
        builtinFunctions.add(new TanFunction());
        builtinFunctions.add(new SqrtFunction());

        startREPL();
    }

    private BuiltinFunction findBuiltin(String id) {
        Optional<BuiltinFunction> f = builtinFunctions.stream().filter((e) -> e.id().equalsIgnoreCase(id)).findFirst();
        if (!f.isPresent())
            return null;
        return f.get();
    }

    private DefinitionNode findUserDefined(String id) {
        Optional<DefinitionNode> f = runtimeFunctions.stream().filter((e) -> e.id().equalsIgnoreCase(id)).findFirst();
        if (!f.isPresent())
            return null;
        return f.get();
    }

    private void functionCtl(Node node) {
        if (node instanceof DefinitionNode) {
            DefinitionNode n = (DefinitionNode) node;

            if (findBuiltin(n.id()) != null || findUserDefined(n.id()) != null) {
                System.out.println("This function is already defined: \"" + n.id() + "\". Use 'undef <function>' to remove it.");
                return;
            }

            runtimeFunctions.add(n);
            System.out.println("New function registered \"" + n.id() + "\".");
        }

        if (node instanceof UndefineNode) {
            UndefineNode udef = (UndefineNode) node;

            if (variables.containsKey(udef.id())) {
                variables.remove(udef.id());
                if (!udef.silent())
                    System.out.println("Variable \"" + udef.id() + "\" was undefined.");
                return;
            }

            if (findBuiltin(udef.id()) != null) {
                if (!udef.silent())
                    System.out.println("The function \"" + udef.id() + "\" is a builtin function and hence cannot be undefined.");
                return;
            }
            if (findUserDefined(udef.id()) != null) {
                for (int i = 0; i < runtimeFunctions.size(); i++) {
                    if (runtimeFunctions.get(i).id().equalsIgnoreCase(udef.id()))
                        runtimeFunctions.remove(i);
                }
                if (!udef.silent())
                    System.out.println("Function \"" + udef.id() + "\" was removed successfully.");
                return;
            }
            if (!udef.silent())
                System.out.println("Could not find symbol \"" + udef.id() + "\".");
        }
    }

    public double solve(Node node, List<Node> placeholders) throws SolverError {
        if (node instanceof ImmediateNode) {
            return ((ImmediateNode) node).value();
        }

        if (node instanceof BinaryNode) {
            BinaryNode bin = (BinaryNode) node;
            double left = solve(bin.left(), placeholders);
            double right = solve(bin.right(), placeholders);
            switch (bin.op()) {
                case ADD:
                    left += right;
                    break;
                case SUB:
                    left -= right;
                    break;
                case MUL:
                    left *= right;
                    break;
                case DIV:
                    left /= right;
                    break;
                case POW:
                    left = Math.pow(left, right);
                    break;
                default:
                    throw new SolverError("Binary operation type is set to undefined.");
            }
            return left;
        }

        if (node instanceof PlaceholderAtomNode) {
            if (placeholders == null)
                throw new SolverError("Illegal placeholder found: No reference table given.");

            PlaceholderAtomNode placeholder = (PlaceholderAtomNode) node;
            int n = placeholder.ref();

            if (n <= 0 || n > placeholders.size())
                throw new SolverError("Invalid placeholder reference number: " + n);

            return solve(placeholders.get(n - 1), placeholders);
        }

        if (node instanceof CallNode) {
            CallNode call = (CallNode) node;

            List<Node> params = call.params();

            BuiltinFunction builtin = findBuiltin(call.id());
            DefinitionNode user = findUserDefined(call.id());

            if (builtin != null) {
                if (params.size() != 1)
                    throw new SolverError("A builtin function always needs a single parameter. (Got " + params.size() + ", expected 1)");
                double d = solve(call.params().get(0), null);
                return builtin.run(d);
            }

            if (user != null) {
                if (params.size() != user.params())
                    throw new SolverError("The function \"" + user.id() + "\" expected " + user.params() + " parameters, got " + params.size());
                return solve(user.expr(), params);
            }

            throw new SolverError("No function by the name of \"" + call.id() + "\" could be found.");

        }

        if (node instanceof VariableCallNode) {
            VariableCallNode call = (VariableCallNode) node;
            if (!variables.containsKey(call.id()))
                throw new SolverError("The variable \"" + call.id() + "\" is not defined.");
            return solve(variables.get(call.id()), placeholders);
        }

        if (node instanceof UserPromptNode) {
            UserPromptNode prompt = (UserPromptNode) node;

            Parser.ALLOW_PROMPT = false;
            System.out.print(prompt.message() + ": ");
            Node expr = parse(scanner.nextLine(), true);
            Parser.ALLOW_PROMPT = true;

            return solve(expr, placeholders);
        }

        throw new SolverError("Unsupported node type passed.");
    }

    public boolean solveLogical(Node node) throws SolverError {
        if (node instanceof LogicalBinaryNode) {
            LogicalBinaryNode bin = (LogicalBinaryNode) node;
            boolean left = solveLogical(bin.left());
            boolean right = solveLogical(bin.right());
            return switch (bin.op()) {
                case AND -> left && right;
                case OR -> left || right;
                case XOR -> (left && !right) || (!left && right);
                default -> throw new SolverError("Unknown logical expression: " + bin.op().toString());
            };
        }

        if (node instanceof LogicalExpressionPredicateNode) {
            LogicalExpressionPredicateNode p = (LogicalExpressionPredicateNode) node;
            if (p.p() == Predicate.DEFINED) {
                if (findBuiltin(p.id()) == null && findUserDefined(p.id()) == null) {
                    return false;
                }
                return true;
            }
            throw new SolverError("Unknown predicate: " + p.p().toString());
        }

        if (node instanceof LogicalUnaryNode) {
            LogicalUnaryNode u = (LogicalUnaryNode) node;
            boolean b = solveLogical(u.expr());
            return !b;
        }

        throw new SolverError("Unsupported node type passed.");
    }

    public Node parse(String str, boolean onlyExpr) {
        Parser.ALLOW_REPEAT = true;
        Parser p = null;
        try {
            p = new Parser(str);
        } catch (LexerError e) {
            System.out.println("[Lexical Error] " + e.getMessage());
            return null;
        }
        Node n = null;
        try {
            if (!onlyExpr)
                n = p.parse();
            else
                n = p.parseFirstDegree(false);
        } catch (SyntaxError e) {
            System.out.println("[Syntax Error] " + e.getMessage());
        } catch (LexerError e) {
            System.out.println("[Lexical Error] " + e.getMessage());
        }
        if (showAST && n != null) {
            System.out.println("----------[ AST Dump ]----------");
            System.out.print(n.dump(0));
            System.out.println("--------------------------------");
        }
        return n;
    }

    public double solveFrontend(Node node) {
        double d = 0.0;
        try {
            d = solve(node, null);
        } catch (SolverError e) {
            System.out.println("[Solver Error] " + e.getMessage());
        }
        return d;
    }

    public boolean logicalSolveFrontend(Node node) {
        boolean b = false;
        try {
            b = solveLogical(node);
        } catch (SolverError e) {
            System.out.println("[Solver Error] " + e.getMessage());
        }
        return b;
    }

    private boolean handleEnvCommand(String cmd) {
        if (cmd.equalsIgnoreCase(".exit")) {
            return false;
        }

        if (cmd.equalsIgnoreCase(".show-ast")) {
            showAST = true;
            System.out.println("The AST will be displayed from now on.");
            return true;
        }

        if (cmd.equalsIgnoreCase(".hide-ast")) {
            showAST = false;
            System.out.println("The AST will not be displayed anymore.");
            return true;
        }

        if (cmd.equalsIgnoreCase(".lfn")) {
            if (runtimeFunctions.size() == 0) {
                System.out.println("(No user defined functions)");
            } else {
                System.out.println("----------[ User-Defined ]----------");
                for (int i = 0; i < runtimeFunctions.size(); i++) {
                    process("info " + runtimeFunctions.get(i).id());
                }
                System.out.println("------------------------------------");
            }

            if (builtinFunctions.size() == 0) {
                System.out.println("(No builtin functions found)");
            } else {
                System.out.println("----------[ Builtin ]----------");
                for (int i = 0; i < builtinFunctions.size(); i++) {
                    process("info " + builtinFunctions.get(i).id());
                }
                System.out.println("------------------------------------");
            }
            return true;
        }

        if (cmd.equalsIgnoreCase(".help")) {
            System.out.println("-".repeat(25));
            System.out.println(".exit       ~~  Exit the program");
            System.out.println(".show-ast   ~~  Show parser output (for debugging)");
            System.out.println(".show-ast   ~~  Don't show parser output");
            System.out.println(".help       ~~  Show the help page");
            System.out.println(".lfn        ~~  List defined functions");
            System.out.println(".hist       ~~  Show past expressions");
            System.out.println("-".repeat(25));
            return true;
        }

        if (cmd.equalsIgnoreCase(".hist")) {
            System.out.println("There are " + history.size() + " entries in the histroy buffer.");
            if (history.size() > 0)
                System.out.println("P?\tID\tLINE");
            for (int i = 0; i < history.size(); i++) {
                System.out.println((history.get(i).val() ? "ER\t" : "OK\t") + i + "\t" + history.get(i).key());
            }
            return true;
        }

        System.out.println("Unknown command. Type '.help' for help.");
        return true;
    }

    private void processNode(Node node) {
        if (node == null && showAST) {
            System.out.println("< Null node >");
            return;
        }

        if (node instanceof SolvableNode)
            System.out.println("= " + solveFrontend(node));

        if (node instanceof DefinitionNode || node instanceof UndefineNode)
            functionCtl(node);

        if (node instanceof NoteNode) {
            NoteNode note = (NoteNode) node;
            System.out.println(note.str());
        }

        if (node instanceof ExecNode) {
            ExecNode exc = (ExecNode) node;
            try {
                execFile(new File(exc.file()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (node instanceof InfoNode) {
            InfoNode ifo = (InfoNode) node;
            String type = "";
            int n = 0;

            BuiltinFunction builtin = findBuiltin(ifo.id());
            DefinitionNode user = findUserDefined(ifo.id());

            if (builtin != null) {
                type = "builtin function";
                n = 1;
            } else if (user != null) {
                type = "function declared at runtime";
                n = user.params();
            } else {
                System.out.println("\"" + ifo.id() + "\" is not defined.");
                return;
            }

            System.out.println("The symbol \"" + ifo.id() + "\" corresponds to a " + type + " with " + n + " parameter(s).");
        }

        if (node instanceof ConditionalNode) {
            ConditionalNode cond = (ConditionalNode) node;
            boolean expr = logicalSolveFrontend(cond.condition());
            if (!expr)
                return;
            processNode(cond.statement());
        }

        if (node instanceof StatementSequenceNode) {
            StatementSequenceNode nested = (StatementSequenceNode) node;
            for (int i = 0; i < nested.nodes().size(); i++) {
                processNode(nested.nodes().get(i));
            }
        }

        if (node instanceof VariableDeclarationNode) {
            VariableDeclarationNode decl = (VariableDeclarationNode) node;
            if (variables.containsKey(decl.id())) {
                System.out.println("This variable has already been defined.");
                return;
            }
            ImmediateNode imm = new ImmediateNode(solveFrontend(decl.def()));
            variables.put(decl.id(), imm);
            System.out.println("Variable \"" + decl.id() + "\" declared.");
        }

        if (node instanceof AssignmentNode) {
            AssignmentNode assign = (AssignmentNode) node;
            if (!variables.containsKey(assign.id())) {
                System.out.println("Variable \"" + assign.id() + "\" does not exist.");
                return;
            }
            ImmediateNode imm = new ImmediateNode(solveFrontend(assign.expr()));
            variables.remove(assign.id());
            variables.put(assign.id(), imm);
        }

        if (node instanceof RepeatNode) {
            RepeatNode rep = (RepeatNode) node;
            int times = (int) solveFrontend(rep.count());
            for (int i = 0; i < times; i++) {
                processNode(rep.statement());
            }
        }

        if (node instanceof RecallNode) {
            RecallNode rc = (RecallNode) node;

            int idx = (int) solveFrontend(rc.index());

            if (idx < 0 || idx >= history.size()) {
                System.out.println("Index out of bounds: " + idx);
                return;
            }

            Pair<String, Boolean> entry = history.get(idx);

            if (entry.val()) {
                System.out.println("Recalling index " + idx + " makes no sense: Line failed during parsing stage.");
                return;
            }

            process(history.get(idx).key());
        }

    }

    private void process(String line) {
        if (line.trim().isEmpty() || line.trim().startsWith("#"))
            return;

        Node node = parse(line, false);
        history.add(new Pair<>(line.trim(), node == null));

        processNode(node);
    }

    private void execFile(File f) throws IOException {
        if (!f.exists()) {
            System.out.println("The specified file does not exist: " + f.getAbsolutePath());
            return;
        }

        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String contents = "";
        while (reader.ready()) {
            contents += (char) reader.read();
        }

        String lines[] = contents.split("\n");

        System.out.println("Executing script (" + f.getName() + ") ..");

        for (String line : lines) {
            line = line.trim().replaceAll("\n", "").replaceAll("\r", "");
            process(line);
        }

        reader.close();
    }

    public void startREPL() {
        System.out.println("(C) Piotr Wywas, 2022\nInput an expression or '.exit' to exit, or '.help' for help.");

        scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();

            if (line.trim().isEmpty())
                continue;

            if (line.trim().charAt(0) == '.') {
                if (!handleEnvCommand(line.trim())) {
                    break;
                }
                continue;
            }

            process(line);

        }

        scanner.close();
        System.out.println("Exiting ..");
    }


}
