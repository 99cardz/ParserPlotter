import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class CanvasPlot extends Canvas {

	CanvasPlot() {
		setBackground(Color.black);
		setSize(200,300);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(75, 75, 150, 75);
	}
}
