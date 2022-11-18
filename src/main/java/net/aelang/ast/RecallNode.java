package net.aelang.ast;

import net.aelang.Tools;

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
