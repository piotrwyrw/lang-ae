package net.aelang.ast;

import net.aelang.Tools;

public class UndefineNode extends Node {

    private String id;
    private boolean silent;

    public UndefineNode(String id, boolean silent) {
        this.id = id;
        this.silent = silent;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean silent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Undefine (" + id + ")" + ((silent) ? " [silent]" : "") + "\n";
    }
}
