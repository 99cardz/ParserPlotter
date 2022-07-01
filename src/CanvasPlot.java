import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class CanvasPlot extends Canvas {
	
//	final Parser parser = new Parser();
	
	private int centerX, centerY;
	private int scaleX = 100, scaleY = 40; // scale pixels equal one unit
	private int accuracy = 100; // the higher the more accurate and expensive to compute

	CanvasPlot(int widht, int height) {
		setBackground(Color.white);
		setSize(widht,height);
		// center is in middle of canvas by default
		centerX = widht / 2;
		centerY = height / 2;
	}
	
	public void paint(Graphics g) {
		Dimension dims = getSize();
		
		// smallest and largest visible x value
		double minX = -(centerX / scaleX); 
		double maxX = (dims.width - centerX) / scaleX;
		double rangeX = maxX - minX;
		// smallest and largest visible y value
		double minY = -(centerY / scaleY); 
		double maxY = (dims.height - centerY) / scaleY;
		double rangeY = maxY - minY;
		
		// paint value indicators
		g.setColor(Color.lightGray);
		for (int x = (int) minX; x <= (int) maxX; x++) 
			g.drawLine(toXCoord(x), 0, toXCoord(x), dims.height);
		for (int y = (int) minY; y <= (int) maxY; y++)
			g.drawLine(0, toYCoord(y), dims.width, toYCoord(y));
		
		// paint axies
		g.setColor(Color.black);
		g.drawLine(0, centerY, dims.width, centerY); // x
		g.drawLine(centerX, 0, centerX, dims.height); // y
		
		// calculate coords for graph
		int[] xCoords = new int[accuracy];
		int[] yCoords = new int[accuracy];
		double stepX = rangeX / (accuracy-1);
		for (int i = 0; i < accuracy; i++) {
			double xValue = (minX + stepX * i);
			xCoords[i] = toXCoord(xValue);
			yCoords[i] = toYCoord(f(xValue));
		}
		
		// paint graph
		g.setColor(Color.blue);
		for (int i = 1; i < accuracy; i++)
			g.drawLine(xCoords[i-1], yCoords[i-1], xCoords[i], yCoords[i]);
	}
	
	private int toXCoord(int x) { return centerX + x * scaleX; }
	private int toYCoord(int y) { return centerY - y * scaleY; }
	private int toXCoord(double x) { return (int) (centerX + x * scaleX); }
	private int toYCoord(double y) { return (int) (centerY - y * scaleY); }
	
	// to be replaced by Parser.eval()
	private double f(double x) {
		return x*x*x;
	}
}
