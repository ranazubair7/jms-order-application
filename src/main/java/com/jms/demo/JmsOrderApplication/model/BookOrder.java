package com.jms.demo.JmsOrderApplication.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class BookOrder {

    private String orderId;
    private Book book;
}
