/**
 * **************************************************************************************
 * File: DoorController.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a device for a door, when 
 * Do1 = Broken door alarm ON 
 * Do0 = Broken door alarm OFF
 * **************************************************************************************
 */

package controllers;

import sensors.DoorSensor;

public class DoorController extends Controller implements Runnable {
	private static final String SENSOR_DOOR_ID = "-7";
	private static final String CONTROLLER_DOOR_ID = "7";
    private boolean doorState = false;	// Door state: false == ok, true == broken
    
    private static DoorController INSTANCE = new DoorController();
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			
    			// Receives a message from the door sensor
    			receiveMessage(SENSOR_DOOR_ID);
    			
    			// Sends a message according to the values of the door device
    			if(!isDoorState()){
    				if(DoorSensor.getInstance().getRandomCoin()){
    					setDoorState(true);
    					sendMessage(CONTROLLER_DOOR_ID, "Do1");
    				}
    			}else{
    				setDoorState(false);
    				sendMessage(CONTROLLER_DOOR_ID, "Do0");
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (DoorController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DoorController();
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
    public static DoorController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }   
    
    /**
     * @method Getters and Setter
     * @description Getters and Setter methods to obtain the door status.
     */
	public boolean isDoorState() {
		return doorState;
	}

	public void setDoorState(boolean doorState) {
		this.doorState = doorState;
	}
}
