/**
 * **************************************************************************************
 * File: Sensor.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class contains the necessary to build a sensor, in order to every 
 * sensor extends from this.
 * **************************************************************************************
 */
package sensors;

import common.Component;
import event.Event;
import event.EventManagerInterface;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Sensor extends Component {
    protected int delay = 2500;				// The loop delay (2.5 seconds)
    protected boolean isDone = false;			// Loop termination flag
    protected float driftValue;				// The amount of temperature gained or lost
    private static final String QUEUE_NAME = "muma";
    String outputMessage = "";
    String errorMessage = "";

    /**
     * This method provides the simulation with random floating point 
     * temperature values between 0.1 and 0.9.
     * 
     * @return A random number
     */
    protected float getRandomNumber() {
        Random r = new Random();
        Float val;
        val = Float.valueOf((float) -1.0);
        while (val < 0.1) {
            val = r.nextFloat();
        }
        return (val.floatValue());
    }

    /**
     * This method provides a random true or
     * false value used for determining the positiveness or negativeness of the
     * drift value.
     * 
     * @return A random boolean value
     */
    protected boolean coinToss() {
        Random r = new Random();
        return (r.nextBoolean());
    }

    /**
     * This method posts the specified temperature value to the specified event
     * manager.
     *
     * @param ei This is the eventmanger interface where the event will be
     * posted.
     * @param eventId This is the ID to identify the type of event
     * @param value Is the value to publish in the event queue
     */
    protected void postEvent(EventManagerInterface ei, int eventId, float value) {
        // Create the event.
        Event evt = new Event(eventId, String.valueOf(value));
        // Send the event to the event manager.
        try {
            ei.sendEvent(evt);
        }
        catch (Exception e) {
            System.out.println("Error Posting Temperature:: " + e);
        }
    }
    
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
			int detected_value = Integer.parseInt(CHANNEL_SEND_ID);
			switch (detected_value){
				case 4:
					errorMessage = ">>> [HUMIDITY CONTROLLER] ERROR! The Message could not be received: ";
					break;
				case 5:
					errorMessage = ">>> [TEMPERATURE CONTROLLER] ERROR! The Message could not be delivered: ";
					break;
				case 6:
					errorMessage = ">>> [WINDOW CONTROLLER] ERROR! The Message could not be delivered: ";
					break;
				case 7:
					errorMessage = ">>> [DOOR CONTROLLER] ERROR! The Message could not be delivered: ";
					break;
				case 8:
					errorMessage = ">>> [MOVEMENT CONTROLLER] ERROR! The Message could not be delivered: ";
					break;
				case 9:
					errorMessage = ">>> [INTRUDER CONTROLLER] ERROR! The Message could not be delivered: ";
					break;
				case 10:
					errorMessage = ">>> [FIRE CONTROLLER] ERROR! The Message could not be delivered: ";
					break;
				case 11:
					errorMessage = ">>> [SPRINKLERS CONTROLLER] ERROR! The Message could not be delivered: ";
					break;
				default:
					break;
			}
			System.out.println(errorMessage + e.getMessage());
			errorMessage = "";
			return false;
		}
	}
	
	/**
	 * @method receiveMessage
	 * @parameter Receives the identifier for the HumidityController
	 * @return True is the message was correctly retrieved.
	 */
	protected void receiveMessage(String CHANNEL_CONTROLLER_ID){
		try{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			// Receives the message
			channel.exchangeDeclare(CHANNEL_CONTROLLER_ID, "fanout");
			channel.queueBind(QUEUE_NAME, CHANNEL_CONTROLLER_ID, "");
			Consumer consumer;

			int id_detected = Integer.parseInt(CHANNEL_CONTROLLER_ID);
			switch (id_detected) {
			case 4:
				outputMessage = ">>> [HUMIDITY CONTROLLER] SUCCESS! I received a message from the Humidity Sensor: ";
				break;
			case 5:
				outputMessage = ">>> [TEMPERATURE CONTROLLER] SUCCESS! I received a message from the Temperature Sensor: ";
				break;
			case 6:
				outputMessage = ">>> [WINDOW CONTROLLER] SUCCESS! I received a message from the Window Sensor: ";
				break;
			case 7:
				outputMessage = ">>> [DOOR CONTROLLER] SUCCESS! I received a message from the Door Sensor: ";
				break;
			case 8:
				outputMessage = ">>> [MOVEMENT CONTROLLER] SUCCESS! I received a message from the Movement Sensor: ";
				break;
			case 9:
				outputMessage = ">>> [INTRUDER CONTROLLER] SUCCESS! I received a message from the Intruder Sensor: ";
				break;
			case 10:
				outputMessage = ">>> [FIRE CONTROLLER] SUCCESS! I received a message from the Fire Sensor: ";
				break;
			case 11:
				outputMessage = ">>> [SPRINKLERS CONTROLLER] SUCCESS! I received a message from the Sprinklers Sensor: ";
				break;
			default:
				outputMessage = ">>> [SENSOR WARNING!] Code Unknown";
				break;
			}
			consumer = new DefaultConsumer(channel){
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
					System.out.println(outputMessage + new String(body, "UTF-8"));
					outputMessage = "";
				}
			};
			channel.basicConsume(QUEUE_NAME, true, consumer);
			channel.close();
			connection.close();
		}catch(IOException|TimeoutException e){
			int detected_value = Integer.parseInt(CHANNEL_CONTROLLER_ID);
			switch (detected_value){
				case 4:
					errorMessage = ">>> [HUMIDITY SENSOR] ERROR! The Message could not be received: ";
					break;
				case 5:
					errorMessage = ">>> [TEMPERATURE SENSOR] ERROR! The Message could not be delivered: ";
					break;
				case 6:
					errorMessage = ">>> [DOOR SENSOR] ERROR! The Message could not be delivered: ";
					break;
				case 7:
					errorMessage = ">>> [WINDOW SENSOR] ERROR! The Message could not be delivered: ";
					break;
				default:
					errorMessage = ">>> ERROR! The message could not be delivered: ";
					break;
			}
			System.out.println(errorMessage + e.getMessage());
			errorMessage = "";
		}
	}
}
