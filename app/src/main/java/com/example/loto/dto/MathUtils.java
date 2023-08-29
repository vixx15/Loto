package com.example.loto.dto;




import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * Created by dejanm on 26-Apr-17.
 */

public class MathUtils {
    public static final DecimalFormat twoDecimals = new DecimalFormat("#.00");
    public static final DecimalFormat oneDecimal = new DecimalFormat("#.0");

    static {
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        oneDecimal.setDecimalFormatSymbols(sym);
        twoDecimals.setDecimalFormatSymbols(sym);
        //twoDecimals.setRoundingMode(RoundingMode.DOWN);
    }

    @Deprecated
    public static String roundQuota(double quota) {
        if (quota == 0) return "0";
//        if (quota <= 9.99) {//dve decimale
        return twoDecimals.format(quota);
//        } else if (quota <= 99.9) {
//            return oneDecimal.format(quota);
//        }
//        return Long.toString(Math.round(quota));
    }

    public static int compareInt(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    public static int compareDouble(double x, double y) {
        return BigDecimal.valueOf(x).compareTo(BigDecimal.valueOf(y));
    }

    //    TODO ovo nece raditi za ogromne brojeve, moze se prebaciti numberOfCombinations u long i razmotriti dodatne optimizacije
//    ali je za sada ovo dovoljno dobro jer ionako se dalje prosledjuje ticketCruncheru
    public static int calculateNumberOfCombinations(List<TicketSystemCombinationGroup> combinationGroups) {
        int numberOfCombinations = 0;
        try {
            for (TicketSystemCombinationGroup combinationGroup : combinationGroups) {
                numberOfCombinations += calculateNumberOfCombinationsForGroup(combinationGroup.getFromHowMany(), combinationGroup.getHowMany());
                if (numberOfCombinations > 10000) {
                    return numberOfCombinations;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return numberOfCombinations;
    }

    private static int calculateNumberOfCombinationsForGroup(int fromHowMany, int howMany) {
        int numberOfCombinations = 1;
        for (int i = 0; i < howMany; i++) {
            numberOfCombinations *= (fromHowMany - i);
            numberOfCombinations /= (i + 1);
        }
        return numberOfCombinations;
    }

    @Deprecated
    public static DecimalFormat getQuotaDecimalFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.00");
        df.setDecimalFormatSymbols(symbols);
        return df;
    }

    public static int compareLong(long number1, long number2) {
        long result = number1 - number2;
        if (result > 0) {
            return 1;
        } else if (result < 0) {
            return -1;
        }
        return 0;
    }

}
