package net.aelang;

import net.aelang.ast.Node;
import net.aelang.ast.StatementSequenceNode;
import net.aelang.parse.Parser;
import net.aelang.runtime.Runtime;

import java.util.ArrayList;
import java.util.Scanner;

public class ReadEvalPrintLoop {

    private ArrayList<Pair<String, Boolean>> history;

    private Scanner scanner;


    public ReadEvalPrintLoop() {
        history = new ArrayList<>();
        startREPL();
    }

    private boolean handleEnvCommand(String cmd) {
        if (cmd.equalsIgnoreCase(".exit")) {
            return false;
        }

        if (cmd.equalsIgnoreCase(".show-ast")) {
            Global.showSyntaxTree = true;
            System.out.println("The AST will be displayed from now on.");
            return true;
        }

        if (cmd.equalsIgnoreCase(".hide-ast")) {
            Global.showSyntaxTree = false;
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

    public void startREPL() {
        System.out.println("Advanced Expression Language v" + Global.VERSION + " (" + Global.PREFIX.toString().toLowerCase() + ")\nInput an expression or '.exit' to exit, or '.help' for help.");

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

            Node n = Parser.parse(line);
            if (!(n instanceof StatementSequenceNode ssn))
                continue;
            Runtime runtime = new Runtime(ssn);
            runtime.execute();
        }

        scanner.close();
        System.out.println("Exiting ..");
    }


}
