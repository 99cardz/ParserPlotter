package canvas;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import gui.Function;

public class CanvasPlot extends Canvas {
	
	// location of coordinate center on canvas
	private int centerOffsetX = 0, centerOffsetY = 0;
	private int centerX, centerY;
	
	// pixels per unit
	private double scaleX, scaleY;
	static final int DEFAULT_SCALE = 40;
	
	// value indicator line spacing values
	private double lineSpacingX = 1, lineSpacingY = 1;
	
	// formats to draw values
	private String valueFormatterX = "%.0f", valueFormatterY= "%.0f";
	
	// function list reference
	ArrayList<Function> functions;

	public CanvasPlot(ArrayList<Function> f) {
		functions = f;
		scaleX = DEFAULT_SCALE;
		scaleY = DEFAULT_SCALE;
	}
	/**
	 * Scales the coordinate system by the provided factors.
	 * 'Zooming in' would be a factor above 1 and 
	 * 'Zooming out' would be a factor below 1.
	 * A factor of 0 will will reset the scale to the default value.
	 * The Canvas will be redrawn!
	 * @param factorX
	 * @param factorY
	 */
	public void scale(double factorX, double factorY) {
		scaleX = factorX == 0 ? DEFAULT_SCALE : scaleX * factorX;
		scaleY = factorY == 0 ? DEFAULT_SCALE : scaleY * factorY;
		
		double pixelsPerUnitX = getWidth() / (toXValue(getWidth()) - toXValue(0));
		double pixelsPerUnitY = getHeight() / (toYValue(0) - toYValue(getHeight()));
		
		// recalculate lineSpacingFactor and lineSpacing of both x and y
		final double[] choices = {1, 2, 5};
		int iX = 0;
		factorX = 1;
		while (pixelsPerUnitX * choices[iX] * factorX < 40) {
			factorX *= iX + 1 > 2 ? 10 : 1;
			iX = (iX + 1) % 3;
		}
		while (pixelsPerUnitX * choices[iX] * factorX > 100) {
			factorX /= iX - 1 < 0 ? 10 : 1;
			iX = (iX + 2) % 3;
		}
		lineSpacingX = choices[iX] * factorX;
		valueFormatterX = "%." + decimalAmount(factorX) + "f";
		
		int iY = 0;
		factorY = 1;
		while (pixelsPerUnitY * choices[iY] * factorY < 40) {
			factorY *= iY + 1 > 2 ? 10 : 1;
			iY = (iY + 1) % 3;
		}
		while (pixelsPerUnitY * choices[iY] * factorY > 100) {
			factorY /= iY - 1 < 0 ? 10 : 1;
			iY = (iY + 2) % 3;
		}
		lineSpacingY = choices[iY] * factorY;
		valueFormatterY = "%." + decimalAmount(factorY) + "f";
		
		repaint();
	}
	/**
	 * Offset the coordinate system center by a provided amount of line Spacings.
	 * The Canvas will be redrawn!
	 * @param offsetX
	 * @param offsetY
	 */
	public void offset(int offsetX, int offsetY) {
		centerOffsetX += offsetX * scaleX * lineSpacingX;
		centerOffsetY += -offsetY * scaleY * lineSpacingY;
		repaint();
	}
	/**
	 * Offset the coordinate system center by a provided amount of pixels.
	 * The Canvas will be redrawn!
	 * @param x
	 * @param y
	 */
	public void offsetPx(int x, int y) {
		centerOffsetX += x;
		centerOffsetY += y;
		repaint();
	}
	/**
	 * Places the coordinate system center in the middle of the Canvas.
	 * The Canvas will be redrawn!
	 */
	public void resetOffset() {
		centerOffsetX = 0;
		centerOffsetY = 0;
		repaint();
	}
	
	public void paint(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		centerX = w / 2 + centerOffsetX;
		centerY = h / 2 + centerOffsetY;
		
		// paint value indicator lines
		double minX = toXValue(0);
		double maxX = toXValue(w);
		int yTextCoord = centerY < 0 || centerY > h ? h - 10 : centerY - 10;
		for (double x = lineSpacingX; x < maxX; x += lineSpacingX) {
			int xCoord = toXCoord(x);
			g.setColor(Color.lightGray);
			g.drawLine(xCoord, 0, xCoord, h);
			g.setColor(Color.black);
			g.drawString(String.format(valueFormatterX, x), xCoord, yTextCoord);
		}
		for (double x = -lineSpacingX; x > minX; x -= lineSpacingX) {
			int xCoord = toXCoord(x);
			g.setColor(Color.lightGray);
			g.drawLine(xCoord, 0, xCoord, h);
			g.setColor(Color.black);
			g.drawString(String.format(valueFormatterX, x), xCoord, yTextCoord);
		}
		double minY = toYValue(h);
		double maxY = toYValue(0);
		int xTextCoord = centerX < 0 || centerX > w ? 10 : centerX + 10;
		for (double y = lineSpacingY; y < maxY; y += lineSpacingY) {
			int yCoord = toYCoord(y);
			g.setColor(Color.lightGray);
			g.drawLine(0, yCoord, w, yCoord);
			g.setColor(Color.black);
			g.drawString(String.format(valueFormatterY, y), xTextCoord, yCoord);
		}
		for (double y = -lineSpacingY; y > minY; y -= lineSpacingY) {
			int yCoord = toYCoord(y);
			g.setColor(Color.lightGray);
			g.drawLine(0, yCoord, w, yCoord);
			g.setColor(Color.black);
			g.drawString(String.format(valueFormatterY, y), xTextCoord, yCoord);
		}
		
		// paint axies
		g.setColor(Color.black);
		drawAxies(g, 0, centerY, w, centerY); // x
		drawAxies(g, centerX, 0, centerX, h); // y
		
		// paint graph
		double[] xValues = new double[w];
		for (int xCoord = 0; xCoord < w; xCoord++)
			xValues[xCoord] = toXValue(xCoord);
		
		double stride = toXValue(centerX+1);
		
		for (Function f : functions) {
			g.setColor(f.getColor());
			double prevYValue = f.eval(xValues[0], stride);
			int prevYCoord = toYCoord(prevYValue);
			for (int xCoord = 1; xCoord < w; xCoord++) {
				double currYValue = f.eval(xValues[xCoord], stride);
				int currYCoord = toYCoord(currYValue);
				
				if ((Double.isFinite(currYValue) || Double.isFinite(prevYValue))
						&& !Double.isNaN(currYValue) && !Double.isNaN(prevYValue))
					thickLine(g, xCoord-1, prevYCoord, xCoord, currYCoord);
				
				prevYValue = currYValue;
				prevYCoord = currYCoord;
			}
		}
	}
	
	// Methods to convert Values to Coordinates back and fourth.
	public int toXCoord(double value) { return (int) (centerX + value * scaleX); }
	public int toYCoord(double value) { 
		int coord = (int) (centerY - value * scaleY);
		int max = getHeight();
		if (value == Double.NEGATIVE_INFINITY || coord > max)
			return max+100;
		if (value == Double.POSITIVE_INFINITY || coord < 0)
			return -100;
		return coord;
	}
	public double toXValue(int coord) { return ((double) (coord - centerX)) / scaleX; }
	public double toYValue(int coord) { return ((double) -(coord - centerY)) / scaleY; }
	
	/*
	 * Calculate the amount of decimals of a given factor.
	 * 
	 * For Example: 
	 * 100 -> 0 
	 * 1 -> 0
	 * 0.01 -> 2 
	 * 0.00001 -> 5
	 */
	private int decimalAmount(double factor) {
		return factor >= 1 ? 0 : -(int) Math.ceil((Math.log10(factor)));
	}
	
	private void drawAxies(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1-1, x2, y2-1);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x1+1, y1, x2+1, y2);
	}
	private void thickLine(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1-1, y1, x2-1, y2);
		g.drawLine(x1, y1-1, x2, y2-1);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x1+1, y1, x2+1, y2);
		g.drawLine(x1, y1+1, x2, y2+1);
	
	}
}
