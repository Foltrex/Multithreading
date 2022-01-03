package com.epam.multithreading.entity.currency;

public class Dollar extends Money {

    public Dollar() {
    }

    public Dollar(int banknotes, int coins) {
        super(banknotes, coins);
    }

    @Override
    public String toString() {
        return super.toString() + " USD";
    }
}
