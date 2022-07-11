package gui;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;
import canvas.CanvasPlot;

public class Window extends JFrame {
	
	// layout
	GridBagLayout 			layout 				= new GridBagLayout();
	GridBagConstraints 		gbc 				= new GridBagConstraints();
	Insets 					insets 				= new Insets(5, 5, 5, 5);
	
	// beauty stuff
	private final String	title 				= "Funky";
	private final Color 	BG_COLOR 			= Color.orange;
	private Font 			mathFont 			= new Font("Arial", Font.BOLD, 30);
	private Font 			genericFont 		= new Font("Verdana", Font.PLAIN, 30);
	
	// components
	private JPanel 			topPanel 			= new JPanel();
	private JLabel 			funcLabel 			= new JLabel("f(x) =");
	private JTextField 		textInput 			= new JTextField("");
	private JButton			hideButton			= new JButton("Hide");
	
	private JLabel 			errorLabel 			= new JLabel("Error. Everything that could be wrong, is.");
	
	private JPanel 			canvasPanel 		= new JPanel();
	private CanvasPlot		canvas;
	
	private JPanel 			bottomPanel 		= new JPanel();
	private double[]		currScale 			= {1.000, 1.000};
	private JLabel			scaleLabel 			= new JLabel("x: " + currScale[1] + " y: " + currScale[1]);
	private JButton 		xZoomOutButton 		= new JButton("-");
	private JButton 		xZoomInButton 		= new JButton("+");
	private JButton 		yZoomOutButton 		= new JButton("-");
	private JButton 		yZoomInButton 		= new JButton("+");
	private JButton			resetButton 		= new JButton("R");
	
	public Window() {
		
		// general stuff
		this.setTitle(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(new Dimension(600, 800));
		this.setMinimumSize(new Dimension(600, 800));
		this.setLayout(new GridBagLayout());
		
		// topPanel setup
		topPanel.setBackground(Color.ORANGE);
		topPanel.setLayout(new GridBagLayout());
		funcLabel.setFont(mathFont);
		textInput.setFont(genericFont);
		textInput.setSize(this.getWidth(), 300);
		hideButton.setSize(50, 300);
		
		this.addComponent(topPanel, funcLabel, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.REMAINDER, .5, .5);
		this.addComponent(topPanel, textInput, 1, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, 10, .5);
		this.addComponent(topPanel, errorLabel, 0, 1, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.REMAINDER, .5, .5);
		this.addComponent(topPanel, hideButton, 2, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.REMAINDER, .5, .5);
		
		// canvasPanel setup
		canvas = new CanvasPlot(canvasPanel.getWidth(), canvasPanel.getHeight());
		canvasPanel.setLayout(new BorderLayout());
		canvasPanel.add(canvas);
		canvasPanel.addMouseWheelListener(e -> {
			if(e.getWheelRotation() < 0)
				setCanvasScale(.5, .5);
			else if (e.getWheelRotation() > 0)
				setCanvasScale(2, 2);
		});
		canvas.setBackground(Color.white);
		
		// bottomPanel setup
		bottomPanel.setLayout(new GridLayout(1, 8));
		bottomPanel.setPreferredSize(new Dimension(this.getWidth(), 500));
		JButton[] buttons = { xZoomOutButton, xZoomInButton, yZoomOutButton, yZoomInButton, resetButton};
		double[][] scalars = {{2.0, 1.0}, {0.5, 1.0}, {1.0, 2.0}, {1.0, 0.5}, {0.0, 0.0}};
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].setMinimumSize(new Dimension(50,50));
			buttons[i].setMaximumSize(new Dimension(50,50));
			int j = i;
			buttons[i].addActionListener(e -> setCanvasScale(scalars[j][0], scalars[j][1]));
		}

		bottomPanel.add(new JLabel("X:", SwingConstants.CENTER));
		bottomPanel.add(xZoomInButton);
		bottomPanel.add(xZoomOutButton);
		bottomPanel.add(new JLabel("Y:", SwingConstants.CENTER));
		bottomPanel.add(yZoomInButton);
		bottomPanel.add(yZoomOutButton);
		bottomPanel.add(new JPanel());
		bottomPanel.add(resetButton);
		
		// add to window
		this.addToWindow(topPanel, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 1, 1);
		this.addToWindow(canvasPanel, 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 1, 1);
		this.addToWindow(scaleLabel, 0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.REMAINDER, 1, 1);
		this.addToWindow(bottomPanel, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 1, 1);
	}
	
	private void addToWindow(Component component, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, 0, 0);
		this.add(component, gbc);
	}
	
	private void addComponent(Container outer, Component inner, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, 0, 0);
		outer.add(inner, gbc);
	}

	private void setCanvasScale(double x, double y) {
		
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
		scaleLabel.setText(String.format("X: %.3f Y: %.3f", currScale[0], currScale[1]));
		System.out.println("x: " + currScale[0] + " y: " + currScale[1]);
		this.repaint();
	}
	
}