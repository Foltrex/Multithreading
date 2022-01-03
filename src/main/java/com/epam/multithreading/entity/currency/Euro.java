package com.epam.multithreading.entity.currency;

public class Euro extends Money {

    public Euro() {
    }

    public Euro(int banknotes, int coins) {
        super(banknotes, coins);
    }

    @Override
    public String toString() {
        return super.toString() + " EUR";
    }
}
