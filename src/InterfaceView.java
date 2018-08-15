
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class InterfaceView extends JFrame implements ActionListener{
	private JLabel labelTemp = new JLabel("  Temperature Settings      ");
	private JLabel labelTemp2 = new JLabel(" This corresponds to");

	private JLabel labelVibration = new JLabel(" Vibration Settings");
	private JLabel labelSec = new JLabel(" Rate the security level of this page: ");
	private JLabel labelPerso = new JLabel(" Would you enter personal information on this page?");

	
	public static JLabel peltierStatus = new JLabel();
	public static JLabel tempStatus = new JLabel(" ");

	long time_windowOpened, time_firstTempChange = 0, time_lastTempChange = 0;
	Thread tempTimerThread;
	final int cooldownTime = 45;
	JButton reconnect = new JButton("Reconnect");


	JButton validateSettings;
	JButton resetVib;
	JButton testVib;
	
	ArrayList<ImageIcon> listOfImages = new ArrayList<ImageIcon>();
	private int questNum=1;
		
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
	
	static final int secMin = 1;
	static final int secMax = 5;
	static final int secInit = 3; 
	
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
	
	JRadioButton securityLevel1 = new JRadioButton();
	JRadioButton securityLevel2 = new JRadioButton();
	JRadioButton securityLevel3 = new JRadioButton();
	JRadioButton securityLevel4 = new JRadioButton();
	JRadioButton securityLevel5 = new JRadioButton();
	ButtonGroup securityGroup = new ButtonGroup();
	
	JRadioButton persoYes = new JRadioButton();
	JRadioButton persoNo = new JRadioButton();
	ButtonGroup persoYN = new ButtonGroup();

	JRadioButton veryCold = new JRadioButton();
	JRadioButton cold = new JRadioButton();
	JRadioButton neutral = new JRadioButton();
	JRadioButton warm = new JRadioButton();
	JRadioButton veryWarm = new JRadioButton();
	ButtonGroup tempGroup = new ButtonGroup();

	
	private JSlider temperature = new JSlider();

	String participantId = JOptionPane.showInputDialog(this, "Participant ID:");
		
	//private Interface interf;

	
	/**
	 * Defines the basic of the interface
	 * @param interf
	 */
	public InterfaceView() {
		setupWestPanel();
		setupEastPanel();
		setupSouthPanel();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(1300, 750);
		setVisible(true);
		setTitle("Question "+questNum+"/12");
		setLocationRelativeTo(null);
		initListeners();

		tempTimerThread = new Thread();
		time_windowOpened = System.currentTimeMillis();
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	if (time_firstTempChange != 0){
		    		PeltierControl.cooldown((long) 30.0);
		    	}
		    }
		    });
	}


	/**
	 * Sets up the west panel
	 * @param interf
	 */
	private void setupWestPanel() {
		JPanel img = new JPanel();
		img.setLayout(new FlowLayout());
		img.add(imageLabel);
		
		listOfImages.add(screen1);
		listOfImages.add(screen2);
		listOfImages.add(screen3);
		listOfImages.add(screen4);
		listOfImages.add(screen5);
		listOfImages.add(screen6);
		listOfImages.add(screen7);
		listOfImages.add(screen8);
		listOfImages.add(screen9);
		listOfImages.add(screen10);
		listOfImages.add(screen11);
		listOfImages.add(screen12);
		chargeScreen();

		JPanel WestPanel = new JPanel();
		WestPanel.setLayout(new FlowLayout());
		WestPanel.add(img);
		this.add(WestPanel, BorderLayout.WEST);
	}
	
	/**
	 * Sets up the east panel
	 * @param interf
	 */
	private void setupEastPanel() {

		JPanel vib = new JPanel();		
		vib.setLayout(new GridLayout(14,1));
		
		JPanel vib2 = new JPanel();
		vib2.setLayout(new GridLayout(14,1));
		
		JPanel vib3 = new JPanel();
		vib3.setLayout(new GridLayout(14,1));
		
		JPanel vib4 = new JPanel();
		vib4.setLayout(new GridLayout(14,1));
		
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
	private void setupSouthPanel() {
		JPanel sec = new JPanel();
		sec.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel perso = new JPanel();
		perso.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel temp = new JPanel();
		temp.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel temp2 = new JPanel();
		temp2.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		
		JPanel temp3 = new JPanel();
		temp3.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		temp3.add(labelTemp2);
		temp3.add(veryCold);
		temp3.add(cold);
		temp3.add(neutral);
		temp3.add(warm);
		temp3.add(veryWarm);

		sec.add(labelSec);
		sec.add(securityLevel1);
		sec.add(securityLevel2);
		sec.add(securityLevel3);
		sec.add(securityLevel4);
		sec.add(securityLevel5);

		perso.add(labelPerso);
		perso.add(persoYes);
		perso.add(persoNo);
		
		persoYes.setText("Yes");
		persoYN.add(persoYes);
		persoNo.setText("No");
		persoYN.add(persoNo);


		securityGroup.add(securityLevel1);
		securityLevel1.setText("Very unsecure");
		securityGroup.add(securityLevel2);
		securityLevel2.setText("Unsecure");
		securityGroup.add(securityLevel3);
		securityLevel3.setText("Average");
		securityGroup.add(securityLevel4);
		securityLevel4.setText("Secure");
		securityGroup.add(securityLevel5);
		securityLevel5.setText("Very secure");
		
		tempGroup.add(veryCold);
		veryCold.setText("Very cold");
		tempGroup.add(cold);
		cold.setText("Cold");
		tempGroup.add(neutral);
		neutral.setText("Neutral");
		tempGroup.add(warm);
		warm.setText("Warm");
		tempGroup.add(veryWarm);
		veryWarm.setText("Very warm");
		
		
		reconnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				PeltierControl.init();
			}
		});
		
		temp.add(labelTemp);
		temp.add(temperature);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(220), new JLabel("22°C"));
		labelTable.put(new Integer(380), new JLabel("38°C"));

		temperature.setLabelTable(labelTable);
		temperature.setPaintLabels(true);

		temperature.setMinimum(220);
		temperature.setMaximum(380);
		temperature.setValue(300);
		

		peltierStatus = new JLabel(("              Connecting...  "));		
		temp.add(peltierStatus);
		temp.add(reconnect);
		temp2.add(tempStatus);

		JPanel validate = new JPanel();
		validate.setLayout(new FlowLayout());
		validateSettings = new JButton("Validate Settings");
		validateSettings.setPreferredSize(new Dimension(500,40));
		validate.add(validateSettings);
		validateSettings.addActionListener(this);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(6,1));
		southPanel.add(temp);
		southPanel.add(temp2);
		southPanel.add(temp3);
		southPanel.add(sec);
		southPanel.add(perso);
		southPanel.add(validate);
	
		this.add(southPanel, BorderLayout.SOUTH);
	}

	
	final Runnable tempControl = new Runnable()
	{
		public void run() {
			System.out.println("Setting the temperature to: "+Integer.toString(temperature.getValue()));
			PeltierControl.setPeltierTemperature((temperature.getValue()));
				
			for (int i = 10; i > 0; i--){
				System.out.println("changing the temperature");
				tempStatus.setText(" Setting temperature...");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				return;
				}
			}
			System.out.println("Finished setting the temperature to "+temperature.getValue());
			tempStatus.setText("Temperature Set");

		}
			
	};
	
	
	private void initListeners()
	{
		temperature.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			
			public void mouseReleased(MouseEvent arg0) {
				if (time_firstTempChange == 0){
					time_firstTempChange = System.currentTimeMillis();
					time_lastTempChange = System.currentTimeMillis();
				}
				if (tempTimerThread.isAlive()){
					tempTimerThread.interrupt();
				}
				tempTimerThread = new Thread(tempControl);
				tempTimerThread.start();
			}	
		});
		
		temperature.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				time_lastTempChange = System.currentTimeMillis();
			}
			
		});
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
	 * Adds to the results file the selected temperature for the current warning
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
			bw.write("\n" + currentScreen() + "," +temperature.getValue());
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
	 * Adds to the results file the selected buttons (vibration and other) for each beat
	 * @param selec
	 * @param filename
	 */
	private void addToResults(String selec, String filename)
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
	

	private void lineBreak(String filename)
	{
		BufferedWriter bw = null;
		FileWriter fw = null;
		String newline = System.getProperty("line.separator");
		try {
			File file = new File(filename);
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write(newline);
			
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
			addToFile("results-"+ participantId + ".txt");
			chargeScreen();
		    whatIsSelected("results-"+ participantId + ".txt");
		    lineBreak("results-"+ participantId + ".txt");
		    resetButtons();
		    securityGroup.clearSelection();
		    persoYN.clearSelection();
			temperature.setValue(300);
			PeltierControl.setPeltierTemperature(300);
			System.out.println("Resetting temperature to 30°C");
			questNum++;
			setTitle("Question "+questNum+"/12");
			if (questNum == 13)
			{
				System.exit(0);
			}
		}
		else if (e.getSource() == resetVib)
		{
			resetButtons();
		}
		else if (e.getSource() == testVib)
		{
			whatIsSelectedTestVib();
		}			
	}

	/**
	 * Returns a random index, to make sure images are shown in random order
	 * @return index
	 */
	private int randomIndex()
	{
		Random rand = new Random();
		int index = 0;
		if (listOfImages.size() != 0)
		{
			index = rand.nextInt(listOfImages.size())+1;
			return index;
		}
		else
			return index;
	}
	
	/**
	 * Returns the current screen/warning
	 * @return warningNumber
	 */
	private int currentScreen()
	{
		int warningNumber = 0;
		if (imageLabel.getIcon() == screen1){
			warningNumber = 1;
		}
		else if(imageLabel.getIcon() == screen2){
			warningNumber = 2;
		}
		
		else if(imageLabel.getIcon() == screen3){
			warningNumber = 3;
		}
		else if(imageLabel.getIcon() == screen4){
			warningNumber = 4;
		}
		else if(imageLabel.getIcon() == screen5){
			warningNumber = 5;
		}
		else if(imageLabel.getIcon() == screen6){
			warningNumber = 6;
		}
		else if(imageLabel.getIcon() == screen7){
			warningNumber = 7;
		}
		else if(imageLabel.getIcon() == screen8){
			warningNumber = 8;
		}
		else if(imageLabel.getIcon() == screen9){
			warningNumber = 9;
		}
		else if(imageLabel.getIcon() == screen10){
			warningNumber = 10;
		}
		else if(imageLabel.getIcon() == screen11){
			warningNumber = 11;
		}
		else if(imageLabel.getIcon() == screen12){
			warningNumber = 12;
		}
		return warningNumber;
	}
	
	/**
	 * Changes the screen to the next one, using the random function
	 * to pick an image in the image list
	 * then deletes that image from the list so it's not picked again
	 */
	private void chargeScreen()
	{
		int randindex = randomIndex();
		if (randindex !=0)
		{
			imageLabel.setIcon(listOfImages.get(randindex - 1));
			listOfImages.remove(randindex - 1);
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
		addToResults("corresponds", filename);
		if (veryCold.isSelected())
		{
			addToResults("1", filename);
		}
		if (cold.isSelected())
		{
			addToResults("2", filename);
		}
		if (neutral.isSelected())
		{
			addToResults("3", filename);
		}
		if (warm.isSelected())
		{
			addToResults("4", filename);
		}
		if (veryWarm.isSelected())
		{
			addToResults("5", filename);
		}
		
		addToResults("security level", filename);

		
		if (securityLevel1.isSelected())
		{
			addToResults("1", filename);
		}
		if (securityLevel2.isSelected())
		{
			addToResults("2", filename);
		}
		if (securityLevel3.isSelected())
		{
			addToResults("3", filename);
		}
		if (securityLevel4.isSelected())
		{
			addToResults("4", filename);
		}
		if (securityLevel5.isSelected())
		{
			addToResults("5", filename);
		}
		
		addToResults("personal info", filename);

		if (persoNo.isSelected())
		{
			addToResults("0", filename);
		}
		if (persoYes.isSelected())
		{
			addToResults("1", filename);
		}
		addToResults("vib pattern", filename);

		
	//0 is pause, 1 is low short, 2 is low long, 3 is high short, 4 is low long
		if (intensityLow1.isSelected() && shortVib1.isSelected())
		{
			addToResults("1", filename);
		}
		if (intensityLow1.isSelected() && longVib1.isSelected())
		{
			addToResults("2", filename);
		}
		if (intensityHigh1.isSelected() && shortVib1.isSelected())
		{
			addToResults("3", filename);
		}
		if (intensityHigh1.isSelected() && longVib1.isSelected())
		{
			addToResults("4", filename);
		}
		
		if (intensityLow2.isSelected() && shortVib2.isSelected())
		{
			addToResults("1", filename);
		}
		if (intensityLow2.isSelected() && longVib2.isSelected())
		{
			addToResults("2", filename);
		}
		if (intensityHigh2.isSelected() && shortVib2.isSelected())
		{
			addToResults("3", filename);
		}
		if (intensityHigh2.isSelected() && longVib2.isSelected())
		{
			addToResults("4", filename);
		}
		if (pause2.isSelected())
		{
			addToResults("0", filename);
		}
		if (intensityLow3.isSelected() && shortVib3.isSelected())
		{
			addToResults("1", filename);
		}
		if (intensityLow3.isSelected() && longVib3.isSelected())
		{
			addToResults("2", filename);
		}
		if (intensityHigh3.isSelected() && shortVib3.isSelected())
		{
			addToResults("3", filename);
		}
		if (intensityHigh3.isSelected() && longVib3.isSelected())
		{
			addToResults("4", filename);
		}
		if (pause3.isSelected())
		{
			addToResults("0", filename);
		}
		if (intensityLow4.isSelected() && shortVib4.isSelected())
		{
			addToResults("1", filename);
		}
		if (intensityLow4.isSelected() && longVib4.isSelected())
		{
			addToResults("4", filename);
		}
		if (intensityHigh4.isSelected() && shortVib4.isSelected())
		{
			addToResults("3", filename);
		}
		if (intensityHigh4.isSelected() && longVib4.isSelected())
		{
			addToResults("4", filename);
		}
	
	}
	
	/**
	 * Plays the audio file depending on what buttons are selected
	 */
	private void whatIsSelectedTestVib()
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
}
