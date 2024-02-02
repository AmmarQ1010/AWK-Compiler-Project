import java.util.Optional;

public class OperationNode extends Node {

    private Node left;
    private OperationNode.operations operationtype;
    private Optional<Node> right;

    private OperationNode.operations operationtype2;

    public OperationNode(Node left, OperationNode.operations operationtype2){
        this.left = left;
        this.operationtype2 = operationtype2;
        this.right = Optional.empty();
    }

    public OperationNode(Node left, OperationNode.operations operationtype, Optional<Node> right){
        this.left = left;
        this.operationtype = operationtype;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public Optional<Node> getRight() {
        return right;
    }

    public OperationNode.operations getType() {
        if (right.isEmpty()){
            return operationtype2;
        }
        else {
            return operationtype;
        }

    }

    public enum operations{ //enum constructor
        EQ, NE, LT, LE, GT, GE, AND, OR, NOT, MATCH, NOTMATCH, DOLLAR,
        PREINC,POSTINC,PREDEC, POSTDEC,UNARYPOS, UNARYNEG, IN,
        EXPONENT, ADD, SUBTRACT,MULTIPLY, DIVIDE,MODULO, CONCATENATION,

    }

    @Override
    public String toString() {
        if(right.isEmpty()) {
            return left + " " + operationtype2 + " ";
        }
        else {
            return left + " " + operationtype + " " + right.get();
        }
    }

}
