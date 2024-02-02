import java.util.Optional;

public class ReturnNode extends StatementNode {

    private Optional<Node> returns;

    public ReturnNode (Optional<Node> returns){
        this.returns = returns;
    }

    public Optional<Node> getReturns() {
        return returns;
    }

    @Override
    public String toString() {
        return "Returning: " + returns.get();
    }
}