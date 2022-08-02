package gui;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import canvas.CanvasPlot;

public class Window extends JFrame {
	
	private ArrayList<Function> functions		= new ArrayList<Function>();
	
	// layout
	private final Dimension	defaultSize			= new Dimension(1200, 700);
	
	// beauty stuff
	private final String	title 				= "Funktionsplotter";
	
	// components
	private JPanel			leftPanel			= new JPanel(new BorderLayout());
	
	private int 			inputPanelSize 		= 8;
	private JPanel 			inputPanel 			= new JPanel(new GridLayout(inputPanelSize, 1));
	private ArrayList<FunctionInput> inputArray 	= new ArrayList<FunctionInput>();
	
	private JPanel 			canvasPanel 		= new JPanel();
	private CanvasPlot		canvas;
	private JLabel			valueLable 			= new JLabel((""),  SwingConstants.CENTER);
	
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
		this.setSize(defaultSize);
		this.setMinimumSize(defaultSize);
		this.setLayout(new BorderLayout());
		
		// inputPanel setup
		inputPanel.setBackground(new Color(232, 235, 252));
		leftPanel.setPreferredSize(new Dimension((int)(this.getWidth()*.3), this.getHeight()));
		redrawFields();
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				resizeEvent();
		    }
		});
		leftPanel.add(inputPanel, BorderLayout.CENTER);
		
		canvas = new CanvasPlot(inputArray);
		canvasPanel.setLayout(new BorderLayout());
		canvasPanel.setBorder(new EmptyBorder(2, 10, 2, 2));
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
		bottomPanel.setLayout(new FlowLayout());
		JButton[] buttons = { xZoomOutButton, xZoomInButton, yZoomOutButton, yZoomInButton, resetButton};
		double[][] scalars = {{2.0, 1.0}, {0.5, 1.0}, {1.0, 2.0}, {1.0, 0.5}, {0.0, 0.0}};
		for(int i = 0; i < buttons.length; i++) {
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
		leftPanel.add(bottomPanel, BorderLayout.SOUTH);
		this.add(canvasPanel, BorderLayout.CENTER);
		this.add(leftPanel, BorderLayout.WEST);
	}

	private void redrawFields() {
		inputArray.removeIf(l -> l.isBlank());
		inputPanel.removeAll();
		for(FunctionInput f: inputArray) {
			inputPanel.add(f);
		}
		if(inputArray.size() < inputPanelSize) {
			FunctionInput f = new FunctionInput();
			f.getInput().addActionListener(e -> {
				int prevPos = inputArray.indexOf(f);
				redrawFields();
				int nowPos = inputArray.indexOf(f);
				int next = (nowPos == -1 ? prevPos : nowPos+1) % inputArray.size();
				inputArray.get(next).transferFocus();
			});
			f.getInput().getDocument().addDocumentListener(new DocumentListener() {
				//this document listener only update the canvas, the rest is handled inside the class
				@Override
				public void insertUpdate(DocumentEvent e) {
					canvas.repaint();
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
			functions.add(f.getFunction());
			inputArray.add(f);
			inputPanel.add(f);
		}
		revalidate();
		repaint();
	}
	
	private void resizeEvent() {
		leftPanel.setPreferredSize(new Dimension((int)(this.getWidth()*.3), this.getHeight()));
		this.repaint();
	}
}