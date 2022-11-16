package net.aelang.ast;

import net.aelang.Tools;

public class ExecNode extends Node {

    private String file;

    public ExecNode(String file) {
        this.file = file;
    }

    public String file() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Execute script (" + file + ")\n";
    }

}
