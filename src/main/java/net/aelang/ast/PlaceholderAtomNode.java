package net.aelang.ast;

import net.aelang.Tools;

public class PlaceholderAtomNode extends Node {

    private int ref;

    public PlaceholderAtomNode(int ref) {
        this.ref = ref;
    }

    public int ref() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Placeholder (" + ref + ")\n";
    }
}
