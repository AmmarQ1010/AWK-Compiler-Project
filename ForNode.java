import java.util.Optional;

public class ForNode extends StatementNode {

    private Optional<Node> assignment;
    private Optional<Node> condition;
    private Optional<Node> increment;
    private BlockNode block;

    public ForNode(Optional<Node> assignment, Optional<Node> condition, Optional<Node> increment, BlockNode block) {
        this.assignment = assignment;
        this.condition = condition;
        this.increment = increment;
        this.block = block;

    }

    public Optional<Node> getAssignment() {
        return assignment;
    }

    public Optional<Node> getCondition() {
        return condition;
    }

    public Optional<Node> getIncrement() {
        return increment;
    }

    public BlockNode getBlock() {
        return block;
    }


    @Override
    public String toString() {
        return "For assignment: " + assignment.get() + " condition: " + condition.get() + " increment: " + increment.get() + "block contents: " + block;
    }
}