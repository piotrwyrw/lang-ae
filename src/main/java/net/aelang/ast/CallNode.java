package net.aelang.ast;

import net.aelang.Pair;
import net.aelang.Tools;
import net.aelang.codegen.Register;
import net.aelang.codegen.instruction.Instruction;

import java.util.List;

public class CallNode extends SolvableNode {

    private String id;
    private List<Node> params;

    public CallNode(String id, List<Node> params) {
        this.id = id;
        this.params = params;
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Node> params() {
        return params;
    }

    public void setParams(List<Node> params) {
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

    @Override
    public Pair<Register, Instruction[]> assemble() {
        return null;
    }
}
