/**
 * **************************************************************************************
 * File:HumiditySensor.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a humidity sensor. It polls the event manager for events
 * corresponding to changes in state of the humidifier or dehumidifier and
 * reacts to them by trending the relative humidity up or down. The current
 * relative humidity is posted to the event manager.
 * **************************************************************************************
 */
package sensors;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import common.Component;
import instrumentation.MessageWindow;

public class HumiditySensor extends Sensor implements Runnable {
	private static final String QUEUE_NAME = "muma";
	private static final String SENSOR_HUMIDITY_ID = "-4";
	private static final String CONTROLLER_HUMIDITY_ID = "4";
	private static final String CHANGE_HUMIDITY_ID = "CH";
    private boolean humidifierState = false;	// Humidifier state: false == off, true == on
    private boolean dehumidifierState = false;	// Dehumidifier state: false == off, true == on
    private float relativeHumidity;		// Current simulated ambient room humidity
    private static HumiditySensor INSTANCE = new HumiditySensor();

    
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			// Receives a message from the humidity controller
    			receiveMessage(CONTROLLER_HUMIDITY_ID);
    			
    			// Sends a message to the humidity controller
    			if(sendMessage(SENSOR_HUMIDITY_ID, "H0")){
    				System.out.println(">>> [HUMIDITY SENSOR] SUCCESS! New message was sent.");
    			}else{
    				System.out.println(">>> [HUMIDITY SENSOR] ERROR! A problem was encounter when sending the new message.");
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (HumiditySensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HumiditySensor();
                }
            }
        }
    }

    /**
     * This method calls createInstance method to creates and ensure that 
     * only one instance of this class is created. Singleton design pattern.
     * 
     * @return The instance of this class.
     */
    public static HumiditySensor getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }
} 
