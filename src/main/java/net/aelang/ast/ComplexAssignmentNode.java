package net.aelang.ast;

import net.aelang.Tools;

public class ComplexAssignmentNode extends Node {

    private String id;
    private String field;
    private SolvableNode expr;

    public ComplexAssignmentNode(String id, String field, SolvableNode expr) {
        this.id = id;
        this.field = field;
        this.expr = expr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public SolvableNode getExpr() {
        return expr;
    }

    public void setExpr(SolvableNode expr) {
        this.expr = expr;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Complex Assignment (" + id + "." + field + "):\n";
        lpad++;
        s += expr.dump(lpad);
        lpad--;
        return s;
    }

}
