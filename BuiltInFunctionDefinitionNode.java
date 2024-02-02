import java.util.HashMap;
import java.util.function.Function;

public class BuiltInFunctionDefinitionNode extends FunctionDefinitionNode {
    private Function<HashMap<String, InterpreterDataType>, String> execute;
    private boolean isVariadic;

    public BuiltInFunctionDefinitionNode(Token name, Function<HashMap<String, InterpreterDataType>, String> execute, boolean isVariadic) {
        super(name);
        this.execute = execute;
        this.isVariadic = isVariadic;
    }

    public String execute(HashMap<String, InterpreterDataType> argumentMap) {
        return execute.apply(argumentMap);
    }

}
