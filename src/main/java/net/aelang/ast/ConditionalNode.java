package net.aelang.ast;

import net.aelang.Tools;

public class ConditionalNode extends Node {

    private Node condition;
    private Node statement;

    public ConditionalNode(Node condition, Node statement) {
        this.condition = condition;
        this.statement = statement;
    }

    public Node condition() {
        return condition;
    }

    public void setCondition(Node condition) {
        this.condition = condition;
    }

    public Node statement() {
        return statement;
    }

    public void setStatement(Node statement) {
        this.statement = statement;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Conditional exec:\n";
        lpad++;
        s += Tools.leftpad(lpad) + "Conditional expression:\n";
        lpad++;
        s += condition.dump(lpad);
        lpad--;
        s += Tools.leftpad(lpad) + "Statement:\n";
        lpad++;
        s += statement.dump(lpad);
        lpad--;
        lpad--;
        return s;
    }
}
