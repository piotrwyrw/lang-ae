package net.piotrwyrw.ast;

import net.piotrwyrw.Tools;

public class LogicalUnaryNode extends Node {

    private Node expr;

    public LogicalUnaryNode(Node expr) {
        this.expr = expr;
    }

    public Node expr() {
        return expr;
    }

    public void setExpr(Node expr) {
        this.expr = expr;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Not:\n" + expr.dump(lpad + 1);
    }

}
