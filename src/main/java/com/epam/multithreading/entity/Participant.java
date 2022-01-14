package com.epam.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Participant implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(Participant.class);

    private String name;
    private double belarusianRuble;
    private double euro;
    private double dollar;


    // Currency curses
    private static final double EURO_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO = 2.908912;
    private static final double DOLLAR_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO = 2.557521;

    private static final double BELARUSIAN_RUBLE_TO_EURO_CONVERSION_RATIO = 0.343846;
    private static final double BELARUSIAN_RUBLE_TO_DOLLAR_CONVERSION_RATIO = 0.391003;



    public Participant() {
    }

    public Participant(String name, double belarusianRuble, double euro, double dollar) {
        this.name = name;
        this.belarusianRuble = belarusianRuble;
        this.euro = euro;
        this.dollar = dollar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBelarusianRuble() {
        return belarusianRuble;
    }

    public void setBelarusianRuble(double belarusianRuble) {
        this.belarusianRuble = belarusianRuble;
    }

    public double getEuro() {
        return euro;
    }

    public void setEuro(double euro) {
        this.euro = euro;
    }

    public double getDollar() {
        return dollar;
    }

    public void setDollar(double dollar) {
        this.dollar = dollar;
    }

    @Override
    public void run() {
        StockExchange stockExchange = StockExchange.getInstance();
        try {
            stockExchange.process(this);

        } catch (InterruptedException ex) {
            LOGGER.warn(ex.getMessage(), ex);
        }
    }


    public void exchangeCurrency(Participant participant) {

        double amountOfBuyingDollars = Math.random() * 400;
        double amountOfBuyingEuro = Math.random() * 400;
        double amountOfBuyingBelarusianRubles = Math.random() * 1000;

        if (isEnoughToBuyDollars(participant, amountOfBuyingDollars)) {
            buyDollars(participant, amountOfBuyingDollars);
        }

        if (isEnoughToBuyEuro(participant, amountOfBuyingEuro)) {
            buyEuro(participant, amountOfBuyingEuro);
        }

        if (isEnoughToBuyBelarusianRubles(participant, amountOfBuyingBelarusianRubles)) {
            buyBelarusianRubles(participant, amountOfBuyingBelarusianRubles);
        }
    }


    private boolean isEnoughToBuyDollars(Participant participant, double amountOfBuyingDollars) {
        double currentAmountOfConvertedDollars = this.getBelarusianRuble() * BELARUSIAN_RUBLE_TO_DOLLAR_CONVERSION_RATIO;

        return Double.compare(currentAmountOfConvertedDollars, amountOfBuyingDollars) >= 0 &&
                Double.compare(participant.getDollar(), amountOfBuyingDollars) >= 0;
    }

    private boolean isEnoughToBuyEuro(Participant participant, double amountOfBuyingEuro) {
        double currentAmountOfConvertedEuro = this.getBelarusianRuble() * BELARUSIAN_RUBLE_TO_EURO_CONVERSION_RATIO;

        return Double.compare(currentAmountOfConvertedEuro, amountOfBuyingEuro) >= 0 &&
                Double.compare(participant.getEuro(), amountOfBuyingEuro) >= 0;
    }

    private boolean isEnoughToBuyBelarusianRubles(Participant participant, double amountOfBuyingBelarusianRubles) {
        double dollarsConvertedOnBelarusianRubles = this.getDollar() * DOLLAR_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;
        double euroConvertedOnBelarusianRubles = this.getEuro() * EURO_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;

        return Double.compare(participant.getBelarusianRuble(), amountOfBuyingBelarusianRubles) >= 0 &&
                (Double.compare(dollarsConvertedOnBelarusianRubles, amountOfBuyingBelarusianRubles) >= 0 ||
                        Double.compare(euroConvertedOnBelarusianRubles, amountOfBuyingBelarusianRubles) >= 0);
    }


    private void buyDollars(Participant participant, double amountOfBuyingDollars) {
        double dollarsConvertedOnRubles = amountOfBuyingDollars * DOLLAR_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;

        double thisParticipantRemainingAmountOfBelarusianRubles = this.getBelarusianRuble() - dollarsConvertedOnRubles;
        double thisParticipantRealAmountOfDollars = this.getDollar() + amountOfBuyingDollars;

        double otherParticipantRemainingAmountOfDollars = participant.getDollar() - amountOfBuyingDollars;
        double otherParticipantRealAmountOfBelarusianRubles = participant.getBelarusianRuble() + dollarsConvertedOnRubles;

        this.setDollar(thisParticipantRealAmountOfDollars);
        this.setBelarusianRuble(thisParticipantRemainingAmountOfBelarusianRubles);

        participant.setBelarusianRuble(otherParticipantRealAmountOfBelarusianRubles);
        participant.setDollar(otherParticipantRemainingAmountOfDollars);
    }

    private void buyEuro(Participant participant, double amountOfBuyingEuro) {
        double euroConvertedOnRubles = amountOfBuyingEuro * EURO_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;

        double thisParticipantRemainingAmountOfBelarusianRubles = this.getBelarusianRuble() - euroConvertedOnRubles;
        double thisParticipantRealAmountOfEuro = this.getEuro() + amountOfBuyingEuro;

        double otherParticipantRemainingAmountOfEuro = participant.getEuro() - amountOfBuyingEuro;
        double otherParticipantRealAmountOfBelarusianRubles = participant.getBelarusianRuble() + euroConvertedOnRubles;

        this.setEuro(thisParticipantRealAmountOfEuro);
        this.setBelarusianRuble(thisParticipantRemainingAmountOfBelarusianRubles);

        participant.setBelarusianRuble(otherParticipantRealAmountOfBelarusianRubles);
        participant.setEuro(otherParticipantRemainingAmountOfEuro);
    }

    private void buyBelarusianRubles(Participant participant, double amountOfBuyingBelarusianRubles) {
        double thisParticipantEuroConvertedOnBelarusianRubles = this.getEuro() * EURO_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;

        double thisParticipantRemainingAmountOfEuro = this.getEuro();
        double thisParticipantRemainingAmountOfDollars = this.getDollar();

        double otherParticipantRealAmountOfEuro = participant.getEuro();
        double otherParticipantRealAmountOfDollars = participant.getDollar();

        // if there is enough euro, then we buy for euro, otherwise we buy for dollars
        if (Double.compare(thisParticipantEuroConvertedOnBelarusianRubles, amountOfBuyingBelarusianRubles) >= 0) {
            thisParticipantRemainingAmountOfEuro -= amountOfBuyingBelarusianRubles * BELARUSIAN_RUBLE_TO_EURO_CONVERSION_RATIO;
            otherParticipantRealAmountOfEuro += amountOfBuyingBelarusianRubles * BELARUSIAN_RUBLE_TO_EURO_CONVERSION_RATIO;
        } else {
            thisParticipantRemainingAmountOfDollars -= amountOfBuyingBelarusianRubles * BELARUSIAN_RUBLE_TO_DOLLAR_CONVERSION_RATIO;
            otherParticipantRealAmountOfDollars += amountOfBuyingBelarusianRubles * BELARUSIAN_RUBLE_TO_DOLLAR_CONVERSION_RATIO;
        }

        double thisParticipantRealAmountOfBelarusianRubles = this.getBelarusianRuble() + amountOfBuyingBelarusianRubles;
        double otherParticipantRemainingAmountOfBelarusianRubles = participant.getBelarusianRuble() - amountOfBuyingBelarusianRubles;

        this.setBelarusianRuble(thisParticipantRealAmountOfBelarusianRubles);
        this.setEuro(thisParticipantRemainingAmountOfEuro);
        this.setDollar(thisParticipantRemainingAmountOfDollars);

        participant.setEuro(otherParticipantRealAmountOfEuro);
        participant.setDollar(otherParticipantRealAmountOfDollars);
        participant.setBelarusianRuble(otherParticipantRemainingAmountOfBelarusianRubles);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "name='" + name + '\'' +
                ", belarusianRuble=" + belarusianRuble + " RUB"  +
                ", euro=" + euro + " EUR"  +
                ", dollar=" + dollar + " USD" +
                '}';
    }
}
