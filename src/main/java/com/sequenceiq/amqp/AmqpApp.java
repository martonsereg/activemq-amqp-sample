package com.sequenceiq.amqp;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AmqpApp {

    public static void main(String args[]) {
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        SimpleProducer producer = context.getBean(SimpleProducer.class);
        context.registerShutdownHook();

        try {
            producer.produce();
        } catch (Throwable t) {
            System.out.println("error sending message");
        } finally {
            context.close();
        }
    }
}
