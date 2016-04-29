/**
 * **************************************************************************************
 * File: DoorSensor.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a sensor for a door. It post to the event manager when a door
 * is broken.
 * **************************************************************************************
 */
package sensors;

public class DoorSensor extends Sensor implements Runnable {
	private static final String SENSOR_DOOR_ID = "-7";
	private boolean doorState = false; // Door state: false == ok, true == broken
    
    private static DoorSensor INSTANCE = new DoorSensor();
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			
    			// Sends a message to the door controller
    			if(doorState){
    				sendMessage(SENSOR_DOOR_ID, "Do1");
    				System.out.println(">>> [DOOR SENSOR] SUCCESS! New message was sent.");
    			}else{
    				sendMessage(SENSOR_DOOR_ID, "Do0");
    				System.out.println(">>> [DOOR SENSOR] SUCCESS! New message was sent.");
    			}
    			
    			// Simulates if the door get broken
    			if(getRandomCoin()){
    				doorState = true;
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (DoorSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DoorSensor();
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
    public static DoorSensor getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }
    
    /**
     * @method getRandomCoin
     * @description Returns a randomly-generated true or false
     */
    public boolean getRandomCoin(){
    	return super.coinToss();
    }
} 
