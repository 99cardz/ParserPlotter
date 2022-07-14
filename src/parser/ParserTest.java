package parser;

import parser.syntaxtree.SyntaxNode;

import java.util.List;
import java.util.Scanner;

public class ParserTest {
    /**
     * Prints the List of tokens.
     * @param tokens
     */
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
            System.out.println("Enter your expression:");
            String expr = input.nextLine();

            try {
                // printTokenList(Parser.lex(expr));
                root = parser.buildSyntaxTree(expr);
                root.print();
                System.out.println();
                System.out.println(root.eval(1));
                System.out.println();
            }
            catch(SyntaxException e) {
                if(e.getStartIndex() == -1) {
                    System.out.println("Unexpected end of input!");
                }
                else {
                    System.out.println("Illegal symbol \"" + e.getString() + "\" at position " + e.getStartIndex() + ":");
                    System.out.println(expr);
                    int i = 0;
                    while(i++ < e.getStartIndex()) {
                        System.out.print(' ');
                    }
                    while(i++ < e.getEndIndex()) {
                        System.out.print('^');
                    }
                    System.out.println();

                    System.out.println();
                }
                root = null;
            }
        }
    }
}
