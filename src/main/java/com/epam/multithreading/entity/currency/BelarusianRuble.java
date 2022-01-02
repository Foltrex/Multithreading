package com.epam.multithreading.entity.currency;

public class BelarusianRuble extends Money {

    public BelarusianRuble(int banknotes, int coins) {
        super(banknotes, coins);
    }

    @Override
    public String toString() {
        return super.toString() + " RUB";
    }
}
