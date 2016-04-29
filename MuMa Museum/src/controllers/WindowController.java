/**
 * **************************************************************************************
 * File: WindowController.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a device for a window, when 
 * Wi1 = Broken window alarm ON 
 * Wi0 = Broken window alarm OFF
 * **************************************************************************************
 */

package controllers;

import sensors.WindowSensor;

public class WindowController extends Controller implements Runnable {
	private static final String SENSOR_WINDOW_ID = "-6";
	private static final String CONTROLLER_WINDOW_ID = "6";
    private boolean windowState = false;	// Window state: false == ok, true == broken
    
    private static WindowController INSTANCE = new WindowController();
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			
    			// Receives a message from the window sensor
    			receiveMessage(SENSOR_WINDOW_ID);
    			
    			// Sends a message according to the values of the window device
    			if(!isWindowState()){
    				if(WindowSensor.getInstance().getRandomCoin()){
    					setWindowState(true);
    					sendMessage(CONTROLLER_WINDOW_ID, "Wi1");
    				}
    			}else{
    				setWindowState(false);
    				sendMessage(CONTROLLER_WINDOW_ID, "Wi0");
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (WindowController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WindowController();
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
    public static WindowController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }   
    
    /**
     * @method Getters and Setter
     * @description Getters and Setter methods to obtain the window status.
     */
	public boolean isWindowState() {
		return windowState;
	}

	public void setWindowState(boolean windowState) {
		this.windowState = windowState;
	}
}
