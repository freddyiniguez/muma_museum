/**
 * **************************************************************************************
 * File: HumidityController.java 
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
 * Hu1 = humidifier on 
 * Hu0 = humidifier off 
 * De1 = dehumidifier on 
 * De0 = dehumidifier off
 * **************************************************************************************
 */

package controllers;

import sensors.HumiditySensor;

public class HumidityController extends Controller implements Runnable {
	private static final String SENSOR_HUMIDITY_ID = "-4";
	private static final String CONTROLLER_HUMIDITY_ID = "4";
	private float minHumidity = 45.0F;				// Minimum Humidity percertage
	private float maxHumidity = 55.0F;				// Maximum Humidity percentage
	private float currentHumidity = (maxHumidity+minHumidity)/2; // Current humidity
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
    			
    			// Sends a message according to the values of the humidity device
    			if(getCurrentHumidity() > maxHumidity){			// Dehumidifier ON
    				sendMessage(CONTROLLER_HUMIDITY_ID, "De1");
    				dehumidifierState = true;
    				Thread.sleep(10000);
    				Thread dehumidifierThread = new Thread(){
    					@Override
    					public void run(){
    						while(getCurrentHumidity() > ((maxHumidity+minHumidity)/2)){
    							try{
    								Thread.sleep(1000);
    								HumidityController.getInstance().setCurrentHumidity(HumidityController.getInstance().getCurrentHumidity() - HumiditySensor.getInstance().getRandomFloat());
    							}catch(Exception e){
    								e.printStackTrace();
    							}
    						}
    					}
    				};
    				dehumidifierThread.start();
    				
    			}else if(getCurrentHumidity() < minHumidity){	// Humidifier ON
    				sendMessage(CONTROLLER_HUMIDITY_ID, "Hu1");
    				humidifierState = true;
    				Thread.sleep(10000);
    				Thread humidifierThread = new Thread(){
    					@Override
    					public void run(){
    						while(getCurrentHumidity() < ((maxHumidity+minHumidity)/2)){
    							try{
    								Thread.sleep(1000);
    								HumidityController.getInstance().setCurrentHumidity(HumidityController.getInstance().getCurrentHumidity() + HumiditySensor.getInstance().getRandomFloat());
    							}catch(Exception e){
    								e.printStackTrace();
    							}
    						}
    					}
    				};
    				humidifierThread.start();
    			}else{
    				// Gets a new random value for the humidity
    				humidifierState = false;
    				sendMessage(CONTROLLER_HUMIDITY_ID, "Hu0");
    				dehumidifierState = false;
    				sendMessage(CONTROLLER_HUMIDITY_ID, "De0");
    				if(HumiditySensor.getInstance().getRandomCoin()){
    					this.setCurrentHumidity(this.getCurrentHumidity() + HumiditySensor.getInstance().getRandomFloat());
    				}else{
    					this.setCurrentHumidity(this.getCurrentHumidity() - HumiditySensor.getInstance().getRandomFloat());
    				}
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

    /**
     * @method Getters and Setter
     * @description Getters and Setter methods to obtain the humidity percentage, and the devices (humidifier and dehumifier) status.
     */
	public float getMinHumidity() {
		return minHumidity;
	}

	public void setMinHumidity(int minHumidity) {
		this.minHumidity = minHumidity;
	}

	public float getMaxHumidity() {
		return maxHumidity;
	}

	public void setMaxHumidity(int maxHumidity) {
		this.maxHumidity = maxHumidity;
	}
	
	public float getCurrentHumidity(){
		return currentHumidity;
	}
	
	public void setCurrentHumidity(float currentHumidity){
		this.currentHumidity = currentHumidity;
	}

	public boolean isHumidifierState() {
		return humidifierState;
	}

	public void setHumidifierState(boolean humidifierState) {
		this.humidifierState = humidifierState;
	}

	public boolean isDehumidifierState() {
		return dehumidifierState;
	}

	public void setDehumidifierState(boolean dehumidifierState) {
		this.dehumidifierState = dehumidifierState;
	}    
}
