# jms-order-application
This is a demo project to demonstarte the use of JMS APIs for sending and reciving the messages over queues and topics


#send to topic request

#path: http://localhost:8080/api/order/totopic?destination=tcp://localhost:61616
#JSON: {
    "orderId":"3",
    "book":{
        "bookId":"1",
        "title":"System Design"
    },
    "customer":{
        "customerId":"1",
        "name":"Usama"
    }
}
