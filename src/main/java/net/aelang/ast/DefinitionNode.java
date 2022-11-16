package net.aelang.ast;

import net.aelang.Tools;

public class DefinitionNode extends Node {

    private String id;
    private int params;
    private Node expr;

    public DefinitionNode(String id, int params, Node expr) {
        this.id = id;
        this.params = params;
        this.expr = expr;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int params() {
        return params;
    }

    public void setParams(int params) {
        this.params = params;
    }

    public Node expr() {
        return expr;
    }

    public void setExpr(Node expr) {
        this.expr = expr;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Function definition (" + id + ") {" + params + "}:\n";
        lpad++;
        s += Tools.leftpad(lpad) + "Expression:\n";
        lpad++;
        s += expr.dump(lpad);
        lpad--;
        lpad--;
        return s;
    }
}
