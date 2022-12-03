package net.aelang.parse;

import net.aelang.Global;
import net.aelang.ast.*;
import net.aelang.exception.LexerError;
import net.aelang.exception.SyntaxError;
import net.aelang.tokenizer.Token;
import net.aelang.tokenizer.TokenType;
import net.aelang.tokenizer.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static boolean ALLOW_PROMPT = true;
    public static boolean ALLOW_REPEAT = true;

    private String input;
    private Tokenizer tokenizer;

    private Token current;
    private Token next;

    public Parser(String input) throws LexerError {
        this.input = input;
        this.tokenizer = new Tokenizer(this.input);

        nextToken();
        nextToken();
    }

    public static Node parse(String str) {
        ALLOW_REPEAT = true;
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
        if (Global.showSyntaxTree) {
            System.out.print((n != null) ? n.dump(0) : "<Null node>\n");
        }
        return n;
    }

    private void nextToken() throws LexerError {
        current = next;
        next = tokenizer.nextToken();
        if (next == null)
            next = new Token(TokenType.UNDEFINED, "", 0);
    }

    public String input() {
        return input;
    }

    public Node parse() throws SyntaxError, LexerError {
        List<Node> nodes = new ArrayList<>();
        while (true) {
            nodes.add(parseStatement(false));
            if (next.type() == TokenType.IDEN_THEN) {
                nextToken();
                nextToken();
                if (current.type() == TokenType.UNDEFINED)
                    throw new SyntaxError("Expected expression after 'then'. Encountered empty token.");
                continue;
            }
            break;
        }

        if (next.type() != TokenType.UNDEFINED)
            throw new SyntaxError("Expected a single statement per line, unless linked by 'then'.");

        return new StatementSequenceNode(nodes);
    }

    public Node parseStatement(boolean checkLast) throws SyntaxError, LexerError {
        Node n = null;
        if (current.type() == TokenType.IDEN_DEFINE)
            n = parseFunctionDefinition();
        else if (current.type() == TokenType.IDEN_UNDEFINE)
            n = parseUndefine();
        else if (current.type() == TokenType.IDEN_NOTE)
            n = parseNote();
        else if (current.type() == TokenType.IDEN_LD)
            n = parseExec();
        else if (current.type() == TokenType.IDEN_INFO)
            n = parseInfo();
        else if (current.type() == TokenType.IDEN_IF)
            n = parseIfExec();
        else if (current.type() == TokenType.IDEN_VAR)
            n = parseVariableDeclaration();
        else if (current.type() == TokenType.IDEN && next.type() == TokenType.POINT_RIGHT)
            n = parseAssignment();
        else if (current.type() == TokenType.IDEN_REPEAT)
            n = parseRepeat();
        else if (current.type() == TokenType.IDEN_RECALL)
            n = parseRecall();
        else if (current.type() == TokenType.IDEN_COMPLEX)
            n = parseComplexDefinition();
        else if (current.type() == TokenType.IDEN_NEW)
            n = parseInstantiation();
        else if (current.type() == TokenType.IDEN && next.type() == TokenType.DOT)
            n = parseComplexAssignment();
        else
            n = parseFirstDegree(false);

        if (tokenizer.hasNext() && checkLast)
            throw new SyntaxError("Expected a single expression per line.");

        return n;
    }

    public SolvableNode parseFirstDegree(boolean withinFunction) throws SyntaxError, LexerError {
        SolvableNode left = parseSecondDegree(withinFunction);

        if (next.type() == TokenType.ADD || next.type() == TokenType.SUB)
            nextToken();

        while (current.type() == TokenType.ADD || current.type() == TokenType.SUB) {
            BinaryOperation op = BinaryOperation.fromToken(current);

            nextToken();

            if (current.type() == TokenType.UNDEFINED)
                throw new SyntaxError("Expression expected.");

            SolvableNode right = parseSecondDegree(withinFunction);
            left = new BinaryNode(left, right, op);

            if (next.type() == TokenType.ADD || next.type() == TokenType.SUB)
                nextToken();
        }

        return left;
    }

    public SolvableNode parseSecondDegree(boolean withinFunction) throws SyntaxError, LexerError {
        SolvableNode left = parseAtomWrapper(withinFunction);

        if (next.type() == TokenType.MUL || next.type() == TokenType.DIV || next.type() == TokenType.POW)
            nextToken();

        while (current.type() == TokenType.MUL || current.type() == TokenType.DIV || current.type() == TokenType.POW) {
            BinaryOperation op = BinaryOperation.fromToken(current);

            nextToken();

            if (current.type() == TokenType.UNDEFINED)
                throw new SyntaxError("Expression expected.");

            SolvableNode right = parseAtomWrapper(withinFunction);
            left = new BinaryNode(left, right, op);

            if (next.type() == TokenType.MUL || next.type() == TokenType.DIV || next.type() == TokenType.POW)
                nextToken();
        }

        return left;
    }

    public SolvableNode parseAtomWrapper(boolean withinFunction) throws SyntaxError, LexerError {
        boolean subz = false;

        if (current.type() == TokenType.SUB) {
            subz = true;
            nextToken();
        }

        SolvableNode n = parseAtom(withinFunction);
        if (subz)
            n = new BinaryNode(n, new ImmediateNode(-1.0), BinaryOperation.MUL);

        return n;
    }

    public SolvableNode parseAtom(boolean withinFunction) throws SyntaxError, LexerError {
        if (current.type() == TokenType.INT_LIT || current.type() == TokenType.REAL_LIT)
            return new ImmediateNode(current);

        if (current.type() == TokenType.LPAREN) {
            nextToken();
            SolvableNode sub = parseFirstDegree(withinFunction);
            nextToken();
            if (current.type() != TokenType.RPAREN)
                throw new SyntaxError("Expected ')' after expression.");
            return sub;
        }

        if (current.type() == TokenType.IDEN_PROMPT) {
            if (!ALLOW_PROMPT)
                throw new SyntaxError("Prompts are not allowed here.");

            nextToken();

            if (current.type() != TokenType.LBRACKET)
                throw new SyntaxError("Expected '[' after 'prompt'.");

            nextToken();

            if (current.type() != TokenType.STR_LIT)
                throw new SyntaxError("Expected prompt message (string) after 'prompt'.");

            String msg = current.val();

            nextToken();

            if (current.type() != TokenType.RBRACKET)
                throw new SyntaxError("Expected ']' after prompt message.");

            return new UserPromptNode(msg);
        }

        if (current.type() == TokenType.IDEN && next.type() == TokenType.COLON) {
            return parseComplexAccess();
        }

        if (current.type() == TokenType.IDEN) {
            String id = current.val();

            // Variable call
            if (next.type() != TokenType.LPAREN)
                return new VariableCallNode(current.val());
            else
                nextToken();

            nextToken();

            List<SolvableNode> params = new ArrayList<>();

            if (current.type() == TokenType.RPAREN)
                return new FunctionCallNode(id, params);

            while (true) {
                SolvableNode n = parseFirstDegree(withinFunction);

                params.add(n);

                nextToken();

                if (current.type() == TokenType.RPAREN)
                    break;

                if (current.type() == TokenType.SEMI) {
                    nextToken();
                    continue;
                }

                throw new SyntaxError("Expected ')' or ';' and more parameters.");
            }

            return new FunctionCallNode(id, params);
        }

        if (current.type() == TokenType.PLACEHOLDER && !withinFunction) {
            throw new SyntaxError("A placeholder was not expected here.");
        } else if (withinFunction) {
            return new PlaceholderAtomNode(current.data());
        }

        throw new SyntaxError("Invalid atom: \"" + current.val() + "\"." + ((current.type() == TokenType.STR_LIT) ? " Did you mean to use 'note'?" : ""));
    }

    // def name:2 -> $1 + $2
    public Node parseFunctionDefinition() throws LexerError, SyntaxError {
        nextToken(); // Skip 'def'

        if (current.type() != TokenType.IDEN)
            throw new SyntaxError("The function name must be an identifier.");

        String id = current.val();

        nextToken();

        if (current.type() != TokenType.COLON)
            throw new SyntaxError("Expected ':' after function name");

        nextToken();

        if (current.type() != TokenType.INT_LIT)
            if (current.type() == TokenType.REAL_LIT)
                throw new SyntaxError("The number of parameters must nost not be a floating-point number");
            else
                throw new SyntaxError("Expected number of parameters.");

        int n = Integer.parseInt(current.val());

        nextToken();

        if (current.type() != TokenType.POINT_RIGHT)
            throw new SyntaxError("Expected right-pointing assignment operator '->'");

        nextToken();

        SolvableNode expr = parseFirstDegree(true);

        return new FunctionDefinitionNode(id, n, expr);
    }

    public Node parseUndefine() throws LexerError, SyntaxError {
        nextToken(); // SKip 'undef'

        String id;
        if (current.type() != TokenType.IDEN)
            throw new SyntaxError("Expected identifier after \"undef\".");
        id = current.val();

        boolean silent = false;

        if (next.type() == TokenType.QUESTION) {
            nextToken();
            silent = true;
        }

        return new UndefineNode(id, silent);
    }

    public Node parseNote() throws LexerError, SyntaxError {
        nextToken(); // Skip 'note'

        if (current.type() != TokenType.STR_LIT)
            throw new SyntaxError("Expected a string literal after 'note'.");

        String note = current.val();

        return new NoteNode(note);
    }

    public Node parseExec() throws LexerError, SyntaxError {
        nextToken(); // Skip 'ld'

        if (current.type() != TokenType.STR_LIT)
            throw new SyntaxError("Expected a string literal after 'ld'.");

        String file = current.val();

        return new LoadNode(file);
    }

    public Node parseInfo() throws LexerError, SyntaxError {
        nextToken(); // Skip 'info'

        if (current.type() != TokenType.IDEN)
            throw new SyntaxError("Expected an identifier after 'info'.");

        String id = current.val();

        return new InfoNode(id);
    }

    public Node parseIfExec() throws LexerError, SyntaxError {
        nextToken(); // Skip 'if'

        if (current.type() == TokenType.UNDEFINED)
            throw new SyntaxError("Expected predicate expression after 'if'.");

        Node cond = parseLogicalFirstDegree();

        nextToken();

        if (current.type() != TokenType.IDEN_RUN)
            throw new SyntaxError("Expected 'run' after expression.");

        nextToken();

        if (current.type() == TokenType.UNDEFINED)
            throw new SyntaxError("Expected statement after 'run'.");

        Node stat = parse();

        return new ConditionalNode(cond, stat);
    }

    public Node parseLogicalFirstDegree() throws LexerError, SyntaxError {
        Node left = parseLogicalSecondDegree();

        if (next.type() == TokenType.IDEN_OR || next.type() == TokenType.IDEN_XOR)
            nextToken();

        while (current.type() == TokenType.IDEN_OR || current.type() == TokenType.IDEN_XOR) {
            LogicalOperation op = LogicalOperation.fromToken(current);

            nextToken();

            if (current.type() == TokenType.UNDEFINED)
                throw new SyntaxError("Expression expected.");

            Node right = parseLogicalSecondDegree();
            left = new LogicalBinaryNode(left, right, op);

            if (next.type() == TokenType.IDEN_OR || next.type() == TokenType.IDEN_XOR)
                nextToken();
        }

        return left;
    }

    public Node parseLogicalSecondDegree() throws SyntaxError, LexerError {
        Node left = parseLogicalExpressionAtom();

        if (next.type() == TokenType.IDEN_AND)
            nextToken();

        while (current.type() == TokenType.IDEN_AND) {
            LogicalOperation op = LogicalOperation.fromToken(current);

            nextToken();

            if (current.type() == TokenType.UNDEFINED)
                throw new SyntaxError("Expression expected.");

            Node right = parseLogicalExpressionAtom();
            left = new LogicalBinaryNode(left, right, op);

            if (next.type() == TokenType.IDEN_AND)
                nextToken();
        }

        return left;
    }

    public Node parseLogicalExpressionAtom() throws SyntaxError, LexerError {
        Predicate p;

        boolean not = false;

        if (current.type() == TokenType.IDEN_NOT) {
            not = true;
            nextToken();
            if (current.type() == TokenType.UNDEFINED)
                throw new SyntaxError("Expected expression after 'not'. Empty token encountered.");
        }

        if (current.val().matches("[a-zA-Z_]+") && next.type() == TokenType.LBRACKET)
            p = Predicate.fromToken(current);
        else
            throw new SyntaxError("Expected a predicate.");

        nextToken();

        if (current.type() != TokenType.LBRACKET)
            throw new SyntaxError("Expected '[' after predicate.");

        nextToken();

        if (current.type() != TokenType.IDEN)
            throw new SyntaxError("Expected identifier after '['.");

        String id = current.val();

        nextToken();

        if (current.type() != TokenType.RBRACKET)
            throw new SyntaxError("Expected ']' after identifier within the predicate.");

        if (!not)
            return new LogicalExpressionPredicateNode(p, id);
        else
            return new LogicalUnaryNode(new LogicalExpressionPredicateNode(p, id));
    }

    public Node parseVariableDeclaration() throws LexerError, SyntaxError {
        nextToken(); // SKip 'var'

        if (current.type() != TokenType.IDEN)
            throw new SyntaxError("Expected identifier after 'var'.");

        String id = current.val();

        nextToken();

        if (current.type() != TokenType.IDEN_DEFAULT)
            throw new SyntaxError("Expected 'default' after identifier.");

        nextToken();

        if (current.type() == TokenType.UNDEFINED)
            throw new SyntaxError("Expected expression after 'default'.");

        SolvableNode expr = parseFirstDegree(false);

        return new VariableDeclarationNode(id, expr);
    }

    public Node parseAssignment() throws LexerError, SyntaxError {
        String id = current.val();

        nextToken();

        if (current.type() != TokenType.POINT_RIGHT)
            throw new SyntaxError("Expected assignment operator (->) after variable name.");

        nextToken();

        if (current.type() == TokenType.UNDEFINED)
            throw new SyntaxError("Expected expression after '->'.");

        SolvableNode expr = parseFirstDegree(false);

        return new AssignmentNode(id, expr);
    }

    public Node parseRepeat() throws LexerError, SyntaxError {
        if (!ALLOW_REPEAT)
            throw new SyntaxError("Repeat is not allowed here.");

        nextToken();

        if (current.type() == TokenType.UNDEFINED)
            throw new SyntaxError("Expected number of iterations.");

        Node times = parseFirstDegree(false);

        nextToken();

        if (current.type() != TokenType.IDEN_TIMES)
            throw new SyntaxError("Expected 'times' after number of iterations.");

        nextToken();

        if (current.type() != TokenType.IDEN_RUN)
            throw new SyntaxError("Expected 'run' after 'times' count.");

        nextToken();

        if (current.type() == TokenType.UNDEFINED)
            throw new SyntaxError("Expected statement(s).");

        ALLOW_REPEAT = false;
        Node statement = parse();
        ALLOW_REPEAT = true;

        return new RepeatNode(times, statement);
    }

    public Node parseRecall() throws LexerError, SyntaxError {
        nextToken();

        if (current.type() == TokenType.UNDEFINED)
            throw new SyntaxError("Expected expression (index) after 'recall'.");

        Node expr = parseFirstDegree(false);

        return new RecallNode(expr);
    }

    public SolvableNode parseComplexAccess() throws LexerError, SyntaxError {
        String id = current.val();

        nextToken();

        if (current.type() != TokenType.COLON)
            throw new SyntaxError("Expected ':' after instance name \"" + id + "\".");

        nextToken();

        if (current.type() != TokenType.IDEN)
            throw new SyntaxError("Expected identifier after ':'");

        String field = current.val();

        return new ComplexAccessNode(id, field);
    }

    public ComplexDefinitionNode parseComplexDefinition() throws LexerError, SyntaxError {
        nextToken(); // Skip 'complex'

        if (current.type() != TokenType.IDEN)
            throw new SyntaxError("Expected identifier after 'complex'.");

        String id = current.val();

        nextToken(); // SKip identifier

        if (current.type() != TokenType.LBRACKET)
            throw new SyntaxError("Expected '[' after complex type name \"" + id + "\"");

        nextToken(); // Skip '['

        if (current.type() == TokenType.RBRACKET)
            throw new SyntaxError("Expected at least one field in complex type \"" + id + "\"");

        ArrayList<String> fields = new ArrayList<>();

        while (true) {
            if (current.type() != TokenType.IDEN)
                break;

            fields.add(current.val());

            nextToken(); // Skip identifier

            // Closing bracket (end of fields)
            if (current.type() == TokenType.RBRACKET)
                break;

            if (current.type() != TokenType.SEMI)
                throw new SyntaxError("Expected ']' or ';' and more fields.");

            nextToken(); // SKip ';'
        }

        if (current.type() != TokenType.RBRACKET)
            throw new SyntaxError("Expected ']' after the list of complex fields.");

        return new ComplexDefinitionNode(id, fields);
    }

    public Node parseInstantiation() throws LexerError, SyntaxError {
        nextToken(); // SKip 'new'

        if (current.type() != TokenType.IDEN)
            throw new SyntaxError("Expected complex type name after \"new\"");

        String complex = current.val();

        nextToken(); // SKip type

        if (current.type() != TokenType.IDEN)
            throw new SyntaxError("Expected instance name after complex type.");

        String id = current.val();

        return new InstantiationNode(complex, id);
    }

    public Node parseComplexAssignment() throws LexerError, SyntaxError {
        String id = current.val();

        nextToken();

        if (current.type() != TokenType.DOT)
            throw new SyntaxError("Expected '.' after instance name.");

        nextToken();

        if (current.type() != TokenType.IDEN)
            throw new SyntaxError("Expected field name (identifier) after '.'");

        String field = current.val();

        nextToken();

        if (current.type() != TokenType.POINT_RIGHT)
            throw new SyntaxError("Expected assignment operator (->) after complex path.");

        nextToken();

        if (current.type() == TokenType.UNDEFINED)
            throw new SyntaxError("Expected expression after '->'.");

        SolvableNode expr = parseFirstDegree(false);

        return new ComplexAssignmentNode(id, field, expr);
    }

}