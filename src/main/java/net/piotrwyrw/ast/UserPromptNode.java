package net.piotrwyrw.ast;

import net.piotrwyrw.Tools;

public class UserPromptNode extends SolvableNode {

    private String message;

    public UserPromptNode(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String dump(int lpad) {
        return Tools.leftpad(lpad) + "Prompt (" + message +")\n";
    }
}
