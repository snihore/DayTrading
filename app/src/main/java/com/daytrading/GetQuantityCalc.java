package com.daytrading;

import android.content.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetQuantityCalc {

    private Context context;
    private long investment;
    private double rptPercentage;
    private double entry;
    private double rrValue;
    private String tradeType;

    private static final double[] stopLossPercentages = {
            0.20,
            0.25,
            0.30,
            0.35,
            0.40,
            0.45,
            0.50
    };

    public GetQuantityCalc(Context context, long investment, double rptPercentage, double entry, double rrValue, String tradeType) {
        this.context = context;
        this.investment = investment;
        this.rptPercentage = rptPercentage;
        this.entry = entry;
        this.rrValue = rrValue;
        this.tradeType = tradeType;
    }

    public List<Double> getStopLossPercentages(){

        List<Double> list = new ArrayList<>();

        for(int i=0; i<stopLossPercentages.length; i++){
            list.add(stopLossPercentages[i]);
        }

        return list;
    }

    public List<Long> getQuantites(){

        List<Long> list = new ArrayList<>();

        for(int i=0; i<stopLossPercentages.length; i++){

            double stopLossPercentage = stopLossPercentages[i];

            double stopLossPoint = getPointChange(stopLossPercentage);

            double stopLoss = getStopLoss(stopLossPoint);

            RiskManagement riskManagement = new RiskManagement(
                    context,
                    investment,
                    rptPercentage,
                    entry,
                    0.0,
                    stopLoss
            );

            long quantity = riskManagement.expectedQuantity();

            list.add(quantity);

        }

        return list;
    }

    public String toString(){

        List<Double> percentages = getStopLossPercentages();
        List<Long> quantites = getQuantites();

        String finalStr = "Quantities:\n\n";

        for(int i=0; i<quantites.size(); i++){
            long quantity = quantites.get(i);
            double percentage = percentages.get(i);

            String str = "Stop Loss:\t"+percentage+"\t\t-->\t\tQty:\t"+quantity;

            finalStr += (str+"\n\n");
        }

        return finalStr;
    }

    private double getPointChange(double percentage){
        double point = (entry*percentage)/100;

        return round(point, 2);
    }

    private double getStopLoss(double stopLossPoint){

        if(tradeType.equals("BUY")){

            return round(entry-stopLossPoint, 2);

        }else if(tradeType.equals("SELL")){

            return round(entry+stopLossPoint, 2);
        }

        return 0.0;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
