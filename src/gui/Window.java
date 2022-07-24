package gui;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import canvas.CanvasPlot;
import parser.Parser;
import parser.SyntaxException;

public class Window extends JFrame {
	
	private ArrayList<Function> functions		= new ArrayList<Function>();
	private Parser			parser				= new Parser();
	
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
	private CanvasPlot		canvas				= new CanvasPlot(functions);
	private JLabel			valueLable 			= new JLabel((""),  SwingConstants.CENTER);
	
	private JPanel 			bottomPanel 		= new JPanel();
	private double[]		currScale 			= {1., 1.};
	private JButton 		xZoomOutButton 		= new JButton("-");
	private JButton 		xZoomInButton 		= new JButton("+");
	private JButton 		yZoomOutButton 		= new JButton("-");
	private JButton 		yZoomInButton 		= new JButton("+");
	private JButton			resetButton 		= new JButton("Reset");
	
	public Window() {
		
		// just for testing
		try {
			functions.add(parser.buildFunction("sin(x)^10"));
			
		} catch (SyntaxException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getStartIndex());
			System.out.println(e1.getEndIndex());
			System.out.println(e1.getString());
			e1.printStackTrace();
		}
		
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
				canvas.scale(.9, .9);
			else if (e.getWheelRotation() > 0)
				canvas.scale(1.1, 1.1);
			valueLable.setText("x: " + canvas.toXValue(e.getX()) + " y: " + canvas.toYValue(e.getY()));
		});
		canvas.addMouseMotionListener(new MouseMotionListener() {
			private int beforeX, beforeY;
			@Override
			public void mouseDragged(MouseEvent e) {
				canvas.offsetPx(e.getX() - beforeX, e.getY() - beforeY);
				beforeX = e.getX();
				beforeY = e.getY();
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				beforeX = e.getX();
				beforeY = e.getY();
				valueLable.setText("x: " + canvas.toXValue(beforeX) + " y: " + canvas.toYValue(beforeY));
			}
		});
		canvasPanel.add(valueLable, BorderLayout.SOUTH);
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
		resetButton.addActionListener(e -> canvas.resetOffset());

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
	}
	
//	private void addToWindow(Component component, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, double weightx, double weighty) {
//		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, 0, 0);
//		this.add(component, gbc);
//	}
	
	private void addFunction() {
		try {
			functions.add(parser.buildFunction("cos(x)"));
		} catch (SyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		canvas.repaint();
	}
	
	private void addComponent(Container outer, Component inner, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, 0, 0);
		outer.add(inner, gbc);
	}
	
}