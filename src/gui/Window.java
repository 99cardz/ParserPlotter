package gui;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import canvas.CanvasPlot;

public class Window extends JFrame {
	
	// layout
	GridBagLayout 			layout 				= new GridBagLayout();
	GridBagConstraints 		gbc 				= new GridBagConstraints();
	Insets 					insets 				= new Insets(5, 5, 5, 5);
	
	// beauty stuff
	private final String	title 				= "Funktionsplotter";
	private final Color 	BG_COLOR 			= Color.orange;
	private Font 			mathFont 			= new Font("Arial", Font.BOLD, 30);
	private Font 			genericFont 		= new Font("Verdana", Font.PLAIN, 30);
	
	// components
	private JPanel 			topPanel 			= new JPanel();
	private JLabel 			funcLabel 			= new JLabel("f(x) =");
	private JTextField 		textInput 			= new JTextField("");
	
	private JLabel 			errorLabel 			= new JLabel("Enter a function.", SwingConstants.CENTER);
	
	private JPanel 			canvasPanel 		= new JPanel();
	private CanvasPlot		canvas				= new CanvasPlot();
	private JLabel			scaleLabel 			= new JLabel(("x: 1.000 y: 1.000"),  SwingConstants.CENTER);
	
	private JPanel 			bottomPanel 		= new JPanel();
	private double[]		currScale 			= {1., 1.};
	private JButton 		xZoomOutButton 		= new JButton("-");
	private JButton 		xZoomInButton 		= new JButton("+");
	private JButton 		yZoomOutButton 		= new JButton("-");
	private JButton 		yZoomInButton 		= new JButton("+");
	private JButton			resetButton 		= new JButton("R");
	
	public Window() {
		
		// general stuff
		this.setTitle(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(new Dimension(400, 500));
		this.setMinimumSize(new Dimension(400, 500));
		this.setLayout(new BorderLayout());
		
		// topPanel setup
		topPanel.setPreferredSize(new Dimension(this.getWidth(), 100));
		topPanel.setBackground(BG_COLOR);
		topPanel.setLayout(new GridBagLayout());
		funcLabel.setFont(mathFont);
		textInput.setFont(genericFont);
		textInput.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				errorLabel.setText(!textInput.getText().isEmpty() ? "Parsing \"f(x) = " + textInput.getText() + "\"..." : "Enter a function.");
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				insertUpdate(e);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				insertUpdate(e);
			}
		});
		
		this.addComponent(topPanel, funcLabel, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.REMAINDER, .5, .5);
		this.addComponent(topPanel, textInput, 1, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, 10, .5);
		this.addComponent(topPanel, errorLabel, 0, 1, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.REMAINDER, .5, .5);
		
		// canvasPanel setup
		
		canvasPanel.setLayout(new BorderLayout());
		canvasPanel.add(canvas);
		canvasPanel.addMouseWheelListener(e -> {
			if(e.getWheelRotation() < 0)
				canvas.scale(.5, .5);
			else if (e.getWheelRotation() > 0)
				canvas.scale(2, 2);
		});
		canvasPanel.add(scaleLabel, BorderLayout.SOUTH);
		canvasPanel.setBackground(Color.white);
		
		// bottomPanel setup
		bottomPanel.setLayout(new GridLayout(1, 8));
		bottomPanel.setPreferredSize(new Dimension(this.getWidth(), 50));
		JButton[] buttons = { xZoomOutButton, xZoomInButton, yZoomOutButton, yZoomInButton, resetButton};
		double[][] scalars = {{2.0, 1.0}, {0.5, 1.0}, {1.0, 2.0}, {1.0, 0.5}, {0.0, 0.0}};
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].setMinimumSize(new Dimension(50,50));
			buttons[i].setMaximumSize(new Dimension(50,50));
			int j = i;
			buttons[i].addActionListener(e -> canvas.scale(scalars[j][0], scalars[j][1]));
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
		this.add(topPanel, BorderLayout.NORTH);
		this.add(canvasPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		canvas.size(getWidth(), getWidth());
	}
	
//	private void addToWindow(Component component, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, double weightx, double weighty) {
//		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, 0, 0);
//		this.add(component, gbc);
//	}
	
	private void addComponent(Container outer, Component inner, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, 0, 0);
		outer.add(inner, gbc);
	}
	
}