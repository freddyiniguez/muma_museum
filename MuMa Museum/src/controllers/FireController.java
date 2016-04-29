/**
 * **************************************************************************************
 * File: FireController.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a fire detector, when 
 * Fi1 = Fire-detected alarm ON 
 * Fi0 = Fire-detected alarm OFF
 * **************************************************************************************
 */

package controllers;

import sensors.FireSensor;

public class FireController extends Controller implements Runnable {
	private static final String SENSOR_FIRE_ID = "-10";
	private static final String CONTROLLER_FIRE_ID = "10";
    private boolean fireState = false;	// Fire state: false == no fire detected, true == fire detected
    
    private static FireController INSTANCE = new FireController();
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			
    			// Receives a message from the fire sensor
    			receiveMessage(SENSOR_FIRE_ID);
    			
    			// Sends a message according to the values of the fire device
    			if(!isFireState()){
    				if(FireSensor.getInstance().getRandomCoin()){ // Detects fire!
    					setFireState(true);
    					sendMessage(CONTROLLER_FIRE_ID, "Fi1");
    				}
    			}else{
    				setFireState(false);
    				sendMessage(CONTROLLER_FIRE_ID, "Fi0");
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (FireController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FireController();
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
    public static FireController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }   
    
    /**
     * @method Getters and Setter
     * @description Getters and Setter methods to obtain the fire status.
     */
	public boolean isFireState() {
		return fireState;
	}

	public void setFireState(boolean fireState) {
		this.fireState = fireState;
	}
}
