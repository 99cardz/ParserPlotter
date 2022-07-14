package parser;

public class SyntaxException extends Exception {

    // Begin and start indices of the error in the corresponding input string
    int startIndex;
    int endIndex;

    // The portion of the string where the error occurs
    String string;

    public SyntaxException(String string, int start, int end) {
        this.string     = string;
        this.startIndex = start;
        this.endIndex   = end;
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
}
