import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {
    @Test
    public void parseFunctionCallTest2() throws Exception {
        String input = "getline";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Function Name: getline", p.toString());
    }

    @Test
    public void parseFunctionCallTest3() throws Exception {
        String input = "print";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Function Name: print", p.toString());
    }

    @Test
    public void parseFunctionCallTest4() throws Exception {
        String input = "printf";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Function Name: printf", p.toString());
    }

    @Test
    public void parseFunctionCallTest5() throws Exception {
        String input = "exit";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Function Name: exit", p.toString());
    }

    @Test
    public void parseFunctionCallTest6() throws Exception {
        String input = "nextfile";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Function Name: nextfile", p.toString());
    }

    @Test
    public void parseFunctionCallTest7() throws Exception {
        String input = "next";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Function Name: next", p.toString());
    }

    @Test
    public void splitAndAssignTest(){
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        boolean result = manager.splitAndAssign();
        assertTrue(result);
        assertEquals("This is the first line. ", interpreter.getGlobalVariables().get("$0").getValue());
        assertEquals("This", interpreter.getGlobalVariables().get("$1").getValue());
        assertEquals("is", interpreter.getGlobalVariables().get("$2").getValue());
        assertEquals("the", interpreter.getGlobalVariables().get("$3").getValue());
        assertEquals("first", interpreter.getGlobalVariables().get("$4").getValue());
        assertEquals("line.", interpreter.getGlobalVariables().get("$5").getValue());
        assertEquals("5", interpreter.getGlobalVariables().get("NF").getValue());
        boolean result2 = manager.splitAndAssign();
        assertTrue(result2);
        assertEquals("Here's the second line. ", interpreter.getGlobalVariables().get("$0").getValue());
        assertEquals("Here's", interpreter.getGlobalVariables().get("$1").getValue());
        assertEquals("the", interpreter.getGlobalVariables().get("$2").getValue());
        assertEquals("second", interpreter.getGlobalVariables().get("$3").getValue());
        assertEquals("line.", interpreter.getGlobalVariables().get("$4").getValue());
        assertEquals("4", interpreter.getGlobalVariables().get("NF").getValue());
        boolean result3 = manager.splitAndAssign();
        assertFalse(result3);
        assertEquals("And this is the third line. ", interpreter.getGlobalVariables().get("$0").getValue());
        assertEquals("And", interpreter.getGlobalVariables().get("$1").getValue());
        assertEquals("this", interpreter.getGlobalVariables().get("$2").getValue());
        assertEquals("is", interpreter.getGlobalVariables().get("$3").getValue());
        assertEquals("the", interpreter.getGlobalVariables().get("$4").getValue());
        assertEquals("third", interpreter.getGlobalVariables().get("$5").getValue());
        assertEquals("line.", interpreter.getGlobalVariables().get("$6").getValue());
        assertEquals("6", interpreter.getGlobalVariables().get("NF").getValue());
    }

    @Test
    public void printTest() {
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        String input = "This is a sample text with some sample content.";

        executeMap.put("print", new InterpreterDataType(input));

        BuiltInFunctionDefinitionNode printFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("print");
        String result = printFunction.execute(executeMap);
        //assertEquals("yuh aye yuh what we doin raaaaaaaaaaaaaah", result);
        //check the console to see if it printed
    }

    @Test
    public void printfTest() {
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        String input = "This is a sample text with some sample content.";
        String format = "%s";

        executeMap.put("format", new InterpreterDataType(format));
        executeMap.put("printf", new InterpreterDataType(input));

        BuiltInFunctionDefinitionNode printfFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("printf");
        String result = printfFunction.execute(executeMap);

        //check the console to see if it printed
    }

    @Test
    public void getlineTest() {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        BuiltInFunctionDefinitionNode getlineFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("getline");
        String result = getlineFunction.execute(executeMap);
        assertEquals("1", result);

        BuiltInFunctionDefinitionNode subFunction2 = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("getline");
        String result2 = getlineFunction.execute(executeMap);
        assertEquals("1", result2);

        BuiltInFunctionDefinitionNode subFunction3 = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("getline");
        String result3 = getlineFunction.execute(executeMap);
        assertEquals("0", result3);
        //check the console to see if it printed
    }

    @Test
    public void nextTest() {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("next");
        String result = subFunction.execute(executeMap);
        assertEquals("1", result);

        BuiltInFunctionDefinitionNode subFunction2 = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("next");
        String result2 = subFunction.execute(executeMap);
        assertEquals("1", result2);

        BuiltInFunctionDefinitionNode subFunction3 = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("next");
        String result3 = subFunction.execute(executeMap);
        assertEquals("0", result3);
        //check the console to see if it printed
    }

    @Test
    public void gsubTest(){
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        String test1 = "This is a sample text with some sample content.";
        String test2 = "sample";
        String test3 = "poopy";

        executeMap.put("target", new InterpreterDataType(test1));
        executeMap.put("regex", new InterpreterDataType(test2));
        executeMap.put("replacement", new InterpreterDataType(test3));

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("gsub");
        String result = subFunction.execute(executeMap);

        assertEquals("This is a poopy text with some poopy content.", result);
    }

    @Test
    public void indexTest(){
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        //              12345678901234567
        String test1 = "This is a sample text with some sample content.";
        String test2 = "text";

        executeMap.put("target", new InterpreterDataType(test1));
        executeMap.put("substring", new InterpreterDataType(test2));

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("index");
        String result = subFunction.execute(executeMap);

        assertEquals("17", result);
    }

    @Test
    public void lengthTest(){
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        //https://wordcounter.net/character-count
        //Characters 47 Words 9 Lines 1
        String test1 = "This is a sample text with some sample content.";

        executeMap.put("target", new InterpreterDataType(test1));

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("length");
        String result = subFunction.execute(executeMap);

        assertEquals("47", result);
    }

    @Test
    public void matchTest(){
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        String test1 = "This is a sample text with some sample content.";
        String test2 = "This is a sample text with some sample content.";

        executeMap.put("target", new InterpreterDataType(test1));
        executeMap.put("regex", new InterpreterDataType(test2));

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("match");
        String result = subFunction.execute(executeMap);

        assertEquals("1", result);
    }

    @Test
    public void matchTest2(){
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        String test1 = "This is a sample text with some sample content.";
        String test2 = "trolling";

        executeMap.put("target", new InterpreterDataType(test1));
        executeMap.put("regex", new InterpreterDataType(test2));

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("match");
        String result = subFunction.execute(executeMap);

        assertEquals("0", result);
    }

    @Test
    public void splitTest(){
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        String test1 = "This is a sample text with some sample content.";
        String test2 = " ";

        executeMap.put("target", new InterpreterDataType(test1));
        executeMap.put("delimiter", new InterpreterDataType(test2));

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("split");
        String result = subFunction.execute(executeMap);

        assertEquals("{0=This, 1=is, 2=a, 3=sample, 4=text, 5=with, 6=some, 7=sample, 8=content.}", result);
    }

    @Test
    public void sprintfTest(){
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        String input = "This is a sample text with some sample content.";
        String format = "%s";

        executeMap.put("format", new InterpreterDataType(format));
        executeMap.put("sprintf", new InterpreterDataType(input));

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("sprintf");
        String result = subFunction.execute(executeMap);
        assertEquals("This is a sample text with some sample content.", result);
    }

    @Test
    public void subTest(){
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        String test1 = "A GREAT GREAT CHAOS";
        String test2 = "GREAT";
        String test3 = "CHAOS";

        executeMap.put("target", new InterpreterDataType(test1));
        executeMap.put("regex", new InterpreterDataType(test2));
        executeMap.put("replacement", new InterpreterDataType(test3));

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("sub");
        String result = subFunction.execute(executeMap);

        assertEquals("A CHAOS GREAT CHAOS", result);
    }

    @Test
    public void substrTest(){
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        String test1 = "This is a sample text with some sample content.";
        String test2 = "3";

        executeMap.put("target", new InterpreterDataType(test1));
        executeMap.put("start", new InterpreterDataType(test2));

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("substr");
        String result = subFunction.execute(executeMap);

        assertEquals("s is a sample text with some sample content.", result);
    }

    @Test
    public void tolowerTest(){
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        String test1 = "This is a sample text with some sample content.";

        executeMap.put("target", new InterpreterDataType(test1));

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("tolower");
        String result = subFunction.execute(executeMap);

        assertEquals("this is a sample text with some sample content.", result);
    }

    @Test
    public void toupperTest(){
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        String test1 = "This is a sample text with some sample content.";

        executeMap.put("target", new InterpreterDataType(test1));

        BuiltInFunctionDefinitionNode subFunction = (BuiltInFunctionDefinitionNode) interpreter.getFunction().get("toupper");
        String result = subFunction.execute(executeMap);

        assertEquals("THIS IS A SAMPLE TEXT WITH SOME SAMPLE CONTENT.", result);
    }

    //ASSIGNMENT NODES
    @Test
    public void assignmentvarTest() throws Exception {
        AssignmentNode node = new AssignmentNode(new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa")), new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "123"))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("123", getter.toString());
    }

    @Test
    public void assignmentdollarTest() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        assertEquals("This", interpreter.getGlobalVariables().get("$1").getValue());

        //$1 = 123;
        AssignmentNode node = new AssignmentNode(new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.DOLLAR), new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "123"))));

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("123", getter.toString());
    }

    @Test
    public void assignmentdollarTest2() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        //$1 = 123;
        AssignmentNode node = new AssignmentNode(new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.DOLLAR), new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "123"))));
        //$1 = 932
        executeMap.put("$1", new InterpreterDataType("932"));


        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("932", getter.toString());
    }

    //CONSTANT NODE
    @Test
    public void constantTest() throws Exception {
        ConstantNode node = new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("2", getter.toString());
    }

    //FUNCTION CALL NODE
    @Test
    public void functioncallTest() throws Exception {
        FunctionCallNode node = new FunctionCallNode();
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("", getter.toString());
    }

    //PATTERN NODE
    @Test
    public void patternTest() {
        PatternNode node = new PatternNode(Optional.of(new Token(Token.TokenType.PATTERN, "'hi guys'")));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.getIDT(node, executeMap);
        });
    }

    //TERNARY NODE
    @Test
    public void ternaryTest() throws Exception {
        Node node1 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.LT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3")))));
        Node node2 = new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "10")));
        Node node3 = new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "237")));
        //(2<3) ? 10 : 237
        TernaryNode node = new TernaryNode(node1, node2, node3);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("10", getter.toString());
    }

    @Test
    public void ternaryTest2() throws Exception {
        Node node1 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.GT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3")))));
        Node node2 = new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "10")));
        Node node3 = new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "237")));
        //(2>3) ? 10 : 237
        TernaryNode node = new TernaryNode(node1, node2, node3);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("237", getter.toString());
    }

    @Test
    public void varTest() throws Exception {
        VariableReferenceNode node = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        executeMap.put("waaaaa", new InterpreterDataType("we love strings"));
        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("we love strings", getter.toString());
    }

    @Test
    public void vararrayTest() throws Exception {
        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("5", getter.toString());
    }

    @Test
    public void vararrayTest2() {
        VariableReferenceNode node = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        assertThrows(Exception.class, () -> {
            interpreter.getIDT(node, executeMap);
        });
    }

    @Test
    public void addTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.ADD, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("4.0", getter.toString());
    }

    @Test
    public void subtractTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.SUBTRACT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0.0", getter.toString());
    }

    @Test
    public void multiplyTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.MULTIPLY, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("4.0", getter.toString());
    }

    @Test
    public void divideTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.DIVIDE, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1.0", getter.toString());
    }

    @Test
    public void equalTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.EQ, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void equalTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.EQ, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void notequalTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.NE, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void notequalTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.NE, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void lessthanTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.LT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void lessthanTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.LT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void greaterthanTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.GT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void greaterthanTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.GT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void andTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.AND, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "25.3")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void andTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "25.3"))), OperationNode.operations.AND, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void andTest3() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.AND, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "PLUH")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void orTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.OR, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "25.3")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void orTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.OR, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void orTest3() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.OR, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void orTest4() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0"))), OperationNode.operations.OR, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "true")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void notTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.NOT);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void notTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0"))), OperationNode.operations.NOT);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void notTest3() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "kablooey"))), OperationNode.operations.NOT);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    //yo this does not seem right at all
    @Test
    public void match2Test() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.STRINGLITERAL, "`yippee`"))), OperationNode.operations.MATCH, Optional.of(new PatternNode(Optional.of(new Token(Token.TokenType.PATTERN, "`yippee`")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    //yo this does not seem right at all
    @Test
    public void match3Test() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.STRINGLITERAL, "yippee"))), OperationNode.operations.MATCH, Optional.of(new PatternNode(Optional.of(new Token(Token.TokenType.PATTERN, "`yippee`")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void dollarTest() throws Exception {

        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("This is the first line. ", getter.toString());

        OperationNode node2 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap2 = new HashMap<>();

        InterpreterDataType getter2 = interpreter.getIDT(node2, executeMap2);
        assertEquals("This", getter2.toString());

        OperationNode node3 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap3 = new HashMap<>();

        InterpreterDataType getter3 = interpreter.getIDT(node3, executeMap3);
        assertEquals("is", getter3.toString());

        OperationNode node4 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap4 = new HashMap<>();

        InterpreterDataType getter4 = interpreter.getIDT(node4, executeMap4);
        assertEquals("the", getter4.toString());

        OperationNode node5 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "4"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap5 = new HashMap<>();

        InterpreterDataType getter5 = interpreter.getIDT(node5, executeMap5);
        assertEquals("first", getter5.toString());

        OperationNode node6 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "5"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap6 = new HashMap<>();

        InterpreterDataType getter6 = interpreter.getIDT(node6, executeMap6);
        assertEquals("line.", getter6.toString());
    }

    @Test
    public void preincTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.PREINC);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("2.0", getter.toString());
    }

    @Test
    public void postincTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.POSTINC);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("2.0", getter.toString());
    }

    @Test
    public void predecTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.PREDEC);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0.0", getter.toString());
    }

    @Test
    public void postdecTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.POSTDEC);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0.0", getter.toString());
    }

    @Test
    public void unaryposTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.UNARYPOS);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1.0", getter.toString());
    }

    @Test
    public void unarynegTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.UNARYNEG);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("-1.0", getter.toString());
    }

    @Test
    public void concatenationTest() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.STRINGLITERAL, "yippee pow pow pow "))), OperationNode.operations.CONCATENATION, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.STRINGLITERAL, "yippee")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("yippee pow pow pow yippee", getter.toString());
    }

    @Test
    public void inTest() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node1 = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        OperationNode node2 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0"))), OperationNode.operations.IN, Optional.of(node1));
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("1", getter.toString());
    }

    @Test
    public void inTest2() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node1 = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        OperationNode node2 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.IN, Optional.of(node1));
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("1", getter.toString());
    }

    @Test
    public void inTest3() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node1 = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        OperationNode node2 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.IN, Optional.of(node1));
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("1", getter.toString());
    }

    @Test
    public void inTest4() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node1 = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        OperationNode node2 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "4"))), OperationNode.operations.IN, Optional.of(node1));
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("0", getter.toString());
    }



    @Test
    public void mathopTest() throws Exception {
        String input = "1+2-3*4/6";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p2 = parser.parseOperation();
        OperationNode node2 = (OperationNode) p2.get();
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        //((((4/6) * -3) +2 ) + 1) uhhhh me when multiplication and division are on the same tier :/
        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("1.0", getter.toString());
    }

    @Test
    public void mathopTest2() throws Exception {
        String input = "1+2-3*4/5%6^7^(8)<9";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p2 = parser.parseOperation();
        OperationNode node2 = (OperationNode) p2.get();
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        //1+2-3*4/5%6^7^(8) = .6, .6<9
        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("1", getter.toString());
    }

    @Test
    public void assignmentStatementTest() throws Exception {
        AssignmentNode node = new AssignmentNode(new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa")), new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "123"))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, node);
        assertEquals("None", getter.toString());
    }

    @Test
    public void breakStatementTest() throws Exception {
        BreakNode node = new BreakNode();
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, node);
        assertEquals("Break", getter.toString());
    }

    @Test
    public void continueStatementTest() throws Exception {
        ContinueNode node = new ContinueNode();
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, node);
        assertEquals("Continue", getter.toString());
    }


    //delete might highkey not work
    @Test
    public void deleteStatementTest() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */

        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node1 = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        DeleteNode node2 = new DeleteNode(Optional.of(node1));
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, node2);
        assertEquals("None", getter.toString());
    }


    @Test
    public void deleteStatementTest2() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */

        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node1 = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();

        DeleteNode node2 = new DeleteNode(Optional.of(node1));
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, node2);
        assertEquals("None", getter.toString());
    }

    @Test
    public void doWhileStatementTest() throws Exception {
        String input = "do { i = 3; i = 4; i = 3; } while (i!=3);";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        DoWhileNode p = (DoWhileNode) parser.parseStatement().get();

        ProgramNode p2 = new ProgramNode();
        Interpreter interpreter = new Interpreter(p2, null);

        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, p);
        assertEquals("None", getter.toString());
    }

    @Test
    public void forStatementTest() throws Exception {
        String input = "for (i = 0; i < 2; i=3) { i = 3; i = 4; i = 3; } ;";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        ForNode p = (ForNode) parser.parseStatement().get();

        ProgramNode p2 = new ProgramNode();
        Interpreter interpreter = new Interpreter(p2, null);

        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, p);
        assertEquals("None", getter.toString());
    }

    @Test
    public void forEachStatementTest() throws Exception {
        String input = "for (i in arr) { i = 3; i = 4; break; i = 3; } ;";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        ForEachNode p = (ForEachNode) parser.parseStatement().get();

        ProgramNode p2 = new ProgramNode();
        Interpreter interpreter = new Interpreter(p2, null);

        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, p);
        assertEquals("None", getter.toString());
    }

    @Test
    public void functionCallStatementTest() throws Exception {
        String input = "for (i = 0; i < 2; i=3) { i = 3; i = 4; break; i = 3; } ;";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        ForNode p = (ForNode) parser.parseStatement().get();

        ProgramNode p2 = new ProgramNode();
        Interpreter interpreter = new Interpreter(p2, null);

        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, p);
        assertEquals("None", getter.toString());
    }

    @Test
    public void ifTest() throws Exception {
        String input = "if (1<2) { i = 3; i = 4; break; i = 3; } ;";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        IfNode p = (IfNode) parser.parseStatement().get();

        ProgramNode p2 = new ProgramNode();
        Interpreter interpreter = new Interpreter(p2, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, p);
        assertEquals("None", getter.toString());
    }

    @Test
    public void ifTest2() throws Exception {
        String input = "if (1<2) { i = 3; i = 4; break; i = 3; } else { i = 3; i = 4; break; i = 3;  };";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        IfNode p = (IfNode) parser.parseStatement().get();

        ProgramNode p2 = new ProgramNode();
        Interpreter interpreter = new Interpreter(p2, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, p);
        assertEquals("None", getter.toString());
    }

    @Test
    public void returnTest() throws Exception {
        String input = "return 2+2";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        ReturnNode p = (ReturnNode) parser.parseStatement().get();

        ProgramNode p2 = new ProgramNode();
        Interpreter interpreter = new Interpreter(p2, null);

        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, p);
        assertEquals("Return 4.0", getter.toString());
    }

    @Test
    public void whileTest() throws Exception {
        String input = "while (2<1) { i = 3; i = 4; break; i = 3; } else { i = 3; i = 4; break; i = 3; } ;";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        WhileNode p = (WhileNode) parser.parseStatement().get();

        ProgramNode p2 = new ProgramNode();
        Interpreter interpreter = new Interpreter(p2, null);

        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, p);
        assertEquals("None", getter.toString());
    }

    //MATH AND LOGIC TESTING
    //ASSIGNMENT NODES
    @Test
    public void assignmentvarTest2() throws Exception {
        AssignmentNode node = new AssignmentNode(new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa")), new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "123"))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("123", getter.toString());
    }

    @Test
    public void assignmentdollarTest3() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        assertEquals("This", interpreter.getGlobalVariables().get("$1").getValue());

        //$1 = 123;
        AssignmentNode node = new AssignmentNode(new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.DOLLAR), new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "123"))));

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("123", getter.toString());
    }

    @Test
    public void assignmentdollarTest4() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        //$1 = 123;
        AssignmentNode node = new AssignmentNode(new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.DOLLAR), new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "123"))));
        //$1 = 932
        executeMap.put("$1", new InterpreterDataType("932"));


        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("932", getter.toString());
    }

    //CONSTANT NODE
    @Test
    public void constantTest2() throws Exception {
        ConstantNode node = new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("2", getter.toString());
    }

    //FUNCTION CALL NODE
    @Test
    public void functioncallTest2() throws Exception {
        FunctionCallNode node = new FunctionCallNode();
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("", getter.toString());
    }

    //PATTERN NODE
    @Test
    public void patternTest2() {
        PatternNode node = new PatternNode(Optional.of(new Token(Token.TokenType.PATTERN, "'hi guys'")));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.getIDT(node, executeMap);
        });
    }

    //TERNARY NODE
    @Test
    public void ternaryTest3() throws Exception {
        Node node1 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.LT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3")))));
        Node node2 = new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "10")));
        Node node3 = new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "237")));
        //(2<3) ? 10 : 237
        TernaryNode node = new TernaryNode(node1, node2, node3);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("10", getter.toString());
    }

    @Test
    public void ternaryTest4() throws Exception {
        Node node1 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.GT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3")))));
        Node node2 = new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "10")));
        Node node3 = new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "237")));
        //(2>3) ? 10 : 237
        TernaryNode node = new TernaryNode(node1, node2, node3);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("237", getter.toString());
    }

    @Test
    public void varTest2() throws Exception {
        VariableReferenceNode node = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        executeMap.put("waaaaa", new InterpreterDataType("we love strings"));
        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("we love strings", getter.toString());
    }

    @Test
    public void vararrayTest3() throws Exception {
        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("5", getter.toString());
    }

    @Test
    public void vararrayTest4() {
        VariableReferenceNode node = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();
        assertThrows(Exception.class, () -> {
            interpreter.getIDT(node, executeMap);
        });
    }

    @Test
    public void addTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.ADD, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("4.0", getter.toString());
    }

    @Test
    public void subtractTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.SUBTRACT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0.0", getter.toString());
    }

    @Test
    public void multiplyTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.MULTIPLY, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("4.0", getter.toString());
    }

    @Test
    public void divideTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.DIVIDE, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1.0", getter.toString());
    }

    @Test
    public void equalTest3() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.EQ, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void equalTest4() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.EQ, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void notequalTest3() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.NE, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void notequalTest4() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.NE, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void lessthanTest3() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.LT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void lessthanTest4() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.LT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void greaterthanTest3() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.GT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void greaterthanTest4() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.GT, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void andTest4() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.AND, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "25.3")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void andTest5() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "25.3"))), OperationNode.operations.AND, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void andTest6() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.AND, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "PLUH")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void orTest5() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.OR, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "25.3")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void orTest6() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.OR, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void orTest7() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.OR, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void orTest8() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0"))), OperationNode.operations.OR, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "true")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void notTest4() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.NOT);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void notTest5() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0"))), OperationNode.operations.NOT);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void notTest6() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "kablooey"))), OperationNode.operations.NOT);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void match4Test() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.STRINGLITERAL, "`yippee`"))), OperationNode.operations.MATCH, Optional.of(new PatternNode(Optional.of(new Token(Token.TokenType.PATTERN, "`yippee`")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1", getter.toString());//1 for true
    }

    @Test
    public void match5Test() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.STRINGLITERAL, "yippee"))), OperationNode.operations.MATCH, Optional.of(new PatternNode(Optional.of(new Token(Token.TokenType.PATTERN, "`yippee`")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0", getter.toString());//0 for false
    }

    @Test
    public void dollarTest2() throws Exception {

        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("This is the first line. ", getter.toString());

        OperationNode node2 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap2 = new HashMap<>();

        InterpreterDataType getter2 = interpreter.getIDT(node2, executeMap2);
        assertEquals("This", getter2.toString());

        OperationNode node3 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap3 = new HashMap<>();

        InterpreterDataType getter3 = interpreter.getIDT(node3, executeMap3);
        assertEquals("is", getter3.toString());

        OperationNode node4 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "3"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap4 = new HashMap<>();

        InterpreterDataType getter4 = interpreter.getIDT(node4, executeMap4);
        assertEquals("the", getter4.toString());

        OperationNode node5 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "4"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap5 = new HashMap<>();

        InterpreterDataType getter5 = interpreter.getIDT(node5, executeMap5);
        assertEquals("first", getter5.toString());

        OperationNode node6 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "5"))), OperationNode.operations.DOLLAR);
        HashMap<String, InterpreterDataType> executeMap6 = new HashMap<>();

        InterpreterDataType getter6 = interpreter.getIDT(node6, executeMap6);
        assertEquals("line.", getter6.toString());
    }

    @Test
    public void preincTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.PREINC);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("2.0", getter.toString());
    }

    @Test
    public void postincTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.POSTINC);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("2.0", getter.toString());
    }

    @Test
    public void predecTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.PREDEC);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0.0", getter.toString());
    }

    @Test
    public void postdecTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.POSTDEC);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("0.0", getter.toString());
    }

    @Test
    public void unaryposTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.UNARYPOS);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("1.0", getter.toString());
    }

    @Test
    public void unarynegTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.UNARYNEG);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("-1.0", getter.toString());
    }

    @Test
    public void concatenationTest2() throws Exception {
        OperationNode node = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.STRINGLITERAL, "yippee pow pow pow "))), OperationNode.operations.CONCATENATION, Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.STRINGLITERAL, "yippee")))));
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node, executeMap);
        assertEquals("yippee pow pow pow yippee", getter.toString());
    }

    @Test
    public void inTest5() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node1 = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        OperationNode node2 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "0"))), OperationNode.operations.IN, Optional.of(node1));
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("1", getter.toString());
    }

    @Test
    public void inTest6() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node1 = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        OperationNode node2 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "1"))), OperationNode.operations.IN, Optional.of(node1));
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("1", getter.toString());
    }

    @Test
    public void inTest7() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node1 = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        OperationNode node2 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2"))), OperationNode.operations.IN, Optional.of(node1));
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("1", getter.toString());
    }

    @Test
    public void inTest8() throws Exception {
        /*contents of testing.txt
        This is the first line.
        Here's the second line.
        And this is the third line.
        */
        HashMap<String, InterpreterDataType> arrayMap = new HashMap<>();
        arrayMap.put("0", new InterpreterDataType("3"));
        arrayMap.put("1", new InterpreterDataType("4"));
        arrayMap.put("2", new InterpreterDataType("5"));
        InterpreterArrayDataType array = new InterpreterArrayDataType(arrayMap);
        VariableReferenceNode node1 = new VariableReferenceNode(new Token(Token.TokenType.WORD,"waaaaa"), Optional.of(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "2")))), array);
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, "C:\\Users\\FatKi\\IdeaProjects\\ICSI 311 Project\\src\\testing.txt");
        Interpreter.LineManager manager = interpreter.getLineManager();
        manager.splitAndAssign();
        OperationNode node2 = new OperationNode(new ConstantNode(Optional.of(new Token(Token.TokenType.NUMBER, "4"))), OperationNode.operations.IN, Optional.of(node1));
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("0", getter.toString());
    }

    @Test
    public void mathopTest3() throws Exception {
        String input = "1+2-3*4/6";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p2 = parser.parseOperation();
        OperationNode node2 = (OperationNode) p2.get();
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        //((((4/6) * -3) +2 ) + 1) uhhhh me when multiplication and division are on the same tier :/
        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("1.0", getter.toString());
    }

    @Test
    public void mathopTest4() throws Exception {
        String input = "1+2-3*4/5%6^7^(8)<9";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p2 = parser.parseOperation();
        OperationNode node2 = (OperationNode) p2.get();
        ProgramNode p = new ProgramNode();
        Interpreter interpreter = new Interpreter(p, null);
        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        //1+2-3*4/5%6^7^(8) = .6, .6<9
        InterpreterDataType getter = interpreter.getIDT(node2, executeMap);
        assertEquals("1", getter.toString());
    }

    @Test
    public void forStatementTest2() throws Exception {
        String input = "for (i = 0; i < 2; i=3) { i = 3; i = 4; i = 3; } ;";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        ForNode p = (ForNode) parser.parseStatement().get();

        ProgramNode p2 = new ProgramNode();
        Interpreter interpreter = new Interpreter(p2, null);

        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, p);
        assertEquals("None", getter.toString());
    }

    @Test
    public void forEachStatementTest2() throws Exception {
        String input = "for (i in arr) { i = 3; i = 4; break; i = 3; } ;";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        ForEachNode p = (ForEachNode) parser.parseStatement().get();

        ProgramNode p2 = new ProgramNode();
        Interpreter interpreter = new Interpreter(p2, null);

        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, p);
        assertEquals("None", getter.toString());
    }

    @Test
    public void whileTest2() throws Exception {
        String input = "while (2<1) { i = 3; i = 4; break; i = 3; } else { i = 3; i = 4; break; i = 3; } ;";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        WhileNode p = (WhileNode) parser.parseStatement().get();

        ProgramNode p2 = new ProgramNode();
        Interpreter interpreter = new Interpreter(p2, null);

        HashMap<String, InterpreterDataType> executeMap = new HashMap<>();

        ReturnType getter = interpreter.processStatement(executeMap, p);
        assertEquals("None", getter.toString());
    }

}
