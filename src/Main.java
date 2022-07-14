import java.awt.Frame;

import canvas.CanvasPlot;

public class Main {
	
	final Frame window = new Frame("Funktionsplotter");
	
	Main() {
		window.add(new CanvasPlot(700, 700));
		window.setLayout(null);    
		window.setSize(700, 700);    
		window.setVisible(true); 
	}

	public static void main(String[] args) {
		
		Main programm = new Main();
	}

}
