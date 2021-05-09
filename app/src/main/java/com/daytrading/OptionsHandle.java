package com.daytrading;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class OptionsHandle {

    private long investment;
    private double rptPercentage;
    private int lotSize;
    private double buyAt;

    public OptionsHandle(long investment, double rptPercentage, int lotSize, double buyAt) {
        this.investment = investment;
        this.rptPercentage = rptPercentage;
        this.lotSize = lotSize;
        this.buyAt = buyAt;
    }

    public Map<String, Double> calculte(){

        double fixedLoss = (investment*rptPercentage)/100;

        double fixedProfit = fixedLoss*2;

        double totalRequiredCapital = lotSize*buyAt;

        double sellAtPer = (fixedProfit/totalRequiredCapital)*100;

        double stopLossAtPer = (fixedLoss/totalRequiredCapital)*100;


        double sellAt = (buyAt*(100 + sellAtPer))/100;

        double stopLossAt = (buyAt*(100 - stopLossAtPer))/100;

        Map<String, Double> map = new HashMap<>();

        map.put("fixedLoss", round(fixedLoss, 2));
        map.put("fixedProfit", round(fixedProfit, 2));
        map.put("totalRequiredCapital", round(totalRequiredCapital, 2));
        map.put("sellAt", round(sellAt, 2));
        map.put("sellAtPer", round(sellAtPer, 2));
        map.put("stopLossAt", round(stopLossAt, 2));
        map.put("stopLossAtPer", round(stopLossAtPer, 2));

        return map;

    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
