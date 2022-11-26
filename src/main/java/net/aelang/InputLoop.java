package net.aelang;

import net.aelang.ast.*;
import net.aelang.codegen.AssemblyOutput;
import net.aelang.codegen.instruction.Instruction;
import net.aelang.parse.Parser;

import java.util.*;

public class InputLoop {

    private ArrayList<Pair<String, Boolean>> history;
    private boolean showAST = false;

    private Scanner scanner;


    public InputLoop() {
        history = new ArrayList<>();
        startREPL();
    }

    public Node parse(String str) {
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
            n = p.parse();
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
        if (n instanceof StatementSequenceNode ssn) {
            for (Node node : ssn.nodes()) {
                if (node instanceof SolvableNode sn) {
                    AssemblyOutput out = new AssemblyOutput();
                    for (Instruction instruction : sn.assemble().val()) {
                        out.add(instruction);
                    }
                    System.out.println(out.generate());
                }
            }
        }
        return n;
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

        if (cmd.equalsIgnoreCase(".help")) {
            System.out.println("-".repeat(25));
            System.out.println(".exit       ~~  Exit the program");
            System.out.println(".show-ast   ~~  Show parser output (for debugging)");
            System.out.println(".hide-ast   ~~  Don't show parser output");
            System.out.println(".help       ~~  Show the help page");
            System.out.println(".hist       ~~  Show past expressions");
            System.out.println("-".repeat(25));
            return true;
        }

        if (cmd.equalsIgnoreCase(".hist")) {
            System.out.println("There are " + history.size() + " entries in the history buffer.");
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

    public void startREPL() {
        System.out.println("Advanced Expression Language\nInput an expression or '.exit' to exit, or '.help' for help.");

        scanner = new Scanner(System.in);

        while (true) {
            System.out.print("~> ");
            String line = scanner.nextLine();

            if (line.trim().isEmpty())
                continue;

            if (line.trim().charAt(0) == '.') {
                if (!handleEnvCommand(line.trim())) {
                    break;
                }
                continue;
            }

            parse(line);

        }

        scanner.close();
        System.out.println("Exiting ..");
    }


}
