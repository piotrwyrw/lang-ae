package net.piotrwyrw.ast;

import net.piotrwyrw.Tools;

public class InfoNode extends Node {

    private String id;

    public InfoNode(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Info (" + id + ")\n";
    }
}
