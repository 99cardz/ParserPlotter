package canvas;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import gui.Function;
import gui.FunctionInput;

public class CanvasPlot extends Canvas {
	
//	final Parser parser = new Parser();
	
	// location of coordinate center on canvas
	private int centerOffsetX, centerOffsetY;
	private int centerX, centerY;
	
	// pixels per unit
	private double scaleX, scaleY; // pixels per unit
	static final int DEFAULT_SCALE = 40;
	
	// value indicator line spacing values and factor
	private double lineSpacingX, lineSpacingY;
	private double lineSpacingFactorX = 1, lineSpacingFactorY = 1;
	
	// formats to draw values
	private String valueFormatterX, valueFormatterY;
	
	// function list reference
	ArrayList<FunctionInput> functions;

	public CanvasPlot(ArrayList<FunctionInput> f) {
		functions = f;
		scale(0,0); // calculate linespacing and linespacingfactors
		resetOffset();
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
		while (pixelsPerUnitX * choices[iX] * lineSpacingFactorX < 40) {
			lineSpacingFactorX *= iX + 1 > 2 ? 10 : 1;
			iX = (iX + 1) % 3;
		}
		while (pixelsPerUnitX * choices[iX] * lineSpacingFactorX > 100) {
			lineSpacingFactorX /= iX - 1 < 0 ? 10 : 1;
			iX = (iX + 2) % 3;
		}
		lineSpacingX = choices[iX] * lineSpacingFactorX;
		valueFormatterX = "%." + decimalAmount(lineSpacingFactorX) + "f";
		
		int iY = 0;
		while (pixelsPerUnitY * choices[iY] * lineSpacingFactorY < 40) {
			lineSpacingFactorY *= iY + 1 > 2 ? 10 : 1;
			iY = (iY + 1) % 3;
		}
		while (pixelsPerUnitY * choices[iY] * lineSpacingFactorY > 100) {
			lineSpacingFactorY /= iY - 1 < 0 ? 10 : 1;
			iY = (iY + 2) % 3;
		}
		lineSpacingY = choices[iY] * lineSpacingFactorY;
		valueFormatterY = "%." + decimalAmount(lineSpacingFactorY) + "f";
		
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
		
		// paint axes
		g.setColor(Color.black);
		thickLine(g, 0, centerY, w, centerY); // x
		thickLine(g, centerX, 0, centerX, h); // y
		
		// paint graph
		double[] xValues = new double[w];
		for (int xCoord = 0; xCoord < w; xCoord++)
			xValues[xCoord] = toXValue(xCoord);
		for (FunctionInput f : functions) {
			g.setColor(f.getColor());
			double prevYValue = f.getFunction().eval(xValues[0]);
			int prevYCoord = toYCoord(prevYValue);
			for (int xCoord = 1; xCoord < w; xCoord++) {
				double currYValue = f.getFunction().eval(xValues[xCoord]);
				int currYCoord = toYCoord(currYValue);
				if (Double.isFinite(prevYValue) && Double.isFinite(currYValue) && Math.abs(prevYCoord - currYCoord) < h)
					thickLine(g, xCoord-1, prevYCoord, xCoord, currYCoord);
				prevYValue = currYValue;
				prevYCoord = currYCoord;
			}
		}	
	}
	// Methods to convert Values to Coordinates back and fourth.
	public int toXCoord(double value) { return (int) (centerX + value * scaleX); }
	public int toYCoord(double value) { return (int) (centerY - value * scaleY); }
	public double toXValue(int coord) { return ((double) (coord - centerX)) / scaleX; }
	public double toYValue(int coord) { return ((double) -(coord - centerY)) / scaleY; }
	
	private int decimalAmount(double factor) {
		int decimals = -(int) Math.ceil((Math.log10(lineSpacingFactorX)));
		return decimals < 0 ? 0 : decimals;
	}
	
	private void thickLine(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1-1, x2, y2-1);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x1+1, y1, x2+1, y2);
	}
}
