package parser;

import parser.syntaxtree.SyntaxNode;

import java.util.List;

public class ParserTest {
    private static void printTokenList(List<Token> tokens) {
        for(Token token : tokens) {
            System.out.println(token.getType().name() + " " + token.getString());
        }
    }

    private static final String[] testEpxressions = { "x + x"};

    public static void main(String[] args) {
        try {
            printTokenList(Parser.lex("x + x -255 * x / * sin()"));
        }
        catch (SyntaxException e) {
            System.out.println("Illegal symbol \"" + e.getString() + "\" at position " + e.getStartIndex());
        }

        Parser parser = new Parser();

        SyntaxNode root = parser.buildSyntaxTree("x + 2 + 20.0 + (-5^2)");
        root.print();
    }
}
