package com.jms.demo.JmsOrderApplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jms.demo.JmsOrderApplication.model.BookOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;

@Service
public class OrderReceiveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderReceiveService.class);

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    @Qualifier("JMSTemplateForTopic")
    private JmsTemplate jmsTopicTemplate;

    /**
     * in case if we want to use the @JMSlistener
     *
     * @param bookOrder
     */
    //@JmsListener(destination = "book.order.queue")
    public void getOrder(BookOrder bookOrder) {
        LOGGER.info("order retrieved is: " + bookOrder.toString());
    }

    public BookOrder receiveMessageFromQueue(String destination) throws JMSException, JsonProcessingException {
        BookOrder order = (BookOrder) jmsTemplate.receiveAndConvert(destination);
        if (order == null) {
            LOGGER.info("No message to be retrieved from queue {}", destination);
            return null;
        }
        LOGGER.info("message received from queue {} is {}: ", destination, order);
        return order;
    }


    public BookOrder receiveMessageFromTopic(String destination) throws JMSException {
        BookOrder order = (BookOrder) jmsTopicTemplate.receiveAndConvert(destination);
        if (order == null) {
            LOGGER.info("No message to be retrieved from topic {}.", destination);
            return null;
        }
        LOGGER.info("message received from topic {} is {}: ", destination, order);
        return order;
    }

    /**
     * in case if we want to use the @JMSlistener
     */
    //@JmsListener(destination = "order_topic", containerFactory = "TopicListenerContainerFactory")
    public void onMessage(BookOrder bookOrder) {
        LOGGER.info("order is: " + bookOrder);
    }

}
