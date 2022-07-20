package gui;

import parser.syntaxtree.SyntaxNode;
import java.awt.Color;
import java.util.List;

public class Function {
    // ! If more than eight functions are in use, expect undefined behaviour.
    private static final Color[] colors =
    	{
    			new Color(39, 88, 216), // blue
    			new Color(150, 30, 225), // purple
    			new Color(225, 52, 30), // red
    			new Color(106, 225, 30), // green
    			new Color(223, 205, 32), // yellow
    			new Color(223, 109, 32), // orange
    			new Color(36, 206, 219), // cyan
    			new Color(119, 136, 119) // gray
    	};
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
    
    public double eval(double prev, double x, double next) {
    	return treeRoot.eval(prev, x, next);
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
