import java.util.Optional;

public class DoWhileNode extends StatementNode {


    private Optional<Node> condition;
    private BlockNode block;

    public DoWhileNode(BlockNode block, Optional<Node> condition) {
        this.block = block;
        this.condition = condition;

    }

    public Optional<Node> getCondition(){
        return condition;
    }


    public BlockNode getBlock(){
        return block;
    }


    @Override
    public String toString() {
        return "Do While Block: " + block + " while condition: " + condition.get();
    }
}