package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import muma.EventManager;

public class MainMenu extends JFrame implements ActionListener {
	private static final String SENSOR_TEMPERATURE_ID = "-5";
	private static final String SENSOR_HUMIDITY_ID = "-4";
	private static final String CONTROLLER_TEMPERATURE_ID = "5";
	private static final String CONTROLLER_HUMIDITY_ID = "4";
	private static final String CHANGE_TEMPERATURE_ID = "CT";
	private static final String CHANGE_HUMIDITY_ID = "CH";
	private static final long serialVersionUID = 1L;
	
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
	private JTextField jtfHumidifier;
	private JTextField jtfDehumifier;
	private JTextField jtfChiller;
	private JTextField jtfHeater;
	
	
	/**
	 * @method Constructor
	 * @parameter Receives the title of the Window
	 */
	public MainMenu(String windowTitle){
		super(windowTitle);
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
		this.setSize(600,600);
		this.setLayout(new GridLayout(2,2));
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		jlHumidifier = new JLabel("Humidifier", JLabel.CENTER);
		jtfHumidifier = new JTextField("");
		jtfHumidifier.setBackground(Color.GRAY);
		JPanel firstQuarter = new JPanel(new BorderLayout());
		firstQuarter.add(jlHumidifier, BorderLayout.NORTH);
		firstQuarter.add(jtfHumidifier, BorderLayout.CENTER);
		this.add(firstQuarter);
		
		jlDehumidifier = new JLabel("Dehumidifier", JLabel.CENTER);
		jtfDehumifier = new JTextField("");
		jtfDehumifier.setBackground(Color.GRAY);
		JPanel secondQuarter = new JPanel(new BorderLayout());
		secondQuarter.add(jlDehumidifier, BorderLayout.NORTH);
		secondQuarter.add(jtfDehumifier, BorderLayout.CENTER);
		this.add(secondQuarter);
		
		jlChiller = new JLabel("Chiller", JLabel.CENTER);
		jtfChiller = new JTextField("");
		jtfChiller.setBackground(Color.GRAY);
		JPanel thirdQuarter = new JPanel(new BorderLayout());
		thirdQuarter.add(jlChiller, BorderLayout.NORTH);
		thirdQuarter.add(jtfChiller, BorderLayout.CENTER);
		this.add(thirdQuarter);
		
		jlHeater = new JLabel("Heater", JLabel.CENTER);
		jtfHeater = new JTextField("");
		jtfHeater.setBackground(Color.GRAY);
		JPanel fourthQuarter = new JPanel(new BorderLayout());
		fourthQuarter.add(jlHeater, BorderLayout.NORTH);
		fourthQuarter.add(jtfHeater, BorderLayout.CENTER);
		this.add(fourthQuarter);
		
		this.setVisible(true);
	}
	
	/** 
	 * @method updateDevices
	 * @description Receives the action to change the status of a device
	 */
	public void updateDevices(String action){
		switch(action){
			case "Hu1":		// Humidifier ON
				jtfHumidifier.setBackground(Color.GREEN);
				break;
			case "Hu0":		// Humidifier OFF
				jtfHumidifier.setBackground(Color.GRAY);
				break;
			case "De1":		// Dehumidifier ON
				jtfDehumifier.setBackground(Color.GREEN);
				break;
			case "De0":		// Dehumidifier OFF
				jtfDehumifier.setBackground(Color.GRAY);
				break;
			case "He1":		// Heater ON
				jtfHeater.setBackground(Color.GREEN);
				break;
			case "He0":		// Heater OFF
				jtfHeater.setBackground(Color.GRAY);
				break;
			case "Ch1":		// Chiller ON
				jtfChiller.setBackground(Color.GREEN);
				break;
			case "Ch0":		// Chiller OFF
				jtfChiller.setBackground(Color.GRAY);
				break;
			default:
				jtfHumidifier.setBackground(Color.GRAY);
				jtfDehumifier.setBackground(Color.GRAY);
				jtfHeater.setBackground(Color.GRAY);
				jtfChiller.setBackground(Color.GRAY);
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
				Desktop.getDesktop().open(new File("src/manuals/userManual.pdf"));
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "The User's Manual could not be found.", "File not found.", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
