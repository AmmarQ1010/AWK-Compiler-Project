import java.util.Optional;

public class ConstantNode extends Node {

    private Optional<Token> value;

    public ConstantNode(Optional<Token> value) {
        if (value.isPresent() && value.get().getType() != Token.TokenType.NUMBER &&
                 value.get().getType() != Token.TokenType.STRINGLITERAL) {
            throw new IllegalArgumentException("ConstantNode can only accept numbers or string literals.");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value.get().getVal();
    }

    public String getValue(){
        return value.get().getVal();
    }
}