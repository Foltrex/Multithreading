package com.epam.multithreading.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class Participant implements Runnable {

    private String name;

    private double belarusianRuble;
    private double euro;
    private double dollar;


    @Override
    public void run() {
        StockExchange stockExchange = StockExchange.getInstance();
        try {
            stockExchange.register(this);

        } catch (InterruptedException ex) {
            throw new RuntimeException("Can't exchange currency on stock exchange", ex);
        }
    }
}
