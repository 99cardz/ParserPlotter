package gui;

import parser.syntaxtree.SyntaxNode;
import java.awt.Color;
import java.util.List;

public class Function {
    // ! If more than eight functions are in use, expect undefined behaviour.
    private static final Color[] colors =
            { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN,
                    Color.ORANGE, Color.MAGENTA, Color.LIGHT_GRAY };
    private static int colorIndex = 0;

    private static long  uuidCounter = 1L;

    /**
     * Generate a unique identifier.
     * @return the unique identifier.
     */
    private static long generateUUID() {
        return uuidCounter++;
    }

    /**
     * Get the next color from the color pool.
     * @return The next color from the pool.
     */
    private static Color getNextColor() {
        return colors[colorIndex++ % 8];
    }

    /**
     * Build a Function object.
     * @param expressionString - The parsed expression.
     * @param root - The root of the syntax tree that was parsed from the expression string.
     */
    public Function(String expressionString, SyntaxNode root) {
        this.expressionString = expressionString;
        this.treeRoot = root;
        this.color = getNextColor();
        this.UUID = generateUUID();
    }

    final private long UUID;
    final private String expressionString;
    final private SyntaxNode treeRoot;
    final private Color color;

    public long getUUID() {
        return UUID;
    }
    public String getExpressionString() {
        return expressionString;
    }
    public SyntaxNode getTreeRoot() {
        return treeRoot;
    }
    public Color getColor() {
        return color;
    }

}
