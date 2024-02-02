import java.util.HashMap;
import java.util.Optional;

public class VariableReferenceNode extends Node {

    private String variablename;
    private Optional<Node> index;
    private InterpreterArrayDataType array;

    public VariableReferenceNode(Token variablename){
        this.variablename=variablename.getVal();
        this.index=Optional.empty();
        this.array = null;
    }

    public VariableReferenceNode(Node variablename){
        this.variablename=null;
        this.index=Optional.empty();
        this.array = null;
    }

    public VariableReferenceNode(Token variablename, Optional<Node> index){
        this.variablename=variablename.getVal();
        this.index=index;
        this.array = null;
    }

    public VariableReferenceNode(Token variablename, Optional<Node> index, InterpreterArrayDataType array){
        this.variablename=variablename.getVal();
        this.index=index;
        this.array=array;
    }

    public VariableReferenceNode(Token variablename, InterpreterArrayDataType array){
        this.variablename=variablename.getVal();
        this.index=Optional.empty();
        this.array=array;
    }

    public String getName() {
        return variablename;
    }

    public InterpreterArrayDataType getArray() {
        return array;
    }

    public Node getIndex() {
        if (index.isPresent()){
            return index.get();
        }
        else {
            return null;
        }
    }

    public boolean isArray(){
        return array != null;
    }


    @Override
    public String toString() {
        if (index.isEmpty()){
            return variablename;
        }
        else if (array!=null){
            return variablename + "[" + index.get() + "] " + array;
        }
        else {
            return variablename + "[" + index.get() + "] ";
        }
    }
}
