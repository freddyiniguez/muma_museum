/**
 * **************************************************************************************
 * File: WindowSensor.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Mathematics Research Center
 * Date: April 2016
 * Developer: José Luis Blanco Aguirre, Freddy Íñiguez López, Carlos Adrian Naal Avila
 * Reviewer: Dra. Perla Velasco Elizondo
 * **************************************************************************************
 * This class simulates a sensor for a window. It post to the event manager when a window
 * is broken.
 * **************************************************************************************
 */
package sensors;

public class WindowSensor extends Sensor implements Runnable {
	private static final String SENSOR_WINDOW_ID = "-6";
	private boolean windowState = false; // Window state: false == ok, true == broken
    
    private static WindowSensor INSTANCE = new WindowSensor();
    
    @Override
    public void run(){
    	while(true){
    		try {
    			Thread.sleep(delay);
    			
    			// Sends a message to the window controller
    			if(windowState){
    				sendMessage(SENSOR_WINDOW_ID, "Wi1");
    				System.out.println(">>> [WINDOW SENSOR] SUCCESS! New message was sent.");
    			}else{
    				sendMessage(SENSOR_WINDOW_ID, "Wi0");
    				System.out.println(">>> [WINDOW SENSOR] SUCCESS! New message was sent.");
    			}
    			
    			// Simulates if the window get broken
    			if(getRandomCoin()){
    				windowState = true;
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (WindowSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WindowSensor();
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
    public static WindowSensor getInstance() {
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
