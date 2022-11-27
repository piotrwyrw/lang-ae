package net.aelang.ast;

import net.aelang.Tools;

import java.util.List;

public class FunctionCallNode extends SolvableNode {

    private String id;
    private List<SolvableNode> params;

    public FunctionCallNode(String id, List<SolvableNode> params) {
        this.id = id;
        this.params = params;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SolvableNode> params() {
        return params;
    }

    public void setParams(List<SolvableNode> params) {
        this.params = params;
    }

    @Override
    public String dump(int lpad) {
        String s = Tools.leftpad(lpad) + "Function call (" + id + "):\n";
        lpad++;
        for (int i = 0; i < params.size(); i++) {
            s += Tools.leftpad(lpad) + "Parameter ($" + (i + 1) + "):\n";
            lpad++;
            s += params.get(i).dump(lpad);
            lpad--;
        }
        lpad--;

        return s;
    }
}
