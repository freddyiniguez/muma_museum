/**
 * **************************************************************************************
 * File:HumidityController.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a device that controls a humidifier and dehumidifier.
 * It polls the event manager for event ids = 4 and reacts to them by turning on or
 * off the humidifier/dehumidifier. The following command are valid strings for
 * controlling the humidifier and dehumidifier. 
 * H1 = humidifier on 
 * H0 = humidifier off 
 * D1 = dehumidifier on 
 * D0 = dehumidifier off
 * **************************************************************************************
 */

package controllers;

public class HumidityController extends Controller implements Runnable {
	private static final String QUEUE_NAME = "muma";
	private static final String SENSOR_HUMIDITY_ID = "-4";
	private static final String CONTROLLER_HUMIDITY_ID = "4";
	private static final String CHANGE_HUMIDITY_ID = "CH";
    private boolean humidifierState = false;	// Heater state: false == off, true == on
    private boolean dehumidifierState = false;	// Dehumidifier state: false == off, true == on
    private static HumidityController INSTANCE = new HumidityController();
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			// Receives a message from the humidity sensor
    			receiveMessage(SENSOR_HUMIDITY_ID);
    			
    			// Sends a message to the humidity sensor
    			if(sendMessage(CONTROLLER_HUMIDITY_ID, "H1")){
    				System.out.println(">>> [HUMIDITY CONTROLLER] SUCCESS! New message was sent.");
    			}else{
    				System.out.println(">>> [HUMIDITY CONTROLLER] ERROR! A problem was encounter when sending the new message.");
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (HumidityController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HumidityController();
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
    public static HumidityController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }
}
