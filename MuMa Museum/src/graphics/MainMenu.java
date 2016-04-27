package graphics;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import muma.EventManager;

public class MainMenu extends JFrame implements ActionListener {
	private static final String SENSOR_TEMPERATURE_ID = "-5";
	private static final String SENSOR_HUMIDITY_ID = "-4";
	private static final String CONTROLLER_TEMPERATURE_ID = "5";
	private static final String CONTROLLER_HUMIDITY_ID = "4";
	private static final String CHANGE_TEMPERATURE_ID = "CT";
	private static final String CHANGE_HUMIDITY_ID = "CH";
	private static final long serialVersionUID = 1L;
	
	private EventManager em;
	
	private JMenuBar jmbTopBar;
	private JMenu jmFile;
	private JMenu jmHelp;
	private JMenuItem jmiExit;
	private JMenuItem jmiAbout;
	private JMenuItem jmiUserManual;
	private JLabel jlTemperature;
	private JTextField jtfTemperature;
	private JButton jbTemperature;
	private JLabel jlHumidity;
	private JTextField jtfHumidity;
	private JButton jbHumidity;
	
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
		this.setLayout(new GridLayout(2,3));
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		jlTemperature = new JLabel("Temperature");
		jtfTemperature = new JTextField("Temperature");
		jbTemperature = new JButton("Change Temperature");
		jbTemperature.addActionListener(this);
		this.add(jlTemperature);
		this.add(jtfTemperature);
		this.add(jbTemperature);
		
		jlHumidity = new JLabel("Humidity");
		jtfHumidity = new JTextField("Humidity");
		jbHumidity = new JButton("Change Humidity");
		jbHumidity.addActionListener(this);
		this.add(jlHumidity);
		this.add(jtfHumidity);
		this.add(jbHumidity);
		
		this.setVisible(true);
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
		
		// Change Temperature
		if (e.getSource()==jbTemperature){
			if (em.sendMessage(CHANGE_TEMPERATURE_ID, jtfTemperature.getText())){
				System.out.println(">>> INFO! The temperature has changed.");
			}else{
				System.out.println(">>> ERROR! There was an error changing the temperature.");
			}
		}
		
		// Change Humidity
		if (e.getSource()==jbHumidity){
			if(em.sendMessage(CONTROLLER_HUMIDITY_ID, jtfHumidity.getText())){
				System.out.println(">>> INFO! The humidity has changed.");
			}else{
				System.out.println(">>> ERROR! There was an error changing the humidity");
			}
		}
	}
}
