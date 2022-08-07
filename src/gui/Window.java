package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import canvas.CanvasPlot;
import viewmodel.ViewModel;

public class Window extends JFrame {

	ViewModel				viewModel			= ViewModel.getInstance();

	// layout
	private final Dimension	defaultSize			= new Dimension(1200, 700);


	// beauty stuff
	private final String	title 				= "Funktionsplotter";

	// components
	private JMenuBar		menuBar				= new JMenuBar();
	private JMenu			menu				= new JMenu("Menü");
	private JMenuItem		aboutMenu			= new JMenuItem("Über dieses Programm");
	private JMenuItem		helpMenu			= new JMenuItem("Kurzanleitung");
	private final Dimension aboutWindowSize		= new Dimension(400, 400);
	private final Dimension helpWindowSize 		= new Dimension(600, 600);

	private JPanel			leftPanel			= new JPanel(new BorderLayout());

	private int 			inputPanelSize 		= 8;
	private JPanel 			inputPanel 			= new JPanel(new GridLayout(inputPanelSize, 1));
	private ArrayList<FunctionInput> inputArray = new ArrayList<>();

	private JPanel 			canvasPanel 		= new JPanel();
	private CanvasPlot		canvas				= new CanvasPlot();
	private JLabel			valueLable 			= new JLabel((""),  SwingConstants.CENTER);

	private JPanel 			bottomPanel 		= new JPanel();
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


		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent componentEvent) {
				resizeEvent();
				canvas.updateXValues();
		    }
		});
		leftPanel.add(inputPanel, BorderLayout.CENTER);

		canvasPanel.setLayout(new BorderLayout());
		canvasPanel.setBorder(new EmptyBorder(2, 10, 2, 2));
		canvasPanel.add(canvas);
		canvasPanel.addMouseWheelListener(e -> {
			if(e.getWheelRotation() < 0)
				canvas.scale(.95, .95, e.getX(), e.getY());
			else if (e.getWheelRotation() > 0)
				canvas.scale(1.05, 1.05, e.getX(), e.getY());
			valueLable.setText("x: " + canvas.toXValue(e.getX()) + " y: " + canvas.toYValue(e.getY()));
		});
		canvas.addMouseMotionListener(new MouseMotionListener() {
			private int beforeX, beforeY;
			@Override
			public void mouseDragged(MouseEvent e) {
				canvas.offsetPx(e.getX() - beforeX, e.getY() - beforeY);
				beforeX = e.getX();
				beforeY = e.getY();
				valueLable.setText("x: " + canvas.toXValue(beforeX) + " y: " + canvas.toYValue(beforeY));
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				beforeX = e.getX();
				beforeY = e.getY();
				valueLable.setText(String.format("x: %.5f y: %.5f", canvas.toXValue(beforeX), canvas.toYValue(beforeY)));
			}
		});
		canvasPanel.add(valueLable, BorderLayout.SOUTH);
		canvasPanel.setBackground(Color.white);

		redrawFields();

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

		aboutMenu.addActionListener(e -> {
			JFrame aboutWindow = new JFrame("Über dieses Programm");
			aboutWindow.setLayout(new BorderLayout());
			JTextPane aboutText = new JTextPane();
			aboutText.setEditable(false);
			aboutText.setContentType("text/html");
			aboutText.setText("<!DOCTYPE html> <html> <body> <center> <h1><br><br></br></br>" + title + "</h1><p>Dieses Projekt entstand im Rahmen des Moduls \"Anwendungsorientierte Programmierung\" bei Prof. Heinrich Krämer.</p><p>Urheberrecht Jonathan Schulze, Hans Schreiter, Wieland Zweynert (2022) </p></center></body></html>");
			aboutWindow.add(aboutText);
			aboutWindow.setSize(aboutWindowSize);
			aboutWindow.setResizable(false);
			aboutWindow.setBackground(Color.WHITE);
			aboutWindow.setVisible(true);
		});

		helpMenu.addActionListener(e -> {
			JFrame helpWindow = new JFrame("Kurzanleitung");
			helpWindow.setLayout(new BorderLayout());
			JTextPane helpText = new JTextPane();
			helpText.setEditable(false);
			helpText.setBorder(new EmptyBorder(40, 40, 40, 40));
			helpText.setContentType("text/html");
			helpText.setText("<!DOCTYPE html> <html><p>Das Programm liest beliebige mathematische Funktionen, wertet diese aus und zeichnet sie. Die mathematischen Ausdrücke können auf der linken Seite eingegeben werden.</p><p> Bis zu acht Ausdrücke/Funktionen können parallel bearbeitet und gezeichnet werden. Ist die Eingabe syntaktisch nicht korrekt, wird dies vom Programm gemeldet.<p>Es sind beliebige mathematische Ausdrücke möglich, mit den Operatoren +, -, *, / und ^. Zusätzlich sind einige wichtige Funktionen implementiert: sin(), cos(), tan(), abs(), log(), und sqrt(). </p>Die unbekannte muss immer mit dem Namen “x” bezeichnet werden. Es können aber auch mathematische Ausdrücke bearbeitet werden, die keine Unbekannte enthalten.<p>Auf der rechten Seite der Benutzeroberfläche wird nun der Ausdruck / die Funktion visualisiert.</p></p>Durch das Drücken und Halten der linken Maustaste lässt sich das Sichtfenster verschieben. Durch Drehen des Mausrades kann hinein- und herausgezoomt werden. Alternativ kann dies auch durch das Betätigen der “+” und “-” Bedienelemente am unteren Rand der Oberfläche erreicht werden. Durch drücken des “R” Buttons wird das Sichtfenster auf die Ausgangsposition zurückgesetzt. Zusätzlich kann die Farbe des Graphen durch Klicken auf das farbige Rechteck links neben dem Textfeld geändert werden.</p></html>");
			helpText.setFont(new Font("Arial", Font.PLAIN, 20));
			JScrollPane scrollPane = new JScrollPane(helpText);
			helpWindow.add(scrollPane, BorderLayout.CENTER);
			helpWindow.setSize(helpWindowSize);
			helpWindow.setVisible(true);
		});
		menu.add(aboutMenu);
		menu.addSeparator();
		menu.add(helpMenu);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		this.add(canvasPanel, BorderLayout.CENTER);
		this.add(leftPanel, BorderLayout.WEST);
	}

	private void redrawFields() {

		for (FunctionInput fi : inputArray)
			if (fi.isBlank())
				viewModel.deleteFunction(fi.id);
		inputArray.removeIf(l -> l.isBlank());
		inputPanel.removeAll();
		for(FunctionInput f: inputArray) {
			inputPanel.add(f);
		}
		if(inputArray.size() < inputPanelSize) {
			FunctionInput f = new FunctionInput(canvas);
			f.getInput().addActionListener(e -> {
				int prevPos = inputArray.indexOf(f);
				redrawFields();
				int nowPos = inputArray.indexOf(f);
				int next = (nowPos == -1 ? prevPos : nowPos+1) % inputArray.size();
				inputArray.get(next).transferFocus();
			});
//			f.getInput().getDocument().addDocumentListener(new DocumentListener() {
//				//this document listener only update the canvas, the rest is handled inside the class
//				@Override
//				public void insertUpdate(DocumentEvent e) {
//					canvas.repaint();
//				}
//				@Override
//				public void removeUpdate(DocumentEvent e) {
//					insertUpdate(e);
//				}
//				@Override
//				public void changedUpdate(DocumentEvent e) {
//					insertUpdate(e);
//				}
//			});
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