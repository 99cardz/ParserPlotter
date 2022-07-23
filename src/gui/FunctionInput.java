package gui;

import javax.swing.JPanel;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FunctionInput extends JPanel {

	private static final String 	EMPTY_INPUT = "Enter a function.";
	
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
	
	public boolean			status		= true;
	
	private JTextField 		input 		= new JTextField(EMPTY_INPUT);
	private JPanel 			color 		= new JPanel();
	private JLabel 			label 		= new JLabel("", SwingConstants.CENTER);
	private JPanel			right		= new JPanel(new BorderLayout());
	private Function		function;
	
	FunctionInput() {
		super(new BorderLayout());
		color.setBackground(getNextColor());
		function = new Function(color.getBackground());
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
				}
			}
		});
		input.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(!isBlank()) {
					function.update(getInputText());
					label.setText(function.getError());
				}
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
		right.add(input, BorderLayout.CENTER);
		right.add(label, BorderLayout.SOUTH);
		this.add(right, BorderLayout.CENTER);
		this.add(color, BorderLayout.WEST);	
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
	
	/*
	 * @returns: Color of colored line
	 */
	public Color getColor() {
		return color.getBackground();
	}
	
	/*
	 * @param c: sets the colored line on the left to the chosen Color
	 */
	public void setColor(Color c) {
		color.setBackground(c);
	}
	
	/*
	 * @returns: Function inside LabeledInput
	 */
	public Function getFunction() {
		return function;
	}
	
	public static Color getNextColor() {
		return colors[(colorIndex++) % colors.length];
	}
}
