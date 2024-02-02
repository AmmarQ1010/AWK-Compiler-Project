import java.util.HashMap;
import java.util.LinkedList;

public class Lexer {
	private stringHandler stringHandler;
    private LinkedList<Token> tokens;
    private int lineNumber = 1;
    private int charPosition = 0;
    private HashMap<String, Token.TokenType> keywordMap;
    private HashMap<String, Token.TokenType> twoSymbolMap;
    private HashMap<String, Token.TokenType> oneSymbolMap;
    
    public Lexer(String file) { //constructor
        this.stringHandler = new stringHandler(file);
        this.tokens = new LinkedList<>();
        this.keywordMap = new HashMap<>();
        this.twoSymbolMap = new HashMap<>();
        this.oneSymbolMap = new HashMap<>();
        populateKeywordMap();
    }

    public LinkedList<Token> lex() throws Exception {
    	while (!stringHandler.isDone()) { //while file not over
            char nextChar = stringHandler.peek(0);
            if (nextChar == ' ' || nextChar == '\t' || nextChar == '\r') { //if blank or \t, go forward in index by 1
                stringHandler.swallow(1);
            }  else if (nextChar == '#'){
        			while(nextChar != '\n' || nextChar != ';') {
        				if(!stringHandler.isDone()) {
                            nextChar = stringHandler.peek(0);
                            stringHandler.swallow(1);
                            if (nextChar == '\n') {
                            	charPosition++;
                            	tokens.add(new Token(Token.TokenType.SEPARATOR, lineNumber, charPosition)); //if \n, go forward in index by 1 and return that this char is a separator
                                stringHandler.swallow(1);
                                lineNumber++;
                                charPosition = 0;
            					break;
                            } else if (nextChar == ';') {
                            	charPosition++;
                            	tokens.add(new Token(Token.TokenType.SEPARATOR, lineNumber, charPosition-1)); //if \n, go forward in index by 1 and return that this char is a separator
                                stringHandler.swallow(1);
            					break;
                            
                            }
        				} else if (stringHandler.isDone()){
                            break;
                        }
                       
					}
        	} else if (nextChar == '\n') {
                charPosition++;	
            	tokens.add(new Token(Token.TokenType.SEPARATOR, lineNumber, charPosition)); //if \n, go forward in index by 1 and return that this char is a separator
                stringHandler.swallow(1);
                lineNumber++;
                charPosition = 0;
            } else if (nextChar == ';') {
            	charPosition++;
            	tokens.add(new Token(Token.TokenType.SEPARATOR, lineNumber, charPosition-1)); //if \n, go forward in index by 1 and return that this char is a separator
                stringHandler.swallow(1);
            } else if (nextChar == '"') {

            	tokens.add(handleStringLiteral());
            } else if (nextChar == '`') {
            	tokens.add(handlePattern());
            } else if (Character.isLetter(nextChar)) { //if char is a letter, send to processword
                tokens.add(processWord());
            } else if (Character.isDigit(nextChar)) {
                tokens.add(processDigit());
            } else {
                Token temp = processSymbol();
            	if (temp==null) {
            		throw new Exception("Unrecognized character: " + nextChar);
            	} else {
            		tokens.add(temp);
            		if (stringHandler.isDone()){
            			break;
            		}
            	}
            }
        }
        return tokens;
    }

	private Token processWord() { //char we're looking at is a letter
    	int count = 0;
        StringBuilder sb = new StringBuilder();
        while (!stringHandler.isDone()) {
            char nextChar = stringHandler.peek(0);
            if (Character.isLetterOrDigit(nextChar) || nextChar == '_') { //if nextchar is letter or digit or _, add to a token with type WORD.
                sb.append(stringHandler.getChar());
                charPosition++;
                count++;
            } else { //else go next
                break;
            }
        }
        //we have the full word now.
        String word = sb.toString();
        Token.TokenType tokenType = keywordMap.containsKey(word) //does our hashmap have this word?
        							? keywordMap.get(word) //if yes, tokentype = that word
        							: Token.TokenType.WORD; //if no, tokentype = WORD
        if (tokenType == Token.TokenType.WORD) {
            return new Token(tokenType, word, lineNumber, charPosition - count);
        } else {
            return new Token(tokenType, lineNumber, charPosition - count);
        }
    }

    private Token processDigit() {
    	int count = 0;
        StringBuilder sb = new StringBuilder();
        boolean hasDecimalPoint = false;
        while (!stringHandler.isDone()) {
            char nextChar = stringHandler.peek(0);
            if (Character.isDigit(nextChar)) { //if nextchar is a digit, keep going
                sb.append(stringHandler.getChar());
                charPosition++;
                count++;
            } else if (nextChar == '.' && !hasDecimalPoint) { //if nextchar is a decimal, set hasDecimalPoint flag true and keep going
                sb.append(stringHandler.getChar());
                charPosition++;
                count++;
                hasDecimalPoint = true;
            } else { //else return the token
                break;
            }
        }
        return new Token(Token.TokenType.NUMBER, sb.toString(), lineNumber, charPosition-count);
    }
    
    private void populateKeywordMap() { //put keywords into their respective hashmaps
        keywordMap.put("while", Token.TokenType.WHILE);
        keywordMap.put("if", Token.TokenType.IF);
        keywordMap.put("do", Token.TokenType.DO);
        keywordMap.put("for", Token.TokenType.FOR);
        keywordMap.put("break", Token.TokenType.BREAK);
        keywordMap.put("continue", Token.TokenType.CONTINUE);
        keywordMap.put("else", Token.TokenType.ELSE);
        keywordMap.put("return", Token.TokenType.RETURN);
        keywordMap.put("BEGIN", Token.TokenType.BEGIN);
        keywordMap.put("END", Token.TokenType.END);
        keywordMap.put("print", Token.TokenType.PRINT);
        keywordMap.put("printf", Token.TokenType.PRINTF);
        keywordMap.put("next", Token.TokenType.NEXT);
        keywordMap.put("in", Token.TokenType.IN);
        keywordMap.put("delete", Token.TokenType.DELETE);
        keywordMap.put("getline", Token.TokenType.GETLINE);
        keywordMap.put("exit", Token.TokenType.EXIT);
        keywordMap.put("nextfile", Token.TokenType.NEXTFILE);
        keywordMap.put("function", Token.TokenType.FUNCTION);
        twoSymbolMap.put(">=", Token.TokenType.GREATERTHANOREQUALTO);
        twoSymbolMap.put("++", Token.TokenType.PLUSONE);
        twoSymbolMap.put("--", Token.TokenType.MINUSONE);
        twoSymbolMap.put("<=", Token.TokenType.LESSTHANOREQUALTO);
        twoSymbolMap.put("==", Token.TokenType.EQUALEQUAL);
        twoSymbolMap.put("!=", Token.TokenType.NOTEQUAL);
        twoSymbolMap.put("^=", Token.TokenType.EXPEQUAL);
        twoSymbolMap.put("%=", Token.TokenType.MODEQUAL);
        twoSymbolMap.put("*=", Token.TokenType.MULTEQUAL);
        twoSymbolMap.put("/=", Token.TokenType.DIVEQUAL);
        twoSymbolMap.put("+=", Token.TokenType.PLUSEQUAL);
        twoSymbolMap.put("-=", Token.TokenType.MINUSEQUAL);
        twoSymbolMap.put("!~", Token.TokenType.NOTMATCH);
        twoSymbolMap.put("&&", Token.TokenType.AND);
        twoSymbolMap.put(">>", Token.TokenType.APPEND);
        twoSymbolMap.put("||", Token.TokenType.OR);
        oneSymbolMap.put("{", Token.TokenType.LEFTCURLYBRACE);
    	oneSymbolMap.put("}", Token.TokenType.RIGHTCURLYBRACE);
    	oneSymbolMap.put("[", Token.TokenType.LEFTBRACKET);
    	oneSymbolMap.put("]", Token.TokenType.RIGHTBRACKET);
    	oneSymbolMap.put("(", Token.TokenType.LEFTPARANTHESIS);
    	oneSymbolMap.put(")", Token.TokenType.RIGHTPARANTHESIS);
    	oneSymbolMap.put("$", Token.TokenType.DOLLAR);
    	oneSymbolMap.put("~", Token.TokenType.MATCH);
    	oneSymbolMap.put("=", Token.TokenType.EQUAL);
    	oneSymbolMap.put("<", Token.TokenType.LESSTHAN);
    	oneSymbolMap.put(">", Token.TokenType.GREATERTHAN);
    	oneSymbolMap.put("!", Token.TokenType.NOT);
    	oneSymbolMap.put("+", Token.TokenType.PLUS);
    	oneSymbolMap.put("^", Token.TokenType.EXP);
    	oneSymbolMap.put("-", Token.TokenType.MINUS);
    	oneSymbolMap.put("?", Token.TokenType.TERNARY);
    	oneSymbolMap.put(":", Token.TokenType.COLON);
    	oneSymbolMap.put("*", Token.TokenType.TIMES);
    	oneSymbolMap.put("/", Token.TokenType.DIV);
    	oneSymbolMap.put("%", Token.TokenType.MOD);
    	//oneSymbolMap.put(";", Token.TokenType.SEMICOLON);
    	//oneSymbolMap.put("\n", Token.TokenType.NEWLINE);
    	oneSymbolMap.put("|", Token.TokenType.BITWISEOR);
    	oneSymbolMap.put(",", Token.TokenType.COMMA);
    }

    private Token handleStringLiteral() { //handles if there is a "
    	int count = 0;
        StringBuilder sb = new StringBuilder();
        
        stringHandler.swallow(1); //go ahead of the current "
        charPosition++;
    	if (!stringHandler.isDone()) {
	    	while (stringHandler.peek(0)!='"') { //add to sb until another " is found
	    		if (stringHandler.peek(0)=='\\') {
	    			stringHandler.swallow(1);
	    		}
	    			if (stringHandler.peek(0) == '"') {
		    			sb.append(stringHandler.getChar());
	    			}
	    		sb.append(stringHandler.getChar());
	            charPosition++;
	            count++;
	            if (stringHandler.peek(0)=='"') { //moves index by 1 if " is found
	            	stringHandler.swallow(1);
	            	break;
	            }
	        }
	    	charPosition++;
	    	stringHandler.swallow(1);
    	}
    	return new Token(Token.TokenType.STRINGLITERAL, sb.toString(), lineNumber, charPosition - count);
    }
    
    private Token handlePattern() { //handles if there is a `
    	
    	int count = 0;
        StringBuilder sb = new StringBuilder();
        stringHandler.swallow(1); //go ahead of the current `
    	if (!stringHandler.isDone()) {
	    	while (stringHandler.peek(0)!='`') { //add to sb until another ` is found
	            sb.append(stringHandler.getChar());
	            charPosition++;
	            count++;
	            if (stringHandler.peek(0)=='`') { //moves index by 1 if ` is found
	            	stringHandler.swallow(1);
	            	break;
	            }
	        }
    	}
    	return new Token(Token.TokenType.PATTERN, sb.toString(), lineNumber, charPosition - count);
    }

    private Token processSymbol(){ //if the current character is not a letter or digit, come here.
    	    	
    	int count = 0;
        if(stringHandler.remainder().length()>=2){
            String check2sym = stringHandler.peekString(2); //string of next 2 characters
            if (twoSymbolMap.containsKey(check2sym)) { //check if the symbol matches hashmap twoSymbolMap
                count+=2;
                charPosition+=2;
                stringHandler.swallow(2);
                return new Token(twoSymbolMap.get(check2sym), check2sym, lineNumber, charPosition - count);
            }
        } if (stringHandler.remainder().length()>=1){
            String check1sym = stringHandler.peekString(1);//string of next character
            if (oneSymbolMap.containsKey(check1sym)) { //check if the symbol matches hashmap oneSymbolMap
                count++;
                charPosition++;
                stringHandler.swallow(1);
                return new Token(oneSymbolMap.get(check1sym), check1sym, lineNumber, charPosition - count);
            }
        }
        return null;
    }
}