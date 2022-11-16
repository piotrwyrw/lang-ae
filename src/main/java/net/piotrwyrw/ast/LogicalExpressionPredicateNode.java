package net.piotrwyrw.ast;

import net.piotrwyrw.Tools;

public class LogicalExpressionPredicateNode extends Node {

    private Predicate p;
    private String id;

    public LogicalExpressionPredicateNode(Predicate p, String id) {
        this.p = p;
        this.id = id;
    }

    public Predicate p() {
        return p;
    }

    public void setP(Predicate p) {
        this.p = p;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Predicate (" + p.toString() + "): " + id + "\n";
    }

}
