import java.awt.Frame;

public class Main {
	
	public static void main(String[] args) {
		
		CanvasPlot plot = new CanvasPlot();
		Window window = new Window(plot);
		window.setVisible(true);
	}

}
