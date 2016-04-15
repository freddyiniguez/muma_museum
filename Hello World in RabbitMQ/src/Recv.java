/** 
 * @author freddyiniguez
 * @class Recv.java
 * @description Consumer (receiver) will pushed messages from RabbitMQ and we'll keep it running for messages and print them out.
 */

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Recv {
	private final static String QUEUE_NAME = "hello";
	
	public static void main(String[] args) throws java.io.IOException, java.lang.InterruptedException {
		try{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			System.out.println("[*] Waiting for messages. To exit press CTRL+C");
			Consumer consumer = new DefaultConsumer(channel){
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println(" [x] Received '" + message + "'");
				}
			};
			channel.basicConsume(QUEUE_NAME, true, consumer);
		}catch(Exception e){
			System.out.println(">>> WARNING! We have encounter an error: " + e.getMessage());
		}
	}
}