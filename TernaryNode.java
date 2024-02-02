import java.util.Optional;
public class TernaryNode extends Node {

    private Node condition;
    private Node consequent;
    private Node alternate;

    public TernaryNode(Node condition, Node consequent, Node alternate){

        this.condition = condition;
        this.consequent = consequent;
        this.alternate = alternate;
    }

    @Override
    public String toString() {
        return condition.toString() + consequent.toString() + alternate.toString();
    }

    public Node getCondition() {
        return condition;
    }

    public Node getConsequent() {
        return consequent;
    }

    public Node getAlternate() {
        return alternate;
    }
}
