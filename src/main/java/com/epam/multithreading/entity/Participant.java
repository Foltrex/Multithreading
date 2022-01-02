package com.epam.multithreading.entity;

import com.epam.multithreading.StockExchange;
import com.epam.multithreading.entity.currency.Dollar;
import com.epam.multithreading.entity.currency.Euro;
import com.epam.multithreading.entity.currency.BelarusianRuble;
import com.epam.multithreading.exception.TransactionException;
import com.epam.multithreading.logic.CurrencyCalculator;
import com.epam.multithreading.logic.CurrencyConverter;

public class Participant implements Runnable {
    private BelarusianRuble belarusianRuble;
    private Euro euro;
    private Dollar dollar;


    public Participant(BelarusianRuble belarusianRuble, Euro euro, Dollar dollar) {
        this.belarusianRuble = belarusianRuble;
        this.euro = euro;
        this.dollar = dollar;
    }


    public BelarusianRuble getBelarusianRuble() {
        return belarusianRuble;
    }

    public void setBelarusianRuble(BelarusianRuble belarusianRuble) {
        this.belarusianRuble = belarusianRuble;
    }

    public Euro getEuro() {
        return euro;
    }

    public void setEuro(Euro euro) {
        this.euro = euro;
    }

    public Dollar getDollar() {
        return dollar;
    }

    public void setDollar(Dollar dollar) {
        this.dollar = dollar;
    }


    @Override
    public void run() {
        StockExchange stockExchange = StockExchange.getInstance();
        stockExchange.process(this);
    }

    private static int MINIMUM_EXCHANGE_AMOUNT = 30;
    public void exchange(Participant otherParticipant) throws TransactionException {
        if (euro.getBanknote() >= MINIMUM_EXCHANGE_AMOUNT) {
            sellEuro(otherParticipant);
        } else {
            buyEuro();
        }

        if (dollar.getBanknote() >= MINIMUM_EXCHANGE_AMOUNT) {
            sellDollar();
        } else {
            buyDollar();
        }
    }

    private void buyEuro() {

    }

    private void sellEuro(Participant otherParticipant) throws TransactionException {
        BelarusianRuble convertedEuroToBelarusianRuble = CurrencyConverter.convertToBelarusianRuble(euro);
        BelarusianRuble otherParticipantBelarusianRuble = otherParticipant.getBelarusianRuble();

        if (otherParticipantBelarusianRuble.compareTo(convertedEuroToBelarusianRuble) >= 0) {
            BelarusianRuble currentOtherParticipantBelarusianRuble = (BelarusianRuble) CurrencyCalculator
                    .subtract(otherParticipantBelarusianRuble, convertedEuroToBelarusianRuble);

            Euro currentOtherParticipantEuro = (Euro) CurrencyCalculator.add(otherParticipant.getEuro(), euro);


            otherParticipant.setBelarusianRuble(currentOtherParticipantBelarusianRuble);
            otherParticipant.setEuro(currentOtherParticipantEuro);

            BelarusianRuble currentThisBelarusianRuble = (BelarusianRuble) CurrencyCalculator
                    .add(belarusianRuble, convertedEuroToBelarusianRuble);

            setBelarusianRuble(currentThisBelarusianRuble);
            setEuro(new Euro(0, 0));
        }
    }


    private void buyDollar() {

    }

    private void sellDollar() {

    }


    private void buyBelarusianRuble() {

    }

    private void sellBelarusianRuble() {

    }
}
