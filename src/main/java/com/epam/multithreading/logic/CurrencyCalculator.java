package com.epam.multithreading.logic;

import com.epam.multithreading.entity.currency.BelarusianRuble;
import com.epam.multithreading.entity.currency.Dollar;
import com.epam.multithreading.entity.currency.Euro;
import com.epam.multithreading.entity.currency.Money;
import com.epam.multithreading.exception.TransactionException;

public class CurrencyCalculator {
    private CurrencyCalculator() {}

    public static Money add(Money firstMoney, Money secondMoney) throws TransactionException {
        int firstAndSecondCoins = firstMoney.getCoins() + secondMoney.getCoins();
        int firstAndSecondBanknote = firstMoney.getBanknotes() + secondMoney.getBanknotes();

        int totalBanknote = firstAndSecondBanknote + firstAndSecondCoins / 100;
        int totalCoins = firstAndSecondCoins % 100;

        Money moneySum;
        if (firstMoney instanceof Dollar && secondMoney instanceof Dollar) {
            moneySum = new Dollar(totalBanknote, totalCoins);
        } else if (firstMoney instanceof Euro && secondMoney instanceof Euro) {
            moneySum = new Euro(totalBanknote, totalCoins);
        } else if (firstMoney instanceof BelarusianRuble && secondMoney instanceof BelarusianRuble) {
            moneySum = new BelarusianRuble(totalBanknote, totalCoins);
        } else {
            throw new TransactionException("Mismatch of types of summands");
        }

        return moneySum;
    }

    public static Money subtract(Money firstMoney, Money secondMoney) throws TransactionException {
        int totalFirstMoneyCoins = firstMoney.getBanknotes() * 100 + firstMoney.getCoins();
        int totalSecondMoneyCoins = secondMoney.getBanknotes() * 100 + secondMoney.getCoins();

        int resultCoins = totalFirstMoneyCoins - totalSecondMoneyCoins;

        int totalBanknote = resultCoins / 100;
        int totalCoins = resultCoins % 100;

        Money moneyDif;
        if (firstMoney instanceof Dollar && secondMoney instanceof Dollar) {
            moneyDif = new Dollar(totalBanknote, totalCoins);
        } else if (firstMoney instanceof Euro && secondMoney instanceof Euro) {
            moneyDif = new Euro(totalBanknote, totalCoins);
        } else if (firstMoney instanceof BelarusianRuble && secondMoney instanceof BelarusianRuble) {
            moneyDif = new BelarusianRuble(totalBanknote, totalCoins);
        } else {
            throw new TransactionException("Mismatch of types of difference");
        }
        return moneyDif;
    }
}
