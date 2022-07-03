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
	
	public void paint(Graphics g) {
		Dimension dims = getSize();
		
		// smallest and largest visible x and y value of coordinate system
		int minX = -(centerX / scaleX); 
		int maxX = (dims.width - centerX) / scaleX;
		int minY = -(dims.height - centerY) / scaleY; 
		int maxY = centerY / scaleY;
		
		// paint value indicator lines
		g.setColor(Color.lightGray);
		for (int x = minX; x <= maxX; x++) {
			g.drawLine(toXCoord(x), 0, toXCoord(x), dims.height);
//			String str = toString(x);
//			g.drawString(str, toXCoord(x) - str.length() * 4, centerY - 10);
		}
		for (int y = minY; y <= maxY; y++)
			g.drawLine(0, toYCoord(y), dims.width, toYCoord(y));
		
		// paint axies
		g.setColor(Color.black);
		thickLine(g, 0, centerY, dims.width, centerY); // x
		thickLine(g, centerX, 0, centerX, dims.height); // y
		
		// paint value indicator numbers
		
		
		// get lowest and highest visible x value of Graph
		double minXVisible = minX;
		while(minXVisible < maxX && (f(minXVisible) < minY || f(minXVisible) > maxY))
			minXVisible += 0.2;
		double maxXVisible = maxX;
		while(maxXVisible > minX && (f(maxXVisible) < minY || f(maxXVisible) > maxY))
			maxXVisible -= 0.2;
		
		// give some space
		minXVisible = Math.floor(minXVisible) - 1; 
		maxXVisible = Math.ceil(maxXVisible) + 1; 
		double visibleXRange = maxXVisible - minXVisible;
		if (visibleXRange <= 0) 
			return;
		
		// determine accuracy: every fourth px gets a point, but not above 1000
		int pointCount = (int) (visibleXRange * scaleX/4);
		int accuracy = pointCount > 1000 ? 1000 : pointCount;
		
		
		// calculate coords for graph
		int[] xCoords = new int[accuracy];
		int[] yCoords = new int[accuracy];
		double stepX = visibleXRange / accuracy;
		System.out.println(stepX);
		for (int i = 0; i < accuracy; i++) {
			double xValue = minXVisible + stepX * i;
			xCoords[i] = toXCoord(xValue);
			yCoords[i] = toYCoord(f(xValue));
			System.out.println(xValue);
		}
		
		// paint graph
		g.setColor(Color.blue);
		for (int i = 1; i < accuracy; i++)
			thickLine(g, xCoords[i-1], yCoords[i-1], xCoords[i], yCoords[i]);
	}
	private int toXCoord(double x) { return (int) (centerX + x * scaleX); }
	private int toYCoord(double y) { return (int) (centerY - y * scaleY); }
	private String toString(int a) { return "" + a; }
	
	// to be replaced by Parser.eval()
	private double f(double x) {
//		return Math.cos(x);
		return 0.1*x*x-4;
	}
	private void thickLine(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1-1, x2, y2-1);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x1+1, y1, x2+1, y2);
	}
}
