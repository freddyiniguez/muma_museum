/**
 * **************************************************************************************
 * File:Controller.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class contains the necessary to build a controller, in order to every 
 * controller extends from this.
 * **************************************************************************************
 */
package controllers;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import common.Component;
import event.Event;
import event.EventManagerInterface;

public class Controller extends Component {
    protected int delay = 2500;				// The loop delay (2.5 seconds)
    protected boolean isDone = false;			// Loop termination flag
    private static final String QUEUE_NAME = "muma";
    
    protected Controller() {
        super();
    }
    
    /**
     * This method posts the specified message to the specified event manager
     * 
     * @param ei This is the eventmanger interface where the event will be posted.
     * @param evtId This is the ID to identify the type of event
     * @param m This is the received command.
     */
    protected void confirmMessage(EventManagerInterface ei, int evtId, String m) {
        // Here we create the event.
        Event evt = new Event(evtId, m);
	// Here we send the event to the event manager.
        try {
            ei.sendEvent(evt);
        } // try
        catch (Exception e) {
            System.out.println("Error Confirming Message:: " + e);
        } // catch
    } // PostMessage
    
    /**
	 * @method sendMessage
	 * @parameter Receives two parameter. The first one is an identifier to the action to take. The second one is a message.
	 * @return True is the message was correctly sent.
	 */
	protected boolean sendMessage(String CHANNEL_SEND_ID, String message){
		try{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			// Sends the message
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			channel.exchangeDeclare(CHANNEL_SEND_ID, "fanout");
			channel.basicPublish("", QUEUE_NAME, false, false, null, message.getBytes("UTF-8"));
			channel.close();
			connection.close();
			return true;
		}catch(IOException|TimeoutException e){
			System.out.println(">>> [HUMIDITY CONTROLLER] ERROR! The Message could not be delivered: \n" + e.getMessage());
			return false;
		}
	}
	
	/**
	 * @method receiveMessage
	 * @parameter Receives the identifier for the HumiditySensor
	 * @return True is the message was correctly retrieved.
	 */
	protected void receiveMessage(String CHANNEL_HUMIDITY_SENSOR_ID){
		try{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			// Receives the message
			channel.exchangeDeclare(CHANNEL_HUMIDITY_SENSOR_ID, "fanout");
			channel.queueBind(QUEUE_NAME, CHANNEL_HUMIDITY_SENSOR_ID, "");
			Consumer consumer;
			consumer = new DefaultConsumer(channel){
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
					System.out.println(">>> [HUMIDITY CONTROLLER] SUCESS! I received a message from the Humidity Sensor: " + new String(body, "UTF-8"));
				}
			};
			channel.basicConsume(QUEUE_NAME, true, consumer);
			channel.close();
			connection.close();
		}catch(IOException|TimeoutException e){
			System.out.println(">>> [HUMIDITY CONTROLLER] ERROR! The Message could not be received: \n" + e.getMessage());
		}
	}
}
