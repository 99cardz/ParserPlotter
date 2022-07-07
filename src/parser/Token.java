package parser;

public class Token {

    // The tokens begin and start indices in the corresponding input string
    private int startIndex; // inclusive
    private int endIndex;   // exclusive

    // The tokens string
    private String string;

    // Type of the Token
    private TokenType type;

    // Value (if number)
    private double value = 0;

    // Function type (if function)
    private FunctionType funcType = null;


    Token(String string, int start, int end, TokenType type) {
        this.endIndex   = end;
        this.startIndex = start;
        this.string     = string;
        this.type       = type;
    }

    // Special constructor for function tokens
    Token(String string, int start, int end, TokenType type, FunctionType funcType) {
        this.endIndex   = end;
        this.startIndex = start;
        this.string     = string;
        this.type       = type;
        this.funcType   = funcType;
    }

    // Special constructor for number tokens
    Token(String string, int start, int end, TokenType type, double value) {
        this.endIndex   = end;
        this.startIndex = start;
        this.string     = string;
        this.type       = type;
        this.value      = value;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public String getString() {
        return string;
    }

    public TokenType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public FunctionType getFuncType() {
        return funcType;
    }
}
