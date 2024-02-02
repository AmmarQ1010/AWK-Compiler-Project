import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

public class LexerTest {
	
	
    @Test
    public void testSingleLineTokens() throws Exception {
        String input = "hello world 123";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();

        assertEquals(3, tokens.size());
        assertEquals("Token(WORD, \"hello\")", tokens.get(0).toString());
        assertEquals("Token(WORD, \"world\")", tokens.get(1).toString());
        assertEquals("Token(NUMBER, \"123\")", tokens.get(2).toString());
    }

    @Test
    public void testMultiLineTokens() throws Exception {
        String input = "Line: 1\nli\tne 2\nline_3\nLine: 4";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();

        assertEquals(13, tokens.size());
        assertEquals("Token(WORD, \"Line\")", tokens.get(0).toString());
        assertEquals("Token(COLON, \":\")", tokens.get(1).toString());
        assertEquals("Token(NUMBER, \"1\")", tokens.get(2).toString());
        assertEquals("Token(SEPARATOR", tokens.get(3).toString());
        assertEquals("Token(WORD, \"li\")", tokens.get(4).toString());
        assertEquals("Token(WORD, \"ne\")", tokens.get(5).toString());
        assertEquals("Token(NUMBER, \"2\")", tokens.get(6).toString());
        assertEquals("Token(SEPARATOR", tokens.get(7).toString());
        assertEquals("Token(WORD, \"line_3\")", tokens.get(8).toString());
        assertEquals("Token(SEPARATOR", tokens.get(9).toString());
        assertEquals("Token(WORD, \"Line\")", tokens.get(10).toString());
        assertEquals("Token(COLON, \":\")", tokens.get(11).toString());
        assertEquals("Token(NUMBER, \"4\")", tokens.get(12).toString());

    }

    @Test
    public void testMixedTokens() throws Exception {
        String input = "abc 123.2 xyz 456";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();

        assertEquals(4, tokens.size());
        assertEquals("Token(WORD, \"abc\")", tokens.get(0).toString());
        assertEquals("Token(NUMBER, \"123.2\")", tokens.get(1).toString());
        assertEquals("Token(WORD, \"xyz\")", tokens.get(2).toString());
        assertEquals("Token(NUMBER, \"456\")", tokens.get(3).toString());
    }
    
    @Test
    public void testBackticks() throws Exception {
        String input = "woo `abc@7hf108vh=--_-././?` 123";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();

        assertEquals(3, tokens.size());
        assertEquals("Token(WORD, \"woo\")", tokens.get(0).toString());
        assertEquals("Token(PATTERN, \"abc@7hf108vh=--_-././?\")", tokens.get(1).toString());
        assertEquals("Token(NUMBER, \"123\")", tokens.get(2).toString());
    }
    
    @Test
    public void testOneSymbol() throws Exception{ //split up because i accidentally put '=' as MINUS
    	String input = "yaba "
    			+ "{ } [ ] ( )"
    			+ " $ ~ = < > !"
    			+ " + ^ "
    			+ "-"
    			+ "?"
    			+ " : "
    			+ "* / % ; \n"
    			+ " | , zaa";
    	Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        assertEquals("Token(WORD, \"yaba\")", tokens.get(0).toString());
        assertEquals("Token(LEFTCURLYBRACE, \"{\")", tokens.get(1).toString());
        assertEquals("Token(RIGHTCURLYBRACE, \"}\")", tokens.get(2).toString());
        assertEquals("Token(LEFTBRACKET, \"[\")", tokens.get(3).toString());
        assertEquals("Token(RIGHTBRACKET, \"]\")", tokens.get(4).toString());
        assertEquals("Token(LEFTPARANTHESIS, \"(\")", tokens.get(5).toString());
        assertEquals("Token(RIGHTPARANTHESIS, \")\")", tokens.get(6).toString());
        assertEquals("Token(DOLLAR, \"$\")", tokens.get(7).toString());
        assertEquals("Token(MATCH, \"~\")", tokens.get(8).toString());
        assertEquals("Token(EQUAL, \"=\")", tokens.get(9).toString());
        assertEquals("Token(LESSTHAN, \"<\")", tokens.get(10).toString());
        assertEquals("Token(GREATERTHAN, \">\")", tokens.get(11).toString());
        assertEquals("Token(NOT, \"!\")", tokens.get(12).toString());
        assertEquals("Token(PLUS, \"+\")", tokens.get(13).toString());
        assertEquals("Token(EXP, \"^\")", tokens.get(14).toString());
        assertEquals("Token(MINUS, \"-\")", tokens.get(15).toString());
        assertEquals("Token(TERNARY, \"?\")", tokens.get(16).toString());
        assertEquals("Token(COLON, \":\")", tokens.get(17).toString());
        assertEquals("Token(TIMES, \"*\")", tokens.get(18).toString());
        assertEquals("Token(DIV, \"/\")", tokens.get(19).toString());
        assertEquals("Token(MOD, \"%\")", tokens.get(20).toString());
        assertEquals("Token(SEPARATOR", tokens.get(21).toString());
        assertEquals("Token(SEPARATOR", tokens.get(21).toString());
        assertEquals("Token(BITWISEOR, \"|\")", tokens.get(23).toString());
        assertEquals("Token(COMMA, \",\")", tokens.get(24).toString());
        assertEquals("Token(WORD, \"zaa\")", tokens.get(25).toString());

    	
    }
    
    
    @Test
    public void testTwoSymbol() throws Exception{ //split up because i accidentally put '=' as MINUS
    	String input = "yaba >=  ++  --  <=  ==  !=  ^=  %=  *=  /=  +=  -=  !~   &&   >>   || zaa";
    	Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        assertEquals("Token(WORD, \"yaba\")", tokens.get(0).toString());
        assertEquals("Token(GREATERTHANOREQUALTO, \">=\")", tokens.get(1).toString());
        assertEquals("Token(PLUSONE, \"++\")", tokens.get(2).toString());
        assertEquals("Token(MINUSONE, \"--\")", tokens.get(3).toString());
        assertEquals("Token(LESSTHANOREQUALTO, \"<=\")", tokens.get(4).toString());
        assertEquals("Token(EQUALEQUAL, \"==\")", tokens.get(5).toString());
        assertEquals("Token(NOTEQUAL, \"!=\")", tokens.get(6).toString());
        assertEquals("Token(EXPEQUAL, \"^=\")", tokens.get(7).toString());
        assertEquals("Token(MODEQUAL, \"%=\")", tokens.get(8).toString());
        assertEquals("Token(MULTEQUAL, \"*=\")", tokens.get(9).toString());
        assertEquals("Token(DIVEQUAL, \"/=\")", tokens.get(10).toString());
        assertEquals("Token(PLUSEQUAL, \"+=\")", tokens.get(11).toString());
        assertEquals("Token(MINUSEQUAL, \"-=\")", tokens.get(12).toString());
        assertEquals("Token(NOTMATCH, \"!~\")", tokens.get(13).toString());
        assertEquals("Token(AND, \"&&\")", tokens.get(14).toString());
        assertEquals("Token(APPEND, \">>\")", tokens.get(15).toString());
        assertEquals("Token(OR, \"||\")", tokens.get(16).toString());
        assertEquals("Token(WORD, \"zaa\")", tokens.get(17).toString());

    	
    }
    
    
    @Test
    public void testKeywords() throws Exception{ //split up because i accidentally put '=' as MINUS
    	String input = "yaba while if do for break continue else return BEGIN END print printf next in delete getline exit nextfile function zaa";
    	Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();
        assertEquals("Token(WORD, \"yaba\")", tokens.get(0).toString());
        assertEquals("Token(WHILE", tokens.get(1).toString());
        assertEquals("Token(IF", tokens.get(2).toString());
        assertEquals("Token(DO", tokens.get(3).toString());
        assertEquals("Token(FOR", tokens.get(4).toString());
        assertEquals("Token(BREAK", tokens.get(5).toString());
        assertEquals("Token(CONTINUE", tokens.get(6).toString());
        assertEquals("Token(ELSE", tokens.get(7).toString());
        assertEquals("Token(RETURN", tokens.get(8).toString());
        assertEquals("Token(BEGIN", tokens.get(9).toString());
        assertEquals("Token(END", tokens.get(10).toString());
        assertEquals("Token(PRINT", tokens.get(11).toString());
        assertEquals("Token(PRINTF", tokens.get(12).toString());
        assertEquals("Token(NEXT", tokens.get(13).toString());
        assertEquals("Token(IN", tokens.get(14).toString());
        assertEquals("Token(DELETE", tokens.get(15).toString());
        assertEquals("Token(GETLINE", tokens.get(16).toString());
        assertEquals("Token(EXIT", tokens.get(17).toString());
        assertEquals("Token(NEXTFILE", tokens.get(18).toString());
        assertEquals("Token(FUNCTION", tokens.get(19).toString());
        assertEquals("Token(WORD, \"zaa\")", tokens.get(20).toString());
    }
    
    @Test
    public void testComments() throws Exception { //completely ignores # until ; or \n
        String input = "woo #wowza 123 ; woo #woopee \n woo";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.lex();

        assertEquals(5, tokens.size());
        assertEquals("Token(WORD, \"woo\")", tokens.get(0).toString());
        assertEquals("Token(SEPARATOR", tokens.get(1).toString());
        assertEquals("Token(WORD, \"woo\")", tokens.get(2).toString());
        assertEquals("Token(SEPARATOR", tokens.get(3).toString());
        assertEquals("Token(WORD, \"woo\")", tokens.get(4).toString());
    }
    
    @Test
    public void testStringLiterals() throws Exception{
    	
    	String input = "\"She said, \\\"hello there\\\" and then she left\"";
    	Lexer lexer = new Lexer(input);
    	LinkedList<Token> tokens = lexer.lex();
    	
    	assertEquals(1, tokens.size());
        assertEquals("Token(STRINGLITERAL, \"She said, \"hello there\" and then she left\")", tokens.get(0).toString());
    }
}
