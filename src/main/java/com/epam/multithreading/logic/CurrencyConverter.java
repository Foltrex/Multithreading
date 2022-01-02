package com.epam.multithreading.logic;

import com.epam.multithreading.entity.currency.BelarusianRuble;
import com.epam.multithreading.entity.currency.Dollar;
import com.epam.multithreading.entity.currency.Euro;

public class CurrencyConverter {
    private static final double EURO_TO_DOLLAR_CONVERSION_RATIO = 1.137395;
    private static final double EURO_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO = 2.908912;

    private static final double DOLLAR_TO_EURO_CONVERSION_RATIO = 0.879395;
    private static final double DOLLAR_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO = 2.557521;

    private static final double BELARUSIAN_RUBLE_TO_EURO_CONVERSION_RATIO = 0.343846;
    private static final double BELARUSIAN_RUBLE_TO_DOLLAR_CONVERSION_RATIO = 0.391003;

    private CurrencyConverter() {}

    public static Euro convertToEuro(Dollar dollar) {
        double totalCoinsOfDollar = 100 * dollar.getBanknote() + dollar.getCoins();
        double totalCoinsOfEuro = totalCoinsOfDollar * DOLLAR_TO_EURO_CONVERSION_RATIO;

        int banknotesOfEuro = (int) (totalCoinsOfEuro / 100);
        int coinsOfEuro = (int) (totalCoinsOfEuro - 100 * banknotesOfEuro);

        return new Euro(banknotesOfEuro, coinsOfEuro);
    }

    public static Euro convertToEuro(BelarusianRuble belarusianRuble) {
        double totalCoinsOfBelarusianRuble = 100 * belarusianRuble.getBanknote() + belarusianRuble.getCoins();
        double totalCoinsOfEuro = totalCoinsOfBelarusianRuble * BELARUSIAN_RUBLE_TO_EURO_CONVERSION_RATIO;

        int banknotesOfEuro = (int) (totalCoinsOfEuro / 100);
        int coinsOfEuro = (int) (totalCoinsOfEuro - 100 * banknotesOfEuro);

        return new Euro(banknotesOfEuro, coinsOfEuro);
    }


    public static Dollar convertToDollar(Euro euro) {
        double totalCoinsOfEuro = 100 * euro.getBanknote() + euro.getCoins();
        double totalCoinsOfDollar = totalCoinsOfEuro * EURO_TO_DOLLAR_CONVERSION_RATIO;

        int banknotesOfDollar = (int) (totalCoinsOfDollar / 100);
        int coinsOfDollar = (int) (totalCoinsOfDollar - 100 * banknotesOfDollar);

        return new Dollar(banknotesOfDollar, coinsOfDollar);
    }

    public static Dollar convertToDollar(BelarusianRuble belarusianRuble) {
        double totalCoinsOfBelarusianRuble = 100 * belarusianRuble.getBanknote() + belarusianRuble.getCoins();
        double totalCoinsOfDollar = totalCoinsOfBelarusianRuble * BELARUSIAN_RUBLE_TO_DOLLAR_CONVERSION_RATIO;

        int banknotesOfDollar = (int) (totalCoinsOfDollar / 100);
        int coinsOfDollar = (int) (totalCoinsOfDollar - 100 * banknotesOfDollar);

        return new Dollar(banknotesOfDollar, coinsOfDollar);
    }


    public static BelarusianRuble convertToBelarusianRuble(Euro euro) {
        double totalCoinsOfEuro = 100 * euro.getBanknote() + euro.getCoins();
        double totalCoinsOfBelarusianRuble = totalCoinsOfEuro * EURO_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;

        int banknotesOfDollar = (int) (totalCoinsOfBelarusianRuble / 100);
        int coinsOfDollar = (int) (totalCoinsOfBelarusianRuble - 100 * banknotesOfDollar);

        return new BelarusianRuble(banknotesOfDollar, coinsOfDollar);
    }

    public static BelarusianRuble convertToBelarusianRuble(Dollar dollar) {
        double totalCoinsOfDollar = 100 * dollar.getBanknote() + dollar.getCoins();
        double totalCoinsOfBelarusianRuble = totalCoinsOfDollar * DOLLAR_TO_BELARUSIAN_RUBLE_CONVERSION_RATIO;

        int banknotesOfDollar = (int) (totalCoinsOfBelarusianRuble / 100);
        int coinsOfDollar = (int) (totalCoinsOfBelarusianRuble - 100 * banknotesOfDollar);

        return new BelarusianRuble(banknotesOfDollar, coinsOfDollar);
    }
}
