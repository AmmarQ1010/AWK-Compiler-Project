public class AssignmentNode extends StatementNode {

    private Node target;
    private Node Expression;

    public AssignmentNode(Node target, Node Expression){ //constructor
        this.target=target;
        this.Expression=Expression;
    }

    @Override
    public String toString() {
        if (target == null){
            return Expression.toString();
        }
        else{
            return (target + Expression.toString());
        }
    }

    public Node getTarget(){
        return target;
    }

    public Node getExpression(){
        return Expression;
    }
}
