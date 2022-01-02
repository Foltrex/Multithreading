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
        try {
            stockExchange.process(this);
        } catch (InterruptedException | TransactionException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final int MINIMUM_EXCHANGE_AMOUNT = 30;
    public void exchange(Participant otherParticipant) throws TransactionException {
        if (euro.getBanknote() >= MINIMUM_EXCHANGE_AMOUNT) {
            sellEuro(otherParticipant);
        } else {
            buyEuro(otherParticipant);
        }

        if (dollar.getBanknote() >= MINIMUM_EXCHANGE_AMOUNT) {
            sellDollar(otherParticipant);
        } else {
            buyDollar(otherParticipant);
        }
    }

    private void buyEuro(Participant otherParticipant) throws TransactionException {
        Euro convertedBelarusianRubleToEuro = CurrencyConverter.convertToEuro(belarusianRuble);
        Euro otherParticipantEuro = otherParticipant.getEuro();

        if (otherParticipantEuro.compareTo(convertedBelarusianRubleToEuro) >= 0) {
            Euro currentOtherParticipantEuro = (Euro) CurrencyCalculator
                    .subtract(otherParticipantEuro, convertedBelarusianRubleToEuro);

            BelarusianRuble currentOtherParticipantBelarusianRuble = (BelarusianRuble) CurrencyCalculator
                    .add(otherParticipant.getBelarusianRuble(), belarusianRuble);


            Euro currentThisEuro = (Euro) CurrencyCalculator.add(euro, convertedBelarusianRubleToEuro);

            otherParticipant.setEuro(currentOtherParticipantEuro);
            otherParticipant.setBelarusianRuble(currentOtherParticipantBelarusianRuble);

            setEuro(currentThisEuro);
            setBelarusianRuble(new BelarusianRuble(0, 0));
        }
    }

    private void sellEuro(Participant otherParticipant) throws TransactionException {
        BelarusianRuble convertedEuroToBelarusianRuble = CurrencyConverter.convertToBelarusianRuble(euro);
        BelarusianRuble otherParticipantBelarusianRuble = otherParticipant.getBelarusianRuble();

        if (otherParticipantBelarusianRuble.compareTo(convertedEuroToBelarusianRuble) >= 0) {
            BelarusianRuble currentOtherParticipantBelarusianRuble = (BelarusianRuble) CurrencyCalculator
                    .subtract(otherParticipantBelarusianRuble, convertedEuroToBelarusianRuble);

            Euro currentOtherParticipantEuro = (Euro) CurrencyCalculator.add(otherParticipant.getEuro(), euro);


            BelarusianRuble currentThisBelarusianRuble = (BelarusianRuble) CurrencyCalculator
                    .add(belarusianRuble, convertedEuroToBelarusianRuble);


            otherParticipant.setBelarusianRuble(currentOtherParticipantBelarusianRuble);
            otherParticipant.setEuro(currentOtherParticipantEuro);

            setBelarusianRuble(currentThisBelarusianRuble);
            setEuro(new Euro(0, 0));
        }
    }


    private void buyDollar(Participant otherParticipant) throws TransactionException {
        Dollar convertedBelarusianRubleToDollar = CurrencyConverter.convertToDollar(belarusianRuble);
        Dollar otherParticipantDollar = otherParticipant.getDollar();

        if (otherParticipantDollar.compareTo(convertedBelarusianRubleToDollar) >= 0) {
            Dollar currentOtherParticipantDollar = (Dollar) CurrencyCalculator
                    .subtract(otherParticipantDollar, convertedBelarusianRubleToDollar);

            BelarusianRuble currentOtherParticipantBelarusianRuble = (BelarusianRuble) CurrencyCalculator
                    .add(otherParticipant.getBelarusianRuble(), belarusianRuble);


            Dollar currentThisDollar = (Dollar) CurrencyCalculator.add(dollar, convertedBelarusianRubleToDollar);

            otherParticipant.setDollar(currentOtherParticipantDollar);
            otherParticipant.setBelarusianRuble(currentOtherParticipantBelarusianRuble);

            setDollar(currentThisDollar);
            setBelarusianRuble(new BelarusianRuble(0, 0));
        }
    }

    private void sellDollar(Participant otherParticipant) throws TransactionException {
        BelarusianRuble convertedDollarToBelarusianRuble = CurrencyConverter.convertToBelarusianRuble(dollar);
        BelarusianRuble otherParticipantBelarusianRuble = otherParticipant.getBelarusianRuble();

        if (otherParticipantBelarusianRuble.compareTo(convertedDollarToBelarusianRuble) >= 0) {
            BelarusianRuble currentOtherParticipantBelarusianRuble = (BelarusianRuble) CurrencyCalculator
                    .subtract(otherParticipantBelarusianRuble, convertedDollarToBelarusianRuble);

            Dollar currentOtherParticipantDollar = (Dollar) CurrencyCalculator.add(otherParticipant.getDollar(), dollar);


            BelarusianRuble currentThisBelarusianRuble = (BelarusianRuble) CurrencyCalculator
                    .add(belarusianRuble, convertedDollarToBelarusianRuble);


            otherParticipant.setBelarusianRuble(currentOtherParticipantBelarusianRuble);
            otherParticipant.setDollar(currentOtherParticipantDollar);

            setBelarusianRuble(currentThisBelarusianRuble);
            setDollar(new Dollar(0, 0));
        }
    }

    @Override
    public String toString() {
        return "Participant{" +
                "belarusianRuble=" + belarusianRuble +
                ", euro=" + euro +
                ", dollar=" + dollar +
                '}';
    }
}
