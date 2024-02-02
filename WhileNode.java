import java.util.LinkedList;
import java.util.Optional;

public class WhileNode extends StatementNode {

    private Optional<Node> condition;
    private BlockNode block;

    public WhileNode(Optional<Node> condition, BlockNode block){
        this.condition = condition;
        this.block = block;

    }

    public Optional<Node> getCondition() {
        return condition;
    }

    public BlockNode getBlock() {
        return block;
    }

    @Override
    public String toString() {
        return "While Condition: " + condition.get() + " block list: " + block;
    }
}