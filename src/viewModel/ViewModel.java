package viewModel;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import parser.Parser;
import parser.SyntaxException;

public class ViewModel {
	private static Parser parser = new Parser();
	private double[] xValues;
	private HashMap<UUID, GraphData> graphs = new HashMap<UUID, GraphData>();
	
	public Collection<GraphData> getGraphData() {
		return graphs.values();
	}
	public void updateXValues(double[] v) {
		xValues = v;
		for (GraphData graph : graphs.values()) {
			graph.calculateYValues(xValues);
		}
	}
	public UUID addFunction(Color c) {
		UUID id = UUID.randomUUID();
		graphs.put(id, new GraphData(c));
		return id;
	}
	public String updateFunctionExpression(UUID id, String expr) {
		GraphData graph = graphs.get(id);
		try {
			graph.setRoot(parser.buildSyntaxTree(expr));
			if (hasXValues())
				graph.calculateYValues(xValues);
			return null;
		} catch (SyntaxException e) {
			if (e.getStartIndex() == -1) 
				return "Unexpected end of Expression";
			return "Syntax error at position " + e.getStartIndex() + " with Symbol " + e.getString();
		}
	}
	public void deleteFunction(UUID id) {
		graphs.remove(id);
	}
	public boolean hasXValues() {
		return xValues != null;
	}
}
