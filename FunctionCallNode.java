import java.util.LinkedList;
import java.util.Optional;

public class FunctionCallNode extends StatementNode {

    private Optional<Node> name;
    private LinkedList<Node> ifList;
    private String functions;


    public FunctionCallNode(String functions){
        this.functions = functions;
        this.name = Optional.empty();
        this.ifList = null;
    }

    public FunctionCallNode(Optional<Node> name, LinkedList<Node> ifList){
        this.name = name;
        this.ifList = ifList;
    }

    public FunctionCallNode() {
        this.name = Optional.empty();
        this.ifList = null;
    }

    public Optional<Node> getName() {
        return name;
    }

    public LinkedList<Node> getIfList() {
        return ifList;
    }

    public String getFunctions() {
        return functions;
    }

    @Override
    public String toString() {
        if (name.isEmpty() && ifList==null){
            return "Function Name: " + functions;
        }
        else if (ifList==null){
            return "Function Name: " + name.get();
        }
        else {
            return "Function Name: " + name.get() + " Params: " + ifList;
        }
    }
}
