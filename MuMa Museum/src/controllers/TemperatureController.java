/**
 * **************************************************************************************
 * File: TemperatureController.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a device that controls a heater and chiller. 
 * It polls the event manager for event ids = 5 and reacts to them by turning 
 * on or off the heater or chiller. The following command are valid strings for 
 * controlling the heater and chiller.
 * He1 = heater on 
 * He0 = heater off 
 * Ch1 = chiller on 
 * Ch0 = chiller off
 * **************************************************************************************
 */

package controllers;

import sensors.TemperatureSensor;

public class TemperatureController extends Controller implements Runnable {
	private static final String SENSOR_TEMPERATURE_ID = "-5";
	private static final String CONTROLLER_TEMPERATURE_ID = "5";
	private float minTemperature = 70.0F;		// Minimum Fahrenheit degrees
	private float maxTemperature = 75.0F; 		// Maximum Fahrenheit degrees
	private float currentTemperature = (maxTemperature+minTemperature)/2; // Current Fahrenheit degrees
    private boolean heaterState = false;		// Heater state: false == off, true == on
    private boolean heaterMalfunction = false; 	// Heater malfunctioning: true == is malfunctioning, false == works well
    private boolean chillerState = false;		// Chiller state: false == off, true == on
    private int chillerCont = 0;
    
    private static TemperatureController INSTANCE = new TemperatureController();

    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			
    			// Receives a message from the humidity sensor
    			receiveMessage(SENSOR_TEMPERATURE_ID);
    			
    			// Sends a message according to the values of the humidity device
    			if(getCurrentTemperature() > maxTemperature){	// Chiller ON
    				sendMessage(CONTROLLER_TEMPERATURE_ID, "Ch1");
    				chillerState = true;
    				Thread.sleep(10000);
    				Thread chillerThread = new Thread(){
    					@Override
    					public void run(){
    						while(getCurrentTemperature() > ((maxTemperature+minTemperature)/2)){
    							try{
    								Thread.sleep(1000);
    								TemperatureController.getInstance().setCurrentTemperature(TemperatureController.getInstance().getCurrentTemperature() - TemperatureSensor.getInstance().getRandomFloat());
    							}catch(Exception e){
    								e.printStackTrace();
    							}
    						}
    					}
    				};
    				chillerThread.start();
    			}else if(getCurrentTemperature() < minTemperature){	// Heater ON
    				sendMessage(CONTROLLER_TEMPERATURE_ID, "He1");
    				heaterState = true;
    				Thread.sleep(10000);
    				Thread heaterThread = new Thread(){
    					@Override
    					public void run(){
    						while(getCurrentTemperature() < ((maxTemperature+minTemperature)/2)){
    							try{
        							Thread.sleep(1000);
        							TemperatureController.getInstance().setCurrentTemperature(TemperatureController.getInstance().getCurrentTemperature() + TemperatureSensor.getInstance().getRandomFloat());
        						}catch(Exception e){
        							e.printStackTrace();
        						}
    						}
    					}
    				};
    				heaterThread.start();
    			}else{
    				// Gets a new random value for the temperature
    				chillerState = false;
    				sendMessage(CONTROLLER_TEMPERATURE_ID, "Ch0");
    				heaterState = false;
    				sendMessage(CONTROLLER_TEMPERATURE_ID, "He0");
    				if(TemperatureSensor.getInstance().getRandomCoin()){
    					this.setCurrentTemperature(this.getCurrentTemperature() + TemperatureSensor.getInstance().getRandomFloat());
    				}else{
    					this.setCurrentTemperature(this.getCurrentTemperature() - TemperatureSensor.getInstance().getRandomFloat());
    				}
    			}
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
	
	public boolean isHeaterMalfunction(){
		float malfunctioning = TemperatureSensor.getInstance().getRandomFloat();
		if(malfunctioning < 0.50F && malfunctioning > 0.49F){
			heaterMalfunction = true; // It's having a malfunctioning
			heaterState = false;
			return heaterMalfunction;
		}else{
			return heaterMalfunction;
		}
	}
	
	public void setChillerCont(int chillerCont){
		this.chillerCont = chillerCont;
	}
	
	public int getChillerCont(){
		return chillerCont;
	}
}
