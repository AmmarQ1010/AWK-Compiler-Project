import java.util.LinkedList;
import java.util.Optional;

public class BlockNode extends Node {

    private LinkedList<StatementNode> statement;
    private Optional<Node> condition;


    public BlockNode() { //default constructor
        statement = new LinkedList<>();
        condition = Optional.empty();
    }
    public BlockNode(LinkedList<StatementNode> statement, Optional<Node> condition){ //constructor
            this.statement=statement;
            this.condition=condition;
    }

    public BlockNode(LinkedList<StatementNode> statement){ //constructor
        this.statement=statement;
        this.condition=Optional.empty();
    }

    public LinkedList<StatementNode> getStatements(){
        return statement;
    }

    public Optional<Node> getCondition() {
        return condition;
    }

    public void addStatement(StatementNode b){
        statement.add(b);
    }

    public boolean isFalse() throws Exception {
        if (getCondition().isPresent()){
            return "0".equals(getCondition().get().toString());
        }
        else {
            throw new Exception("Tried to call isFalse() on BlockNode without a condition.");
        }
    }

    @Override
    public String toString() {
        if (condition.isEmpty()){
            return "Statements: " + statement;
        }
        else {
            return "Statements: " + statement + " Condition: " + condition.get();
        }
    }
}
