package parser;

import parser.syntaxtree.SyntaxNode;

import java.util.List;
import java.util.Scanner;

public class ParserTest {
    private static void printTokenList(List<Token> tokens) {
        for(Token token : tokens) {
            System.out.println(token.getType().name() + " " + token.getString());
        }
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
        SyntaxNode root;
        Scanner input = new Scanner(System.in);

        while(true) {
            String expr = input.nextLine();

            try {
                // printTokenList(Parser.lex(expr));
                root = parser.buildSyntaxTree(expr);
                root.print();
                System.out.println();
                System.out.println(root.eval(1));
            }
            catch(SyntaxException e) {
                if(e.getStartIndex() == -1) {
                    System.out.println("Unexpected end of input!");
                }
                else {
                    System.out.println("Illegal symbol \"" + e.getString() + "\" at position " + e.getStartIndex());
                }
                root = null;
            }
        }
    }
}
