package net.aelang.ast;

import net.aelang.Tools;

public class FunctionDefinitionNode extends Node {

    private String id;
    private int params;
    private SolvableNode expr;

    public FunctionDefinitionNode(String id, int params, SolvableNode expr) {
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

    public SolvableNode expr() {
        return expr;
    }

    public void setExpr(SolvableNode expr) {
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
