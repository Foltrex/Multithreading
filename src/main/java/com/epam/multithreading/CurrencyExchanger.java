package com.epam.multithreading;

import com.epam.multithreading.entity.Participant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CurrencyExchanger {
    private static final Logger LOGGER = LogManager.getLogger(CurrencyExchanger.class);

    // Currency curses
    private static final double EURO_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO = 2.908912;
    private static final double DOLLAR_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO = 2.557521;

    private static final double BELARUSIAN_RUBLE_TO_EURO_CONVERSION_RATIO = 0.343846;
    private static final double BELARUSIAN_RUBLE_TO_DOLLAR_CONVERSION_RATIO = 0.391003;

    public void exchangeCurrency(Participant firstParticipant, Participant secondParticipant) {

        double amountOfBuyingDollars = Math.random() * 400;
        double amountOfBuyingEuro = Math.random() * 400;
        double amountOfBuyingBelarusianRubles = Math.random() * 1000;

        if (isEnoughToBuyDollars(firstParticipant, secondParticipant, amountOfBuyingDollars)) {
            buyDollars(firstParticipant, secondParticipant, amountOfBuyingDollars);
        }

        if (isEnoughToBuyEuro(firstParticipant, secondParticipant, amountOfBuyingEuro)) {
            buyEuro(firstParticipant, secondParticipant, amountOfBuyingEuro);
        }

        if (isEnoughToBuyBelarusianRubles(firstParticipant, secondParticipant, amountOfBuyingBelarusianRubles)) {
            buyBelarusianRubles(firstParticipant, secondParticipant, amountOfBuyingBelarusianRubles);
        }

        LOGGER.info(firstParticipant.getName() + " exchanged currency with " + secondParticipant.getName());
    }


    private boolean isEnoughToBuyDollars(Participant firstParticipant, Participant secondParticipant, double amountOfBuyingDollars) {
        double currentAmountOfConvertedDollars = firstParticipant.getBelarusianRuble() * BELARUSIAN_RUBLE_TO_DOLLAR_CONVERSION_RATIO;

        return Double.compare(currentAmountOfConvertedDollars, amountOfBuyingDollars) >= 0 &&
                Double.compare(secondParticipant.getDollar(), amountOfBuyingDollars) >= 0;
    }

    private boolean isEnoughToBuyEuro(Participant firstParticipant, Participant secondParticipant, double amountOfBuyingEuro) {
        double currentAmountOfConvertedEuro = firstParticipant.getBelarusianRuble() * BELARUSIAN_RUBLE_TO_EURO_CONVERSION_RATIO;

        return Double.compare(currentAmountOfConvertedEuro, amountOfBuyingEuro) >= 0 &&
                Double.compare(secondParticipant.getEuro(), amountOfBuyingEuro) >= 0;
    }

    private boolean isEnoughToBuyBelarusianRubles(Participant firstParticipant, Participant secondParticipant, double amountOfBuyingBelarusianRubles) {
        double dollarsConvertedOnBelarusianRubles = firstParticipant.getDollar() * DOLLAR_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;
        double euroConvertedOnBelarusianRubles = firstParticipant.getEuro() * EURO_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;

        return Double.compare(secondParticipant.getBelarusianRuble(), amountOfBuyingBelarusianRubles) >= 0 &&
                (Double.compare(dollarsConvertedOnBelarusianRubles, amountOfBuyingBelarusianRubles) >= 0 ||
                        Double.compare(euroConvertedOnBelarusianRubles, amountOfBuyingBelarusianRubles) >= 0);
    }


    private void buyDollars(Participant firstParticipnat, Participant secondParticipant, double amountOfBuyingDollars) {
        double dollarsConvertedOnRubles = amountOfBuyingDollars * DOLLAR_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;

        double thisParticipantRemainingAmountOfBelarusianRubles = firstParticipnat.getBelarusianRuble() - dollarsConvertedOnRubles;
        double thisParticipantRealAmountOfDollars = firstParticipnat.getDollar() + amountOfBuyingDollars;

        double otherParticipantRemainingAmountOfDollars = secondParticipant.getDollar() - amountOfBuyingDollars;
        double otherParticipantRealAmountOfBelarusianRubles = secondParticipant.getBelarusianRuble() + dollarsConvertedOnRubles;

        firstParticipnat.setDollar(thisParticipantRealAmountOfDollars);
        firstParticipnat.setBelarusianRuble(thisParticipantRemainingAmountOfBelarusianRubles);

        secondParticipant.setBelarusianRuble(otherParticipantRealAmountOfBelarusianRubles);
        secondParticipant.setDollar(otherParticipantRemainingAmountOfDollars);
    }

    private void buyEuro(Participant firstParticipant, Participant secondParticipant, double amountOfBuyingEuro) {
        double euroConvertedOnRubles = amountOfBuyingEuro * EURO_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;

        double thisParticipantRemainingAmountOfBelarusianRubles = firstParticipant.getBelarusianRuble() - euroConvertedOnRubles;
        double thisParticipantRealAmountOfEuro = firstParticipant.getEuro() + amountOfBuyingEuro;

        double otherParticipantRemainingAmountOfEuro = secondParticipant.getEuro() - amountOfBuyingEuro;
        double otherParticipantRealAmountOfBelarusianRubles = secondParticipant.getBelarusianRuble() + euroConvertedOnRubles;

        firstParticipant.setEuro(thisParticipantRealAmountOfEuro);
        firstParticipant.setBelarusianRuble(thisParticipantRemainingAmountOfBelarusianRubles);

        secondParticipant.setBelarusianRuble(otherParticipantRealAmountOfBelarusianRubles);
        secondParticipant.setEuro(otherParticipantRemainingAmountOfEuro);
    }

    private void buyBelarusianRubles(Participant firstParticipant, Participant secondParticipant, double amountOfBuyingBelarusianRubles) {
        double thisParticipantEuroConvertedOnBelarusianRubles = firstParticipant.getEuro() * EURO_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;

        double thisParticipantRemainingAmountOfEuro = firstParticipant.getEuro();
        double thisParticipantRemainingAmountOfDollars = firstParticipant.getDollar();

        double otherParticipantRealAmountOfEuro = secondParticipant.getEuro();
        double otherParticipantRealAmountOfDollars = secondParticipant.getDollar();

        // if there is enough euro, then we buy for euro, otherwise we buy for dollars
        if (Double.compare(thisParticipantEuroConvertedOnBelarusianRubles, amountOfBuyingBelarusianRubles) >= 0) {
            thisParticipantRemainingAmountOfEuro -= amountOfBuyingBelarusianRubles * BELARUSIAN_RUBLE_TO_EURO_CONVERSION_RATIO;
            otherParticipantRealAmountOfEuro += amountOfBuyingBelarusianRubles * BELARUSIAN_RUBLE_TO_EURO_CONVERSION_RATIO;
        } else {
            thisParticipantRemainingAmountOfDollars -= amountOfBuyingBelarusianRubles * BELARUSIAN_RUBLE_TO_DOLLAR_CONVERSION_RATIO;
            otherParticipantRealAmountOfDollars += amountOfBuyingBelarusianRubles * BELARUSIAN_RUBLE_TO_DOLLAR_CONVERSION_RATIO;
        }

        double thisParticipantRealAmountOfBelarusianRubles = firstParticipant.getBelarusianRuble() + amountOfBuyingBelarusianRubles;
        double otherParticipantRemainingAmountOfBelarusianRubles = secondParticipant.getBelarusianRuble() - amountOfBuyingBelarusianRubles;

        firstParticipant.setBelarusianRuble(thisParticipantRealAmountOfBelarusianRubles);
        firstParticipant.setEuro(thisParticipantRemainingAmountOfEuro);
        firstParticipant.setDollar(thisParticipantRemainingAmountOfDollars);

        secondParticipant.setEuro(otherParticipantRealAmountOfEuro);
        secondParticipant.setDollar(otherParticipantRealAmountOfDollars);
        secondParticipant.setBelarusianRuble(otherParticipantRemainingAmountOfBelarusianRubles);
    }
}
