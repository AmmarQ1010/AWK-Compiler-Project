import java.util.Optional;

public class PatternNode extends Node{

    Optional<Token> value;

    public PatternNode(Optional<Token> value){

        this.value = value;
        if (value.isPresent() && value.get().getType() != Token.TokenType.PATTERN) {
            throw new IllegalArgumentException("PatternNode can only accept patterns.");
        }
    }


    @Override
    public String toString() {
        return value.get().getVal();
    }
}
