/**
 * **************************************************************************************
 * File: MovementController.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a movement device, when 
 * Mo1 = Movement-detected alarm ON 
 * Mo0 = Movement-detected alarm OFF
 * **************************************************************************************
 */

package controllers;

import sensors.MovementSensor;

public class MovementController extends Controller implements Runnable {
	private static final String SENSOR_MOVEMENT_ID = "-8";
	private static final String CONTROLLER_MOVEMENT_ID = "8";
    private boolean movementState = false;	// Movement state: false == ok, true == move-detected
    
    private static MovementController INSTANCE = new MovementController();
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			
    			// Receives a message from the movement sensor
    			receiveMessage(SENSOR_MOVEMENT_ID);
    			
    			// Sends a message according if detects movement or not
    			if(!isMovementState()){
    				if(MovementSensor.getInstance().getRandomCoin()){
    					setMovementState(true);
    					sendMessage(CONTROLLER_MOVEMENT_ID, "Mo1");
    				}
    			}else{
    				setMovementState(false);
    				sendMessage(CONTROLLER_MOVEMENT_ID, "Mo0");
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (MovementController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovementController();
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
    public static MovementController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }   
    
    /**
     * @method Getters and Setter
     * @description Getters and Setter methods to obtain the movement status.
     */
	public boolean isMovementState() {
		return movementState;
	}

	public void setMovementState(boolean movementState) {
		this.movementState = movementState;
	}
}
