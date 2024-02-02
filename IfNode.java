import java.util.Optional;

public class IfNode extends StatementNode {

    private Optional<Node> condition;
    private BlockNode block;
    private Optional<StatementNode> right;
    private Optional<StatementNode> else1;

    public IfNode(Optional<Node> condition, BlockNode block, Optional<StatementNode> right){
        this.condition = condition;
        this.block = block;
        this.right = right;

    }

    public IfNode(Optional<Node> condition, BlockNode block, Optional<StatementNode> right, Optional<StatementNode> else1){
        this.condition = condition;
        this.block = block;
        this.right = right;
        this.else1 = else1;

    }

    public Optional<Node> getCondition() {
        return condition;
    }

    public BlockNode getBlock() {
        return block;
    }

    public Optional<StatementNode> getRight() {
        return right;
    }

    public Optional<StatementNode> getElse1() {
        return else1;
    }

    @Override
    public String toString() {
        if (right.isEmpty()){
            return "If node condition: " + condition.get() + " block contents: " + block;
        }
        else {
            if (else1 == null || else1.isEmpty()){
                return right.get() + " else: " + block;
            }
            else {
                return right.get() + " else: " + block + " " + else1.get();
            }

        }

    }
}