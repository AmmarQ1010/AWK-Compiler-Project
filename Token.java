public class Token {
	
	private TokenType type; //word or number or separator
    private String value;
    /*private int lineNumber;
    private int charPosition;*/


	public enum TokenType { //enum constructor
		WORD,
		NUMBER,
		SEPARATOR,
		WHILE,
    	IF,
    	DO,
    	FOR,
    	BREAK,
    	CONTINUE,
    	ELSE,
    	RETURN,
    	BEGIN,
    	END,
    	PRINT,
    	PRINTF,
    	NEXT,
    	IN,
    	DELETE,
    	GETLINE,
    	EXIT,
    	NEXTFILE,
    	FUNCTION,
    	STRINGLITERAL,
    	PATTERN,
    	GREATERTHANOREQUALTO, //start of two character symbols
    	PLUSONE,
    	MINUSONE,
    	LESSTHANOREQUALTO,
    	EQUALEQUAL,
    	NOTEQUAL,
    	EXPEQUAL,
    	MODEQUAL,
    	MULTEQUAL,
    	DIVEQUAL,
    	PLUSEQUAL,
    	MINUSEQUAL,
    	NOTMATCH,
    	AND,
    	APPEND,
    	OR, //end of two character symbols
    	LEFTCURLYBRACE, //start of one character symbols
    	RIGHTCURLYBRACE,
    	LEFTBRACKET,
    	RIGHTBRACKET,
    	LEFTPARANTHESIS,
    	RIGHTPARANTHESIS,
    	DOLLAR,
    	MATCH,
    	EQUAL,
    	LESSTHAN,
    	GREATERTHAN,
    	NOT,
    	PLUS,
    	EXP,
    	MINUS,
    	TERNARY,
    	COLON,
    	TIMES,
    	DIV,
    	MOD,
    	SEMICOLON,
    	NEWLINE,
    	BITWISEOR,
    	COMMA //end of one character symbols	
    }

    public Token(TokenType type, int lineNumber, int charPosition) { //token constructor
        this.type = type;
        /*this.lineNumber = lineNumber;
        this.charPosition = charPosition;*/
    }

    public Token(TokenType type, String value, int lineNumber, int charPosition) { //token constructor with value
        this.type = type;
        this.value = value;
        /*this.lineNumber = lineNumber;
        this.charPosition = charPosition;*/
    }

	public Token(TokenType type, String value) { //token constructor with value
		this.type = type;
		this.value = value;
        /*this.lineNumber = lineNumber;
        this.charPosition = charPosition;*/
	}

    public String toString() { //toString from token to string
        StringBuilder builder = new StringBuilder(); 
        builder.append("Token(").append(type); //token type to string

        if (value != null) { //if there is a value in token
            builder.append(", \"")//put a comma and slash to separate it and then put it in quotes
            .append(value).append("\")"); //close slash
        }

		/*
		builder.append(") - Line: ").append(lineNumber)//display line number
		.append(", Char: ").append(charPosition);//display character position
		*/
        return builder.toString();
    }

	public Token.TokenType getType(){
		return type;
	}

	public String getVal() {
		return value;
	}
}