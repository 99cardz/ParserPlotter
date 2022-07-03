import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;
import javax.swing.event.*;

public class Window extends JFrame {
	
	// beauty stuff
	private final Color 	BG_COLOR = Color.orange;
	private Font 			mathFont = new Font("Times New Roman", Font.BOLD, 25);
	private Font 			genericFont = new Font("Arial", Font.PLAIN, 25);
	
	// layout
	private final Dimension MIN_WINDOW_SIZE = new Dimension(600, 800);
	private final Dimension BUTTON_SIZE = new Dimension(50, 50);
	private final int 		TOP_HEIGHT = 60;
	private final int		ERR_HEIGHT = 40;
	private final int		BOT_HEIGHT = TOP_HEIGHT;
 	
	// components
	private JPanel 			topPanel = new JPanel();
	private JLabel 			funcLabel = new JLabel("f(x) =");
	private JTextField 		textInput = new JTextField("");
	
	private JPanel			errorPanel = new JPanel();
	private JLabel 			errorLabel = new JLabel("Error.");
	
	private JPanel 			canvasPanel = new JPanel();
	private Canvas 			canvas;
	
	private JPanel 			bottomPanel = new JPanel();
	private double[]		currScale = {1.0, 1.0};
	private JLabel			scaleLabel = new JLabel("x: " + currScale[1] + " y: " + currScale[1]);
	private JButton 		xZoomOutButton = new JButton("-");
	private JButton 		xZoomInButton = new JButton("+");
	private JButton 		yZoomOutButton = new JButton("-");
	private JButton 		yZoomInButton = new JButton("+");
	private JButton			resetButton = new JButton("R");
	
	
	Window(Canvas c) {
		
		this.canvas = c;
		
		// window setup
		this.setSize(MIN_WINDOW_SIZE);
		this.setTitle("Funky");
		this.setLayout(new BorderLayout());
		this.setResizable(true);
		this.setMinimumSize(MIN_WINDOW_SIZE);
		
		// upper panel setup
		topPanel.setLayout(null);
		topPanel.setBounds(0, 0, this.getWidth(), TOP_HEIGHT);
		topPanel.setBackground(BG_COLOR);
		funcLabel.setBounds(20, 20, 70, 20);
		textInput.setBounds(funcLabel.getX() + funcLabel.getWidth(), funcLabel.getY() - 15, 200, 50);
		errorLabel.setBounds(400, 100, 20, 50);
		funcLabel.setFont(mathFont);
		textInput.setFont(genericFont);
		textInput.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				  System.out.println("Veränderung");
			  }
			  public void removeUpdate(DocumentEvent e) {
				  changedUpdate(e);
			  }
			  
			  public void insertUpdate(DocumentEvent e) {
				  changedUpdate(e);
			  }
		});
		scaleLabel.setBounds(200, 20, 20, 60);
		scaleLabel.setBackground(Color.black);
		topPanel.add(textInput);
		topPanel.add(funcLabel);
		topPanel.add(scaleLabel);
		
		// error panel setup
		errorPanel.setLayout(null);
		errorPanel.setBackground(Color.LIGHT_GRAY);
		errorPanel.add(errorLabel);
		
		// canvas panel setup
		canvasPanel.setLayout(null);
//		canvasPanel.setBounds(0, TOP_HEIGHT + ERR_HEIGHT, this.getWidth(), this.getHeight() - TOP_HEIGHT - ERR_HEIGHT - BOT_HEIGHT);
		canvasPanel.setBackground(Color.orange);
		canvasPanel.add(canvas);
		
		// bottom panel setup
		bottomPanel.setBackground(Color.blue);
		JButton[] buttons = {xZoomOutButton, xZoomInButton, yZoomOutButton, yZoomInButton, resetButton};
		double[][] scalars = {{10.0, 1.0}, {0.1, 1.0}, {1.0, 10.0}, {1.0, 0.1}, {0.0, 0.0}};
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].setBounds(20 + (i*(BUTTON_SIZE.width + 20)), bottomPanel.getY() + 20, BUTTON_SIZE.width, BUTTON_SIZE.height);
			final int j = i;
			buttons[i].addActionListener(e -> updateScale(scalars[j][0], scalars[j][1]));
			bottomPanel.add(buttons[i]);
		}
		
		// final adds
		this.updateBounds();
		this.add(topPanel);
		this.add(canvasPanel);
		this.add(bottomPanel);
		this.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		        updateBounds();
		    }
		});
	}
	
	private void updateScale(double x, double y) {
		
		if(x == 0 && y == 0) {
			currScale[0] = 1.0;
			currScale[1] = 1.0;
		}
		else {
			currScale[0] *= x;
			currScale[1] *= y;
		}
		// noch zu implementieren
		// canvas.setScale(x, y);
		scaleLabel.setText("x: " + currScale[1] + " y: " + currScale[1]);
		System.out.println("x: " + currScale[1] + " y: " + currScale[1]);
		this.repaint();
	}
	
	private void updateBounds() {
		topPanel.setBounds		(0, 0, 
								this.getWidth(), TOP_HEIGHT);
		errorPanel.setBounds	(0, TOP_HEIGHT,
								this.getWidth(), ERR_HEIGHT);
		canvasPanel.setBounds	(0, TOP_HEIGHT + ERR_HEIGHT,
								this.getWidth(), this.getHeight() - TOP_HEIGHT - ERR_HEIGHT - BOT_HEIGHT);
		bottomPanel.setBounds	(0, this.getHeight() - BOT_HEIGHT,
								this.getWidth(), BOT_HEIGHT);
		repaint();
	}
}
