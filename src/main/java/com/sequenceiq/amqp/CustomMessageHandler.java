package com.sequenceiq.amqp;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class CustomMessageHandler implements MessageListener {

    public void onMessage(Message message) {
        try {
            if (message != null) {
                if (message instanceof TextMessage) {
                    String text;
                    text = ((TextMessage) message).getText();
                    System.out.println("Got message: " + text);
                }
            } else {
                System.out.println("nullmessage");
            }
        } catch (JMSException e) {
            System.out.println("JMS Exception");
            e.printStackTrace();
        }
    }

}
