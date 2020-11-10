package com.daytrading;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageCalc {

    private double entry;
    private double percentage;
    private String tradeType;

    public PercentageCalc(double entry, double percentage, String tradeType) {
        this.entry = entry;
        this.percentage = percentage;
        this.tradeType = tradeType;
    }

    public double getPointChange(){
        double point = (entry*percentage)/100;

        return round(point, 2);
    }

    public double getExit(){

        if(tradeType.equals("BUY")){

            return round(entry+getPointChange(), 2);

        }else if(tradeType.equals("SELL")){

            return round(entry-getPointChange(), 2);
        }

        return 0.0;
    }

    public double getStopLoss(){

        if(tradeType.equals("BUY")){

            return round(entry-getPointChange(), 2);

        }else if(tradeType.equals("SELL")){

            return round(entry+getPointChange(), 2);
        }

        return 0.0;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
