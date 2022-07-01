import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class CanvasPlot extends Canvas {
	
//	final Parser parser = new Parser();
	
	private int centerX, centerY;
	private int scaleX = 80, scaleY = 80; // pixels per unit
	private int accuracy = 50; // the higher the more accurate and expensive to compute

	CanvasPlot(int widht, int height) {
		setBackground(Color.white);
		setSize(widht,height);
		// center is in middle of canvas by default
		centerX = widht / 2;
		centerY = height / 2;
	}
	
	public void paint(Graphics g) {
		Dimension dims = getSize();
		
		// smallest and largest visible x value of coordinate system
		int minX = -(centerX / scaleX); 
		int maxX = (dims.width - centerX) / scaleX;
		// smallest and largest visible y value of coordinate system
		int minY = -(dims.height - centerY) / scaleY ; 
		int maxY = centerY / scaleY;
		
		// paint value indicators
		g.setColor(Color.lightGray);
		for (int x = minX; x <= maxX; x++) 
			g.drawLine(toXCoord(x), 0, toXCoord(x), dims.height);
		for (int y = minY; y <= maxY; y++)
			g.drawLine(0, toYCoord(y), dims.width, toYCoord(y));
		
		// paint axies
		g.setColor(Color.black);
		g.drawLine(0, centerY, dims.width, centerY); // x
		g.drawLine(centerX, 0, centerX, dims.height); // y
		
		// get lowest and highest visible x value of Graph
		double minXVisible = minX;
		while(f(minXVisible) < minY || f(minXVisible) > maxY)
			minXVisible += 0.1;
		double maxXVisible = maxX;
		while(f(maxXVisible) < minY || f(maxXVisible) > maxY)
			maxXVisible -= 0.1;
		minXVisible -= 0.5; maxXVisible += 0.5; // give some space
		
		// calculate coords for graph
		int[] xCoords = new int[accuracy];
		int[] yCoords = new int[accuracy];
		double stepX = (maxXVisible - minXVisible) / accuracy;
		for (int i = 0; i < accuracy; i++) {
			double xValue = (minXVisible + stepX * i);
			xCoords[i] = toXCoord(xValue);
			yCoords[i] = toYCoord(f(xValue));
		}
		
		// paint graph
		g.setColor(Color.blue);
		for (int i = 1; i < accuracy; i++)
			g.drawLine(xCoords[i-1], yCoords[i-1], xCoords[i], yCoords[i]);
	}
	private int toXCoord(double x) { return (int) (centerX + x * scaleX); }
	private int toYCoord(double y) { return (int) (centerY - y * scaleY); }
	
	// to be replaced by Parser.eval()
	private double f(double x) {
//		return Math.cos(x);
		return -4*x*x*x - 2*x*x - 2;
	}
}
