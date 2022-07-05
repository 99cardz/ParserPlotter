import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

public class CanvasPlot extends Canvas {
	
//	final Parser parser = new Parser();
	
	private int centerX, centerY;
	private double scaleX, scaleY; // pixels per unit
	static final int DEFAULT_SCALE = 40;

	CanvasPlot(int width, int height) {
		setBackground(Color.white);
		setSize(width, height);
		// center is in middle of canvas by default
		centerX = width / 2;
		centerY = height / 2;
		scaleX = DEFAULT_SCALE;
		scaleY = DEFAULT_SCALE;
//		scale(.01, .01);
	}
	/**
	 * Scales the coordinate system by the provided factors.
	 * 'Zooming in' would be a factor above 1 and 
	 * 'Zooming out' would be a factor below 1.
	 * A factor of 0 will get ignored.
	 * The Canvas will be redrawn!
	 * @param factorX
	 * @param factorY
	 */
	public void scale(double factorX, double factorY) {
		scaleX = factorX != 0 ? scaleX * factorX : scaleX;
		scaleY = factorY != 0 ? scaleY * factorY : scaleY;
		invalidate();
	}
	/**
	 * Sets the internal scale factors to the default value.
	 * The Canvas will be redrawn!
	 */
	public void resetScale() {
		scaleX = DEFAULT_SCALE;
		scaleY = DEFAULT_SCALE;
		invalidate();
	}
	/**
	 * Offset the coordinate system center by a provided amount of units.
	 * The Canvas will be redrawn!
	 * @param offsetX
	 * @param offsetY
	 */
	public void offset(int offsetX, int offsetY) {
		centerX += scaleX * offsetX;
		centerY += scaleY * -offsetY;
		invalidate();
	}
	/**
	 * Places the coordinate system center in the middle of the Canvas.
	 * The Canvas will be redrawn!
	 */
	public void resetOffset() {
		centerX = getWidth() / 2;
		centerY = getHeight() / 2;
		invalidate();
		
	}
	
	public void paint(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		
		// paint value indicator lines
		g.setColor(Color.lightGray);
		double minX = toXValue(0);
		double maxX = toXValue(w);
		double lineSpaceX = determineLineSpace(maxX - minX, w);
		for (double x = lineSpaceX; x < maxX; x += lineSpaceX)
			g.drawLine(toXCoord(x), 0, toXCoord(x), h);
		for (double x = -lineSpaceX; x > minX; x -= lineSpaceX)
			g.drawLine(toXCoord(x), 0, toXCoord(x), h);
		
		double minY = toYValue(h);
		double maxY = toYValue(0);
		double lineSpaceY = determineLineSpace(maxY - minY, h);
		for (double y = lineSpaceY; y < maxY; y += lineSpaceY)
			g.drawLine(0, toYCoord(y), w, toYCoord(y));
		for (double y = -lineSpaceY; y > minY; y -= lineSpaceY)
			g.drawLine(0, toYCoord(y), w, toYCoord(y));
		
		// paint axies
		g.setColor(Color.black);
		thickLine(g, 0, centerY, w, centerY); // x
		thickLine(g, centerX, 0, centerX, h); // y
		
		// paint graph
		g.setColor(Color.blue);
		int prevY = toYCoord(f(toXValue(0)));
		for (int xCoord = 1; xCoord < w; xCoord++) {
			int currY = toYCoord(f(toXValue(xCoord)));
			thickLine(g, xCoord-1, prevY, xCoord, currY);
			prevY = currY;
		}
	}
	// Methods to convert Values to Coorinates back and fourth.
	private int toXCoord(double value) { return (int) (centerX + value * scaleX); }
	private int toYCoord(double value) { 
		int coord = (int) (centerY - value * scaleY);
		int max = getHeight();
		return coord < 0 ? -3 : coord > max ? max+3 : coord;
	}
	private double toXValue(int coord) { return ((double) (coord - centerX)) / scaleX; }
	private double toYValue(int coord) { return ((double) -(coord - centerY)) / scaleY; }
//	private String toString(int a) { return String.valueOf(a); }
	
	final static double[] lineSpaces = { 0.001, 0.002, 0.005, 0.01, 0.02, 0.05, 0.1, 0.2, 0.5, 1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000 };
	private double determineLineSpace(double valueRange, int pixels) {
		for (double lineSpace : lineSpaces)
			if (pixels / valueRange * lineSpace > 35) // each line only spans aprox 35 px
				return lineSpace;
		return 100000;
	}
	
	// to be replaced by Parser.eval()
	private double f(double x) {
		return x*x;
	}
	private void thickLine(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1-1, x2, y2-1);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x1+1, y1, x2+1, y2);
	}
}
