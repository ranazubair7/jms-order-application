package com.jms.demo.JmsOrderApplication.service;


import com.jms.demo.JmsOrderApplication.model.BookOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;

@Service
public class OrderCreateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCreateService.class);

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    MessageConverter messageConverter;
    @Autowired
    @Qualifier("JMSTemplateForTopic")
    private JmsTemplate jmsTopicTemplate;

    public void send(BookOrder bookOrder, int priority, String destination) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        jmsTemplate.setPriority(priority);
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.convertAndSend(destination, bookOrder, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("order_status", "NEW");
                return message;
            }
        });
    }

    public void send(BookOrder bookOrder, String destination) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        jmsTemplate.convertAndSend(destination, bookOrder, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("order_status", "NEW");
                return message;
            }
        });
    }

    public void createOrderOnTopic(BookOrder order, String destination) {
        LOGGER.info("about to send order: " + order);
        jmsTopicTemplate.convertAndSend(destination, order);
    }

}