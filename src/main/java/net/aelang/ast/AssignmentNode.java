package net.aelang.ast;

import net.aelang.Tools;

public class AssignmentNode extends Node {

    private String id;
    private Node expr;

    public AssignmentNode(String id, Node expr) {
        this.id = id;
        this.expr = expr;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Node expr() {
        return expr;
    }

    public void setExpr(Node expr) {
        this.expr = expr;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Assignment (" + id + "):\n";
        lpad++;
        s += expr.dump(lpad);
        lpad--;
        return s;
    }

}
