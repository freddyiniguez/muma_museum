/**
 * **************************************************************************************
 * File:Component.java 
 * Course: Software Architecture 
 * Project: Event Architectures
 * Institution: Autonomous University of Zacatecas 
 * Date: November 2015
 * Developer: Ferman Ivan Tovar 
 * Reviewer: Perla Velasco Elizondo
 * **************************************************************************************
 * This class is the parent of all devices on this system, sensors and controllers.
 * Contains the IP address of the server where is the event manager.
 * **************************************************************************************
 */
package common;

import event.Event;
import event.EventManagerInterface;
import event.EventQueue;

public class Component {

    public static String SERVER_IP = "127.0.0.1";

    public final static int TEMPERATURE = 1;
    public final static int HUMIDITY = 2;

    public final static int TEMPERATURE_SENSOR = -5;
    public final static int HUMIDITY_SENSOR = -4;
    public final static int TEMPERATURE_CONTROLLER = 5;
    public final static int HUMIDITY_CONTROLLER = 4;
    public final static int END = 99;

    public final static String HEATER_ON = "H1";
    public final static String HEATER_OFF = "H0";
    public final static String CHILLER_ON = "C1";
    public final static String CHILLER_OFF = "C0";

    public final static String HUMIDIFIER_ON = "H1";
    public final static String HUMIDIFIER_OFF = "H0";
    public final static String DEHUMIDIFIER_ON = "D1";
    public final static String DEHUMIDIFIER_OFF = "D0";

    protected Event evt = null;				// Event object
    protected EventQueue queue = null;			// Message Queue
    protected int evtId = 0;			        // User specified event ID
    protected EventManagerInterface evtMgrI = null;     // Interface object to the event manager

    protected Component() {
        /////////////////////////////////////////////////////////////////////////////////
        // Get the IP address of the event manager
        /////////////////////////////////////////////////////////////////////////////////
        String[] args = new String[]{};
        if (args.length == 0) {
            // event manager is on the local system

            System.out.println("\n\nAttempting to register on the local machine...");

            try {
                // Here we create an event manager interface object. This assumes
                // that the event manager is on the local machine

                evtMgrI = new EventManagerInterface();
            }
            catch (Exception e) {
                System.out.println("Error instantiating event manager interface: " + e);

            } // catch

        }
        else {

            // event manager is not on the local system
            //evtMgrIP = args[0];
            System.out.println("\n\nAttempting to register on the machine:: " + SERVER_IP);

            try {
                // Here we create an event manager interface object. This assumes
                // that the event manager is NOT on the local machine

                evtMgrI = new EventManagerInterface(SERVER_IP);
            }
            catch (Exception e) {
                System.out.println("Error instantiating event manager interface: " + e);

            } // catch

        } // if
    }

    // This is to accomplish with singleton pattern
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
