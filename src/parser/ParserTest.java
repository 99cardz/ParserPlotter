package parser;

import java.util.List;

public class ParserTest {
    private static void printTokenList(List<Token> tokens) {
        for(Token token : tokens) {
            System.out.println(token.getType().name() + " " + token.getString());
        }
    }

    public static void main(String[] args) {
        try {
            printTokenList(Parser.lex("x + x -255 * x / *"));
        }
        catch (SyntaxException e) {
            System.out.println("Illegal symbol \"" + e.getString() + "\" at position " + e.getStartIndex());
        }
    }
}
