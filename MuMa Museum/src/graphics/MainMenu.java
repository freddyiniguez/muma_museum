/**
 * **************************************************************************************
 * File: MainMenu.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class is responsible to update all devices connected to MuMa Software and its 
 * values, like temperature degrees or humidity percentage.
 * **************************************************************************************
 */

package graphics;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainMenu extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static MainMenu INSTANCE = new MainMenu();
	
	private JMenuBar jmbTopBar;
	private JMenu jmFile;
	private JMenu jmHelp;
	private JMenuItem jmiExit;
	private JMenuItem jmiAbout;
	private JMenuItem jmiUserManual;
	private JLabel jlHumidifier;
	private JLabel jlDehumidifier;
	private JLabel jlChiller;
	private JLabel jlHeater;
	private JLabel jlHumidifierMeasure;
	private JLabel jlDehumidifierMeasure;
	private JLabel jlChillerMeasure;
	private JLabel jlHeaterMeasure;
	private JLabel jlHumidifierIcon;
	private JLabel jlDehumidifierIcon;
	private JLabel jlChillerIcon;
	private JLabel jlHeaterIcon;	
	private JLabel jlWindowAlarm;
	private JLabel jlWindow;
	private JLabel jlDoorAlarm;
	private JLabel jlDoor;
	private JLabel jlMovementAlarm;
	private JLabel jlMovement;
	private JLabel jlIntruderAlarm;
	private JLabel jlIntruder;
	private JLabel jlFireAlarm;
	private JLabel jlFire;
	private JLabel jlSprinklersAlarm;
	private JLabel jlSprinklers;
	
	/**
	 * @method Constructor
	 * @parameter Receives the title of the Window
	 */
	public MainMenu(){
		super("MuMa Museum: Security and Control Software System");
		createsWindow();
	}
	
	/**
	 * @method createsWindow
	 * @description It creates the EventManager responsible for communicate MuMa components.
	 */
	private void createsWindow(){
		jmbTopBar = new JMenuBar();
		jmFile = new JMenu("File");
		jmiExit = new JMenuItem("Exit");
		jmiExit.addActionListener(this);
		jmFile.add(jmiExit);
		jmbTopBar.add(jmFile);
		
		jmHelp = new JMenu("Help");
		jmiAbout = new JMenuItem("About Muma");
		jmiAbout.addActionListener(this);
		jmiUserManual = new JMenuItem("See User Manual");
		jmiUserManual.addActionListener(this);
		jmHelp.add(jmiUserManual);
		jmHelp.addSeparator();
		jmHelp.add(jmiAbout);
		jmbTopBar.add(jmHelp);
		
		this.setJMenuBar(jmbTopBar);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800,600);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		JPanel mainPanel = new JPanel(new GridLayout(2,2));
		
		jlHumidifier = new JLabel("Humidifier", JLabel.CENTER);
		jlHumidifierMeasure = new JLabel("Initializing...", JLabel.CENTER);
		BufferedImage biHumidifierIcon;
		try{
			biHumidifierIcon = ImageIO.read(this.getClass().getResource("humidifier_off.png"));
			jlHumidifierIcon = new JLabel(new ImageIcon(biHumidifierIcon));
			jlHumidifierIcon.setToolTipText("HUMIDIFIER: This device helps to increase the percentage of the humidity in the environment.");
		}catch(IOException e){
			e.printStackTrace();
		}
		JPanel firstQuarter = new JPanel(new BorderLayout());
		firstQuarter.add(jlHumidifier, BorderLayout.NORTH);
		firstQuarter.add(jlHumidifierIcon, BorderLayout.CENTER);
		firstQuarter.add(jlHumidifierMeasure, BorderLayout.SOUTH);
		mainPanel.add(firstQuarter);
		
		jlDehumidifier = new JLabel("Dehumidifier", JLabel.CENTER);
		jlDehumidifierMeasure = new JLabel("Initializing...", JLabel.CENTER);
		BufferedImage biDehumidifierIcon;
		try{
			biDehumidifierIcon = ImageIO.read(this.getClass().getResource("dehumidifier_off.png"));
			jlDehumidifierIcon = new JLabel(new ImageIcon(biDehumidifierIcon));
			jlDehumidifierIcon.setToolTipText("DEHUMIFIER: This device helps to decrease the percentage of the humidity in the environment.");
		}catch(IOException e){
			e.printStackTrace();
		}
		JPanel secondQuarter = new JPanel(new BorderLayout());
		secondQuarter.add(jlDehumidifier, BorderLayout.NORTH);
		secondQuarter.add(jlDehumidifierIcon, BorderLayout.CENTER);
		secondQuarter.add(jlDehumidifierMeasure, BorderLayout.SOUTH);
		mainPanel.add(secondQuarter);
		
		jlChiller = new JLabel("Chiller", JLabel.CENTER);
		jlChillerMeasure = new JLabel("Initializing...", JLabel.CENTER);
		BufferedImage biChillerIcon;
		try{
			biChillerIcon = ImageIO.read(this.getClass().getResource("chiller_off.png"));
			jlChillerIcon = new JLabel(new ImageIcon(biChillerIcon));
			jlChillerIcon.setToolTipText("CHILLER: This device decrease the temperature of the environment. The measurements are in Fahrenheit degrees.");
		}catch(IOException e){
			e.printStackTrace();
		}
		JPanel thirdQuarter = new JPanel(new BorderLayout());
		thirdQuarter.add(jlChiller, BorderLayout.NORTH);
		thirdQuarter.add(jlChillerIcon, BorderLayout.CENTER);
		thirdQuarter.add(jlChillerMeasure, BorderLayout.SOUTH);
		mainPanel.add(thirdQuarter);
		
		jlHeater = new JLabel("Heater", JLabel.CENTER);
		jlHeaterMeasure = new JLabel("Initializing...", JLabel.CENTER);
		BufferedImage biHeaterIcon;
		try{
			biHeaterIcon = ImageIO.read(this.getClass().getResource("heater_off.png"));
			jlHeaterIcon = new JLabel(new ImageIcon(biHeaterIcon));
			jlHeaterIcon.setToolTipText("HEATER: This device helps to increase the temperature of the environment. The measurements are in Fahrenheit degrees.");
		}catch(IOException e){
			e.printStackTrace();
		}
		JPanel fourthQuarter = new JPanel(new BorderLayout());
		fourthQuarter.add(jlHeater, BorderLayout.NORTH);
		fourthQuarter.add(jlHeaterIcon, BorderLayout.CENTER);
		fourthQuarter.add(jlHeaterMeasure, BorderLayout.SOUTH);
		mainPanel.add(fourthQuarter);
		
		this.add(mainPanel, BorderLayout.CENTER);
		
		JPanel leftPanel = new JPanel(new GridLayout(6,1));
		
		jlMovement = new JLabel("Movement Alarm");
		BufferedImage biMovement;
		try{
			biMovement = ImageIO.read(this.getClass().getResource("alarm_off.png"));
			jlMovementAlarm = new JLabel(new ImageIcon(biMovement));
		}catch(IOException e){
			e.printStackTrace();
		}
		leftPanel.add(jlMovement, JLabel.CENTER);
		leftPanel.add(jlMovementAlarm, JLabel.CENTER);
		
		jlDoor = new JLabel("Door Alarm");
		BufferedImage biDoor;
		try{
			biDoor = ImageIO.read(this.getClass().getResource("alarm_off.png"));
			jlDoorAlarm = new JLabel(new ImageIcon(biDoor));
		}catch(IOException e){
			e.printStackTrace();
		}
		leftPanel.add(jlDoor, JLabel.CENTER);
		leftPanel.add(jlDoorAlarm, JLabel.CENTER);
		
		jlWindow = new JLabel("Window Alarm");
		BufferedImage biWindow;
		try {
			biWindow = ImageIO.read(this.getClass().getResource("alarm_off.png"));
			jlWindowAlarm = new JLabel(new ImageIcon(biWindow));
		} catch (IOException e) {
			e.printStackTrace();
		}
		leftPanel.add(jlWindow, JLabel.CENTER);
		leftPanel.add(jlWindowAlarm, JLabel.CENTER);
		
		JPanel rightPanel = new JPanel(new GridLayout(6,1));
		
		jlSprinklers = new JLabel("Sprinklers Alarm");
		BufferedImage biSprinklers;
		try{
			biSprinklers = ImageIO.read(this.getClass().getResource("alarm_off.png"));
			jlSprinklersAlarm = new JLabel(new ImageIcon(biSprinklers));
		}catch (IOException e){
			e.printStackTrace();
		}
		rightPanel.add(jlSprinklers, JLabel.CENTER);
		rightPanel.add(jlSprinklersAlarm, JLabel.CENTER);
		
		jlFire = new JLabel("Fire Alarm");
		BufferedImage biFire;
		try{
			biFire = ImageIO.read(this.getClass().getResource("alarm_off.png"));
			jlFireAlarm = new JLabel(new ImageIcon(biFire));
		}catch (IOException e){
			e.printStackTrace();
		}
		rightPanel.add(jlFire, JLabel.CENTER);
		rightPanel.add(jlFireAlarm, JLabel.CENTER);
		
		jlIntruder = new JLabel("Intruder Alarm");
		BufferedImage biIntruder;
		try{
			biIntruder = ImageIO.read(this.getClass().getResource("alarm_off.png"));
			jlIntruderAlarm = new JLabel(new ImageIcon(biIntruder));
		}catch (IOException e){
			e.printStackTrace();
		}
		rightPanel.add(jlIntruder, JLabel.CENTER);
		rightPanel.add(jlIntruderAlarm, JLabel.CENTER);
		
		this.add(leftPanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.EAST);
		
		this.setVisible(true);
	}
	
	/** 
	 * @method updateDevices
	 * @description Receives the action to change the status of a device
	 */
	public void updateDevices(String action){
		switch(action){
			case "Hu1":		// Humidifier ON
				jlHumidifierIcon.setIcon(new ImageIcon(this.getClass().getResource("humidifier_on.png")));
				revalidate();
				repaint();
				break;
			case "Hu0":		// Humidifier OFF
				jlHumidifierIcon.setIcon(new ImageIcon(this.getClass().getResource("humidifier_off.png")));
				revalidate();
				repaint();
				break;
			case "De1":		// Dehumidifier ON
				jlDehumidifierIcon.setIcon(new ImageIcon(this.getClass().getResource("dehumidifier_on.png")));
				revalidate();
				repaint();
				break;
			case "De0":		// Dehumidifier OFF
				jlDehumidifierIcon.setIcon(new ImageIcon(this.getClass().getResource("dehumidifier_off.png")));
				revalidate();
				repaint();
				break;
			case "He1":		// Heater ON
				jlHeaterIcon.setIcon(new ImageIcon(this.getClass().getResource("heater_on.png")));
				revalidate();
				repaint();
				break;
			case "He0":		// Heater OFF
				jlHeaterIcon.setIcon(new ImageIcon(this.getClass().getResource("heater_off.png")));
				revalidate();
				repaint();
				break;
			case "He2":		// Heater Malfunction ON
				jlHeaterIcon.setIcon(new ImageIcon(this.getClass().getResource("heater_warning.png")));
				revalidate();
				repaint();
				break;
			case "Ch1":		// Chiller ON
				jlChillerIcon.setIcon(new ImageIcon(this.getClass().getResource("chiller_on.png")));
				revalidate();
				repaint();
				break;
			case "Ch0":		// Chiller OFF
				jlChillerIcon.setIcon(new ImageIcon(this.getClass().getResource("chiller_off.png")));
				revalidate();
				repaint();
				break;
			case "Ch3":		// Chiller get wrong by usage ON
				jlChillerIcon.setIcon(new ImageIcon(this.getClass().getResource("chiller_wrong.png")));
				revalidate();
				repaint();
				break;
			case "Wi1":		// Window Alarm ON
				jlWindowAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_on.png")));
				revalidate();
				repaint();
				break;
			case "Wi0":		// Window Alarm OFF
				jlWindowAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
				break;
			case "Do1":		// Door Alarm ON
				jlDoorAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_on.png")));
				revalidate();
				repaint();
				break;
			case "Do0":		// Door Alarm OFF
				jlDoorAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
				break;
			case "Mo1":		// Movement Alarm ON
				jlMovementAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_on.png")));
				revalidate();
				repaint();
				break;
			case "Mo0":		// Movement Alarm OFF
				jlMovementAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
				break;
			case "In1":		// Intruder Alarm ON
				jlIntruderAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_on.png")));
				revalidate();
				repaint();
				break;
			case "In0":		// Intruder Alarm OFF
				jlIntruderAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
				break;
			case "Fi1":		// Fire Alarm ON
				jlFireAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_on.png")));
				revalidate();
				repaint();
				break;
			case "Fi0":		// Fire Alarm OFF
				jlFireAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
				break;
			case "Sp1":		// Sprinklers Alarm ON
				jlSprinklersAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_on.png")));
				revalidate();
				repaint();
				break;
			case "Sp0":		// Sprinklers Alarm OFF
				jlSprinklersAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
				break;
			default:
				jlDehumidifierIcon.setIcon(new ImageIcon(this.getClass().getResource("dehumidifier_off.png")));
				revalidate();
				repaint();
				jlHeaterIcon.setIcon(new ImageIcon(this.getClass().getResource("heater_on.png")));
				revalidate();
				repaint();
				jlHeaterIcon.setIcon(new ImageIcon(this.getClass().getResource("heater_off.png")));
				revalidate();
				repaint();
				jlChillerIcon.setIcon(new ImageIcon(this.getClass().getResource("chiller_on.png")));
				revalidate();
				repaint();
				
				jlChillerIcon.setIcon(new ImageIcon(this.getClass().getResource("chiller_off.png")));
				revalidate();
				repaint();
				jlWindowAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
				jlDoorAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
				jlMovementAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
				jlIntruderAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
				jlFireAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
				jlSprinklersAlarm.setIcon(new ImageIcon(this.getClass().getResource("alarm_off.png")));
				revalidate();
				repaint();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Exit MuMa Software
		if(e.getSource()==jmiExit){
			System.out.println(">>> [EVENT MANAGER] INFO! Preparing to exit MuMa Software.");
			System.exit(0);
		}
		
		// Open the User Manual
		if(e.getSource()==jmiUserManual){
			try {
				String inputPdf = "manuals/userManual.pdf";
				Path tempOutput = Files.createTempFile("TempManual", ".pdf");
				tempOutput.toFile().deleteOnExit();
				InputStream is = MainMenu.class.getClassLoader().getResourceAsStream(inputPdf);
				Files.copy(is, tempOutput, StandardCopyOption.REPLACE_EXISTING);

				Desktop.getDesktop().open(tempOutput.toFile());
				} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "The User's Manual could not be found.", "File not found.", JOptionPane.ERROR_MESSAGE);
				}
		}
		
		// About MuMa Software
		if(e.getSource()==jmiAbout){
			JOptionPane.showMessageDialog(null, "Release Candidate v2.0\n This version of the MuMa Software is an upgrade made by Choreros, students of the Mathematics Research Center (CIMAT)", "About MuMa Software", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (MainMenu.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MainMenu();
                }
            }
        }
    }
	
	public static MainMenu getInstance() {
		if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
	}
	
	/**
	 * @method Setters
	 * @description Setters methods to update the devices information.
	 */
	public void setHumidity(String humidityMeasure){
		this.jlHumidifierMeasure.setText("Humidity: " + humidityMeasure + "%");
		this.jlDehumidifierMeasure.setText("Humidity: " + humidityMeasure + "%");
	}
	
	public void setTemperature(String temperatureMeasure){
		this.jlHeaterMeasure.setText("Temperature: " + temperatureMeasure + "ºF");
		this.jlChillerMeasure.setText("Temperature: " + temperatureMeasure + "ºF");
	}
}
