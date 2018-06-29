package view;

import model.Interface;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.io.*;


@SuppressWarnings("serial")
public class InterfaceView extends JFrame implements ActionListener{
	private JLabel labelTemp = new JLabel("Temperature Settings                                            ");
	private JLabel labelVibration = new JLabel(" Vibration Settings");
	JButton validateSettings;
	JButton resetVib;
	JButton testVib;
	
	ImageIcon screen1 = new ImageIcon("images/secure-banking.png"); 
	ImageIcon screen2 = new ImageIcon("images/secure-socialmedia.png");
	ImageIcon screen3 = new ImageIcon("images/secure-random.png");
	
	ImageIcon screen4 = new ImageIcon("images/rednotsecure-banking.png");
	ImageIcon screen5 = new ImageIcon("images/rednotsecure-socialmedia.png");
	ImageIcon screen6 = new ImageIcon("images/rednotsecure-random.png");
	
	ImageIcon screen7 = new ImageIcon("images/greynotsecure-banking.png");
	ImageIcon screen8 = new ImageIcon("images/greynotsecure-socialmedia.png");
	ImageIcon screen9 = new ImageIcon("images/greynotsecure-random.png");
	
	ImageIcon screen10 = new ImageIcon("images/grey-banking.png");
	ImageIcon screen11 = new ImageIcon("images/grey-socialmedia.png");
	ImageIcon screen12 = new ImageIcon("images/grey-random.png");
	
	private JLabel imageLabel = new JLabel(screen1, SwingConstants.CENTER);
	private JLabel beat1Label = new JLabel("       Beat 1 /");
	private JLabel beat2Label = new JLabel("       Beat 2 /");
	private JLabel beat3Label = new JLabel("       Beat 3 /");
	private JLabel beat4Label = new JLabel("       Beat 4 /");
	
	static int j=1;
	static final int tempMin = 22;
	static final int tempMax = 38;
	static final int tempInit = 30; 
	
	JRadioButton intensityLow1 = new JRadioButton();
	JRadioButton intensityHigh1 = new JRadioButton();
	ButtonGroup intensityGroup1 = new ButtonGroup();

	JRadioButton shortVib1 = new JRadioButton();
	JRadioButton longVib1 = new JRadioButton();
	ButtonGroup lengthGroup1 = new ButtonGroup();

	JRadioButton intensityLow2 = new JRadioButton();
	JRadioButton intensityHigh2 = new JRadioButton();
	ButtonGroup intensityGroup2 = new ButtonGroup();
	
	JRadioButton shortVib2 = new JRadioButton();
	JRadioButton longVib2 = new JRadioButton();
	ButtonGroup lengthGroup2 = new ButtonGroup();
	JRadioButton pause2 = new JRadioButton();

	JRadioButton intensityLow3 = new JRadioButton();
	JRadioButton intensityHigh3 = new JRadioButton();
	ButtonGroup intensityGroup3 = new ButtonGroup();
	
	JRadioButton pause3 = new JRadioButton();
	JRadioButton shortVib3 = new JRadioButton();
	JRadioButton longVib3 = new JRadioButton();
	ButtonGroup lengthGroup3 = new ButtonGroup();
	
	JRadioButton intensityLow4 = new JRadioButton();
	JRadioButton intensityHigh4 = new JRadioButton();
	ButtonGroup intensityGroup4 = new ButtonGroup();

	JRadioButton shortVib4 = new JRadioButton();
	JRadioButton longVib4 = new JRadioButton();
	ButtonGroup lengthGroup4 = new ButtonGroup();
	
	private JSlider temperature = new JSlider(JSlider.HORIZONTAL,tempMin, tempMax, tempInit);
	String participantId = JOptionPane.showInputDialog(this, "Participant ID:");

	private Interface interf;
	
	/**
	 * Defines the basic of the interface
	 * @param interf
	 */
	public InterfaceView(Interface interf) {
		setupNorthPanel(interf);
		setupWestPanel(interf);
		setupEastPanel(interf);
		setupSouthPanel(interf);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(1300, 650);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	/**
	 * Sets up the north panel
	 * @param interf
	 */
	private void setupNorthPanel(Interface interf) {
		JPanel temp = new JPanel();
		temp.setLayout(new FlowLayout());
		temp.add(labelTemp);
		temp.add(temperature);
		temperature.setMajorTickSpacing(4);
		temperature.setMinorTickSpacing(1);
		temperature.setPaintTicks(true);
		temperature.setPaintLabels(true);
		temperature.addChangeListener(new SliderListener());

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		northPanel.add(temp);
		this.add(northPanel, BorderLayout.NORTH);
	}
	
	/**
	 * Sets up the west panel
	 * @param interf
	 */
	private void setupWestPanel(Interface interf) {
		JPanel img = new JPanel();
		img.setLayout(new FlowLayout());
		img.add(imageLabel);
		
		JPanel WestPanel = new JPanel();
		WestPanel.setLayout(new FlowLayout());
		WestPanel.add(img);
		this.add(WestPanel, BorderLayout.WEST);
	}
	
	/**
	 * Sets up the east panel
	 * @param interf
	 */
	private void setupEastPanel(Interface interf) {
		JPanel vib = new JPanel();
		vib.setLayout(new GridLayout(13,1));
		
		JPanel vib2 = new JPanel();
		vib2.setLayout(new GridLayout(13,1));
		
		JPanel vib3 = new JPanel();
		vib3.setLayout(new GridLayout(13,1));
		
		JPanel vib4 = new JPanel();
		vib4.setLayout(new GridLayout(13,1));
		vib.add(labelVibration);
		
		intensityGroup1.add(intensityLow1);
		intensityLow1.setText("Low");
		intensityGroup1.add(intensityHigh1);
		intensityHigh1.setText("High");
		
		lengthGroup1.add(shortVib1);
		shortVib1.setText("Short Vibration");
		lengthGroup1.add(longVib1);
		longVib1.setText("Long Vibration");
		
		vib.add(beat1Label);
		vib.add(new JSeparator(SwingConstants.HORIZONTAL));
		vib.add(intensityLow1);
		vib.add(intensityHigh1);
		vib.add(new JSeparator(SwingConstants.HORIZONTAL));
		vib.add(shortVib1);
		vib.add(longVib1);
		vib.add(new JSeparator(SwingConstants.HORIZONTAL));

		intensityGroup2.add(intensityLow2);
		intensityLow2.setText("Low");
		intensityGroup2.add(intensityHigh2);
		intensityHigh2.setText("High");

		lengthGroup2.add(shortVib2);
		shortVib2.setText("Short Vibration");
		lengthGroup2.add(longVib2);
		longVib2.setText("Long Vibration");

		lengthGroup2.add(pause2);
		pause2.setText(" Pause");

		vib2.add(new JLabel(""));
		vib2.add(beat2Label);
		vib2.add(new JSeparator(SwingConstants.HORIZONTAL));
		vib2.add(intensityLow2);
		vib2.add(intensityHigh2);
		vib2.add(new JSeparator(SwingConstants.HORIZONTAL));
		vib2.add(shortVib2);
		vib2.add(longVib2);
		vib2.add(new JSeparator(SwingConstants.HORIZONTAL));
		vib2.add(pause2);
		
		resetVib = new JButton("reset");
		vib2.add(resetVib);
		resetVib.addActionListener(this);
		
		intensityGroup3.add(intensityLow3);
		intensityLow3.setText("Low");
		intensityGroup3.add(intensityHigh3);
		intensityHigh3.setText("High");
		
		lengthGroup3.add(shortVib3);
		shortVib3.setText("Short Vibration");
		lengthGroup3.add(longVib3);
		longVib3.setText("Long Vibration");
		
		lengthGroup3.add(pause3);
		pause3.setText(" Pause");

		vib3.add(new JLabel(""));
		vib3.add(beat3Label);
		vib3.add(new JSeparator(SwingConstants.HORIZONTAL));
		vib3.add(intensityLow3);
		vib3.add(intensityHigh3);
		vib3.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		vib3.add(shortVib3);
		vib3.add(longVib3);
		vib3.add(new JSeparator(SwingConstants.HORIZONTAL));
		vib3.add(pause3);
	
		testVib = new JButton("test");
		vib3.add(testVib);
		testVib.addActionListener(this);
	
		intensityGroup4.add(intensityLow4);
		intensityLow4.setText("Low");
		intensityGroup4.add(intensityHigh4);
		intensityHigh4.setText("High");
		
		lengthGroup4.add(shortVib4);
		shortVib4.setText("Short Vibration");
		lengthGroup4.add(longVib4);
		longVib4.setText("Long Vibration");
		
		vib4.add(new JLabel(""));
		vib4.add(beat4Label);
		vib4.add(new JSeparator(SwingConstants.HORIZONTAL));
		vib4.add(intensityLow4);
		vib4.add(intensityHigh4);
		vib4.add(new JSeparator(SwingConstants.HORIZONTAL));

		vib4.add(shortVib4);
		vib4.add(longVib4);
		vib4.add(new JSeparator(SwingConstants.HORIZONTAL));
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(1, 4));
		eastPanel.add(vib);
		eastPanel.add(vib2);
		eastPanel.add(vib3);
		eastPanel.add(vib4);
		this.add(eastPanel, BorderLayout.EAST);
	}
	
	/**
	 * Sets up the south panel
	 * @param interf
	 */
	private void setupSouthPanel(Interface interf) {
		JPanel validate = new JPanel();
		validate.setLayout(new FlowLayout());
		validateSettings = new JButton("Validate Settings");
		validateSettings.setPreferredSize(new Dimension(500,40));
		validate.add(validateSettings);
		validateSettings.addActionListener(this);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		southPanel.add(validate);
		this.add(southPanel, BorderLayout.SOUTH);
	}

	/**
	 * Plays the audiofile and adds forces a pause so that the files are playing one after the other 
	 * and not all at the same time
	 * @param filename
	 * @param pause
	 */
	private void playFile(String filename, long pause){
		try{
		    File f = new File(filename);
		    AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());  
		    Clip clip = AudioSystem.getClip();
		    clip.open(audioIn);
		    clip.start();
		    TimeUnit.MILLISECONDS.sleep(pause);
		}
		catch (LineUnavailableException | UnsupportedAudioFileException | IOException | InterruptedException error2) {
	         error2.printStackTrace();
	      }
	};
	
	/**
	 * Adds to the results file the selected vibration/pause for each beat
	 * @param beat
	 * @param selec
	 * @param filename
	 */
	private void addToResults(int beat, String selec, String filename)
	{
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			File file = new File(filename);
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write(","+selec);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds to the results file the selected temperature
	 * @param filename
	 */
	private void addToFile(String filename)
	{
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			File file = new File(filename);
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write("\n" + j + "," +temperature.getValue());
			j++;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Defines what happens when a button is clicked
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == validateSettings) {
			changeScreen();
			addToFile("results-"+ participantId + ".txt");
		    whatIsSelected("results-"+ participantId + ".txt");
		    resetButtons();
			temperature.setValue(30);
		}
		else if (e.getSource() == resetVib)
		{
			resetButtons();
		}
		else if (e.getSource() == testVib)
		{
			if (intensityLow1.isSelected() && shortVib1.isSelected())
			{
				playFile("sounds/LowShort.wav", 350);
			}
			if (intensityLow1.isSelected() && longVib1.isSelected())
			{
				playFile("sounds/LowLong.wav", 750);
			}
			if (intensityHigh1.isSelected() && shortVib1.isSelected())
			{
				playFile("sounds/HighShort.wav", 350);
			}
			if (intensityHigh1.isSelected() && longVib1.isSelected())
			{
				playFile("sounds/HighLong.wav", 750);
			}


			if (intensityLow2.isSelected() && shortVib2.isSelected())
			{
				playFile("sounds/LowShort.wav", 350);
			}
			if (intensityLow2.isSelected() && longVib2.isSelected())
			{
				playFile("sounds/LowLong.wav", 750);
			}

			if (intensityHigh2.isSelected() && shortVib2.isSelected())
			{
				playFile("sounds/HighShort.wav", 350);
			}
			if (intensityHigh2.isSelected() && longVib2.isSelected())
			{
				playFile("sounds/HighLong.wav", 750);
			}
			if (pause2.isSelected())
			{
				try {
					TimeUnit.MILLISECONDS.sleep(400);
				} catch (InterruptedException error3) {
					error3.printStackTrace();
				}
			}
			
			if (intensityLow3.isSelected() && shortVib3.isSelected())
			{
				playFile("sounds/LowShort.wav", 350);
			}
			if (intensityLow3.isSelected() && longVib3.isSelected())
			{
				playFile("sounds/LowLong.wav", 750);
			}
			if (intensityHigh3.isSelected() && shortVib3.isSelected())
			{
				playFile("sounds/HighShort.wav", 350);
			}
			if (intensityHigh3.isSelected() && longVib3.isSelected())
			{
				playFile("sounds/HighLong.wav", 750);
			}
			if (pause3.isSelected())
			{
				try {
					TimeUnit.MILLISECONDS.sleep(400);
				} catch (InterruptedException error3) {
					error3.printStackTrace();
				}
			}
		
			if (intensityLow4.isSelected() && shortVib4.isSelected())
			{
				playFile("sounds/LowShort.wav", 350);
			}
			if (intensityLow4.isSelected() && longVib4.isSelected())
			{
				playFile("sounds/LowLong.wav", 750);
			}
			if (intensityHigh4.isSelected() && shortVib4.isSelected())
			{
				playFile("sounds/HighShort.wav", 350);
			}
			if (intensityHigh4.isSelected() && longVib4.isSelected())
			{
				playFile("sounds/HighLong.wav", 750);
			}
		}
	}

	/**
	 * Changes the image shown to the next one when called
	 */
	private void changeScreen()
	{
		if (imageLabel.getIcon() == screen1){
			imageLabel.setIcon(screen2);
		}
		else if(imageLabel.getIcon() == screen2){
			imageLabel.setIcon(screen3);
		}
		
		else if(imageLabel.getIcon() == screen3){
			imageLabel.setIcon(screen4);
		}
		else if(imageLabel.getIcon() == screen4){
			imageLabel.setIcon(screen5);
		}
		else if(imageLabel.getIcon() == screen5){
			imageLabel.setIcon(screen6);
		}
		else if(imageLabel.getIcon() == screen6){
			imageLabel.setIcon(screen7);
		}
		else if(imageLabel.getIcon() == screen7){
			imageLabel.setIcon(screen8);
		}
		else if(imageLabel.getIcon() == screen8){
			imageLabel.setIcon(screen9);
		}
		else if(imageLabel.getIcon() == screen9){
			imageLabel.setIcon(screen10);
		}
		else if(imageLabel.getIcon() == screen10){
			imageLabel.setIcon(screen11);
		}
		else if(imageLabel.getIcon() == screen11){
			imageLabel.setIcon(screen12);
		}
		else 
		{
			JOptionPane.showMessageDialog(null,
			    "Thank you!!");
		}
	}
	
	/**
	 * Uses the addToResults function to fill out the results file depending on user selection
	 * @param filename
	 */
	private void whatIsSelected(String filename)
	{
	//0 is pause, 1 is low short, 2 is low long, 3 is high short, 4 is low long
		if (intensityLow1.isSelected() && shortVib1.isSelected())
		{
			addToResults(1,"1", filename);
		}
		if (intensityLow1.isSelected() && longVib1.isSelected())
		{
			addToResults(1,"2", filename);
		}
		if (intensityHigh1.isSelected() && shortVib1.isSelected())
		{
			addToResults(1,"3", filename);
		}
		if (intensityHigh1.isSelected() && longVib1.isSelected())
		{
			addToResults(1,"4", filename);
		}
		
		if (intensityLow2.isSelected() && shortVib2.isSelected())
		{
			addToResults(2,"1", filename);
		}
		if (intensityLow2.isSelected() && longVib2.isSelected())
		{
			addToResults(2,"2", filename);
		}
		if (intensityHigh2.isSelected() && shortVib2.isSelected())
		{
			addToResults(2,"3", filename);
		}
		if (intensityHigh2.isSelected() && longVib2.isSelected())
		{
			addToResults(2,"4", filename);
		}
		if (pause2.isSelected())
		{
			addToResults(2,"0", filename);
		}
		if (intensityLow3.isSelected() && shortVib3.isSelected())
		{
			addToResults(3,"1", filename);
		}
		if (intensityLow3.isSelected() && longVib3.isSelected())
		{
			addToResults(3,"2", filename);
		}
		if (intensityHigh3.isSelected() && shortVib3.isSelected())
		{
			addToResults(3,"3", filename);
		}
		if (intensityHigh3.isSelected() && longVib3.isSelected())
		{
			addToResults(3,"4", filename);
		}
		if (pause3.isSelected())
		{
			addToResults(3,"0", filename);
		}
		if (intensityLow4.isSelected() && shortVib4.isSelected())
		{
			addToResults(4,"1", filename);
		}
		if (intensityLow4.isSelected() && longVib4.isSelected())
		{
			addToResults(4,"4", filename);
		}
		if (intensityHigh4.isSelected() && shortVib4.isSelected())
		{
			addToResults(4,"3", filename);
		}
		if (intensityHigh4.isSelected() && longVib4.isSelected())
		{
			addToResults(4,"4", filename);
		}
		
	}
	
	/**
	 * Resets the buttons selected by the user
	 */
	private void resetButtons()
	{
		lengthGroup1.clearSelection();
		lengthGroup2.clearSelection();
		lengthGroup3.clearSelection();
		lengthGroup4.clearSelection();
		intensityGroup1.clearSelection();
		intensityGroup2.clearSelection();
		intensityGroup3.clearSelection();
		intensityGroup4.clearSelection();
	}
	
	/**
	 * Sets up a change listener on the temperature slider
	 * @author Gabriel
	 *
	 */
	class SliderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	        JSlider source = (JSlider)e.getSource();
	        if (!source.getValueIsAdjusting()) {
	            int fps = (int)source.getValue();
	            System.out.println(fps);
	        }    
	    }
	}
}