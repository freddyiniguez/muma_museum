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
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import controllers.DoorController;
import controllers.HumidityController;
import graphics.MainMenu;
import controllers.TemperatureController;
import controllers.WindowController;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import sensors.DoorSensor;
import sensors.HumiditySensor;
import sensors.TemperatureSensor;
import sensors.WindowSensor;

public class EventManager implements Runnable{
	private static final String QUEUE_NAME = "muma";
	private static final String SENSOR_HUMIDITY_ID = "-4";
	private static final String SENSOR_TEMPERATURE_ID = "-5";
	private static final String SENSOR_WINDOW_ID = "-6";
	private static final String SENSOR_DOOR_ID = "-7";
	private static final String SENSOR_MOVEMENT_ID = "-8";
	private static final String SENSOR_INTRUDER_ID = "-9";
	private static final String SENSOR_FIRE_ID = "-10";
	private static final String SENSOR_SPRINKLERS_ID = "-11";
	
	private static MainMenu mmMuma = MainMenu.getInstance();
	private static TemperatureController temperatureController = TemperatureController.getInstance();
	private static TemperatureSensor temperatureSensor = TemperatureSensor.getInstance();
	private static HumidityController humidityController = HumidityController.getInstance();
	private static HumiditySensor humiditySensor = HumiditySensor.getInstance();
	private static WindowController windowController = WindowController.getInstance();
	private static WindowSensor windowSensor = WindowSensor.getInstance();
	private static DoorController doorController = DoorController.getInstance();
	private static DoorSensor doorSensor = DoorSensor.getInstance();
	
	/**
	 * @method Constructor
	 * @description It creates the EventManager responsible for communicate MuMa components.
	 */
	public EventManager(){
		if (connectToRabbit()){
			System.out.println(">>> [EVENT MANAGER] SUCCESS! The connection with RabbitMQ was successfully.");
		}else{
			JOptionPane.showMessageDialog(null, 
					"In order to execute MuMa Software, you must start RabbitMQ first.", 
					"Error initializing MuMa Software", 
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
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
	public boolean sendMessage(String CHANNEL_SEND_ID, String message){
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
	protected void receiveMessageFromTemperatureController(){
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
	protected void receiveMessageFromHumidityController(){
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
			try{
				Thread.sleep(20);
				// Update all the devices state and its measurements
				if (humidityController.isHumidifierState()){
					mmMuma.updateDevices("Hu1");
				}else{
					mmMuma.updateDevices("Hu0");
				}
				if(humidityController.isDehumidifierState()){
					mmMuma.updateDevices("De1");
				}else{
					mmMuma.updateDevices("De0");
				}
				
				if(temperatureController.isChillerState()){
					mmMuma.updateDevices("Ch1");
				}else{
					mmMuma.updateDevices("Ch0");
				}
				if(temperatureController.isHeaterState()){
					mmMuma.updateDevices("He1");
				}else{
					mmMuma.updateDevices("He0");
				}
				
				if(windowController.isWindowState()){
					mmMuma.updateDevices("Wi1");
				}else{
					mmMuma.updateDevices("Wi0");
				}
				
				if(doorController.isDoorState()){
					mmMuma.updateDevices("Do1");
				}else{
					mmMuma.updateDevices("Do0");
				}
				
				mmMuma.setTemperature("" + temperatureController.getCurrentTemperature());
				mmMuma.setHumidity("" + humidityController.getCurrentHumidity()); 
				
				// The two following methods are just for testing purposes.
				// receiveMessageFromTemperatureController();
				// receiveMessageFromHumidityController();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @method main
	 */
	public static void main(String[] args){
		@SuppressWarnings("unused")
		MainMenu mmMuma = MainMenu.getInstance();
		new Thread(new EventManager()).start();
		
		humidityController = HumidityController.getInstance();
		new Thread(humidityController).start();
		
		humiditySensor = HumiditySensor.getInstance();
		new Thread(humiditySensor).start();

		temperatureController = TemperatureController.getInstance();
		new Thread(temperatureController).start();
		
		temperatureSensor = TemperatureSensor.getInstance();
		new Thread(temperatureSensor).start();
		
		windowController = WindowController.getInstance();
		new Thread(windowController).start();
		
		windowSensor = WindowSensor.getInstance();
		new Thread(windowSensor).start();
		
		doorController = DoorController.getInstance();
		new Thread(doorController).start();
		
		doorSensor = DoorSensor.getInstance();
		new Thread(doorSensor).start();
	}
}
