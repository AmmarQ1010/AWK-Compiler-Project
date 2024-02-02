import java.util.Optional;

public class ForEachNode extends StatementNode {

    private Optional<Node> index;
    private BlockNode block;

    public ForEachNode(Optional<Node> index, BlockNode block) {
        this.index = index;

        this.block = block;
    }

    public Optional<Node> getIndex() {
        return index;
    }

    public BlockNode getBlock() {
        return block;
    }

    @Override
    public String toString() {
        return "For Each contents: " + index.get() + " block contents: " + block;
    }
}