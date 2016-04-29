/**
 * **************************************************************************************
 * File: TemperatureSensor.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a temperature sensor. It polls the event manager for
 * events corresponding to changes in state of the heater or chiller and reacts
 * to them by trending the ambient temperature up or down. The current ambient
 * room temperature is posted to the event manager.
 * **************************************************************************************
 */
package sensors;

public class TemperatureSensor extends Sensor implements Runnable {
	private static final String SENSOR_TEMPERATURE_ID = "-5";
	private static final String CONTROLLER_TEMPERATURE_ID = "5";

    private static TemperatureSensor INSTANCE = new TemperatureSensor();
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			
    			// Receives a message from the temperature controller
    			receiveMessage(CONTROLLER_TEMPERATURE_ID);
    			
    			// Sends a message to the temperature controller
    			if(sendMessage(SENSOR_TEMPERATURE_ID, "Ch0")){
    				System.out.println(">>> [TEMPERATURE SENSOR] SUCCESS! New message was sent.");
    			}else{
    				System.out.println(">>> [TEMPERATURE SENSOR] ERROR! A problem was encounter when sending the new message.");
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (TemperatureSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TemperatureSensor();
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
    public static TemperatureSensor getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     * @method getRandomFloat()
     * @description Returns a randomly-generated number between 0 and 1
     */
    public float getRandomFloat(){
    	return super.getRandomNumber();
    }
    
    /**
     * @method getRandomCoin
     * @description Returns a randomly-generated true or false
     */
    public boolean getRandomCoin(){
    	return super.coinToss();
    }
}
