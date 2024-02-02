import java.util.Optional;

public class DeleteNode extends StatementNode {

    private Optional<Node> arr;

    public DeleteNode(Optional<Node> arr){
        this.arr = arr;
    }

    public Optional<Node> getArr(){
        return arr;
    }

    @Override
    public String toString() {

        return "Deleting: " + arr.get();

    }

}