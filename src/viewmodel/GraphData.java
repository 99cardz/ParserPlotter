package viewmodel;

import java.awt.Color;

import parser.syntaxtree.SyntaxNode;

/**
 * Graph Data storage object
 *
 * Stores the color, root of the function and yValues associated
 * with a Expression in a FunctionInput View.
 *
 */
public class GraphData {

	private SyntaxNode root;
	private double[] yValues;
	private Color color;

	GraphData(Color c) {
		color = c;
	}

	public double[] getYValues() {
		return yValues;
	}
	public void calculateYValues(double[] xValues) {
		yValues = root == null ? null : root.evalAll(xValues);
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color c) {
		color = c;
	}
	public void setRoot(SyntaxNode r) {
		root = r;
	}
}