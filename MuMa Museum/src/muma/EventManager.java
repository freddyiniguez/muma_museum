package muma;

/**
 * @author Choreros
 * @class EventManager.java
 * @version 0.1
 * @description This class's purpose is to manage the messages between the different devices connected to MuMa Software. 
 * It implements a RabbitMQ's queue to store the messages and to retrieve these ones when devices are ready to pull information.
 */

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import javax.swing.*;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class EventManager extends JFrame implements Runnable, ActionListener{
	// Variables
	private final static String QUEUE_NAME = "muma";
	private static final long serialVersionUID = 1L;
	private JMenuBar jmbTopBar;
	private JMenu jmFile;
	private JMenu jmHelp;
	private JMenuItem jmiExit;
	private JMenuItem jmiAbout;
	private JMenuItem jmiUserManual;
	
	/**
	 * @method Constructor
	 * @description It creates the EventManager responsible for communicate MuMa components.
	 */
	public EventManager(){
		if (connectToRabbit()){
			System.out.println(">>> SUCCESS! The connection with RabbitMQ was successfully.");
		}else{
			JOptionPane.showMessageDialog(null, "In order to execute MuMa Software, you must start RabbitMQ first.", "Error initializing MuMa Software", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	/**
	 * @method Constructor
	 * @description It creates the EventManager responsible for communicate MuMa components.
	 * @parameter Receives the name of the window.
	 */
	public EventManager(String windowTitle){
		if (connectToRabbit()){
			System.out.println(">>> SUCCESS! The connection with RabbitMQ was successfully.");
		}else{
			JOptionPane.showMessageDialog(null, "In order to execute MuMa Software, you must start RabbitMQ first.", "Error initializing MuMa Software", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		createsWindow(windowTitle);
	}
	
	/**
	 * @method createsWindow
	 * @description It creates the EventManager responsible for communicate MuMa components.
	 * @parameter Receives the title of the window.
	 */
	private void createsWindow(String windowTitle){
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
		
		this.setTitle(windowTitle);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600,600);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	/**
	 * @method ConnectToRabbit()
	 * @return True is connection was successful. False otherwise.
	 */
	private boolean connectToRabbit(){
		try{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare();
			return true;
		}catch(Exception e){
			System.out.println(">>> WARNING! We have encounter errors when trying to connect to RabbitMQ: " + e.getMessage());
			return false;
		}
	}

	@Override
	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(">>> INFO! MuMa Software is running.");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Exit MuMa Software
		if(e.getSource()==jmiExit){
			System.out.println(">>> INFO! Preparing to exit MuMa Software.");
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
	
	/**
	 * @method main
	 */
	public static void main(String[] args){
		EventManager muma = new EventManager("MuMa Museum: Security Software System");
		muma.run();
	}
}
