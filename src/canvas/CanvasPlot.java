package canvas;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class CanvasPlot extends Canvas {
	
//	final Parser parser = new Parser();
	
	private int centerX, centerY;
	private double scaleX, scaleY; // pixels per unit
	static final int DEFAULT_SCALE = 40;

	public CanvasPlot() {
		// center is in middle of canvas by default
		scaleX = DEFAULT_SCALE;
		scaleY = DEFAULT_SCALE;
	}
	public void size(int width, int height) {
		setSize(width, height);
		centerX = width / 2;
		centerY = height / 2;
		super.repaint();
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
		scaleX *= factorX != 0 ? factorX : 1;
//		scaleX = factorX != 0 ? scaleX * factorX : scaleX;
		scaleY *= factorY != 0 ? factorY : 1;
//		scaleY = factorY != 0 ? scaleY * factorY : scaleY;
		super.repaint();
	}
	/**
	 * Sets the internal scale factors to the default value.
	 * The Canvas will be redrawn!
	 */
	public void resetScale() {
		scaleX = DEFAULT_SCALE;
		scaleY = DEFAULT_SCALE;
		super.repaint();
	}
	/**
	 * Offset the coordinate system center by a provided amount of line Spacings.
	 * The Canvas will be redrawn!
	 * @param offsetX
	 * @param offsetY
	 */
	public void offset(int offsetX, int offsetY) {
//		centerX += offsetX * scaleX * determineLineSpacing(getWidth() / (toXValue(getWidth()) - toXValue(0)));
//		centerY += -offsetY * scaleY * determineLineSpacing(getHeight() / (toYValue(0) - toYValue(getHeight())));
		super.repaint();
	}
	/**
	 * Places the coordinate system center in the middle of the Canvas.
	 * The Canvas will be redrawn!
	 */
	public void resetOffset() {
		centerX = getWidth() / 2;
		centerY = getHeight() / 2;
		super.repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		int w = getWidth();
		int h = getHeight();
		
		// paint value indicator lines
		g.setColor(Color.lightGray);
		double minX = toXValue(0);
		double maxX = toXValue(w);
		int yTextCoord = centerY - 10;
		double[] lsX = determineLineSpacing(w / (maxX - minX));
		double lineSpaceX = lsX[0];
		double lineSpaceFactorX = lsX[1];
		for (double x = lineSpaceX; x < maxX; x += lineSpaceX) {
			int xCoord = toXCoord(x);
			System.out.println("raw: " + x / lineSpaceFactorX + " round: " + Math.round(x / lineSpaceFactorX) + " back: " + Math.round(x / lineSpaceFactorX) * lineSpaceFactorX);
			g.drawLine(xCoord, 0, xCoord, h);
			g.drawString(toString(Math.round(x / lineSpaceFactorX) * lineSpaceFactorX), xCoord, yTextCoord);
		}
		for (double x = -lineSpaceX; x > minX; x -= lineSpaceX) {
			int xCoord = toXCoord(x);
			g.drawLine(xCoord, 0, xCoord, h);
			g.drawString(toString(x), xCoord, yTextCoord);
		}
//		double minY = toYValue(h);
//		double maxY = toYValue(0);
//		double lineSpaceY = determineLineSpacing(h / (maxY - minY));
//		for (double y = lineSpaceY; y < maxY; y += lineSpaceY)
//			g.drawLine(0, toYCoord(y), w, toYCoord(y));
//		for (double y = -lineSpaceY; y > minY; y -= lineSpaceY)
//			g.drawLine(0, toYCoord(y), w, toYCoord(y));
		
		// paint axies
		g.setColor(Color.black);
		thickLine(g, 0, centerY, w, centerY); // x
		thickLine(g, centerX, 0, centerX, h); // y
		
		// paint graph
		g.setColor(Color.blue);
		double prevYValue = f(toXValue(0));
		int prevYCoord = toYCoord(prevYValue);
		for (int xCoord = 1; xCoord < w; xCoord++) {
			double currYValue = f(toXValue(xCoord));
			int currYCoord = toYCoord(currYValue);
			// ignore if Values are NaN or Infinity, or if distance of Coords is grater than the height 
			if (Double.isFinite(prevYValue) && Double.isFinite(currYValue) && Math.abs(prevYCoord - currYCoord) < h)
				thickLine(g, xCoord-1, prevYCoord, xCoord, currYCoord);
			prevYValue = currYValue;
			prevYCoord = currYCoord;
		}
	}
	// Methods to convert Values to Coordinates back and fourth.
	private int toXCoord(double value) { return (int) (centerX + value * scaleX); }
	private int toYCoord(double value) { return (int) (centerY - value * scaleY); }
	private double toXValue(int coord) { return ((double) (coord - centerX)) / scaleX; }
	private double toYValue(int coord) { return ((double) -(coord - centerY)) / scaleY; }
	private String toString(double a) { return String.valueOf(a); }
	
	private double[] determineLineSpacing(double pixelsPerUnit) {
		double[] choices = {1, 2, 5};
		int index = 0;
		double factor = 1;
		while (pixelsPerUnit * choices[index] * factor < 40) {
			factor *= index + 1 > 2 ? 10 : 1;
			index = (index + 1) % 3;
		}
		while (pixelsPerUnit * choices[index] * factor > 100) {
			factor /= index - 1 < 0 ? 10 : 1;
			index = (index + 2) % 3;
		}
		double[] ret = { choices[index] * factor, factor };
		return ret;
	}
	private double f(double x) {
		return 1/x;
	}
	
	private void thickLine(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1-1, x2, y2-1);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x1+1, y1, x2+1, y2);
	}
}
