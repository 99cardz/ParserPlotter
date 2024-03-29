package gui;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;

import canvas.CanvasPlot;
import viewmodel.ViewModel;

import java.awt.*;
import java.awt.event.*;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.*;

/*
 *  combination of label and input to display error during parsing process and provide a visual link to the graph color
 */
public class FunctionInput extends JPanel {

	private static final String 	EMPTY_INPUT = "Ausdruck eingeben";
	
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
	private JPanel 			colorLine 	= new JPanel();
	private JLabel 			label 		= new JLabel("", SwingConstants.CENTER);
	private JPanel			right		= new JPanel(new BorderLayout());
	private JButton			delete		= new JButton();
	
	FunctionInput(CanvasPlot cp) {
		super(new BorderLayout());
		
		canvas = cp;
		
		id = viewModel.addFunction(color);
		
		colorLine.setBackground(color);
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
				pickerWindow.setSize(300, 250);
				
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
				
				// construct sliders corresponding to RGB values
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
 				// clicking previous color panel resets changes
 				oldCol.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						viewModel.updateFunctionColor(id, oldCol.getBackground());
						nextCol.setBackground(oldCol.getBackground());
						colorLine.setBackground(oldCol.getBackground());
						canvas.repaint();
					}
				});
 				
 				pickerWindow.add(labelPanel, BorderLayout.WEST);
 				pickerWindow.add(sliderPanel, BorderLayout.CENTER);
				pickerWindow.setVisible(true);
			}
		});
		
		delete.setBorderPainted(false);
		delete.setContentAreaFilled(false);
		delete.setFocusPainted(false);
		right.setBackground(Color.WHITE);
		try {
			Image img = ImageIO.read(getClass().getResource("./deleteIcon.png"));
			img = img.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
			delete.setIcon(new ImageIcon(img));
			Image img2 = ImageIO.read(getClass().getResource("./deletePressed.png"));
			img2 = img2.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
			delete.setPressedIcon(new ImageIcon(img2));
		} catch (Exception ex) {
			delete.setIcon(null);
			delete.setText("x");
		}

		right.add(delete, BorderLayout.EAST);
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
	
	public JButton getDeleteButton() {
		return delete;
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
