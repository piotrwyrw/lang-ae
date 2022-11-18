package net.aelang.ast;

import net.aelang.Tools;

public class NoteNode extends Node {

    private String str;

    public NoteNode(String str) {
        this.str = str;
    }

    public String str() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Note (" + str + ")\n";
    }
}
