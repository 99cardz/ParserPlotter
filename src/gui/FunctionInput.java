package gui;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;

import canvas.CanvasPlot;
import viewModel.ViewModel;

import java.awt.*;
import java.awt.event.*;
import java.util.UUID;

import javax.swing.*;

public class FunctionInput extends JPanel {

	private static final String 	EMPTY_INPUT = "Enter a function.";
	
	// references
	private ViewModel viewModel = ViewModel.getInstance();
	private CanvasPlot canvas;
	
    private static final Color[] colors = {
		new Color(39, 88, 216), // blue
		new Color(150, 30, 225), // purple
		new Color(225, 52, 30), // red
		new Color(106, 225, 30), // green
		new Color(223, 205, 32), // yellow
		new Color(223, 109, 32), // orange
		new Color(36, 206, 219), // cyan
		new Color(119, 136, 119) // gray
	};
    
    private static int colorIndex = 0;
	private Color			color		= getNextColor();
    final UUID id;
	
	public boolean			status		= true;
	
	private JTextField 		input 		= new JTextField(EMPTY_INPUT);
	private JPanel 			colorLine = new JPanel();
	private JLabel 			label 		= new JLabel("", SwingConstants.CENTER);
	private JPanel			right		= new JPanel(new BorderLayout());
	
	FunctionInput(CanvasPlot cp) {
		super(new BorderLayout());
		
		canvas = cp;
		
		id = viewModel.addFunction(color);
		
		colorLine.setBackground(color);
//>>>>>>> main
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		input.setBorder(new EmptyBorder(0, 20, 0, 20));
		input.setText(EMPTY_INPUT);
		input.setForeground(Color.GRAY);
		input.setEditable(true);
		input.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if(input.getText().equals(EMPTY_INPUT)) {
					input.setText("");
					input.setForeground(Color.BLACK);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(input.getText().isBlank()) {
					input.setText(EMPTY_INPUT);
					input.setForeground(Color.GRAY);
					label.setText("");
				}
			}
		});
		input.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
					label.setText(viewModel.updateFunctionExpression(id, getInputText()));
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
		
		colorLine.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e1) {
				JFrame pickerWindow = new JFrame("Neue Farbe");
				pickerWindow.setLayout(new BorderLayout());
				pickerWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				pickerWindow.setSize(300, 400);
				
				JPanel colorChange = new JPanel(new GridLayout(1,2));
				colorChange.setPreferredSize(new Dimension(300, 80));
				JPanel oldCol = new JPanel();
				oldCol.setBackground(color);
				JPanel nextCol = new JPanel();
				nextCol.setBackground(color);
				colorChange.add(oldCol);
				colorChange.add(nextCol);
				pickerWindow.add(colorChange, BorderLayout.SOUTH);
				
				JPanel sliderPanel = new JPanel(new GridLayout(3,1));
				JPanel labelPanel = new JPanel(new GridLayout(3,1));
				
				int[] previousCol = {color.getRed(), color.getGreen(), color.getBlue()};
				Color[] colors = {Color.red, Color.green, Color.blue};
				JSlider[] sliders = new JSlider[3];
				JPanel[] labels = new JPanel[3];
				
 				for (int i = 0; i < 3; i++) {
 					labels[i] = new JPanel();
 					labels[i].setBackground(colors[i]);
 					labelPanel.add(labels[i]);
					sliders[i] = new JSlider(0, 255, previousCol[i]);
					sliders[i].setForeground(Color.blue);
					sliders[i].addChangeListener(e -> {
						color = new Color(sliders[0].getValue(), sliders[1].getValue(), sliders[2].getValue());
						viewModel.updateFunctionColor(id, color);
						nextCol.setBackground(color);
						colorLine.setBackground(color);
						canvas.repaint();
					});
					sliderPanel.add(sliders[i]);
				}
 				pickerWindow.add(labelPanel, BorderLayout.WEST);
 				pickerWindow.add(sliderPanel, BorderLayout.CENTER);
				pickerWindow.setVisible(true);
			}
		});
		
		right.add(input, BorderLayout.CENTER);
		right.add(label, BorderLayout.SOUTH);
		this.add(colorLine, BorderLayout.WEST);
		this.add(right, BorderLayout.CENTER);
	}
	
	/*
	 * @returns: input
	 */
	public JTextField getInput() {
		return input;
	}
	
	/*
	 * @returns: String contained by input
	 */
	public String getInputText() {
		return input.getText();
	}
	
	/*
	 * @returns: true if input contains only whitespace or the empty field prompt
	 */
	public boolean isBlank() {
		return input.getText().isBlank() || input.getText().equals(EMPTY_INPUT);
	}
	
	/*
	 * @param s: String to set input to
	 */
	public void setInputText(String s) {
		input.setText(s);
	}
	
	/*
	 * @returns: String contained by label
	 */
	public String getLabelText() {
		return label.getText();
	}
	
	/*
	 * @param s: String to set label to
	 */
	public void setLabelText(String s) {
		label.setText(s);
	}
		
	public static Color getNextColor() {
		return colors[(colorIndex++) % colors.length];
	}
}
