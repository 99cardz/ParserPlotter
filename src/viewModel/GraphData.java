package viewModel;

import java.awt.Color;

import parser.syntaxtree.SyntaxNode;

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
		if (root != null)
			yValues = root.evalAll(xValues);
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