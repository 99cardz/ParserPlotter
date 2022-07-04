import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

public class CanvasPlot extends Canvas {
	
//	final Parser parser = new Parser();
	
	private int centerX, centerY;
	private int scaleX, scaleY; // pixels per unit
	
	static final int DEFAULT_SCALE = 40;

	CanvasPlot(int width, int height) {
		setBackground(Color.white);
		setSize(width, height);
		// center is in middle of canvas by default
		centerX = width / 2;
		centerY = height / 2;
		scaleX = DEFAULT_SCALE;
		scaleY = DEFAULT_SCALE;
	}
	public void scale(double factorX, double factorY) {
		scaleX = (int) (factorX != 0 ? scaleX * factorX : scaleX);
		scaleY = (int) (factorY != 0 ? scaleY * factorY : scaleY);
		invalidate();
	}
	public void resetScale() {
		scaleX = DEFAULT_SCALE;
		scaleY = DEFAULT_SCALE;
		invalidate();
	}
	public void offset(int offsetX, int offsetY) {
		centerX += scaleX * offsetX;
		centerY += scaleY * -offsetY;
		invalidate();
	}
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
		int maxX = (int) toXValue(w);
		for (int x = (int) toXValue(0); x <= maxX; x++)
			g.drawLine(toXCoord(x), 0, toXCoord(x), h);
		int maxY = (int) toYValue(0);
		for (int y = (int) toYValue(h); y <= maxY; y++)
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
	private int toXCoord(double x) { return (int) (centerX + x * scaleX); }
	private int toYCoord(double y) { 
		int coord = (int) (centerY - y * scaleY);
		if (coord < 0) return -3;
		else if (coord > getHeight()) return getHeight()+3;
		else return coord;
	}
	private double toXValue(int x) { return ((double) (x - centerX)) / scaleX; }
	private double toYValue(int y) { return ((double) -(y - centerY)) / scaleY; }
	private String toString(int a) { return String.valueOf(a); }
	
	// to be replaced by Parser.eval()
	private double f(double x) {
//		return Math.cos(x);
		return x*x*x - 3*x;
	}
	private void thickLine(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1-1, x2, y2-1);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x1+1, y1, x2+1, y2);
	}
}
