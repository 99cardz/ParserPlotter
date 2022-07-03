import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

public class CanvasPlot extends Canvas {
	
//	final Parser parser = new Parser();
	
	private int centerX, centerY;
	private int scaleX = 40, scaleY = 40; // pixels per unit

	CanvasPlot(int width, int height) {
		setBackground(Color.white);
		setSize(width,height);
		// center is in middle of canvas by default
		centerX = width / 2;
		centerY = height / 2;
	}
	public void setScale(int factorX, int factorY) {
		scaleX = factorX != 0 ? scaleX * factorY : scaleX;
		scaleY = factorY != 0 ? scaleY * factorY : scaleY;
		
		invalidate();
	}
	
	public void paint(Graphics g) {
		Dimension dims = getSize();
		
		// smallest and largest visible x and y value of coordinate system
		double minX = -((double) centerX / scaleX); 
		double maxX = ((double) (dims.width - centerX)) / scaleX;
		double minY = (-(double) (dims.height - centerY)) / scaleY; 
		double maxY = ((double) centerY) / scaleY;
		
//		System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY);
		
		// paint value indicator lines
		g.setColor(Color.lightGray);
		for (double x = Math.ceil(minX); x < maxX; x++) {
			g.drawLine(toXCoord(x), 0, toXCoord(x), dims.height);
//			String str = toString(x);
//			g.drawString(str, toXCoord(x) - str.length() * 4, centerY - 10);
		}
		for (double y = Math.ceil(minY); y < maxY; y++)
			g.drawLine(0, toYCoord(y), dims.width, toYCoord(y));
		
		// paint axies
		g.setColor(Color.black);
		thickLine(g, 0, centerY, dims.width, centerY); // x
		thickLine(g, centerX, 0, centerX, dims.height); // y
		
		// calculate coords for graph
		int[] yCoords = new int[dims.width];
		for (int xCoord = 0; xCoord < dims.width; xCoord++)
			yCoords[xCoord] = toYCoord(f(toXValue(xCoord)));
		
		// paint graph
		g.setColor(Color.blue);
		for (int xCoord = 1; xCoord < dims.width; xCoord++) 
			if (Math.abs(yCoords[xCoord-1] - yCoords[xCoord]) < dims.height)
				thickLine(g, xCoord-1, yCoords[xCoord-1], xCoord, yCoords[xCoord]);
	}
	private int toXCoord(double x) { return (int) (centerX + x * scaleX); }
	private int toYCoord(double y) { return (int) (centerY - y * scaleY); }
	private double toXValue(int x) { return ((double) (x - centerX)) / scaleX; }
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
