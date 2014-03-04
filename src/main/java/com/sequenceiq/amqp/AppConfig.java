package com.sequenceiq.amqp;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

@Configuration
@ComponentScan
public class AppConfig implements ApplicationListener<ContextClosedEvent> {

    private static final String CONNECTION_FACTORY_NAME = "qpidConnectionFactory";
    private static final String QUEUE_NAME = "queue/simple";

    @Bean
    public InitialContext initialContext() throws NamingException {
        return new InitialContext();
    }

    @Bean
    public ConnectionFactory connectionFactory() throws NamingException {
        return (ConnectionFactory) initialContext().lookup(CONNECTION_FACTORY_NAME);
    }

    @Bean
    public Destination destination() throws NamingException {
        return (Destination) initialContext().lookup(QUEUE_NAME);
    }

    @Bean
    public Connection connection() throws JMSException, NamingException {
        Connection connection = connectionFactory().createConnection();
        connection.start();
        return connection;
    }

    @Bean
    public Session session() throws JMSException, NamingException {
        return connection().createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Bean
    public MessageProducer producer() throws JMSException, NamingException {
        MessageProducer producer = session().createProducer(destination());
        producer.setTimeToLive(0);
        return producer;
    }

    @Bean
    public MessageConsumer consumer() throws JMSException, NamingException {
        MessageConsumer consumer = session().createConsumer(destination());
        consumer.setMessageListener(new CustomMessageHandler());
        return consumer;
    }

    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            session().close();
            consumer().close();
            producer().close();
            connection().close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
