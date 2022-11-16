package net.piotrwyrw.ast;

import net.piotrwyrw.Tools;

public class RepeatNode extends Node {

    private Node count;
    private Node statement;

    public RepeatNode(Node count, Node statement) {
        this.count = count;
        this.statement = statement;
    }

    public Node count() {
        return count;
    }

    public void setCount(Node count) {
        this.count = count;
    }

    public Node statement() {
        return statement;
    }

    public void setStatement(Node statement) {
        this.statement = statement;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Repeat Statement:\n";

        lpad ++;
            s += Tools.leftpad(lpad) + "Times:\n";
            lpad ++;
                s += count.dump(lpad);
            lpad --;
            s += Tools.leftpad(lpad) + "Statement:\n";
            lpad ++;
                s += statement.dump(lpad);
            lpad --;
        lpad --;

        return s;
    }
}
