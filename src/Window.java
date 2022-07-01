import java.awt.*;
import javax.swing.*;

public class Window extends JFrame {
	
	final Color bgColor = Color.orange;
	
	JPanel upperPanel = new JPanel();
	JLabel funcLabel = new JLabel("f(x) =");
	JTextField textInput = new JTextField("");
	JButton quitButton = new JButton("Button");
	JLabel errorLabel = new JLabel("Error.");
	
	JPanel canvasPanel = new JPanel();
	Canvas canvas;
	
	JPanel lowerPanel = new JPanel();
	JSlider xSlider = new JSlider();
	JSlider ySlider = new JSlider();
	
	Font mathFont = new Font("Times New Roman", Font.BOLD, 25);
	Font genericFont = new Font("Arial", Font.PLAIN, 25);
	
	Window(Canvas c) {
		
		this.canvas = c;
		
		// window setup
		this.setSize(600, 400);
		this.setTitle("Funky Funky");
		this.setLayout(new BorderLayout());
		
		// upper panel setup
		upperPanel.setLayout(null);
		upperPanel.setBounds(0, 0, this.getWidth(), 70);
		upperPanel.setBackground(bgColor);
		upperPanel.add(textInput);
		upperPanel.add(errorLabel);
		upperPanel.add(funcLabel);
		funcLabel.setBounds(20, 20, 70, 20);
		textInput.setBounds(funcLabel.getX() + funcLabel.getWidth(), funcLabel.getY() - 15, 200, 50);
		errorLabel.setBounds(400, 100, 20, 50);
		funcLabel.setFont(mathFont);
		textInput.setFont(genericFont);
		
		// canvas panel
		canvasPanel.setLayout(null);
		canvasPanel.setBounds(0, upperPanel.getX() + upperPanel.getHeight(), this.getWidth(), this.getHeight() - 150);
		canvasPanel.setBackground(Color.white);
		canvasPanel.add(canvas);
		
		// lower panel setup
		lowerPanel.setLayout(null);
		lowerPanel.setBounds(0, canvasPanel.getX() + canvasPanel.getHeight(), this.getWidth(), this.getHeight() - 200);
		lowerPanel.setBackground(bgColor);
		xSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
		xSlider.setBounds(50, lowerPanel.getY() + 50, 100, 20);
		lowerPanel.add(xSlider);
		
		// final adds
		this.add(upperPanel);
		this.add(canvasPanel);
		this.add(lowerPanel);
	}
}
