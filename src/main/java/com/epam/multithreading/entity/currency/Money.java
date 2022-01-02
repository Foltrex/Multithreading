package com.epam.multithreading.entity.currency;

public abstract class Money implements Comparable<Money> {
    private int banknotes;
    private int coins;

    public Money(int banknotes, int coins) {
        this.banknotes = banknotes;
        this.coins = coins;
    }

    public int getBanknote() {
        return banknotes;
    }

    public void setBanknote(int banknotes) {
        this.banknotes = banknotes;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public int compareTo(Money otherMoney) {
        int totalCoins = banknotes * 100 + coins;
        int totalOtherCoins = otherMoney.getBanknote() * 100 + otherMoney.getCoins();
        return Integer.compare(totalCoins, totalOtherCoins);
    }

    @Override
    public String toString() {
        return banknotes + "." + coins;
    }
}
