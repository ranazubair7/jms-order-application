package com.jms.demo.JmsOrderApplication.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.jms.demo.JmsOrderApplication.model.BookOrder;
import com.jms.demo.JmsOrderApplication.service.OrderCreateService;
import com.jms.demo.JmsOrderApplication.service.OrderReceiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;

@RestController
@RequestMapping("/api")
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderCreateService bookOrderService;
    @Autowired
    private OrderReceiveService orderReceiveService;

    @PostMapping("/order/toqueue")
    public ResponseEntity createOrder(@RequestParam int priority, @RequestParam String destination, @RequestBody BookOrder bookOrder) {
        //priority is the message priority with which the message will be consumed
        LOGGER.info("the payload received: " + bookOrder);
        bookOrderService.send(bookOrder, priority, destination);
        return ResponseEntity.ok("order sent to destination");
    }

    @PostMapping("/order/totopic")
    public ResponseEntity createOrderOnTopic(@RequestParam String destination, @RequestBody BookOrder bookOrder) {
        LOGGER.info("the payload received: " + bookOrder);
        bookOrderService.createOrderOnTopic(bookOrder, destination);
        return ResponseEntity.ok("order sent to destination");
    }

    @GetMapping("/order/fromqueue/{destination}")
    public ResponseEntity getMessage(@PathVariable String destination) throws JMSException, JsonProcessingException {
        LOGGER.info("Request landed to fetch message from queue");
        BookOrder order = orderReceiveService.receiveMessageFromQueue(destination);
        if (order != null)
            return ResponseEntity.ok(order);
        return ResponseEntity.ok("No message to be retrieved from queue");
    }

    @GetMapping("/order/fromtopic/{destination}")
    public ResponseEntity getMessageFromTopic(@PathVariable String destination) throws JMSException, JsonProcessingException {
        LOGGER.info("Request landed to fetch message from topic");
        BookOrder order = orderReceiveService.receiveMessageFromTopic(destination);
        if (order != null)
            return ResponseEntity.ok(order);
        return ResponseEntity.ok("No message to be retrieved from topic");
    }

}
