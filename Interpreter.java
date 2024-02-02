import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.PatternSyntaxException;

public class Interpreter {

    private HashMap<String,InterpreterDataType> globalVariables;//global variable hashmap
    private HashMap<String, FunctionDefinitionNode> functionMap;//function hashmap
    private LineManager lineManager;
    private int NR = 0;//record number
    private int FNR = 0; //record number in file

    public Interpreter(ProgramNode program, String path) {
        //read in a file from the path provided
        if (path != null) {
            try {
                List<String> inputLines = Files.readAllLines(Paths.get(path));
                lineManager = new LineManager(inputLines);
            } catch (IOException e) {//make sure the file is read properly
                throw new RuntimeException("Error reading the input file: " + e.getMessage(), e);
            }
        } else {//create an empty linemanager
            lineManager = new LineManager(new LinkedList<String>());
        }

        //initialize global variable map
        globalVariables = new HashMap<>();
        globalVariables.put("FS", new InterpreterDataType(" "));
        globalVariables.put("OFMT", new InterpreterDataType("%.6g"));
        globalVariables.put("OFS", new InterpreterDataType(" "));
        globalVariables.put("ORS", new InterpreterDataType("\n"));
        globalVariables.put("FILENAME", new InterpreterDataType(path));

        functionMap = new HashMap<>();
        //gets our function list
        LinkedList<FunctionDefinitionNode> functionDefinitions = program.getFunction();

        //loops through the function list and adds each function to the functionmap
        for (int i = 0; i < functionDefinitions.size(); i++) {
            FunctionDefinitionNode functionDef = functionDefinitions.get(i);
            functionMap.put(functionDef.getName().getVal(), functionDef);
        }

        //lambda function for print
        BuiltInFunctionDefinitionNode printFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.PRINT, "print"), execute-> {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (String key : execute.keySet()) {
                //if not the first argument, put a space
                if (!first) {
                    result.append(" ");//we're adding spaces because our lexer removed them all
                }
                result.append(execute.get(key).toString());//add the argument to our string
                first = false;
            }

            System.out.println(result);//print the string
            //return result.toString();
            return "";
        },true);
        functionMap.put("print", printFunction);//add a print function node to our function map

        //lambda function for printf
        BuiltInFunctionDefinitionNode printfFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.PRINTF, "printf"), execute-> {
            if (execute.containsKey("format")) {//assumes execute.put("format", %d%d%d) or something similar
                String format = execute.get("format").toString();
                StringBuilder result = new StringBuilder();
                boolean first = true;

                for (String key : execute.keySet()) {
                    if (!key.equals("format")) {
                        if (!first) {
                            result.append(" ");
                        }
                        result.append(execute.get(key).toString());
                        first = false;
                    }
                }

                try {
                    String formattedString = String.format(format, result);
                    System.out.println(formattedString);
                    return "";
                } catch (IllegalFormatException e) {
                    throw new RuntimeException("printf: format error", e);
                }
            } else {
                throw new RuntimeException("printf: missing format key");
            }
        }, true);
        functionMap.put("printf", printfFunction);//add a printf function node to our function map

        //lambda function for getline
        BuiltInFunctionDefinitionNode getlineFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.GETLINE, "getline"), execute->{
            if (lineManager.splitAndAssign()) {
                return "1";//success
            } else {
                return "0";//failure
            }
        }, false);
        functionMap.put("getline", getlineFunction);

        //lambda function for next
        BuiltInFunctionDefinitionNode nextFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.NEXT, "next"), execute-> {
            if (lineManager.splitAndAssign()) {
                return "1";//success
            } else {
                return "0";//failure
            }
        }, false);
        functionMap.put("next", nextFunction);

        //lambda function for gsub
        BuiltInFunctionDefinitionNode gsubFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.WORD, "gsub"), execute -> {
            if (execute.containsKey("regex") && execute.containsKey("replacement") && execute.containsKey("target")) {
                try {
                    String regex = execute.get("regex").toString();
                    String replacement = execute.get("replacement").toString();
                    String target = execute.get("target").toString();

                    //perform gsub operation
                    String result = target.replaceAll(regex, replacement);

                    //specifies the name of the variable where the index will be stored as result
                    globalVariables.put(result, execute.get("result"));

                    return result;
                } catch (PatternSyntaxException e) {
                    throw new RuntimeException("gsub: invalid regex pattern", e);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("gsub: invalid replacement string", e);
                }
            } else {
                throw new RuntimeException("gsub: missing regex, replacement, or target key in executeMap");
            }
        }, false);
        functionMap.put("gsub", gsubFunction);

        //lambda function for index
        BuiltInFunctionDefinitionNode indexFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.WORD, "index"), execute -> {
            if (execute.containsKey("substring") && execute.containsKey("target")) {
                try {
                    String substring = execute.get("substring").toString();
                    String target = execute.get("target").toString();

                    //use indexof to get the position
                    int position = target.indexOf(substring);

                    //specifies the name of the variable where the index will be stored as result
                    globalVariables.put("result", execute.get(String.valueOf(position)));

                    return String.valueOf(position);
                } catch (NullPointerException e) {
                    throw new RuntimeException("index: substring not found", e);
                }
            } else {
                throw new RuntimeException("index: missing substring, or target key in executeMap");
            }
        }, false);
        functionMap.put("index", indexFunction);

        //lambda function for length
        BuiltInFunctionDefinitionNode lengthFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.WORD, "length"), execute-> {
            if (execute.containsKey("target")) {
                try {
                    String target = execute.get("target").toString();
                    int length = target.length();

                    //specifies the name of the variable where the length will be stored as result
                    globalVariables.put("result", execute.get(String.valueOf(length)));

                    return String.valueOf(length);
                } catch (NullPointerException e) {
                    throw new RuntimeException("length: invalid target value", e);
                }
            } else {
                throw new RuntimeException("length: missing target key in executeMap");
            }
        }, false);
        functionMap.put("length", lengthFunction);

        //lambda function for match
        BuiltInFunctionDefinitionNode matchFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.MATCH, "match"), execute-> {
            if (execute.containsKey("regex") && execute.containsKey("target")) {
                try {
                    String regex = execute.get("regex").toString();
                    String target = execute.get("target").toString();

                    //use java matches method to check if it matches...
                    boolean isMatch = target.matches(regex);

                    //specifies the name of the variable where matches condition will be stored. 1 for successful match, 0 for not match.
                    globalVariables.put("result", execute.get(isMatch ? "1" : "0"));

                    return isMatch ? "1" : "0";
                } catch (PatternSyntaxException e) {
                    throw new RuntimeException("match: invalid regex or match value", e);
                }
            } else {
                throw new RuntimeException("match: missing regex, or target key in executeMap");
            }
        }, false);
        functionMap.put("match", matchFunction);

        //lambda function for split
        BuiltInFunctionDefinitionNode splitFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.MATCH, "split"), execute->{
            if (execute.containsKey("target") && execute.containsKey("delimiter")) {
                try {
                    String target = execute.get("target").toString();
                    String delimiter = execute.get("delimiter").toString();

                    //split using string split
                    String[] substrings = target.split(delimiter);

                    //create an array to hold the substrings
                    InterpreterArrayDataType resultArray = new InterpreterArrayDataType();

                    //populate the array
                    for (int i = 0; i < substrings.length; i++) {
                        resultArray.set(Integer.toString(i), new InterpreterDataType(substrings[i]));
                    }
                    //specifies the name of the variable where the resultant array will be stored
                    globalVariables.put("result", new InterpreterDataType(resultArray.getArrayMap().toString()));

                    //update NF
                    globalVariables.put("NF", new InterpreterDataType(String.valueOf(substrings.length)));

                    return resultArray.getArrayMap().toString();
                } catch (PatternSyntaxException e) {
                    throw new RuntimeException("split: invalid delimiter pattern", e);
                }
            } else {
                throw new RuntimeException("split: missing target or delimiter key in executeMap");
            }
        }, false);
        functionMap.put("split", splitFunction);

        //lambda function for sprintf
        BuiltInFunctionDefinitionNode sprintfFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.PRINTF, "sprintf"), execute-> {
            if (execute.containsKey("format")) {//assumes execute.put("format", %d%d%d) or something similar
                String format = execute.get("format").toString();
                StringBuilder result = new StringBuilder();
                boolean first = true;

                for (String key : execute.keySet()) {
                    if (!key.equals("format")) {
                        if (!first) {
                            result.append(" ");
                        }
                        result.append(execute.get(key).toString());
                        first = false;
                    }
                }

                try {
                    return String.format(format, result);
                } catch (IllegalFormatException e) {
                    throw new RuntimeException("sprintf: format error", e);
                }
            } else {
                throw new RuntimeException("sprintf: missing format key in executeMap");
            }
        }, true);
        functionMap.put("sprintf", sprintfFunction);//add a printf function node to our function map

        //lambda function for sub
        BuiltInFunctionDefinitionNode subExecute = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.WORD, "sub"), execute -> {
            if (execute.containsKey("regex") && execute.containsKey("replacement") && execute.containsKey("target")) {
                try {
                    String regex = execute.get("regex").toString();
                    String replacement = execute.get("replacement").toString();
                    String target = execute.get("target").toString();

                    //use replaceFirst
                    String result = target.replaceFirst(regex, replacement);

                    //specifies the name of the variable where the result will be stored
                    globalVariables.put(result, execute.get("result"));

                    return result;
                } catch (PatternSyntaxException e) {
                    throw new RuntimeException("sub: invalid regex pattern", e);
                }
            } else {
                throw new RuntimeException("sub: missing regex, replacement, or target key in executeMap");
            }
        },false);
        functionMap.put("sub", subExecute);

        //lambda function for substr
        BuiltInFunctionDefinitionNode substrFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.WORD, "substr"), execute -> {
        if (execute.containsKey("target") && execute.containsKey("start")) {
            try {
                String target = execute.get("target").toString();
                int start = Integer.parseInt(execute.get("start").toString());

                if (start >= 0 && start <= target.length()) {
                    if (execute.containsKey("length")) {
                        int length = Integer.parseInt(execute.get("length").toString());
                        if (length >= 0) {
                            String result = target.substring(start, Math.min(start + length, target.length()));
                            globalVariables.put(execute.get("result").toString(), new InterpreterDataType(result));
                            return result;
                        } else {
                            throw new RuntimeException("substr: negative length");
                        }
                    } else {
                        String result = target.substring(start);
                        globalVariables.put(result, execute.get("substr"));
                        return result;
                    }
                } else {
                    throw new RuntimeException("substr: start position out of range");
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("substr: invalid start number", e);
            }
        } else {
            throw new RuntimeException("substr: missing target or start key in executeMap");
        }
        }, false);
        functionMap.put("substr", substrFunction);

        //lambda function for tolower
        BuiltInFunctionDefinitionNode tolowerFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.WORD, "tolower"), execute -> {
            if (execute.containsKey("target")) {
                try {
                    String target = execute.get("target").toString();
                    String result = target.toLowerCase();

                    //specifies the name of the variable where the result will be stored
                    globalVariables.put(result, execute.get("tolower"));

                    return result;
                } catch (NullPointerException e) {
                    throw new RuntimeException("tolower: invalid target value", e);
                }
            } else {
                throw new RuntimeException("tolower: missing target key in executeMap");
            }
        }, false);
        functionMap.put("tolower", tolowerFunction);

        //lambda function for toupper
        Function<HashMap<String, InterpreterDataType>, String> toupperExecute = (execute) -> {
            if (execute.containsKey("target")) {
                try {
                    String target = execute.get("target").toString();
                    String result = target.toUpperCase();

                    //specifies the name of the variable where the result will be stored
                    globalVariables.put(result, execute.get("toupper"));

                    return result;
                } catch (NullPointerException e) {
                    throw new RuntimeException("toupper: invalid target value", e);
                }
            } else {
                throw new RuntimeException("toupper: missing target key in executeMap");
            }
        };
        BuiltInFunctionDefinitionNode toupperFunction = new BuiltInFunctionDefinitionNode(new Token(Token.TokenType.WORD, "toupper"), toupperExecute, false);
        functionMap.put("toupper", toupperFunction);
    }

    public LineManager getLineManager() {
        return lineManager;
    }

    public HashMap<String, InterpreterDataType> getGlobalVariables() {
        return globalVariables;
    }

    public HashMap<String, FunctionDefinitionNode> getFunction() {
        return functionMap;
    }

    public InterpreterDataType getIDT(Node node, HashMap<String, InterpreterDataType> locals) throws Exception {
        if (node instanceof AssignmentNode assignment) {
            if (assignment.getTarget() instanceof VariableReferenceNode target) {
                InterpreterDataType result = getIDT(assignment.getExpression(), locals);
                String varName = target.getName();
                locals.put(varName, result);
                return result;
            } else if (assignment.getTarget() instanceof OperationNode && ((OperationNode) assignment.getTarget()).getType() == OperationNode.operations.DOLLAR) {
                    String variableName = "$" + getIDT(((OperationNode) assignment.getTarget()).getLeft(), locals).toString();
                    InterpreterDataType variableValue = locals.get(variableName);

                    if (variableValue == null) {//if the variable was not found in our local variables
                        variableValue = getIDT((assignment.getExpression()), locals);
                    }
                    if (variableValue != null) {//if the variable was found in our local variables
                        return variableValue;
                    } else {
                        throw new Exception("Variable not found: " + variableName);
                    }
            }
        } else if (node instanceof ConstantNode constant) {
            return new InterpreterDataType(constant.getValue());//just returns value of constant node
        } else if (node instanceof FunctionCallNode functioncall) {
            String temp = runFunctionCall(functioncall, locals);//currently does nothing
            return new InterpreterDataType(temp);
        } else if (node instanceof PatternNode) {//throws exception for pattern node
            throw new IllegalArgumentException("PatternNode cannot be used in this context.");
        } else if (node instanceof TernaryNode ternary) {
            //get boolean condition
            InterpreterDataType conditionResult = getIDT(ternary.getCondition(), locals);

            //check if condition is true or false
            //isFalse literally checks if string = "0"
            if (conditionResult != null && !conditionResult.isEmpty() && !conditionResult.isFalse()) {//if false
                //return consequent
                return getIDT(ternary.getConsequent(), locals);
            } else {//if true
                //return alternate
                return getIDT(ternary.getAlternate(), locals);
            }
        } else if (node instanceof VariableReferenceNode varRef) {
            if (varRef.getIndex()==null) {
                String varName = varRef.getName();
                //look up the variable in our local or global map and return it
                if (locals.containsKey(varName)) {
                    return locals.get(varName);
                } else {
                    if (globalVariables.containsKey(varName)) {
                        return globalVariables.get(varName);
                    }
                }
            } else {
                if (varRef.isArray()){//if we have an index
                    InterpreterDataType indexIDT = getIDT(varRef.getIndex(), locals);//index
                    HashMap<String, InterpreterDataType> array = varRef.getArray().getArrayMap();//array
                    return array.get(indexIDT.getValue());
                }
                else {
                    throw new Exception("This variable is not an array.");
                }
            }
        }
             else if (node instanceof OperationNode operation) {
                OperationNode.operations operationType = operation.getType();
                InterpreterDataType left = getIDT(operation.getLeft(), locals);
                if (operationType==OperationNode.operations.MATCH){
                    Node right2 = operation.getRight().get();
                    if (right2 instanceof PatternNode) {
                        return new InterpreterDataType(left.toString().matches(right2.toString()) ? "1" : "0");
                    } else {
                        throw new Exception("Invalid use of MATCH operation. Right operand should be a PatternNode.");
                    }
                }
                else if (operationType==OperationNode.operations.NOTMATCH){
                    Node right2 = operation.getRight().get();
                    if (right2 instanceof PatternNode) {
                        return new InterpreterDataType(left.toString().matches(right2.toString()) ? "0" : "1");
                    } else {
                        throw new Exception("Invalid use of MATCH operation. Right operand should be a PatternNode.");
                    }
                }
                else if (operation.getRight().isPresent()) {//if we have a right value
                    InterpreterDataType right = getIDT(operation.getRight().get(), locals);

                    switch (operationType) {
                        case OperationNode.operations.ADD:
                            return new InterpreterDataType(Float.toString(Float.parseFloat(left.toString()) + Float.parseFloat(right.toString())));

                        case OperationNode.operations.SUBTRACT:
                            return new InterpreterDataType(Float.toString(Float.parseFloat(left.toString()) - Float.parseFloat(right.toString())));

                        case OperationNode.operations.MULTIPLY:
                            return new InterpreterDataType(Float.toString(Float.parseFloat(left.toString()) * Float.parseFloat(right.toString())));

                        case OperationNode.operations.DIVIDE:
                            return new InterpreterDataType(Float.toString(Float.parseFloat(left.toString()) / Float.parseFloat(right.toString())));

                        case OperationNode.operations.MODULO:
                            return new InterpreterDataType(Float.toString(Float.parseFloat(left.toString()) % Float.parseFloat(right.toString())));

                        case OperationNode.operations.EXPONENT:
                            return new InterpreterDataType(Float.toString((float) Math.pow(Float.parseFloat(left.toString()), Float.parseFloat(right.toString()))));

                        case OperationNode.operations.EQ:
                            return new InterpreterDataType(left.toString().equals(right.toString()) ? "1" : "0");

                        case OperationNode.operations.NE:
                            return new InterpreterDataType(left.toString().equals(right.toString()) ? "0" : "1");

                        case OperationNode.operations.LT:
                            return new InterpreterDataType(Float.parseFloat(left.toString()) < Float.parseFloat(right.toString()) ? "1" : "0");

                        case OperationNode.operations.GT:
                            return new InterpreterDataType(Float.parseFloat(left.toString()) > Float.parseFloat(right.toString()) ? "1" : "0");

                        case OperationNode.operations.AND:
                            try {//check to see if it can be converted to float
                                float leftValue = Float.parseFloat(left.toString());
                                float rightValue = Float.parseFloat(right.toString());

                                boolean leftIsTrue = leftValue != 0;
                                boolean rightIsTrue = rightValue != 0;

                                return new InterpreterDataType((leftIsTrue && rightIsTrue) ? "1" : "0");
                            } catch (NumberFormatException e) {//if it can't be converted, return "false" (using "0" for false)
                                return new InterpreterDataType("0");
                            }

                        case OperationNode.operations.OR:
                            try {//check to see if it can be converted to float
                                float leftValue = Float.parseFloat(left.toString());
                                float rightValue = Float.parseFloat(right.toString());

                                boolean leftIsTrue = leftValue != 0;
                                boolean rightIsTrue = rightValue != 0;

                                return new InterpreterDataType((leftIsTrue || rightIsTrue) ? "1" : "0");
                            } catch (NumberFormatException e) {//if it can't be converted, return "false" (using "0" for false)
                                return new InterpreterDataType("0");
                            }
                        case OperationNode.operations.CONCATENATION:
                            return new InterpreterDataType(left.toString() + right.toString());

                        case OperationNode.operations.IN:
                            if (operation.getRight().get() instanceof VariableReferenceNode varref) {
                                if (varref.isArray()){//if we have an array hashmap in varref
                                    String value = String.valueOf(varref.getArray().getArrayMap().get(operation.getLeft().toString()));
                                    if (!Objects.equals(value, "null")){
                                        return new InterpreterDataType("1");//1 for true
                                    }
                                    else {
                                        return new InterpreterDataType("0");//0 for false
                                    }
                                }
                                else {
                                    throw new Exception("Invalid use of 'IN' operator. VariableReferenceNode must have a hashmap which is an array.");
                                }
                            } else {
                                throw new Exception("Invalid use of 'IN' operator. Must have an OperationNode with a VariableReferenceNode.");
                            }

                        default:
                            throw new Exception("Unsupported operation: " + operationType);
                    }
                }
                else {//if we don't have a right value
                    switch (operationType) {
                        case OperationNode.operations.NOT:
                            try {//check to see if it can be converted to float
                                float operandValue = Float.parseFloat(left.toString());

                                boolean operandIsTrue = operandValue != 0;

                                return new InterpreterDataType(operandIsTrue ? "0" : "1");
                            } catch (NumberFormatException e) {//if it can't be converted, return "false" (using "0" for false)
                                return new InterpreterDataType("0");
                            }

                        case OperationNode.operations.DOLLAR:
                            String variableName = "$" + left.toString();
                            InterpreterDataType variableValue = locals.get(variableName);
                            if (variableValue == null) {
                                variableValue = globalVariables.get(variableName);
                            }
                            if (variableValue != null) {
                                return variableValue;
                            } else {
                                throw new Exception("Variable not found: " + variableName);
                            }

                        case OperationNode.operations.PREINC, OperationNode.operations.POSTINC://combine pre and postinc
                            return new InterpreterDataType(Float.toString(Float.parseFloat(left.toString()) + 1));

                        case OperationNode.operations.PREDEC, OperationNode.operations.POSTDEC://combine pre and postdec
                            return new InterpreterDataType(Float.toString(Float.parseFloat(left.toString()) - 1));

                        case OperationNode.operations.UNARYPOS:
                            return new InterpreterDataType(Float.toString(Float.parseFloat(left.toString())));

                        case OperationNode.operations.UNARYNEG:
                            return new InterpreterDataType(Float.toString(-Float.parseFloat(left.toString())));
                    }
                }
             }
             //if our node is not a constant, functioncall, pattern, ternary, variablereference, or operation
            return null;
    }

    public String runFunctionCall(FunctionCallNode functioncall, HashMap<String,InterpreterDataType> locals) {//does nothing bless up
        return "";
    }


    public ReturnType processStatement(HashMap<String, InterpreterDataType> locals, StatementNode stmt) throws Exception {
        if (stmt instanceof AssignmentNode assignment) {
            InterpreterDataType value = (getIDT(assignment, locals));
            if (value == null){
                throw new Exception("Not a valid assignment statement.");
            }
            else {
                return new ReturnType(ReturnType.Type.NONE, value.toString());
            }
        } else if (stmt instanceof BreakNode) {
            return new ReturnType(ReturnType.Type.BREAK);
        } else if (stmt instanceof ContinueNode) {
            return new ReturnType(ReturnType.Type.CONTINUE);
        } else if (stmt instanceof DeleteNode deleteNode) {
            //get params of delete
            Optional<Node> arr = deleteNode.getArr();

            if (arr.isPresent() && arr.get() instanceof VariableReferenceNode varRefNode) {
                String varName = varRefNode.getName();

                //if it has an index and has an array in the variable, delete specific elements from the array
                if (varRefNode.getIndex() != null && varRefNode.isArray()) {
                    InterpreterArrayDataType array = new InterpreterArrayDataType(varRefNode.getArray().getArrayMap());
                    InterpreterDataType indexIDT = getIDT(varRefNode.getIndex(), locals);
                    array.getArrayMap().remove(indexIDT.getValue());
                }
                //if no index, delete the entire array
                else {
                    locals.remove(varName);
                }

                return new ReturnType(ReturnType.Type.NONE);
            } else {
                throw new Exception("Invalid use of 'DeleteNode'. DeleteNode must specify a variable reference.");
            }
        } else if (stmt instanceof DoWhileNode doWhileNode) {
            InterpreterDataType conditionResult;
            do {
                //interpret block
                ReturnType blockResult = InterpretListOfStatements(doWhileNode.getBlock().getStatements(), locals);
                //check block
                if (blockResult != null && blockResult.getType() != ReturnType.Type.NONE) {
                    //handle break or continue
                    switch (blockResult.getType()) {
                        case BREAK:
                            return new ReturnType(ReturnType.Type.NONE);//return finishes the loop
                        case CONTINUE:
                            break;//continue will continue to the next iteration
                    }
                }

                //check if we should continue the loop
                conditionResult = getIDT(doWhileNode.getCondition().orElse(null), locals);

                //continue if true
            } while (conditionResult != null && !conditionResult.isEmpty() && !conditionResult.isFalse());

            return new ReturnType(ReturnType.Type.NONE);
        } else if (stmt instanceof ForNode forNode) {
            if (forNode.getAssignment().isPresent()) {
                processStatement(locals, (StatementNode) forNode.getAssignment().get());
            }

            while (true) {
                if (forNode.getCondition().isPresent()) {
                    InterpreterDataType conditionResult = getIDT(forNode.getCondition().get(), locals);
                    if (conditionResult == null || conditionResult.isEmpty() || conditionResult.isFalse()) {
                        //exit if false
                        break;
                    }
                }

                //interpret block
                ReturnType blockResult = InterpretListOfStatements(forNode.getBlock().getStatements(), locals);

                //check block
                if (blockResult != null && blockResult.getType() != ReturnType.Type.NONE) {
                    //handle break or continue
                    switch (blockResult.getType()) {
                        case BREAK:
                            return new ReturnType(ReturnType.Type.NONE);//return finishes the loop
                        case CONTINUE:
                            break;//continue will continue to the next iteration
                    }
                }

                if (forNode.getIncrement().isPresent()) {
                    processStatement(locals, (StatementNode) forNode.getIncrement().get());
                }
            }

            return new ReturnType(ReturnType.Type.NONE);
        } else if (stmt instanceof ForEachNode forEachNode) {
            Node indexNode = forEachNode.getIndex().orElse(null);

            //check if this index is valid for a for each loop
            if (indexNode instanceof VariableReferenceNode indexVar) {

                //get array
                InterpreterArrayDataType array = new InterpreterArrayDataType(indexVar.getArray().getArrayMap());

                    //loop
                    for (String key : array.getArrayMap().keySet()) {
                        //set index into the local variable map
                        locals.put(indexVar.getName(), array.getArrayMap().get(key));

                        //interpret block
                        ReturnType blockResult = InterpretListOfStatements(forEachNode.getBlock().getStatements(), locals);

                        //check block
                        if (blockResult != null && blockResult.getType() != ReturnType.Type.NONE) {
                            // Handle break or continue
                            //handle break or continue
                            switch (blockResult.getType()) {
                                case BREAK:
                                    return new ReturnType(ReturnType.Type.NONE);//return finishes the loop
                                case CONTINUE:
                                    break;//continue will continue to the next iteration
                            }
                        }
                    }

                    //clear index from locals after loop
                    locals.remove(indexVar.getName());
                }

            return new ReturnType(ReturnType.Type.NONE);
        } else if (stmt instanceof FunctionCallNode functionCallNode) {
            if (getIDT(functionCallNode, locals) == null){
                throw new Exception("Not a valid function call statement.");
            }
            else
                return new ReturnType(ReturnType.Type.NONE);
        } else if (stmt instanceof IfNode ifNode) {
            //check condition
        InterpreterDataType conditionResult = getIDT(ifNode.getCondition().orElse(null), locals);

        if (conditionResult != null && !conditionResult.isEmpty() && !conditionResult.isFalse()) {
            //interpret block if true
            ReturnType blockResult = InterpretListOfStatements(ifNode.getBlock().getStatements(), locals);

            //check block
            if (blockResult != null && blockResult.getType() != ReturnType.Type.NONE) {
                //handle break or continue
                switch (blockResult.getType()) {
                    case BREAK:
                        return new ReturnType(ReturnType.Type.NONE);//return finishes the loop
                    case CONTINUE:
                        break;//continue will continue to the next iteration
                }
            }
        } else {
            //check for else
            if (ifNode.getElse1().isPresent()) {
                // Interpret the else block of statements
                ReturnType elseResult = InterpretListOfStatements(ifNode.getBlock().getStatements(), locals);

                if (elseResult != null && elseResult.getType() != ReturnType.Type.NONE) {
                    //handle break or continue
                    switch (elseResult.getType()) {
                        case BREAK:
                            return new ReturnType(ReturnType.Type.NONE);//return finishes the loop
                        case CONTINUE:
                            break;//continue will continue to the next iteration
                    }
                }
            }
        }

            return new ReturnType(ReturnType.Type.NONE);

            } else if (stmt instanceof ReturnNode returnNode) {
                if (returnNode.getReturns().isPresent()) {
                    //evaluate expression being returned
                    InterpreterDataType returnValue = getIDT(returnNode.getReturns().get(), locals);

                    //return..
                    return new ReturnType(ReturnType.Type.RETURN, returnValue.toString());
                } else {
                    //return without a value
                    return new ReturnType(ReturnType.Type.RETURN);
                }
            } else if (stmt instanceof WhileNode whileNode) {
                    while (true) {
                        InterpreterDataType conditionResult = getIDT(whileNode.getCondition().orElse(null), locals);

                        if (conditionResult != null && !conditionResult.isEmpty() && !conditionResult.isFalse()) {
                            //interpret block if true
                            ReturnType blockResult = InterpretListOfStatements(whileNode.getBlock().getStatements(), locals);

                            if (blockResult != null && blockResult.getType() != ReturnType.Type.NONE) {
                                //handle break or continue
                                switch (blockResult.getType()) {
                                    case BREAK:
                                        return new ReturnType(ReturnType.Type.NONE);//return finishes the loop
                                    case CONTINUE:
                                        break;//continue will continue to the next iteration
                                }
                            }
                        } else {
                            //exit if condition is false
                            break;
                        }
                    }
                    return new ReturnType(ReturnType.Type.NONE);
            }

                //if an unsupported statement type is encountered
                throw new Exception("Unsupported statement type: " + stmt.getClass().getSimpleName());
        }

    public ReturnType InterpretListOfStatements(LinkedList<StatementNode> statements, HashMap<String, InterpreterDataType> locals) throws Exception {
        for (StatementNode statement : statements) {
            ReturnType returnType = processStatement(locals, statement);
            if (returnType != null && returnType.getType() != ReturnType.Type.NONE) {
                //handle break or continue
                switch (returnType.getType()) {
                    case BREAK:
                        return new ReturnType(ReturnType.Type.NONE);//return finishes the loop
                    case CONTINUE:
                        break;//continue will continue to the next iteration
                }
            }
        }

        return new ReturnType(ReturnType.Type.NONE);
    }

    public void InterpretProgram(ProgramNode programNode, HashMap<String, InterpreterDataType> locals) throws Exception {
        //call InterpretBlock() on each begin block
        for (BlockNode block : programNode.getBegin()) {
            InterpretBlock(block, locals);
        }

        LineManager lineManager = getLineManager();//make a new line manager so we can look through each line

        while (lineManager.splitAndAssign()) {
            for (BlockNode block : programNode.getOther()) {
                InterpretBlock(block, locals);
            }
        }

        //call InterpretBlock() on each end block
        for (BlockNode block : programNode.getEnd()) {
            InterpretBlock(block, locals);
        }
    }

    public void InterpretBlock(BlockNode blockNode, HashMap<String, InterpreterDataType> locals) throws Exception {
        //check the condition if it exists
        if (blockNode.getCondition().isPresent()) {
            if (!blockNode.isFalse()) {
                for (StatementNode statement : blockNode.getStatements()) {//process each statement in the block
                    processStatement(locals, statement);
                }
            }
        }
        else if (blockNode.getCondition().isEmpty()){
            for (StatementNode statement : blockNode.getStatements()) {//process each statement in the block
                processStatement(locals, statement);
            }
        }
    }

    public String RunFunctionCall(HashMap<String, InterpreterDataType> locals, FunctionCallNode functionCallNode) throws Exception {
        String functionName = functionCallNode.getFunctions();

        if (functionMap.containsKey(functionName)) {
            FunctionDefinitionNode functionDefinition = functionMap.get(functionName);

            if (functionDefinition instanceof BuiltInFunctionDefinitionNode builtInFunction) {

                //check if the parameter count matches
                if (functionCallNode.getIfList() == null || functionCallNode.getIfList().size() == builtInFunction.getParams().size()) {
                    HashMap<String, InterpreterDataType> argumentMap = new HashMap<>();
                    //map function parameters to arguments
                    if (functionCallNode.getIfList() != null) {
                        for (int i = 0; i < functionCallNode.getIfList().size(); i++) {
                            Token parameter = functionDefinition.getParams().get(i);
                            Node argument = functionCallNode.getIfList().get(i);
                            InterpreterDataType argumentValue = getIDT(argument, locals);
                            argumentMap.put(parameter.getVal(), argumentValue);
                        }
                    }
                    //execute the built-in function
                    return builtInFunction.execute(argumentMap);
                } else {
                    throw new RuntimeException("Parameter count mismatch for function: " + functionName);
                }
            } else {
                throw new RuntimeException("Unsupported function type: " + functionName);
            }
        } else {
            throw new RuntimeException("Unknown function: " + functionName);
        }
    }

    class LineManager{

        private List<String> lines;
        public LineManager(List<String> lines){
            this.lines = lines;
        }

        public boolean splitAndAssign(){
            String line = lines.get(0);//make a string of line
            String[] fields = line.split(globalVariables.get("FS").getValue());//split by FS which is " "
            String temp = "";//temp just for $0
            for (int i = 0; i < fields.length; i++) {
                temp+=fields[i] + " ";
            }//loop through line to put all fields into temp
            globalVariables.put("$0", new InterpreterDataType(temp));//whole line = $0
            for (int i = 1; i < fields.length+1; i++) {//rest of the fields are assigned to $1 - $i
                globalVariables.put("$" + i, new InterpreterDataType(fields[i-1]));
            }
            //update NR and FNR
            globalVariables.put("NF", new InterpreterDataType(String.valueOf(fields.length)));
            NR++;
            FNR++;
            globalVariables.put("NR", new InterpreterDataType(String.valueOf(NR)));
            globalVariables.put("FNR", new InterpreterDataType(String.valueOf(FNR)));

            lines.removeFirst();

            if (lines.isEmpty()) {
                return false; //no more lines to split
            }

            return true;
        }

        public List<String> getLines() {
            return lines;
        }
    }

}

