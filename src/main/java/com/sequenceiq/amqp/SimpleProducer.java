package com.sequenceiq.amqp;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleProducer {

    @Autowired
    private MessageProducer producer;

    @Autowired
    private Session session;

    @Autowired
    private Destination destination;

    public void produce() throws JMSException, InterruptedException {
        for (int i = 1; i <= 10; i++) {
            TextMessage message = session.createTextMessage(i + ". message sent");
            System.out.println("Sending to destination: " + destination.toString() + " this text: '" + message.getText());
            producer.send(message);
            Thread.sleep(1000);
        }
    }
}
