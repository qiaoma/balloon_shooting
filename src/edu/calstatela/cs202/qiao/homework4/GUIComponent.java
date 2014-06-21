package edu.calstatela.cs202.qiao.homework4;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GUIComponent {

	private JFrame frame;
	private JPanel panelRight;
	private String inputFileName, outputFileName;
	private final int LISTSIZE = 100;
	private int count, compCount;
	private Component[] components;
	private String[] keys, values, units, guis, options;
	private Timer timer;
	private DrawingCanvas dc;

	public GUIComponent(String inputFileName) {
		panelRight = new JPanel(new GridLayout(0, 3, 10, 10));
		this.inputFileName = inputFileName;
		outputFileName = inputFileName + ".out";

		keys = new String[LISTSIZE];
		values = new String[LISTSIZE];
		units = new String[LISTSIZE];
		guis = new String[LISTSIZE];
		options = new String[LISTSIZE];

		components = new Component[LISTSIZE];
		count = 0;
		compCount = 0;		
	}

	@SuppressWarnings("unchecked")
	public void addComponent(String key, String value, String unit, String gui, String option) {
		keys[count] = key;
		values[count] = value;
		units[count] = unit;
		guis[count] = gui;
		options[count] = option;

		JLabel jlbKey = new JLabel(key);
		panelRight.add(jlbKey);

		if (gui.equals("text_field")) {
			components[compCount] = new JTextField(value);
			panelRight.add(components[compCount]);
		}

		if (gui.equals("radio_button")) {

			String[] names = option.split("\\s*\\|\\s*");
			ButtonGroup group = new ButtonGroup();
			JPanel radioPanel = new JPanel(new GridLayout());
			for (String name : names) {
				components[compCount] = new JRadioButton(name);
				if (name.equals(value)) {
					((JRadioButton) components[compCount]).setSelected(true);
				}
				group.add(((JRadioButton) components[compCount]));
				radioPanel.add(components[compCount]);
				compCount++;
			}
			panelRight.add(radioPanel);
		}
		
		if (gui.equals("pull_down") || gui.equals("combo_box")) {
			String[] names = option.split("\\s*\\|\\s*");
			components[compCount] = new JComboBox<String>(names);
			((JComboBox<String>) components[compCount]).setSelectedItem(value);
			panelRight.add(components[compCount]);
		}

		JLabel jblUnit = new JLabel(unit);
		if (unit.equals("*")) {
			jblUnit.setText("");
		}
		panelRight.add(jblUnit);
		count++;
		compCount++;
	}

	public void setFrame() {		
		dc = new DrawingCanvas(Integer.parseInt(values[0]), Integer.parseInt(values[1]), 
				Integer.parseInt(values[2]), Integer.parseInt(values[3]), 
				Integer.parseInt(values[4]), Integer.parseInt(values[5]), 
				Boolean.parseBoolean(values[6]), Boolean.parseBoolean(values[7]), values[8]);	
		timer = new Timer(1000, new AnimateListener());
		frame = new JFrame("Shoot Balloons");
		frame.setSize(1280, 660);
		frame.setVisible(true);
		frame.setLocation(0, 20);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());				
		frame.add(dc, BorderLayout.CENTER);
		frame.add(panelRight, BorderLayout.EAST);
	}

	public void addButtons() {
		
		JButton jbtPlay = new JButton("Play");
		JButton jbtPause = new JButton("Pause");
		JButton jbtLoad = new JButton("Load");
		JButton jbtSave = new JButton("Save");				
		JButton jbtSaveImage = new JButton("Save Image");
		JButton jbtReset = new JButton("Reset");
		JButton jbtQuit = new JButton("Quit");
		
		jbtPlay.addActionListener(new PlayListener());
		jbtPause.addActionListener(new PauseListener());
		jbtLoad.addActionListener(new LoadListener());
		jbtSave.addActionListener(new SaveListener());
		jbtSaveImage.addActionListener(new SaveImageListener());
		jbtReset.addActionListener(new ResetListener());
		jbtQuit.addActionListener(new QuitListener());
		
		panelRight.add(jbtPlay);
		panelRight.add(jbtPause);
		panelRight.add(jbtLoad);
		panelRight.add(jbtSave);
		panelRight.add(jbtSaveImage);
		panelRight.add(jbtReset);
		panelRight.add(jbtQuit);
	}
	
	@SuppressWarnings("unchecked")
	public void loadValues(String fileName){
		try {
			ConfigurationManager cm = new ConfigurationManager(fileName);
			String[] newValues = new String[count];
			int num = 0;
			for (String key : cm.getKeys()){
				String[] vs = cm.getString(key).split("\\s*!\\s*");
				newValues[num] = vs[0];
				num++;
			}
			
			int j = 0;
			for (int i = 0; i < count; i++) {
				if (guis[i].equals("text_field")) {				 
					((JTextField) components[j]).setText(newValues[i]);
				}
				if (guis[i].equals("radio_button")) {
					while (components[j] instanceof JRadioButton) {
						if (newValues[i].equals( ((JRadioButton) components[j]).getText() )){
							((JRadioButton) components[j]).setSelected(true);
						}
						j++;
					}
				}
				if (guis[i].equals("pull_down") || guis[i].equals("combo_box")) {
					((JComboBox<String>) components[j]).setSelectedItem(newValues[i]);
				}
				j++;
			}
		} catch (Exception e1) {
		}
	}
	
	@SuppressWarnings("unchecked")
	public void getValues(){
		int j = 0;
		for (int i = 0; i < count; i++) {
			if (guis[i].equals("text_field")) {
				values[i] = ((JTextField) components[j]).getText();
			}
			if (guis[i].equals("radio_button")) {
				while (components[j] instanceof JRadioButton) {
					if (((JRadioButton) components[j]).isSelected()) {
						values[i] = ((JRadioButton) components[j]).getText();
					}
					j++;
				}
			}
			if (guis[i].equals("pull_down") || guis[i].equals("combo_box")) {
				values[i] = (String) ((JComboBox<String>) components[j]).getSelectedItem();
			}
			j++;
		}
	}
	
	public boolean validUserInput(){
		int minSize = Integer.parseInt(values[0]);
		int maxSize = Integer.parseInt(values[1]);
		int minBalloon = Integer.parseInt(values[2]);
		int maxBalloon = Integer.parseInt(values[3]);
		int minSpeed = Integer.parseInt(values[4]);
		int maxSpeed = Integer.parseInt(values[5]);
		
		if ((minSize <= maxSize) && (minBalloon <= maxBalloon) && (minSpeed <= maxSpeed)){
			return true;
		}		
		return false;
	}
	
	public boolean valueChange(){
		String[] oldValues = {values[0], values[1], values[2], values[3],
				values[4], values[5], values[6], values[7], values[8]};
		getValues();				
		for (int i = 0; i < oldValues.length; i++){
			if (!oldValues[i].equals(values[i])){
				return true;
			}
		}	
		return false;
	}

	class AnimateListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){
			dc.update();
			if (dc.gameOver()){	
				dc.repaint();
			}
		}
	}
	
	class PlayListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (valueChange() || dc.gameOver()){	
				if (validUserInput()){
				frame.remove(dc);			
				dc = new DrawingCanvas(Integer.parseInt(values[0]), Integer.parseInt(values[1]), 
								Integer.parseInt(values[2]), Integer.parseInt(values[3]), 
								Integer.parseInt(values[4]), Integer.parseInt(values[5]), 
								Boolean.parseBoolean(values[6]), Boolean.parseBoolean(values[7]), values[8]);
				frame.add(dc, BorderLayout.CENTER);
				frame.setSize(1279, 660);
				frame.setSize(1280, 660);	
				}else {
					JOptionPane.showMessageDialog(null, "Invalid user input. The max value should be larger or equal then the min value!",
								"Error", JOptionPane.ERROR_MESSAGE); 
				}
			}
			if (validUserInput()){	
				dc.setPlayStatus(true);
				timer.start();
			}	
		}	
	}
	
	class PauseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			timer.stop();	
			dc.setPlayStatus(false);
		}	
	}

	class SaveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			getValues();
			if (validUserInput()){
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < count; i++) {
					sb.append(keys[i] + " = " + values[i] + " ! " + units[i]
							+ " ! " + guis[i] + " ! " + options[i] + "\n");
				}
				ConfigurationManager cm = new ConfigurationManager();
				cm.save(outputFileName, sb);
				JOptionPane.showMessageDialog(null, "Record saved!", "Save", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "Invalid user input. The max value should be larger or equal then the min value!",
						"Error", JOptionPane.ERROR_MESSAGE); 
			}			
		}
	}
	
	class LoadListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			loadValues(outputFileName);
		}
	}
	
	class SaveImageListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			dc.saveImage();	
		}	
	}
	
	class ResetListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			loadValues(inputFileName);
		}				
	}	
	
	class QuitListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);					
		}				
	}	
}


