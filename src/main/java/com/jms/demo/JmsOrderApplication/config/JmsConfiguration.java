package com.jms.demo.JmsOrderApplication.config;


import com.jms.demo.JmsOrderApplication.model.Book;
import com.jms.demo.JmsOrderApplication.model.BookOrder;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsConfiguration {
    @Value("${jms.broker.url}")
    private String brokerURL;

    //for JSON messages
    @Bean
    public MessageConverter jacksonConverter() {
        MappingJackson2MessageConverter jackson2MessageConverter = new MappingJackson2MessageConverter();
        jackson2MessageConverter.setTargetType(MessageType.TEXT);
        jackson2MessageConverter.setTypeIdPropertyName("_type");
        return jackson2MessageConverter;
    }

    // For xml Messages
    //@Bean
    public MessageConverter xmlMarsheller() {
        MarshallingMessageConverter messageConverter = new MarshallingMessageConverter(xmlXtreamMarsheller());
        messageConverter.setTargetType(MessageType.TEXT);
        return messageConverter;
    }

    //@Bean
    public XStreamMarshaller xmlXtreamMarsheller() {
        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setSupportedClasses(Book.class, BookOrder.class);
        return xStreamMarshaller;
    }


    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(MessageConverter messageConverter) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setMessageConverter(jacksonConverter());
        jmsTemplate.setReceiveTimeout(5000);
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory listenerContainerFactory() {
        DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();
        listenerContainerFactory.setConnectionFactory(connectionFactory());
        listenerContainerFactory.setMessageConverter(jacksonConverter());
        listenerContainerFactory.setConcurrency("1-1");
        return listenerContainerFactory;
    }

    @Bean(name = "TopicListenerContainerFactory")
    public DefaultJmsListenerContainerFactory topicListenerContainerFactory() {
        DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();
        listenerContainerFactory.setConnectionFactory(connectionFactory());
        listenerContainerFactory.setPubSubDomain(true);
        listenerContainerFactory.setMessageConverter(jacksonConverter());
        return listenerContainerFactory;
    }

    @Bean(name = "JMSTemplateForTopic")
    public JmsTemplate getJMSTemplateForTopic(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setMessageConverter(jacksonConverter());
        jmsTemplate.setReceiveTimeout(5000);
        return jmsTemplate;
    }
}