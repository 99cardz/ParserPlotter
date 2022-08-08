package viewmodel;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import parser.Parser;
import parser.SyntaxException;

/**
 * ViewModel Singleton Class
 *
 * Storage Object used to maintain the programs state.
 * Obtain a instance by calling ViewModel.getInstance()
 */
public class ViewModel {

	// Singleton Stuff
	private static final ViewModel INSTANCE = new ViewModel();
	private ViewModel() {}
	public static ViewModel getInstance() {
		return INSTANCE;
	}

	private Parser parser = new Parser();

	/**
	 * x Values are the same for each Graph.
	 * They can be changed with the
	 * updateXValues Method
	 */
	private double[] xValues;

	/**
	 * This HashMap contains a GraphData Object for each
	 * function the user chooses to view on the canvas.
	 * Public methods to add, update and delete are below.
	 */
	private HashMap<UUID, GraphData> graphs = new HashMap<>();
	
	/**
	 * Setting value
	 */
	private boolean fixedPointZoom = true;

	private boolean getFixedPointZoomSetting() {
		return fixedPointZoom;
	}
	
	private void setFixedPointZoomSetting(boolean b) {
		fixedPointZoom = b;
	}
	/**
	 * Retrieve the collection of Graph Data
	 * @return all Graph Data
	 */
	public Collection<GraphData> getGraphData() {
		return graphs.values();
	}

	/**
	 * Update x Value array. This triggers a recalculation of
	 * all graphs y Values.
	 * @param x Values
	 */
	public void updateXValues(double[] v) {
		xValues = v;
		for (GraphData graph : graphs.values()) {
			graph.calculateYValues(xValues);
		}
	}

	/**
	 * Adds a new Graph Data Object to the ViewModel with a
	 * initial color.
	 * @param color
	 * @return the ID of the new GraphData Object for future reference.
	 */
	public UUID addFunction(Color c) {
		UUID id = UUID.randomUUID();
		graphs.put(id, new GraphData(c));
		return id;
	}

	/**
	 * Update the GraphData Object with the provided id.
	 * This will parse the expression and recalculate the
	 * y Values of the new Function. Returns null if the
	 * expression was parsed without a SyntaxException or
	 * a Error String if a Exception was caught.
	 * @param id
	 * @param exprpression
	 * @return null or Error string
	 */
	public String updateFunctionExpression(UUID id, String expr) {
		GraphData graph = graphs.get(id);
		if (graph == null) return null;
		try {
			graph.setRoot(expr.isBlank() ? null : parser.buildSyntaxTree(expr));
			if (hasXValues())
				graph.calculateYValues(xValues);
			return null;
		} catch (SyntaxException e) {
			if (e.getStartIndex() == -1)
				return "Unexpected end of Expression";
			return "Syntax error at position " + e.getStartIndex() + " with Symbol " + e.getString();
		}
	}

	public void updateFunctionColor(UUID id, Color c) {
		GraphData graph = graphs.get(id);
		if (graph == null) return;
		graph.setColor(c);
	}


	public void deleteFunction(UUID id) {
		graphs.remove(id);
	}

	public boolean hasXValues() {
		return xValues != null;
	}
}
