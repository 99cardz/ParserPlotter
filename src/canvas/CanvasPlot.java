package canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import viewModel.GraphData;
import viewModel.ViewModel;

public class CanvasPlot extends JPanel {
	
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
	
	// viewModel reference
	ViewModel viewModel = ViewModel.getInstance();
	

	public CanvasPlot() {
		scaleX = DEFAULT_SCALE;
		scaleY = DEFAULT_SCALE;
		setBackground(Color.white);
	}
	
	/**
	 * Scales the coordinate system around a provided point.
	 * Zoom in with a factor above 1 and 
	 * zoom out with a factor below 1.
	 * A factor of 0 will will reset the scale to the default value.
	 * The Canvas will be redrawn!
	 * @param factorX
	 * @param factorY
	 * @param x coordinate of the point to scale around 
	 * @param y coordinate of the point to scale around 
	 */
	public void scale(double factorX, double factorY, int scaleCenterX, int scaleCenterY) { 
		centerOffsetX += (1.0 - factorX) * (scaleCenterX - centerX);
		centerOffsetY += (1.0 - factorY) * (scaleCenterY - centerY);
		scale(factorX, factorY);
	}
	/**
	 * Scales the coordinate system around the coordinate center.
	 * Zoom in with a factor above 1 and 
	 * zoom out with a factor below 1.
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
		
		updateXValues();
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
		updateXValues();
		repaint();
	}
	/**
	 * Places the coordinate system center in the middle of the Canvas.
	 * The Canvas will be redrawn!
	 */
	public void resetOffset() {
		centerOffsetX = 0;
		centerOffsetY = 0;
		updateXValues();
		repaint();
	}
	
	public void paint(Graphics g) {
		
		super.paint(g);
		
		if (!viewModel.hasXValues())
			updateXValues();
		
		int w = getWidth();
		int h = getHeight();
		
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
		
		// paint graphs
		for (GraphData gd : viewModel.getGraphData()) {
			
			g.setColor(gd.getColor());
			double[] yValues = gd.getYValues();
			if (yValues == null) continue;
			
			int prevYCoord = toYCoord(yValues[0]);
			
			for (int xCoord = 1; xCoord < w; xCoord++) {
				int currYCoord = toYCoord(yValues[xCoord]);
				
				if ((Double.isFinite(yValues[xCoord]) || Double.isFinite(yValues[xCoord-1]))
						&& !Double.isNaN(yValues[xCoord]) && !Double.isNaN(yValues[xCoord-1]))
					thickLine(g, xCoord-1, prevYCoord, xCoord, currYCoord);
				
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
	
	public void updateXValues() {
		int w = getWidth();
		int h = getHeight();
		centerX = w / 2 + centerOffsetX;
		centerY = h / 2 + centerOffsetY;
		
		double[] xValues = new double[w];
		for (int xCoord = 0; xCoord < w; xCoord++)
			xValues[xCoord] = toXValue(xCoord);
		
		viewModel.updateXValues(xValues);
	}
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
		return factor >= 1 ? 0 : -(int) Math.ceil(Math.log10(factor));
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
