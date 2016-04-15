/** 
 * @author freddyiniguez
 * @class Send.java
 * @description Producer (sender) will connect to RabbitMQ, send a single message, then exit.
 */

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Send {
	private final static String QUEUE_NAME = "hello";
	
	public static void main(String[] args) throws java.io.IOException {
		try{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
		}catch(Exception e){
			System.out.println(">>> WARNING! We have encounter an error: " + e.getMessage());
		}
	}
}