/**
 * **************************************************************************************
 * File: MovementSensor.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a movement-detector. It post to the event manager when a move is
 * detected
 * **************************************************************************************
 */
package sensors;

public class MovementSensor extends Sensor implements Runnable {
	private static final String SENSOR_MOVEMENT_ID = "-8";
	private boolean movementState = false; // Movement state: false == ok, true == move-detected
    
    private static MovementSensor INSTANCE = new MovementSensor();
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			
    			// Sends a message to the movement controller
    			if(movementState){
    				sendMessage(SENSOR_MOVEMENT_ID, "Mo1");
    				System.out.println(">>> [MOVEMENT SENSOR] SUCCESS! New message was sent.");
    			}else{
    				sendMessage(SENSOR_MOVEMENT_ID, "Do0");
    				System.out.println(">>> [MOVEMENT SENSOR] SUCCESS! New message was sent.");
    			}
    			
    			// Simulates if detects a movement
    			if(getRandomCoin()){
    				movementState = true;
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (MovementSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovementSensor();
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
    public static MovementSensor getInstance() {
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
