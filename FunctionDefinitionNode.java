import java.util.LinkedList;

public class FunctionDefinitionNode extends Node {
    private Token name;
    private LinkedList<Token> params;
    private LinkedList<StatementNode> statements;


    public FunctionDefinitionNode(Token name, LinkedList<Token> params, LinkedList<StatementNode> statements){ //constructor

        this.name = name;
        this.params = params;
        this.statements = statements;
    }

    public FunctionDefinitionNode(Token name){ //constructor

        this.name = name;
        this.params = null;
        this.statements = null;
    }

    public Token getName(){
        return name;
    }

    public LinkedList<Token> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "Function Name: " + name + " Params: " + params;
    }
}
