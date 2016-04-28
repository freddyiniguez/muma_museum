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

import controllers.HumidityController;

import javax.swing.*;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import sensors.BrokenDoorSensor;
import sensors.BrokenWindowSensor;
import sensors.HumiditySensor;
import controllers.HumidityController;


public class EventManager extends JFrame implements Runnable, ActionListener{
	private static final String QUEUE_NAME = "muma";
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
	private JLabel jlTemperature;
	private JTextField jtfTemperature;
	private JButton jbTemperature;
	private JLabel jlHumidity;
	private JTextField jtfHumidity;
	private JButton jbHumidity;
	
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
		
		// this.setVisible(true);
	}
	
	/**
	 * @method connectToRabbit
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
			System.out.println(">>> [EVENT MANAGER] WARNING! We have encounter errors when trying to connect to RabbitMQ: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * @method sendMessage
	 * @parameter Receives two parameter. The first one is an identifier to the action to take. The second one is a message.
	 * @return True is the message was correctly sent.
	 */
	private boolean sendMessage(String CHANNEL_SEND_ID, String message){
		try{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			// Sends the message
			channel.exchangeDeclare(CHANNEL_SEND_ID, "fanout");
			channel.basicPublish(CHANNEL_SEND_ID, "", null, message.getBytes("UTF-8"));
			channel.close();
			connection.close();
			return true;
		}catch(IOException|TimeoutException e){
			System.out.println(">>> [EVENT MANAGER] ERROR! The Message could not be delivered: \n" + e.getMessage());
			return false;
		}
	}
	
	/**
	 * @method receiveMessageFromTemperatureSensor
	 * @description Receive a message from the Temperature sensor.
	 */
	private void receiveMessageFromTemperatureController(){
		try{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			// Receives the message
			channel.exchangeDeclare(SENSOR_TEMPERATURE_ID, "fanout");
			channel.queueBind(QUEUE_NAME, SENSOR_TEMPERATURE_ID, "");
			// String queueName = channel.queueDeclare().getQueue(); When you unknown the queue name.
			Consumer consumer;
			consumer = new DefaultConsumer(channel){
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
					System.out.println(">>> INFO! I received a message from the Temperature sensor: " + new String(body, "UTF-8"));
				}
			};
			channel.basicConsume(QUEUE_NAME, true, consumer);
			channel.close();
			connection.close();
		}catch(IOException|TimeoutException e){
			System.out.println(">>> [EVENT MANAGER] ERROR! The Message could not be received: \n" + e.getMessage());
		}
	}
	
	/**
	 * @method receiveMessageFromHumiditySensor
	 * @description Receive a message from the Humidity sensor.
	 */
	private void receiveMessageFromHumidityController(){
		try{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			// Receives the message
			channel.exchangeDeclare(SENSOR_HUMIDITY_ID, "fanout");
			channel.queueBind(QUEUE_NAME, SENSOR_HUMIDITY_ID, "");
			// String queueName = channel.queueDeclare().getQueue(); When you unknown the queue name.
			Consumer consumer;
			consumer = new DefaultConsumer(channel){
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
					System.out.println(">>> INFO! I received a message from the Humidity sensor: " + new String(body, "UTF-8"));
				}
			};
			channel.basicConsume(QUEUE_NAME, true, consumer);
			channel.close();
			connection.close();
		}catch(IOException|TimeoutException e){
			System.out.println(">>> [EVENT MANAGER] ERROR! The Message could not be received: \n" + e.getMessage());
		}
	}

	@Override
	public void run() {
		System.out.println(">>> [EVENT MANAGER] INFO! MuMa Software is running.");
		while(true){
			try {
				Thread.sleep(1000);
				// receiveMessageFromTemperatureController();
				// receiveMessageFromHumidityController();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		
		// Change Temperature
		if (e.getSource()==jbTemperature){
			if (sendMessage(CHANGE_TEMPERATURE_ID, jtfTemperature.getText())){
				System.out.println(">>> INFO! The temperature has changed.");
			}else{
				System.out.println(">>> ERROR! There was an error changing the temperature.");
			}
		}
		
		// Change Humidity
		if (e.getSource()==jbHumidity){
			if(sendMessage(CONTROLLER_HUMIDITY_ID, jtfHumidity.getText())){
				System.out.println(">>> INFO! The humidity has changed.");
			}else{
				System.out.println(">>> ERROR! There was an error changing the humidity");
			}
		}
	}
	
	/**
	 * @method main
	 */
	public static void main(String[] args){
		new Thread(new EventManager("MuMa Museum: Security Software System")).start();
		
		HumidityController humidityController = HumidityController.getInstance();
		new Thread(humidityController).start();
		
		HumiditySensor humiditySensor = HumiditySensor.getInstance();
		new Thread(humiditySensor).start();
		
		BrokenWindowSensor  windowSensor = BrokenWindowSensor.getInstance();
		new Thread(windowSensor).start();
		
		BrokenDoorSensor doorSensor = BrokenDoorSensor.getInstance();
		new Thread(doorSensor).start();
	}
}
