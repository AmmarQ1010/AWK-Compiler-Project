import java.util.LinkedList;

public class ProgramNode extends Node {

    private LinkedList<BlockNode> begin;
    private LinkedList<BlockNode> end;
    private LinkedList<BlockNode> other;
    private LinkedList<FunctionDefinitionNode> function;

    public ProgramNode(){
        this.begin = new LinkedList<>();
        this.end = new LinkedList<>();
        this.other = new LinkedList<>();
        this.function = new LinkedList<>();
    }

    public ProgramNode(LinkedList<BlockNode> begin, LinkedList<BlockNode> end, LinkedList<BlockNode> other, LinkedList<FunctionDefinitionNode> function) { //default constructor

        this.begin = begin;
        this.end = end;
        this.other = other;
        this.function = function;

    }

    public void addBegin (BlockNode node) {
        begin.add(node);
    }

    public void addEnd (BlockNode node) {
        end.add(node);
    }

    public void addOther (BlockNode node) {
        other.add(node);
    }

    public void addFunction (FunctionDefinitionNode node) {
        function.add(node);
    }

    public LinkedList<BlockNode> getBegin(){
        return begin;
    }

    public LinkedList<BlockNode> getEnd(){
        return end;
    }

    public LinkedList<BlockNode> getOther(){
        return other;
    }

    public LinkedList<FunctionDefinitionNode> getFunction(){
        return function;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (!begin.isEmpty()) {
            builder.append("BEGIN: ").append(begin).append("\n");
        }
        if (!end.isEmpty()){
            builder.append("END: ").append(end).append("\n");
        }
        if (!other.isEmpty()){
            builder.append("OTHER: ").append(other).append("\n");
        }
        if (!function.isEmpty()){
            builder.append("FUNCTION: ").append(function).append(" Statements: \n");
        }
        if (begin.isEmpty() && end.isEmpty() && other.isEmpty() && function.isEmpty()){
            return "All program lists are empty. There are no begin end other or function.";
        }

        return builder.toString();
    }
}
