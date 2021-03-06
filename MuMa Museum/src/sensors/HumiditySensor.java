/**
 * **************************************************************************************
 * File: HumiditySensor.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a humidity sensor. It polls the event manager for events
 * corresponding to changes in state of the humidifier or dehumidifier and
 * reacts to them by trending the relative humidity up or down. The current
 * relative humidity is posted to the event manager.
 * **************************************************************************************
 */
package sensors;

public class HumiditySensor extends Sensor implements Runnable {
	private static final String SENSOR_HUMIDITY_ID = "-4";
	private static final String CONTROLLER_HUMIDITY_ID = "4";
    
    private static HumiditySensor INSTANCE = new HumiditySensor();
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			
    			// Receives a message from the humidity controller
    			receiveMessage(CONTROLLER_HUMIDITY_ID);
    			
    			// Sends a message to the humidity controller
    			if(sendMessage(SENSOR_HUMIDITY_ID, "Hu0")){
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
