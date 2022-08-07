package gui;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import canvas.CanvasPlot;
import viewmodel.ViewModel;

public class Window extends JFrame {
	
	ViewModel				viewModel			= ViewModel.getInstance();
	
	// layout
	private final Dimension	launchSize			= new Dimension(1200, 700);
	private final Dimension minSize				= new Dimension((int)launchSize.getWidth()/3, 600);
	private final int		lowerWidth			= (int)launchSize.getWidth()/3;
	
	// beauty stuff
	private final String	title 				= "Funktionsplotter";
	private final Color		background			= new Color(232, 235, 252);
			
	// components
	private JTabbedPane		tabs				= new JTabbedPane();
	private JPanel			mainPanel			= new JPanel(new BorderLayout());
	private JPanel			hintPanel			= new JPanel(new BorderLayout());
	private JPanel			otherPanel			= new JPanel(new BorderLayout());
	private String			aboutHTML			= "<body style=\"font:Arial;\"> <meta charset=\"UTF-8\"><center><p>Dieses Projekt entstand im Rahmen des Moduls \"Anwendungsorientierte Programmierung\" bei Prof. Heinrich Krämer.</p><p>Urheberrecht Jonathan Schulze, Hans Schreiter, Wieland Zweynert (2022)</p></center> <p>&nbsp; </p> </body>";
	private String			manualString		= "<html> <body style=\"font:Arial;\"><meta charset=\"UTF-8\"><p>Das Programm liest beliebige mathematische Funktionen, wertet diese aus und zeichnet sie. Die mathematischen Ausdrücke können auf der linken Seite eingegeben werden.</p><p> Bis zu acht Ausdrücke/Funktionen können parallel bearbeitet und gezeichnet werden. Ist die Eingabe syntaktisch nicht korrekt, wird dies vom Programm gemeldet.<p>Es sind beliebige mathematische Ausdrücke möglich, mit den Operatoren +, -, *, / und ^. Zusätzlich sind einige wichtige Funktionen implementiert: sin(), cos(), tan(), abs(), log(), und sqrt(). </p>Die unbekannte muss immer mit dem Namen x bezeichnet werden. Es können aber auch mathematische Ausdrücke bearbeitet werden, die keine Unbekannte enthalten.<p>Auf der rechten Seite der Benutzeroberfläche wird nun der Ausdruck / die Funktion visualisiert.</p></p>Durch das Drücken und Halten der linken Maustaste lässt sich das Sichtfenster verschieben. Durch Drehen des Mausrads kann hinein- und herausgezoomt werden. Alternativ kann dies auch durch das Betätigen der Zoom-Bedienelemente am unteren Rand der Oberfläche erreicht werden. Durch Drücken des R-Buttons wird das Sichtfenster auf die Ausgangsposition zurückgesetzt. Zusätzlich kann die Farbe des Graphen durch Klicken auf das farbige Rechteck links neben dem Textfeld geändert werden.</p> </body> </html>";

	private int 			inputPanelSize 		= 8;
	private JPanel 			inputPanel 			= new JPanel(new GridLayout(inputPanelSize, 1));
	private ArrayList<FunctionInput> inputArray = new ArrayList<FunctionInput>();
	
	private JPanel 			canvasPanel 		= new JPanel();
	private CanvasPlot		canvas				= new CanvasPlot();
	private JLabel			valueLable 			= new JLabel((""),  SwingConstants.CENTER);
	
	private JPanel 			zoomButtonPanel 		= new JPanel();
	private JButton 		xZoomOutButton 		= new JButton("-");
	private JButton 		xZoomInButton 		= new JButton("+");
	private JButton 		yZoomOutButton 		= new JButton("-");
	private JButton 		yZoomInButton 		= new JButton("+");
	private JButton			resetButton 		= new JButton("Zurücksetzen");
	
	public Window() {
		
		// general stuff
		this.setTitle(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(launchSize);
		this.setMinimumSize(minSize);
		this.setLayout(new BorderLayout());
		
		// inputPanel setup
		inputPanel.setBackground(background);
		tabs.setPreferredSize(new Dimension((int)(this.getWidth()*.3), this.getHeight()));
		
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				resizeEvent();
				canvas.updateXValues();
		    }
		});
		mainPanel.add(inputPanel, BorderLayout.CENTER);
		
		canvasPanel.setLayout(new BorderLayout());
		canvasPanel.setBorder(new EmptyBorder(2, 10, 2, 2));
		canvasPanel.add(canvas);
		canvasPanel.addMouseWheelListener(e -> {
			if(e.getWheelRotation() < 0)
				canvas.scale(.9, .9, e.getX(), e.getY());
			else if (e.getWheelRotation() > 0)
				canvas.scale(1.1, 1.1, e.getX(), e.getY());
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
				valueLable.setText("x: " + canvas.toXValue(beforeX) + " y: " + canvas.toYValue(beforeY));
			}
		});
		canvasPanel.add(valueLable, BorderLayout.SOUTH);
		canvasPanel.setBackground(Color.white);
		
		redrawFields();
		
		// zoom button row setup
		zoomButtonPanel.setLayout(new FlowLayout());
		zoomButtonPanel.setBorder(new TitledBorder("Individueller Zoom"));
		zoomButtonPanel.setBackground(background);
		JButton[] buttons = { xZoomOutButton, xZoomInButton, yZoomOutButton, yZoomInButton, resetButton};
		double[][] scalars = {{0.9, 1.0}, {1.1, 1.0}, {1.0, 0.9}, {1.0, 1.1}, {0.0, 0.0}};
		for(int i = 0; i < buttons.length; i++) {
			int j = i;
			buttons[i].addActionListener(e -> canvas.scale(scalars[j][0], scalars[j][1]));
		}
		resetButton.addActionListener(e -> canvas.resetOffset());

		zoomButtonPanel.add(new JLabel("X:", SwingConstants.CENTER));
		zoomButtonPanel.add(xZoomInButton);
		zoomButtonPanel.add(xZoomOutButton);
		zoomButtonPanel.add(new JLabel("Y:", SwingConstants.CENTER));
		zoomButtonPanel.add(yZoomInButton);
		zoomButtonPanel.add(yZoomOutButton);
		zoomButtonPanel.add(new JPanel());
		zoomButtonPanel.add(resetButton);
		
		// add to window
		mainPanel.add(zoomButtonPanel, BorderLayout.SOUTH);
		
		tabs.setBorder(null);
		tabs.addTab("Eingabe", mainPanel);
		
		tabs.addTab("Anleitung", hintPanel);
		JTextPane hintText = new JTextPane();
		hintText.setEditable(false);
		hintText.setContentType("text/html");
		hintText.setText(manualString);
		hintText.setBackground(background);
		hintText.setBorder(new EmptyBorder(10,20,10,20));
		hintPanel.add(hintText);
		tabs.addTab("Weiteres", otherPanel);
		otherPanel.setBackground(background);
		otherPanel.setBorder(new EmptyBorder(10,10,10,10));
		JTextPane aboutText = new JTextPane();
		aboutText.setBackground(background);
		aboutText.setEditable(false);
		aboutText.setContentType("text/html");
		aboutText.setText(aboutHTML);
		aboutText.setBorder(new TitledBorder("Über dieses Programm"));
		otherPanel.add(aboutText, BorderLayout.NORTH);
		otherPanel.add(zoomButtonPanel, BorderLayout.CENTER);
		
		this.add(canvasPanel, BorderLayout.CENTER);
		this.add(tabs, BorderLayout.WEST);
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
			insertFunctionInput();
		}
		revalidate();
		repaint();
	}
	
	private void resizeEvent() {
		if (this.getWidth()/3 < 400)
			tabs.setPreferredSize(new Dimension((int)(this.getWidth()*.3), this.getHeight()));
		else
			tabs.setPreferredSize(new Dimension(lowerWidth, this.getHeight()));
		this.repaint();
	}
	
	private void insertFunctionInput() {
		FunctionInput f = new FunctionInput(canvas);
		f.getInput().addActionListener(e -> {
			int prevPos = inputArray.indexOf(f);
			redrawFields();
			int nowPos = inputArray.indexOf(f);
			int next = (nowPos == -1 ? prevPos : nowPos+1) % inputArray.size();
			inputArray.get(next).transferFocus();
		});
		f.getDeleteButton().addActionListener(e -> {
			f.setInputText("");
			redrawFields();
		});
		inputArray.add(f);
		inputPanel.add(f);
	}
}