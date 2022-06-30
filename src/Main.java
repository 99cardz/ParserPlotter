import java.awt.Frame;

public class Main {
	
	final Frame window = new Frame("Funktionsplotter");
	
	Main() {
		window.add(new CanvasPlot());
		window.setLayout(null);    
		window.setSize(400, 400);    
		window.setVisible(true); 
	}

	public static void main(String[] args) {
		
		Main programm = new Main();
	}

}
