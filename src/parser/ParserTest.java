package parser;

import parser.syntaxtree.SyntaxNode;

import java.util.List;
import java.util.Scanner;

public class ParserTest {

    // Simple REPL to test the expression parser and evaluator
    public static void main(String[] args) {
        Parser parser = new Parser();
        SyntaxNode root;
        Scanner input = new Scanner(System.in);

        while(true) {
            System.out.println("Enter your expression:");
            String expr = input.nextLine();

            try {
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
