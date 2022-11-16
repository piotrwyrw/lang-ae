package net.piotrwyrw.ast;

import net.piotrwyrw.Tools;

public class RecallNode extends Node {

    private Node index;

    public RecallNode(Node index) {
        this.index = index;
    }

    public Node index() {
        return index;
    }

    public void setIndex(Node index) {
        this.index = index;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Recall:\n" + index.dump(lpad);
    }

}
