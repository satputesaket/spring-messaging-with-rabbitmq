package com.tyfone.messagequeuedemo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.tyfone.messagequeuedemo.receivers.Receiver;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@SpringBootApplication
public class Application {
	
	public final static String queueName = "spring-boot";

	@Bean
	public Queue queue(){
		return new Queue(queueName, false);
	}

	@Bean
	public TopicExchange exchange(){
        return new TopicExchange("spring-boot-exchange");
	}
	
	@Bean
	public Binding binding(Queue queue, TopicExchange exchange){
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}
	
	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter){
		   SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	        container.setConnectionFactory(connectionFactory);
	        container.setQueueNames(queueName);
	        container.setMessageListener(listenerAdapter);
	        return container;

	}
	
	@Bean
	public MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
	
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
    }

}
