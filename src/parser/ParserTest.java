package parser;

import parser.syntaxtree.SyntaxNode;

import java.util.List;
import java.util.Scanner;

public class ParserTest {

    private final static String[] testExpressions = {
            "x + x * 2",
            "x^2",
            "tan(10)*x + 5 ^2 ^2",
            "10 + 10 * 1000",
            "(10 + 10) * 1000",
            "x / 2 / 2",
            "2 * 2 / 2",
            "cos(x) * 100",
            "x * 2 * 2 ^ 3",
            "50 * 50",
            "100 tan",
            "2 * 123",
            "98765431",
            "- x",
            "x - ",
            "",
            "((x + x) * (x * 2)) * 2",
            "10 * 10",
            "500  * x",
            "10 * (2 - 2)",
    };

    private static void processExpression(final String expr) {
        Parser parser = new Parser();
        SyntaxNode root;

        try {
            root = parser.buildSyntaxTree(expr);

            System.out.print("Wiedergabe des Parsers: ");
            root.print();
            System.out.println();
            System.out.print("Auswertung: ");
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

    // Simple REPL to test the expression parser and evaluator
    public static void main(String[] args) {
        Parser parser = new Parser();
        SyntaxNode root;
        Scanner input = new Scanner(System.in);

        System.out.println("Hinweis: Zu Testzwecken ist x hier immer 1.\n");

        for(String str : testExpressions) {
            System.out.println("Eingabe: " + str);
            processExpression(str);
        }

        while(true) {
            System.out.print("Eingabe: ");
            String expr = input.nextLine();
            processExpression(expr);
        }
    }
}
