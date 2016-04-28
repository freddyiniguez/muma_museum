/**
 * **************************************************************************************
 * File:TemperatureController.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a device that controls a heater and chiller. 
 * It polls the event manager for event ids = 5 and reacts to them by turning 
 * on or off the heater or chiller. The following command are valid strings for 
 * controlling the heater and chiller.
 * H1 = heater on 
 * H0 = heater off 
 * C1 = chiller on 
 * C0 = chiller off
 * **************************************************************************************
 */

package controllers;

import common.Component;
import instrumentation.Indicator;
import instrumentation.MessageWindow;
import sensors.HumiditySensor;
import sensors.TemperatureSensor;

public class TemperatureController extends Controller implements Runnable {
	private static final String QUEUE_NAME = "muma";
	private static final String SENSOR_TEMPERATURE_ID = "-5";
	private static final String CONTROLLER_TEMPERATURE_ID = "5";
	private static final String CHANGE_HUMIDITY_ID = "CH";
	private float minTemperature = 70;		// Minimum Fahrenheit degrees
	private float maxTemperature = 75; 		// Maximum Fahrenheit degrees
	private float currentTemperature = minTemperature; // Current Fahrenheit degrees
    private boolean heaterState = false;	// Heater state: false == off, true == on
    private boolean chillerState = false;	// Chiller state: false == off, true == on
    
    private static TemperatureController INSTANCE = new TemperatureController();

    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(1000);
    			receiveMessage(SENSOR_TEMPERATURE_ID);
    			// Simulate an increase of the temperature
    			this.setCurrentTemperature(TemperatureSensor.getInstance().getRandomFloat());
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (TemperatureController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TemperatureController();
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
    public static TemperatureController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }
    
    /**
     * @method Getters and Setter
     * @description Getters and Setter methods to obtain the temperature degrees, and the devices (chiller and heater) status.
     */
	public float getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(int minTemperature) {
		this.minTemperature = minTemperature;
	}

	public float getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(int maxTemperature) {
		this.maxTemperature = maxTemperature;
	}
	
	public float getCurrentTemperature(){
		return currentTemperature;
	}
	
	public void setCurrentTemperature(float currentTemperature){
		this.currentTemperature = currentTemperature;
	}

	public boolean isHeaterState() {
		return heaterState;
	}

	public void setHeaterState(boolean heaterState) {
		this.heaterState = heaterState;
	}

	public boolean isChillerState() {
		return chillerState;
	}

	public void setChillerState(boolean chillerState) {
		this.chillerState = chillerState;
	}
}
