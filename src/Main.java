
import java.awt.Frame;
import java.awt.Canvas;
import gui.Window;
import viewModel.ViewModel;
import canvas.CanvasPlot;

public class Main {
	
	public static void main(String[] args) {
		ViewModel viewModel = new ViewModel();
		Window window = new Window(viewModel);
		window.setVisible(true); 
	}
}