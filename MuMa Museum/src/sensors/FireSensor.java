/**
 * **************************************************************************************
 * File: FireSensor.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a fire-detector. It post to the event manager when fire is
 * detected
 * **************************************************************************************
 */
package sensors;

public class FireSensor extends Sensor implements Runnable {
	private static final String SENSOR_FIRE_ID = "-10";
	private boolean fireState = false;	// Fire state: false == no fire detected, true == fire detected
    
    private static FireSensor INSTANCE = new FireSensor();
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			
    			// Sends a message to the fire controller
    			if(fireState){
    				sendMessage(SENSOR_FIRE_ID, "Fi1");
    				System.out.println(">>> [FIRE SENSOR] SUCCESS! New message was sent.");
    			}else{
    				sendMessage(SENSOR_FIRE_ID, "Do0");
    				System.out.println(">>> [FIRE SENSOR] SUCCESS! New message was sent.");
    			}
    			
    			// Simulates if detects fire!
    			if(getRandomCoin()){
    				fireState = true;
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (FireSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FireSensor();
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
    public static FireSensor getInstance() {
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
