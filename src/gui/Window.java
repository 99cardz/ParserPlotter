package gui;
import java.awt.*;
import java.awt.event.ActionEvent;
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
	private final Dimension minSize				= new Dimension(900, 700);
	private final int		lowerWidth			= (int)launchSize.getWidth()/3;
	
	// beauty stuff
	private final String	title 				= "Funktionsplotter";
	private final Color		background			= new Color(232, 235, 252);
			
	// components
	private JTabbedPane		tabs				= new JTabbedPane();
	private JPanel			mainPanel			= new JPanel(new BorderLayout());
	private JPanel			hintPanel			= new JPanel(new BorderLayout());
	private JPanel			otherPanel			= new JPanel(new BorderLayout());
	private String			aboutHTML			= "<body style=\"font:Arial;\"> <meta charset=\"UTF-8\"><center><p>Dieses Projekt entstand im Rahmen des Moduls \"Anwendungsorientierte Programmierung\" bei Prof. Heinrich Kraemer.</p><p>Urheberrecht Jonathan Schulze, Hans Schreiter, Wieland Zweynert (2022)</p></center> <p>&nbsp; </p> </body>";
	private String			manualString		= "<html> <body style=\"font:Arial;\"><meta charset=\"UTF-8\"><p><b>Funktionsweise</b><br>Das Programm liest beliebige mathematische Funktionen, wertet diese aus und zeichnet sie. Die mathematischen Ausdruecke koennen auf der linken Seite eingegeben werden.</p><p> Bis zu acht Ausdruecke/Funktionen koennen parallel bearbeitet und gezeichnet werden. Ist die Eingabe syntaktisch nicht korrekt, wird dies vom Eingabefeld gemeldet.<p>Es sind beliebige mathematische Ausdruecke mit den Operatoren <b>+, -, *, /, ^</b> moeglich. Zusaetzlich sind einige wichtige Funktionen implementiert: <b>sin(), cos(), tan(), abs(), log(), sqrt()</b>. </p>Die unbekannte muss immer mit dem Namen x bezeichnet werden. Es koennen aber auch mathematische Ausdruecke bearbeitet werden, die keine Unbekannte enthalten.</p> <p><b>Navigation</b><br>Durch das Druecken und Halten der linken Maustaste laesst sich das Sichtfenster des Graphs verschieben. Durch Drehen des Mausrads kann hinein- und herausgezoomt werden. Alternativ kann dies auch durch das Betaetigen der Zoom-Bedienelemente im Weiteres-Tab erreicht werden. Durch Druecken des Zuruecksetzen-Buttons wird das Sichtfenster auf die Ausgangsposition zurueckgesetzt. Zusaetzlich kann die Farbe des Graphen durch Klicken auf das farbige Rechteck links neben dem Textfeld geaendert werden.</p> </body> </html>";

	private int 			inputPanelSize 		= 8;
	private JPanel 			inputPanel 			= new JPanel(new GridLayout(inputPanelSize, 1));
	private ArrayList<FunctionInput> inputArray = new ArrayList<FunctionInput>();
	
	private JPanel 			canvasPanel 		= new JPanel();
	private CanvasPlot		canvas				= new CanvasPlot();
	private JLabel			valueLable 			= new JLabel((""),  SwingConstants.CENTER);
	
	private JPanel 			zoomButtonPanel 	= new JPanel();
	private JButton 		xZoomOutButton 		= new JButton("-");
	private JButton 		xZoomInButton 		= new JButton("+");
	private JButton 		yZoomOutButton 		= new JButton("-");
	private JButton 		yZoomInButton 		= new JButton("+");
	private JButton			resetButton 		= new JButton("Zuruecksetzen");
	
	public Window() {
		
		// general stuff
		this.setTitle(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(launchSize);
		this.setMinimumSize(minSize);
		this.setLayout(new BorderLayout());
		
		// inputPanel setup
		inputPanel.setBackground(background);
		tabs.setPreferredSize(new Dimension((int)(this.getWidth()/3), this.getHeight()));
		tabs.setBackground(background);
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				if (getWidth()/3 < 400)
					tabs.setPreferredSize(new Dimension((int)(getWidth()/3), getHeight()));
				else
					tabs.setPreferredSize(new Dimension(lowerWidth, getHeight()));
				repaint();
				canvas.updateXValues();
		    }
		});
		mainPanel.add(inputPanel, BorderLayout.CENTER);
		
		// canvas setup
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
		
		// main tab
		tabs.setBorder(null);
		tabs.addTab("Eingabe", mainPanel);
		
		// manual tab
		tabs.addTab("Anleitung", hintPanel);
		JTextPane hintText = new JTextPane();
		hintText.setEditable(false);
		hintText.setContentType("text/html");
		hintText.setText(manualString);
		hintText.setBackground(background);
		hintText.setBorder(new EmptyBorder(10,20,10,20));
		hintPanel.add(hintText);
		
		// other tab
		tabs.addTab("Weiteres", otherPanel);
		otherPanel.setBackground(background);
		otherPanel.setBorder(new EmptyBorder(10,10,10,10));
		JTextPane aboutText = new JTextPane();
		aboutText.setBackground(background);
		aboutText.setEditable(false);
		aboutText.setContentType("text/html");
		aboutText.setText(aboutHTML);
		aboutText.setBorder(new TitledBorder("Ueber dieses Programm"));
		otherPanel.add(aboutText, BorderLayout.NORTH);
		otherPanel.add(zoomButtonPanel, BorderLayout.CENTER);
		
		// zoom button row setup
		zoomButtonPanel.setLayout(new FlowLayout());
		zoomButtonPanel.setBorder(new TitledBorder("Individueller Zoom"));
		zoomButtonPanel.setBackground(background);
		JButton[] buttons = { xZoomOutButton, xZoomInButton, yZoomOutButton, yZoomInButton, resetButton};
		double[][] scalars = {{0.9, 1.0}, {1.1, 1.0}, {1.0, 0.9}, {1.0, 1.1}, {0.0, 0.0}};
		for(int i = 0; i < buttons.length; i++) {
			int j = i;
			buttons[i].addActionListener(e -> canvas.scaleOrigin(scalars[j][0], scalars[j][1]));
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
		JCheckBox zoomToggle = new JCheckBox("Zoom auf Koordinatenursprung");
		zoomToggle.setBackground(background);
		zoomToggle.setSelected(viewModel.getFixedPointZoomSetting());
		zoomToggle.setFocusPainted(false);
		zoomToggle.addItemListener(e -> {
			viewModel.setFixedPointZoomSetting(zoomToggle.isSelected());
		});
		zoomButtonPanel.add(zoomToggle);
		
		redrawFields();
		this.add(canvasPanel, BorderLayout.CENTER);
		this.add(tabs, BorderLayout.WEST);
	}
	
	// redraws FunctionInputs dropping any that are empty and adding a new one at the end. Focus is then shifted to the next input.
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
	
	// generate FunctionInput and insert it into both array and panel
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