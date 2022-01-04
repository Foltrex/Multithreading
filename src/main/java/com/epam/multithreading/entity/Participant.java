package com.epam.multithreading.entity;

import com.epam.multithreading.StockExchange;
import com.epam.multithreading.entity.currency.Dollar;
import com.epam.multithreading.entity.currency.Euro;
import com.epam.multithreading.entity.currency.BelarusianRuble;
import com.epam.multithreading.entity.currency.Money;
import com.epam.multithreading.exception.TransactionException;
import com.epam.multithreading.logic.CurrencyCalculator;
import com.epam.multithreading.logic.CurrencyConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Participant implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(Participant.class);

    private String name;
    private BelarusianRuble belarusianRuble;
    private Euro euro;
    private Dollar dollar;


    public Participant() {
    }

    public Participant(String name, BelarusianRuble belarusianRuble, Euro euro, Dollar dollar) {
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
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private static final int MINIMUM_EXCHANGE_AMOUNT = 400;
    private static final Random random = new Random();
    public void exchange(Participant otherParticipant) throws TransactionException {
        if (euro.getBanknotes() < MINIMUM_EXCHANGE_AMOUNT && dollar.getBanknotes() < MINIMUM_EXCHANGE_AMOUNT) {
            if (random.nextBoolean()) {
                buyEuro(otherParticipant);
            } else {
                buyDollar(otherParticipant);
            }
        }

        if (euro.getBanknotes() >= MINIMUM_EXCHANGE_AMOUNT) {
            sellEuro(otherParticipant);
            buyDollar(otherParticipant);
        }

        if (dollar.getBanknotes() >= MINIMUM_EXCHANGE_AMOUNT) {
            sellDollar(otherParticipant);
            buyEuro(otherParticipant);
        }
    }

    private void buyEuro(Participant otherParticipant) throws TransactionException {
        if (belarusianRuble.getBanknotes() == 0 && belarusianRuble.getCoins() == 0) {
            LOGGER.info("\n"
                    + "Operation: convert belarusian ruble" + "\n"
                    + "Members: " + name + " -- " + otherParticipant.getName() + "\n"
                    + "Status: refused, because " + name + " doesn't have enough rubles\n");
            return;
        }

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

            logOperationResultMessage(LOGGER, true, belarusianRuble, convertedBelarusianRubleToEuro,
                    name, otherParticipant.getName());

            setEuro(currentThisEuro);
            setBelarusianRuble(new BelarusianRuble(0, 0));
        } else {
            logOperationResultMessage(LOGGER, false, belarusianRuble, convertedBelarusianRubleToEuro,
                    name, otherParticipant.getName());
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

            logOperationResultMessage(LOGGER, true, euro, convertedEuroToBelarusianRuble,
                    name, otherParticipant.getName());

            setBelarusianRuble(currentThisBelarusianRuble);
            setEuro(new Euro(0, 0));
        } else {
            logOperationResultMessage(LOGGER, false, euro, convertedEuroToBelarusianRuble,
                    name, otherParticipant.getName());
        }
    }


    private void buyDollar(Participant otherParticipant) throws TransactionException {
        if (belarusianRuble.getBanknotes() == 0 && belarusianRuble.getCoins() == 0) {
            LOGGER.info("\n"
                    + "Operation: convert belarusian ruble" + "\n"
                    + "Members: " + name + " -- " + otherParticipant.getName() + "\n"
                    + "Status: refused, because " + name + " doesn't have enough rubles\n");
            return;
        }

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

            logOperationResultMessage(LOGGER, true, belarusianRuble, convertedBelarusianRubleToDollar,
                    name, otherParticipant.getName());

            setDollar(currentThisDollar);
            setBelarusianRuble(new BelarusianRuble(0, 0));
        } else {
            logOperationResultMessage(LOGGER, false, belarusianRuble, convertedBelarusianRubleToDollar,
                    name, otherParticipant.getName());
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

            logOperationResultMessage(LOGGER, true, dollar, convertedDollarToBelarusianRuble,
                    name, otherParticipant.getName());

            setBelarusianRuble(currentThisBelarusianRuble);
            setDollar(new Dollar(0, 0));
        } else {
            logOperationResultMessage(LOGGER, false, dollar, convertedDollarToBelarusianRuble,
                    name, otherParticipant.getName());
        }
    }

    @Override
    public String toString() {
        return "\nParticipant{" +
                " name=" + name +
                "\t\tbelarusianRuble=" + belarusianRuble +
                ",\teuro=" + euro +
                ",\tdollar=" + dollar +
                "\t}\n";
    }

    private void logOperationResultMessage(org.apache.logging.log4j.Logger logger, boolean operationStatus, Money sourceMoney, Money convertedMoney,
                                           String thisParticipantName, String otherParticipantName) {

        if (operationStatus) {
            logger.info("\n"
                    + "Operation: " + sourceMoney + " --> " + convertedMoney + "\n"
                    + "Members: " + thisParticipantName + " -- " + otherParticipantName + "\n"
                    + "Status: accepted\n");
        } else {
            logger.info("\n"
                    + "Operation: " + sourceMoney + " --> " + convertedMoney + "\n"
                    + "Members: " + thisParticipantName + " -- " + otherParticipantName + "\n"
                    + "Status: refused, because " + otherParticipantName + " does not have this amount\n");
        }
    }
}
