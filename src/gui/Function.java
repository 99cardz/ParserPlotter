package gui;

import parser.Parser;
import parser.SyntaxException;
import parser.syntaxtree.SyntaxNode;
import java.awt.Color;

public class Function {

 	final private long UUID;
    private String expressionString;
    private SyntaxNode treeRoot;
    private Color color;
    private String error = "";
	    
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
    private static final Parser parser = new Parser();

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
        return colors[colorIndex++ % colors.length];
    }
    
    public Function(Color color) {
    	this.UUID = generateUUID();
    	this.expressionString = "";
    	try {
			this.treeRoot = parser.buildSyntaxTree("0");
		} catch (SyntaxException e) {
			error = "Error during initialization";
		}
    	this.color = color;
    }

    /**
     * Build a Function object.
     * @param expressionString - The parsed expression.
     * @param root - The root of the syntax tree that was parsed from the expression string.
     */
    public Function(String expressionString, SyntaxNode root) {
        this.UUID = generateUUID();
        this.expressionString = expressionString;
        this.treeRoot = root;
        this.color = Color.BLACK;
    }
    
    public double eval(double x) {
    	return treeRoot.eval(x);
    }

    public long getUUID() {
        return UUID;
    }
    public String getExpressionString() {
        return expressionString;
    }
    public SyntaxNode getTreeRoot() {
        return treeRoot;
    }
    
    public void update(String input) {
    	try {
			treeRoot = parser.buildSyntaxTree(input);
			expressionString = input;
			error = "";
		} catch (SyntaxException e) {
			error = "Syntax error at position " + e.getStartIndex();
		}
    }
    
    public String getError() {
    	return error;
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color c) {
    	color = c;
    }

}
