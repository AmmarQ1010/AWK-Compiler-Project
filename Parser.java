import java.sql.Statement;
import java.util.LinkedList;
import java.util.Optional;

public class Parser {
    private LinkedList<Token> tokens;
    private tokenManager tokenManager;

    public Parser(LinkedList<Token> tokens) { //default constructor
        this.tokens = tokens;
        this.tokenManager = new tokenManager(tokens);
    }

    public boolean acceptSeparators() {
        int count = 0;
        if(!tokens.isEmpty()){
            while (tokens.get(0).getType() == Token.TokenType.SEPARATOR) { //matches and removes separators from token list
                tokenManager.MatchAndRemove(Token.TokenType.SEPARATOR);
                count++;
            }
        }

        return count >= 1; //return true if 1 or more, else false.
    }

    public ProgramNode Parse() throws Exception {
        ProgramNode p = new ProgramNode(); //initializes p
        p = new ProgramNode(p.getBegin(), p.getEnd(), p.getOther(), p.getFunction()); //fills p with 4 linked list of blocknode
        while (tokenManager.MoreTokens()) {
            if (ParseFunction(p)) { //call these functions for the whole token list@
                ParseFunction(p);
            } else if (parseAction(p)) {
                parseAction(p);
            } else { //if not, throw exception
                throw new Exception();
            }
        }
        return p;
    }

    public boolean ParseFunction(ProgramNode p) throws Exception {
        LinkedList<Token> params = new LinkedList<>();
        Token name;
        boolean isDone = false;
        if (tokenManager.MoreTokens()){
            if (!(tokenManager.MatchAndRemove(Token.TokenType.FUNCTION).equals(Optional.empty()))) { //if not function
                if (tokenManager.Peek(0).get().getType() == Token.TokenType.WORD) { //check word
                    name = tokenManager.MatchAndRemove(Token.TokenType.WORD).get();
                    if (!(tokenManager.MatchAndRemove(Token.TokenType.LEFTPARANTHESIS).equals(Optional.empty()))) { //if not left paran
                        while (tokenManager.MoreTokens()) { //remove all words in between paranthesis
                            if (tokenManager.Peek(0).get().getType() == Token.TokenType.WORD) {
                                params.add(tokenManager.MatchAndRemove(Token.TokenType.WORD).get());
                                if ((tokenManager.MatchAndRemove(Token.TokenType.COMMA).equals(Optional.empty()))) {
                                    if (tokenManager.Peek(0).get().getType() == Token.TokenType.RIGHTPARANTHESIS) { //check for right paran
                                        tokenManager.MatchAndRemove(Token.TokenType.RIGHTPARANTHESIS);
                                        isDone = true;
                                        break;
                                    } else if (acceptSeparators()) {
                                        throw new Exception("Invalid function syntax");
                                    }
                                }
                            } else {
                                if (tokenManager.Peek(0).get().getType() == Token.TokenType.RIGHTPARANTHESIS) { //check for right paran if there are no words
                                    tokenManager.MatchAndRemove(Token.TokenType.RIGHTPARANTHESIS);
                                    isDone = true;
                                    break;
                                } else if (acceptSeparators()) {
                                    throw new Exception("Invalid function syntax");
                                }
                            }
                        }
                    }
                } else {
                    throw new Exception("Invalid function syntax");
                }
            } else {
                return false;
            }
            if (isDone) {
                BlockNode functionBlock = parseBlock();
                p.addFunction(new FunctionDefinitionNode(name, params, functionBlock.getStatements()));//function has been parsed, add it to the function list
                return true;
            } else {
                throw new Exception("Invalid function syntax");
            }
        }
        return false;
    }

    public boolean parseAction(ProgramNode p) throws Exception {
        if (tokenManager.MoreTokens()){
            if (Optional.of(tokens.get(0)).equals(tokenManager.MatchAndRemove(Token.TokenType.BEGIN))) { //check for begin
                BlockNode beginNode = parseBlock(); //parse appropriately
                p.addBegin(beginNode);
                return true;
            } else if (Optional.of(tokens.get(0)).equals(tokenManager.MatchAndRemove(Token.TokenType.END))) { //check for end
                BlockNode endNode = parseBlock(); //parse appropriately
                p.addEnd(endNode);
                return true;
            } else if (Optional.of(tokens.get(0)).equals(tokenManager.MatchAndRemove(Token.TokenType.WORD))) { //check for other words
                parseOperation();
                BlockNode otherNode = parseBlock(); //parse appropriately
                p.addOther(otherNode);
                return true;
            }
        }
        return false;
    }

    public Optional<Node> parseOperation() throws Exception { //return optional empty
        return parseAssignment(); //calls top level in the table
    }

    public Optional<Node> parseLValue() throws Exception{
        Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.DOLLAR); //check for $
        if (op.isPresent()){
            Optional<Node> index = parseTerm(); //parse expression
            return Optional.of(new VariableReferenceNode(op.get(), index)); //return dollar and parsed information
        }
        if(tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.WORD //check for WORD
            && tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getType() == Token.TokenType.LEFTBRACKET) { //check for [ one ahead
            op = tokenManager.MatchAndRemove(Token.TokenType.WORD);
            Optional<Token> op4 = tokenManager.MatchAndRemove(Token.TokenType.LEFTBRACKET); //check for [
            if (op4.isPresent()) {
                Optional<Node> index = parseTerm(); //parse expression
                if (index.isEmpty()) {
                    Optional<Token> op5 = tokenManager.MatchAndRemove(Token.TokenType.RIGHTBRACKET) ;//check for ]
                    if (op5.isPresent()) {
                        return Optional.of(new VariableReferenceNode(op.get()));
                    }
                }
                Optional<Token> op5 = tokenManager.MatchAndRemove(Token.TokenType.RIGHTBRACKET); //check for ]
                if (op5.isPresent()) {
                    return Optional.of(new VariableReferenceNode(op.get(), index));
                } else {
                    throw new Exception();
                }
            }
        }

        op = tokenManager.MatchAndRemove(Token.TokenType.WORD); //check for WORD
        if (op.isPresent()) {
            Optional<Node> left = Optional.of(new VariableReferenceNode(op.get()));
            Optional<Token> op2 = tokenManager.MatchAndRemove(Token.TokenType.PLUSONE); //check for ++
            if (op2.isPresent()) {
                return Optional.of(new AssignmentNode(null, new OperationNode(left.get(), OperationNode.operations.POSTINC)));
            }
            Optional<Token> op3 = tokenManager.MatchAndRemove(Token.TokenType.MINUSONE); //check for --
            if (op3.isPresent()) {
                return Optional.of(new AssignmentNode(null, new OperationNode(left.get(), OperationNode.operations.POSTDEC)));
            }
            return left;
        }


        return Optional.empty();
    }

    public Optional<Node> parseFunctionCall() throws Exception {
        //if peek 0 = word, peek 1 = (
        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.WORD &&
                tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getType() == Token.TokenType.LEFTPARANTHESIS) {
            Optional<Node> name = parseLValue(); //gets function name
            tokenManager.MatchAndRemove(Token.TokenType.LEFTPARANTHESIS); //remove (
            LinkedList<Node> params = new LinkedList<>(); //list of params
            params.add(parseLValue().get()); //parses first param and adds to list
            do {
                //if there are multiple params, check for commas in between each param and add to list.
                Optional<Token> check = tokenManager.MatchAndRemove(Token.TokenType.COMMA);
                if(check.isPresent()){
                    Optional<Node> nextParam = parseLValue();
                    if (nextParam.isPresent()){
                        params.add(nextParam.get());
                    }
                }
            } while (tokenManager.Peek(0).get().getType()==Token.TokenType.COMMA);
            if (tokenManager.MatchAndRemove(Token.TokenType.RIGHTPARANTHESIS).isPresent()) { //checks for )

                return Optional.of(new FunctionCallNode(name, params));
            }
            else {
                throw new Exception("right paranthesis required.");
            }
        }
        else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.GETLINE){
            tokenManager.MatchAndRemove(Token.TokenType.GETLINE);

            return Optional.of(new FunctionCallNode("getline"));
        }
        else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.PRINT){

            tokenManager.MatchAndRemove(Token.TokenType.PRINT);

            return Optional.of(new FunctionCallNode("print"));
        }
        else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.PRINTF){
            tokenManager.MatchAndRemove(Token.TokenType.PRINTF);

            return Optional.of(new FunctionCallNode("printf"));
        }
        else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.EXIT){
            tokenManager.MatchAndRemove(Token.TokenType.EXIT);

            return Optional.of(new FunctionCallNode("exit"));
        }
        else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.NEXTFILE){
            tokenManager.MatchAndRemove(Token.TokenType.NEXTFILE);

            return Optional.of(new FunctionCallNode("nextfile"));
        }
        else if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getType() == Token.TokenType.NEXT){
            tokenManager.MatchAndRemove(Token.TokenType.NEXT);

            return Optional.of(new FunctionCallNode("next"));
        }

        return Optional.empty();
    }

    public Optional<Node> parseFactor() throws Exception {
        Optional<Node> call = parseFunctionCall(); //calls parsefunctioncall before parselvalue because it uses parselvalue and we would have an infinite loop
        if (call.isPresent()){
            return call;
        }
        Optional<Node> left = parseLValue(); //calls next level in table
        Optional<Token> num = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);//check for NUMBER
        if (num.isPresent()){
            if (left.isPresent()){
                left = Optional.of(new AssignmentNode(left.get(), new ConstantNode(num)));
            }
            else {
                    left = Optional.of(new ConstantNode(num));
            }
        }
        else {
                if (tokenManager.MatchAndRemove(Token.TokenType.LEFTPARANTHESIS).isPresent()){//check for (
                    Optional<Node> exp = parseAssignment(); //parse expression
                    if (exp.isEmpty()) {
                        throw new Exception();
                    }
                    if (tokenManager.MatchAndRemove(Token.TokenType.RIGHTPARANTHESIS).isEmpty()){//check for )
                        throw new Exception();
                    }
                    return exp;
                }
            }
            return left;
    }

    public Optional<Node> parseExponent() throws Exception {
        Optional<Node> left = parseFactor(); //calls next level in table
        Optional<Token> expo = tokenManager.MatchAndRemove(Token.TokenType.EXP);//check for ^
        if (expo.isPresent()) {
            Optional<Node> right = parseTerm(); //parse expression
            return Optional.of(new OperationNode(left.get(), OperationNode.operations.EXPONENT, right));
        }
        return left;
    }

    public Optional<Node> parseBottomLevel() throws Exception {
        Optional<Node> left = parseExponent(); //calls next level in table
        do {
            Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.STRINGLITERAL);//check for \\" \\"
            if (op.isPresent()) {
                left = Optional.of(new ConstantNode(op));
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);//check for NUMBER
                if (op.isPresent()){
                    left = Optional.of(new ConstantNode(op));
                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.PATTERN);//check for ``
                if (op.isPresent()){
                    left = Optional.of(new PatternNode(op));
                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.LEFTPARANTHESIS);//check for (
                if (op.isPresent()){
                    Optional<Node> var = parseTerm(); //parse expression
                    if(var.isEmpty()){
                        throw new Exception();
                    }
                    op = tokenManager.MatchAndRemove(Token.TokenType.RIGHTPARANTHESIS);//check for )
                    if (op.isPresent()){
                        left = Optional.of(new VariableReferenceNode(var.get()));
                    }
                    else {
                        throw new IllegalArgumentException("Must have a right paranthesis.");
                    }
                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.NOT);//check for !
                if (op.isPresent()){
                    left = parseTerm(); //calls parse expression
                    left = Optional.of(new OperationNode(left.get(), OperationNode.operations.NOT));
                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.PLUS);//check for +
                if (op.isPresent()){
                    op = tokenManager.MatchAndRemove(Token.TokenType.WORD);//check for WORD
                    if (op.isPresent()) {
                        left = Optional.of(new OperationNode(new VariableReferenceNode(op.get()), OperationNode.operations.UNARYPOS));
                    }
                    else {
                        Optional<Node> right = parseTerm(); //parse expression
                        left = Optional.of(new OperationNode (left.get(), OperationNode.operations.ADD, right));
                    }

                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.MINUS);//check for -
                if (op.isPresent()){
                    op = tokenManager.MatchAndRemove(Token.TokenType.WORD);//check for WORD
                    if (op.isPresent()) {
                        left = Optional.of(new OperationNode(new VariableReferenceNode(op.get()), OperationNode.operations.UNARYNEG));
                    }
                    else{
                        Optional<Node> right = parseTerm(); //parse expression
                        left = Optional.of(new OperationNode (left.get(), OperationNode.operations.SUBTRACT, right));
                    }
                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.PLUSONE);//check for ++
                if (op.isPresent()){
                    left = parseTerm(); //parse expression
                    left = Optional.of(new AssignmentNode(null, new OperationNode(left.get(), OperationNode.operations.PREINC)));
                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.MINUSONE);//check for --
                if (op.isPresent()){
                    left = parseTerm(); //parse expression
                    left = Optional.of(new AssignmentNode(null, new OperationNode(left.get(), OperationNode.operations.PREDEC)));
                }
            }
            if (op.isEmpty()) {
                return left;
            }
        } while(true);

    }

    public Optional<Node> parseTerm() throws Exception{
        Optional<Node> left = parseBottomLevel(); //calls next level in table
        do {
            Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.TIMES);//check for *
            if (op.isPresent()){
                Optional<Node> right = parseBottomLevel(); //parse expression
                left = Optional.of(new OperationNode(left.get(), OperationNode.operations.MULTIPLY, right));
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.DIV);//check for /
                if (op.isPresent()) {
                    Optional<Node> right = parseBottomLevel(); //parse expression
                    left = Optional.of(new OperationNode(left.get(), OperationNode.operations.DIVIDE, right));
                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.MOD);//check for %
                if (op.isPresent()) {
                    Optional<Node> right = parseBottomLevel(); //parse expression
                    left = Optional.of(new OperationNode(left.get(), OperationNode.operations.MODULO, right));
                }
            }
            if (op.isEmpty()) {
                return left;
            }
        } while(true);
    }
//I implemented parseExpression() inside of parseBottomLevel() because I couldn't
//figure out a way to handle plus and minus without it looking at UNARY first
    public Optional<Node> parseConcatenation() throws Exception {
        Optional<Node> left = parseTerm();  //calls next level in table
        if (left.isPresent()) {//check for valid expression
            Optional<Node> right = parseTerm();
            if (right.isPresent()) {//check for valid expression
                return Optional.of(new OperationNode(left.get(), OperationNode.operations.CONCATENATION, right));
            }
        }
        return left;
    }

    public Optional<Node> parseInequality() throws Exception {
        Optional<Node> left = parseConcatenation(); //calls next level in table
        do {
            Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.LESSTHAN);//check for <
            if (op.isPresent()) {
                Optional<Node> right = parseConcatenation(); //parse expression
                left = Optional.of(new OperationNode(left.get(), OperationNode.operations.LT, right));
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.LESSTHANOREQUALTO);//check for <=
                if (op.isPresent()) {
                    Optional<Node> right = parseConcatenation(); //parse expression
                    left = Optional.of(new OperationNode(left.get(), OperationNode.operations.LE, right));
                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.NOTEQUAL);//check for !=
                if (op.isPresent()) {
                    Optional<Node> right = parseConcatenation(); //parse expression
                    left = Optional.of(new OperationNode(left.get(), OperationNode.operations.NE, right));
                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.EQUALEQUAL);//check for ==
                if (op.isPresent()) {
                    Optional<Node> right = parseConcatenation(); //parse expression
                    left = Optional.of(new OperationNode(left.get(), OperationNode.operations.EQ, right));
                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.GREATERTHAN);//check for >
                if (op.isPresent()) {
                    Optional<Node> right = parseConcatenation(); //parse expression
                    left = Optional.of(new OperationNode(left.get(), OperationNode.operations.GT, right));
                }
            }
            if (op.isEmpty()) {
                op = tokenManager.MatchAndRemove(Token.TokenType.GREATERTHANOREQUALTO);//check for >=
                if (op.isPresent()) {
                    Optional<Node> right = parseConcatenation(); //parse expression
                    left = Optional.of(new OperationNode(left.get(), OperationNode.operations.GE, right));
                }

            }
            if (op.isEmpty()) {
                return left;
            }
        }
        while (true);
    }

        public Optional<Node> parseMatch() throws Exception {
            Optional<Node> left = parseInequality(); //calls next level in table
            do {
                Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.MATCH);//check for ~
                if (op.isPresent()) {
                    Optional<Node> right = parseInequality(); //parse expression
                    left = Optional.of(new OperationNode(left.get(), OperationNode.operations.MATCH, right));
                }
                if (op.isEmpty()) {
                    op = tokenManager.MatchAndRemove(Token.TokenType.NOTMATCH);//check for !~
                    if (op.isPresent()){
                        Optional<Node> right = parseInequality(); //parse expression
                        left = Optional.of(new OperationNode(left.get(), OperationNode.operations.NOTMATCH, right));
                    }
                }
                if (op.isEmpty()) {
                    return left;
                }
            } while(true);
    }

    public Optional<Node> parseArray() throws Exception {
        Optional<Node> left = parseMatch(); //calls next level in table
        do {
            Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.IN);//check for in
            if (op.isPresent()) {
                if (tokenManager.MoreTokens()){
                    if(tokenManager.Peek(0).get().getType() == Token.TokenType.WORD) {//check for WORD
                        Optional<Node> right = parseMatch(); //parse expression
                        if (right.isPresent()){
                            left = Optional.of(new OperationNode(left.get(), OperationNode.operations.IN, right));
                        }
                    }
                    else {
                        throw new Exception("IN must be followed by word[]");
                    }
                }
            }
            if (op.isEmpty()) {
                return left;
            }
        } while(true);
    }

    public Optional<Node> parseAnd() throws Exception {
        Optional<Node> left = parseArray(); //calls next level in table
        do {
            Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.AND);//check for &&
            if (op.isPresent()) {
                Optional<Node> right = parseArray(); //parse expression
                left = Optional.of(new OperationNode(left.get(), OperationNode.operations.AND, right));
            }
            if (op.isEmpty()) {
                return left;
            }
        } while(true);
    }

    public Optional<Node> parseOr() throws Exception{
        Optional<Node> left = parseAnd(); //calls next level in table
        do {
            Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.OR);//check for ||
            if (op.isPresent()) {
                Optional<Node> right = parseAnd(); //parse expression
                left = Optional.of(new OperationNode(left.get(), OperationNode.operations.OR, right));
            }
            if (op.isEmpty()) {
                return left;
            }
        } while(true);
    }

    public Optional<Node> parseTernary() throws Exception{
        Optional<Node> left = parseOr(); //calls next level in table
        do {
            Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.TERNARY);//check for and remove ?
            if (op.isPresent()) {
                Optional<Node> middle = parseOr(); //parse expression
                if (middle.isPresent()) {
                    Optional<Token> colon = tokenManager.MatchAndRemove(Token.TokenType.COLON);//check for and remove :
                    if (colon.isPresent()){
                        Optional<Node> right = parseTerm(); //parse expression
                        if (right.isPresent()) {
                            left = Optional.of(new TernaryNode(left.get(), middle.get(), right.get()));
                        }
                    }
                }
            }
            if (op.isEmpty()) {
                return left;
            }
        } while(true);
    }

    public Optional<Node> parseAssignment() throws Exception{

        Optional<Node> left = parseTernary(); //calls next level in table
        do {
            Optional<Token> op = tokenManager.MatchAndRemove(Token.TokenType.EXPEQUAL);//check for ^=
            if (op.isPresent()) {
                Optional<Node> right = parseTernary();//parse some expression
                left = Optional.of(new AssignmentNode(left.get(),//left = left = left ^ right
                        new OperationNode(left.get(), OperationNode.operations.EXPONENT, right)));
            }
            op = tokenManager.MatchAndRemove(Token.TokenType.MODEQUAL);//check for %=
            if (op.isPresent()) {
                Optional<Node> right = parseTernary();//parse some expression
                left = Optional.of(new AssignmentNode(left.get(),//left = left = left%right
                        new OperationNode(left.get(), OperationNode.operations.MODULO, right)));
            }
            op = tokenManager.MatchAndRemove(Token.TokenType.MULTEQUAL);//check for *=
            if (op.isPresent()) {
                Optional<Node> right = parseTernary();//parse some expression
                left = Optional.of(new AssignmentNode(left.get(),//left = left = left * right
                        new OperationNode(left.get(), OperationNode.operations.MULTIPLY, right)));
            }
            op = tokenManager.MatchAndRemove(Token.TokenType.DIVEQUAL);//check for /=
            if (op.isPresent()) {
                Optional<Node> right = parseTernary();//parse some expression
                left = Optional.of(new AssignmentNode(left.get(),//left = left = left / right
                        new OperationNode(left.get(), OperationNode.operations.DIVIDE, right)));
            }
            op = tokenManager.MatchAndRemove(Token.TokenType.PLUSEQUAL);//check for +=
            if (op.isPresent()) {
                Optional<Node> right = parseTernary();//parse some expression
                left = Optional.of(new AssignmentNode(left.get(),//left = left = left + right
                        new OperationNode(left.get(), OperationNode.operations.ADD, right)));
            }
            op = tokenManager.MatchAndRemove(Token.TokenType.MINUSEQUAL);//check for -=
            if (op.isPresent()) {
                Optional<Node> right = parseTernary();//parse some expression
                left = Optional.of(new AssignmentNode(left.get(),//left = left = left - right
                        new OperationNode(left.get(), OperationNode.operations.SUBTRACT, right)));
            }
            op = tokenManager.MatchAndRemove(Token.TokenType.EQUAL);//check for =
            if (op.isPresent()) {
                Optional<Node> right = parseTernary();//parse some expression
                left = Optional.of(new AssignmentNode(left.get(), right.get()));//left = left = right
            }
            if (op.isEmpty()) {
                return left;
            }
        } while(true);
    }

    public BlockNode parseBlock() throws Exception { //return block node
        //checks for {. if found then parse all statements until you find }. if not then parse a single statement.
        //in both cases add all statements to a list and return it.
        if (tokenManager.MatchAndRemove(Token.TokenType.LEFTCURLYBRACE).isEmpty()) { //check for {
            BlockNode single = new BlockNode();
            Optional<StatementNode> statement = parseStatement();
            if (statement.isPresent()) {
                single.addStatement(statement.get());
                acceptSeparators();
                return single;
            }
            else {
                throw new Exception("must have a statement inside a block");
            }
        }
        else {
            BlockNode multiple = new BlockNode();
            Optional<StatementNode> statement = parseStatement();
            while (statement.isPresent()) {
                multiple.addStatement(statement.get());
                acceptSeparators();
                statement = parseStatement();
            }

            if (tokenManager.MatchAndRemove(Token.TokenType.RIGHTCURLYBRACE).isPresent()) { //check for }
                return multiple;
            } else {
                throw new Exception("must have } with a { or you must have a legal statement");
            }
        }
    }

    public Optional<StatementNode> parseStatement() throws Exception {
        //check if we have a valid statement
        Optional<StatementNode> parsestatements = parseContinue();
        if (parsestatements.isPresent()){
            return parsestatements;
        }
        parsestatements = parseBreak();
        if (parsestatements.isPresent()){
            return parsestatements;
        }
        parsestatements = parseIf();
        if (parsestatements.isPresent()){
            return parsestatements;
        }
        parsestatements = parseFor();
        if (parsestatements.isPresent()){
            return parsestatements;
        }
        parsestatements = parseDelete();
        if (parsestatements.isPresent()){
            return parsestatements;
        }
        parsestatements = parseWhile();
        if (parsestatements.isPresent()){
            return parsestatements;
        }
        parsestatements = parseDoWhile();
        if (parsestatements.isPresent()){
            return parsestatements;
        }
        parsestatements = parseReturn();
        if (parsestatements.isPresent()){
            return parsestatements;
        }
        //else call parseoperation which will also pick up functioncall
        else{
            Optional<Node> temp = parseOperation();
            if (temp.isPresent()){
                if (temp.get() instanceof AssignmentNode){
                    return Optional.of((StatementNode)temp.get());
                }
                else if(temp.get() instanceof FunctionCallNode){
                    return Optional.of((StatementNode)temp.get());
                }
            }
        }
        return Optional.empty();
    }

    public Optional<StatementNode> parseContinue() {
        //check for continue and return a continue node if found
        if (tokenManager.MatchAndRemove(Token.TokenType.CONTINUE).isPresent()){
            acceptSeparators();
            return Optional.of(new ContinueNode());
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<StatementNode> parseBreak() {
        //check for break and return a break node if found
        if (tokenManager.MatchAndRemove(Token.TokenType.BREAK).isPresent()){
            acceptSeparators();
            return Optional.of(new BreakNode());
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<StatementNode> parseIf() throws Exception {
        if (tokenManager.MatchAndRemove(Token.TokenType.IF).isPresent()){ //check for if
            if (tokenManager.MatchAndRemove(Token.TokenType.LEFTPARANTHESIS).isPresent()){ //check for (
                Optional<Node> condition = parseOperation(); //parse condition
                if (condition.isEmpty()){ //if not found throw exception
                    throw new Exception("there must be a condition inside of an if statement.");
                }
                if (tokenManager.MatchAndRemove(Token.TokenType.RIGHTPARANTHESIS).isPresent()){ //if found check for right paran
                    BlockNode block = parseBlock(); //parse block
                    Optional<StatementNode> next = Optional.empty(); //set pointer to empty
                    Optional<StatementNode> ifList = Optional.of(new IfNode(condition, block, next)); //make an ifnode with no else node
                    if (tokenManager.MatchAndRemove(Token.TokenType.ELSE).isPresent()){
                        next = parseIf(); //if else is found, now next is checked properly and added
                        if (next.isPresent()){
                            ifList = Optional.of(new IfNode(condition, block, ifList, next));
                        }
                        else {
                            BlockNode block2 = parseBlock();
                            ifList = Optional.of(new IfNode(condition, block2, ifList));
                        }
                    }
                    return ifList;
                }
                else {
                    throw new Exception("right paranthesis required.");
                }
            }
            else {
                throw new Exception("left paranthesis required.");
            }
        }
        return Optional.empty();
    }

    public Optional<StatementNode> parseFor() throws Exception {
        if (tokenManager.MatchAndRemove(Token.TokenType.FOR).isPresent()) {//check for for
            if (tokenManager.MatchAndRemove(Token.TokenType.LEFTPARANTHESIS).isPresent()) {//check for (
                boolean in = false;
                for (int i = 0; i<tokens.size();i++){ //loop through the entire list of tokens with peek to check for an IN token for for-each loop
                    if (tokenManager.Peek(i).get().getType() == Token.TokenType.RIGHTPARANTHESIS){//check for )
                        break;
                    }
                    else if (tokenManager.Peek(i).get().getType() == Token.TokenType.IN){
                        in = true;
                        break;
                    }
                }
                if (in){ //if for each loop
                    Optional<Node> index = parseOperation();
                    if (tokenManager.MatchAndRemove(Token.TokenType.RIGHTPARANTHESIS).isPresent()) {
                        BlockNode block = parseBlock();
                        return Optional.of(new ForEachNode(index, block));
                    }
                    else {
                        throw new Exception("right paranthesis required.");
                    }
                }
                else { //if for loop
                    Optional<Node> assignment = parseOperation();
                    acceptSeparators();
                    Optional<Node> condition = parseOperation();
                    acceptSeparators();
                    Optional<Node> increment = parseOperation();
                    if (tokenManager.MatchAndRemove(Token.TokenType.RIGHTPARANTHESIS).isPresent()) {
                        BlockNode block = parseBlock();
                        return Optional.of(new ForNode(assignment, condition, increment, block));
                    }
                    else {
                        throw new Exception("right paranthesis required.");
                    }
                }
            }
            else {
                throw new Exception("left paranthesis required.");
            }
        }
        return Optional.empty();
    }

    public Optional<StatementNode> parseDelete() throws Exception {// if delete return delete node
        if (tokenManager.MatchAndRemove(Token.TokenType.DELETE).isPresent()) {
            Optional<Node> array = parseLValue();
            return Optional.of(new DeleteNode(array));
        }
        return Optional.empty();
    }

    public Optional<StatementNode> parseWhile() throws Exception {
        //if while check for left paran, condition, right paran, block, return whilenode
        if (tokenManager.MatchAndRemove(Token.TokenType.WHILE).isPresent()) {
            if (tokenManager.MatchAndRemove(Token.TokenType.LEFTPARANTHESIS).isPresent()) {
                Optional<Node> condition = parseOperation();
                if (tokenManager.MatchAndRemove(Token.TokenType.RIGHTPARANTHESIS).isPresent()) {
                    BlockNode block = parseBlock();
                    return Optional.of(new WhileNode(condition, block));
                }
                else {
                    throw new Exception("right paranthesis required.");
                }
            }
            else {
                throw new Exception("left paranthesis required.");
            }
        }
        return Optional.empty();
    }

    public Optional<StatementNode> parseDoWhile() throws Exception {
        //if do, parse block, check for while, check for left paran, condition, right paran, return dowhilenode
        if (tokenManager.MatchAndRemove(Token.TokenType.DO).isPresent()) {
                    BlockNode block = parseBlock();
                    if (tokenManager.MatchAndRemove(Token.TokenType.WHILE).isPresent()) {
                        if (tokenManager.MatchAndRemove(Token.TokenType.LEFTPARANTHESIS).isPresent()) {
                            Optional<Node> condition = parseOperation();
                            if (tokenManager.MatchAndRemove(Token.TokenType.RIGHTPARANTHESIS).isPresent()) {
                        return Optional.of(new DoWhileNode(block, condition));
                    }
                }
                else {
                    throw new Exception("right paranthesis required.");
                }
            }
            else {
                throw new Exception("left paranthesis required.");
            }
        }
        return Optional.empty();
    }

    public Optional<StatementNode> parseReturn() throws Exception {
        //if return, return what needs to be returned in a returnnode
        if (tokenManager.MatchAndRemove(Token.TokenType.RETURN).isPresent()){
            Optional<Node> returns = parseOperation();
            return Optional.of(new ReturnNode(returns));
        }
        else {
            return Optional.empty();
        }
    }


}