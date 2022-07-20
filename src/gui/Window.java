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
import gui.LabeledInput;
import parser.Parser;
import parser.SyntaxException;

public class Window extends JFrame {
	
	private ArrayList<Function> functions		= new ArrayList<Function>();
	
	// layout
	private final Dimension	defaultSize			= new Dimension(600, 450);
	
	// beauty stuff
	private final String	title 				= "Funktionsplotter";
	private final Color 	BG_COLOR 			= Color.orange;
	private Font 			mathFont 			= new Font("Arial", Font.BOLD, 30);
	private Font 			genericFont 		= new Font("Verdana", Font.PLAIN, 30);
	
	// components
	private JPanel 			leftPanel 			= new JPanel();
	private ArrayList<LabeledInput> inputArray 	= new ArrayList<LabeledInput>();
	private int 			inputPanelSize 		= 8;
	
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
		Parser p = new Parser();
		try {
			functions.add(new Function("cos(x)*x", p.buildSyntaxTree("cos(x)*x")));
			functions.add(new Function("1/x", p.buildSyntaxTree("1/x")));
			functions.add(new Function("x^3-4*x", p.buildSyntaxTree("x^3-4*x")));
			functions.add(new Function("tan(x)", p.buildSyntaxTree("tan(x)")));
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
		
		// general stuff
		this.setTitle(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(defaultSize);
		this.setMinimumSize(defaultSize);
		this.setLayout(new BorderLayout());
		
		// topPanel setup
		leftPanel = new JPanel(new GridLayout(inputPanelSize, 1));
		leftPanel.setPreferredSize(new Dimension((int)(this.getWidth()*.3), this.getHeight()));
		for(int i = 0; i < inputPanelSize; i++) {
			LabeledInput l = new LabeledInput();
			l.getInput().addActionListener(e -> {
				redrawFields();
				LabeledInput nextField = inputArray.get((inputArray.indexOf(l) + 1) % inputArray.size());
				nextField.getInput().requestFocus();
			});
			inputArray.add(l);
			leftPanel.add(l);
		}
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				resizeEvent();
		    }
		});
		this.add(leftPanel, BorderLayout.WEST);
		redrawFields();
		
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
		this.add(canvasPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	private void redrawFields() {
		for(int i = 0; i < inputArray.size(); i++) {
			if(inputArray.get(i).isBlank() ) {
				for(int j = i+1; j < inputArray.size(); j++) {
					inputArray.get(j).setVisible(false);
				}
				break;
			}
			else {
				inputArray.get((i+1) % inputArray.size()).setVisible(true);
			}
		}
		repaint();
	}
	
	private void resizeEvent() {
		leftPanel.setPreferredSize(new Dimension((int)(this.getWidth()*.3), this.getHeight()));
		this.repaint();
	}
}