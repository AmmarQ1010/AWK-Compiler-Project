import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void testTokenManager() throws Exception {
        String input = "123\n 456; 789";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        tokenManager manager = new tokenManager(tokens);

        assertEquals(5, tokens.size());
        assertEquals(Optional.of(tokens.get(0)), manager.MatchAndRemove(Token.TokenType.NUMBER));
        assertTrue(manager.MoreTokens());
        assertEquals(Optional.of(tokens.get(1)), manager.Peek(1));
        assertEquals(Optional.of(tokens.get(0)), manager.MatchAndRemove(Token.TokenType.SEPARATOR));
        assertEquals(Optional.of(tokens.get(0)), manager.MatchAndRemove(Token.TokenType.NUMBER));
        assertEquals(Optional.of(tokens.get(0)), manager.MatchAndRemove(Token.TokenType.SEPARATOR));
        assertEquals(Optional.of(tokens.get(0)), manager.MatchAndRemove(Token.TokenType.NUMBER));
        assertFalse(manager.MoreTokens());
    }

    @Test
    public void parseFunctionTest1() throws Exception {
        String input = "function testing_abc(){}" ;
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        ProgramNode p = parser.Parse();
        assertEquals("FUNCTION: [Function Name: Token(WORD, \"testing_abc\") Params: []] Statements: \n", p.toString());
    }

    @Test
    public void parseFunctionTest2() throws Exception {
        String input = "function test(wow){}" ;
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        ProgramNode p = parser.Parse();
        assertEquals("FUNCTION: [Function Name: Token(WORD, \"test\") Params: [Token(WORD, \"wow\")]] Statements: \n", p.toString());
    }

    @Test
    public void parseActionTest1() throws Exception {
        String input = "BEGIN{}" ;
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        ProgramNode p = parser.Parse();
        assertEquals("BEGIN: [Statements: []]\n", p.toString());
    }

    @Test
    public void parseActionTest2() throws Exception {
        String input = "END{}";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        ProgramNode p = parser.Parse();
        assertEquals("END: [Statements: []]\n", p.toString());
    }

    @Test
    public void parseBottomLevelTestConstantNode1() throws Exception {
        String input = "1";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("1 ", p.toString());
    }

    @Test
    public void parseBottomLevelTestConstantNode2() throws Exception {
        String input = "\"yahoooo\"";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("yahoooo ", p.toString());
    }

    @Test
    public void parseBottomLevelTestPatternNode() throws Exception {
        String input = "`yahoo`";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("yahoo ", p.toString());
    }

    @Test
    public void parseBottomLevelTestVariableReferenceNode() throws Exception {
        String input = "(a)";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a", p.toString());
    }

    @Test
    public void parseBottomLevelTestNOT() throws Exception {
        String input = "!a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a NOT ", p.toString());
    }

    @Test
    public void parseBottomLevelTestUNARYNEG() throws Exception {
        String input = "-a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a UNARYNEG ", p.toString());
    }

    @Test
    public void parseBottomLevelTestUNARYPOS() throws Exception {
        String input = "+a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a UNARYPOS ", p.toString());
    }

    @Test
    public void parseBottomLevelTestPREINC() throws Exception {
        String input = "++a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a PREINC ", p.toString());
    }

    @Test
    public void parseBottomLevelTestPREDEC() throws Exception {
        String input = "--a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a PREDEC ", p.toString());
    }

    @Test
    public void parseLValueTest1() throws Exception {
        String input = "$7";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseLValue().get();

        assertEquals("$7  ", p.toString());

    }

    @Test
    public void parseLValueTest2() throws Exception {
        String input = "a[10]";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseLValue().get();

        assertEquals("a10  ", p.toString());

    }

    @Test
    public void parseLValueTest3() throws Exception {
        String input = "a()";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseLValue().get();

        assertEquals("a", p.toString());

    }

    @Test
    public void documentTest1() throws Exception {
        String input = "++a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a PREINC ", p.toString());
    }

    @Test
    public void documentTest2() throws Exception {
        String input = "++$a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("$a  PREINC ", p.toString());
    }

    @Test
    public void documentTest3() throws Exception {
        String input = "(++d)";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("d PREINC ", p.toString());
    }

    @Test
    public void documentTest4() throws Exception {
        String input = "e[++b]";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("eb PREINC  ", p.toString());
    }

    @Test
    public void documentTest5() throws Exception {
        String input = "$7";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseLValue().get();

        assertEquals("$7  ", p.toString());
    }

    @Test
    public void parseBottomLevelTestPOSTINC() throws Exception {
        String input = "a++";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a POSTINC ", p.toString());
    }

    @Test
    public void parseBottomLevelTestPOSTDEC() throws Exception {
        String input = "a--";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a POSTDEC ", p.toString());
    }

    @Test
    public void assignmentTest1() throws Exception{
        String input = "a=10";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("a10 ", p.get().toString());
    }
    @Test
    public void assignmentTest2() throws Exception{
        String input = "a-=10";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("aa SUBTRACT 10 ", p.get().toString());
    }
    @Test
    public void assignmentTest3() throws Exception{
        String input = "a+=10";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("aa ADD 10 ", p.get().toString());
    }
    @Test
    public void assignmentTest4() throws Exception{
        String input = "a/=10";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("aa DIVIDE 10 ", p.get().toString());
    }
    @Test
    public void assignmentTest5() throws Exception{
        String input = "a*=10";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("aa MULTIPLY 10 ", p.get().toString());
    }
    @Test
    public void assignmentTest6() throws Exception{
        String input = "a%=10";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("aa MODULO 10 ", p.get().toString());
    }

    @Test
    public void assignmentTest7() throws Exception{
        String input = "a^=10";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("aa EXPONENT 10 ", p.get().toString());
    }
    @Test
    public void TernaryTest() throws Exception{
        String input = "(a==5)?(a+=2):(a-=2)";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("a EQ 5 aa ADD 2 aa SUBTRACT 2 ", p.get().toString());
    }

    @Test
    public void OrTest() throws Exception{
        String input = "a||2";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("a OR 2 ", p.get().toString());
    }

    @Test
    public void AndTest() throws Exception{
        String input = "a&&2";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("a AND 2 ", p.get().toString());
    }

    @Test
    public void ArrayTest() throws Exception{
        String input = "PLUH[]";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("PLUH", p.get().toString());
    }

    @Test
    public void ArrayTest2() throws Exception{
        String input = "1+1 in PLUH[]";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  ADD 1  IN PLUH", p.get().toString());
    }

    @Test
    public void MatchTest() throws Exception{
        String input = "1~a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  MATCH a", p.get().toString());
    }

    @Test
    public void NotMatchTest() throws Exception{
        String input = "1!~a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  NOTMATCH a", p.get().toString());
    }

    @Test
    public void InequalityTest1() throws Exception{
        String input = "1>=(2+2)";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  GE 2  ADD 2 ", p.get().toString());
    }

    @Test
    public void InequalityTest2() throws Exception{
        String input = "1>4";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  GT 4 ", p.get().toString());
    }

    @Test
    public void InequalityTest3() throws Exception{
        String input = "1==(2+=3)";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  EQ 2 2  ADD 3 ", p.get().toString());
    }

    @Test
    public void InequalityTest4() throws Exception{
        String input = "1!=(2+=3)";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  NE 2 2  ADD 3 ", p.get().toString());
    }

    @Test
    public void InequalityTest5() throws Exception{
        String input = "1<=9";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  LE 9 ", p.get().toString());
    }

    @Test
    public void InequalityTest6() throws Exception{
        String input = "1<9";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  LT 9 ", p.get().toString());
    }

    @Test
    public void ConcaternationTest() throws Exception{
        String input = "yepper noper";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("yepper CONCATENATION noper", p.get().toString());
    }

    @Test
    public void SubtractionTest() throws Exception{
        String input = "1-1";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  SUBTRACT 1 ", p.get().toString());
    }

    @Test
    public void AdditionTest() throws Exception{
        String input = "1+1";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  ADD 1 ", p.get().toString());
    }

    @Test
    public void TermTest1() throws Exception{
        String input = "1%1";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  MODULO 1 ", p.get().toString());
    }

    @Test
    public void TermTest2() throws Exception{
        String input = "1/9";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  DIVIDE 9 ", p.get().toString());
    }

    @Test
    public void TermTest3() throws Exception{
        String input = "1*9";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  MULTIPLY 9 ", p.get().toString());
    }

    @Test
    public void ParseBottomLevelTest1() throws Exception{
        String input = "-a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("a UNARYNEG ", p.get().toString());
    }

    @Test
    public void ParseBottomLevelTest2() throws Exception{
        String input = "+a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("a UNARYPOS ", p.get().toString());
    }

    @Test
    public void ParseBottomLevelTest3() throws Exception{
        String input = "1/1";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  DIVIDE 1 ", p.get().toString());
    }

    @Test
    public void ParseExponent() throws Exception{
        String input = "1^1";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  EXPONENT 1 ", p.get().toString());
    }

    @Test
    public void parseBottomLevelTest4() throws Exception {
        String input = "++a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a PREINC ", p.toString());
    }

    @Test
    public void parseBottomLevelTest5() throws Exception {
        String input = "--a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a PREDEC ", p.toString());
    }

    @Test
    public void parseLValueTest4() throws Exception {
        String input = "a++";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a POSTINC ", p.toString());
    }

    @Test
    public void parseLValueTest5() throws Exception {
        String input = "a--";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("a POSTDEC ", p.toString());
    }

    @Test
    public void parseExpressionTest() throws Exception {
        String input = "(1+9^3+2-2)";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseBottomLevel().get();

        assertEquals("1  ADD 9  EXPONENT 3  ADD 2  SUBTRACT 2 ", p.toString());
    }

    @Test
    public void parseALOTTest() throws Exception {
        String input = "1+2-3*4/5%6^7^(8)<9<=10!=11==12>13>=14~15!~16&&17||18 in PLUH[]";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Optional<Node> p = parser.parseOperation();

        assertEquals("1  ADD 2  SUBTRACT 3  MULTIPLY 4  DIVIDE 5  MODULO " +
                "6  EXPONENT 7  EXPONENT 8  LT 9  LE 10  NE 11  EQ " +
                "12  GT 13  GE 14  MATCH 15  NOTMATCH 16  AND 17  OR 18  IN PLUH", p.get().toString());
    }

    @Test
    public void parseContinueTest() throws Exception {
        String input = "continue";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Continue Node.", p.toString());
    }

    @Test
    public void parseBreakTest() throws Exception {
        String input = "break";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Break Node.", p.toString());
    }

    @Test
    public void parseIfTest() throws Exception {
        String input = "if(a<2){}";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("If node condition: a LT 2  block contents: Statements: []", p.toString());
    }

    @Test
    public void parseIfTest2() throws Exception {
        String input = "if(a<2){ delete a[1] } else { delete a[2] }";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("If node condition: a LT 2  block contents: Statements: [Deleting: a1  ] else: Statements: [Deleting: a2  ]", p.toString());
    }

    @Test
    public void parseIfTest3() throws Exception {
        String input = "if(a<2){} else if (3<9) {}";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("If node condition: a LT 2  block contents: Statements: [] else: Statements: [] If node condition: 3  LT 9  block contents: Statements: []", p.toString());
    }

    @Test
    public void parseIfTest4() throws Exception {
        String input = "if(a<2){} else if (3<9) {} else {}";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("If node condition: a LT 2  block contents: Statements: [] else: Statements: [] If node condition: 3  LT 9  block contents: Statements: [] else: Statements: []", p.toString());
    }

    @Test
    public void parseDeleteTest() throws Exception {
        String input = "delete a[1]";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Deleting: a1  ", p.toString());
    }

    @Test
    public void parseForTest() throws Exception {
        String input = "for(i = 0; i<2; i++){}";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("For assignment: i0  condition: i LT 2  increment: i POSTINC block contents: Statements: []", p.toString());
    }

    @Test
    public void parseForEachTest() throws Exception {
        String input = "for(i in arr[]){}";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("For Each contents: i IN arr block contents: Statements: []", p.toString());
    }

    @Test
    public void parseWhileTest() throws Exception {
        String input = "while(x<1) {}";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("While Condition: x LT 1  block list: Statements: []", p.toString());
    }

    @Test
    public void parseDoWhileTest() throws Exception {
        String input = "do {} while(x<1)";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Do While Block: Statements: [] while condition: x LT 1 ", p.toString());
    }

    @Test
    public void parseReturnTest() throws Exception {
        String input = "return a";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Returning: a", p.toString());
    }

    @Test
    public void parseFunctionCallTest() throws Exception {
        String input = "wussup(a, b)";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        Parser parser = new Parser(tokens);
        Node p = parser.parseStatement().get();

        assertEquals("Function Name: wussup Params: [a, b]", p.toString());
    }

}