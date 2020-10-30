package com.daytrading;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RiskManagement {

    private long investment;
    private double rptPercentage;
    private double entryPrice;
    private double exitPrice;
    private double stopLoss;
    private long expectedQuantity;
    private long expectedCapital;

    public RiskManagement(long investment, double rptPercentage, double entryPrice, double exitPrice, double stopLoss) {
        this.investment = investment;
        this.rptPercentage = rptPercentage;
        this.entryPrice = entryPrice;
        this.exitPrice = exitPrice;
        this.stopLoss = stopLoss;
    }
    
    public long rpt(){

        return (long) Math.ceil((investment*rptPercentage)/100);
    }

    public double profitPerStock(){

        if(entryPrice>exitPrice){
            return round(entryPrice-exitPrice, 2);
        }

        return round(exitPrice-entryPrice, 2);
    }

    public double lossPerStock(){

        if(entryPrice>stopLoss){
            return round(entryPrice-stopLoss, 2);
        }

        return round(stopLoss-entryPrice, 2);
    }

    public double profitPerStockPercentage(){

        double profit = profitPerStock();

        return round((profit/entryPrice)*100, 2);
    }

    public double lossPerStockPercentage(){

        double loss = lossPerStock();

        return round((loss/entryPrice)*100, 2);
    }

    public String riskToRewardRatio(){

        double profit = profitPerStock();
        double loss = lossPerStock();

        double res = 0.0;

        if(loss<profit){
            res = round(profit/loss, 2);

            return "1:"+String.valueOf(res);
        }else{
            res = round(loss/profit, 2);

            return String.valueOf(res)+":1";
        }
    }

    public long expectedQuantity(){

        double res = rpt()/lossPerStock();

        return (long) Math.floor(res);
    }

    public long expectedCapital(){

        return (long) Math.ceil(expectedQuantity()*entryPrice);
    }

    public long expectedCapitalWithMargin(){

        double res = expectedQuantity()*entryPrice;

        return (long) Math.ceil((res*15)/100); // 15% MARGIN ONLY PAY
    }

    public double totalProfit(){

        double profit = profitPerStock();

        return round(profit*expectedQuantity(), 2);

    }

    public double totalProfitPercentage(){

        double profit = totalProfit();

        return round((profit/expectedCapitalWithMargin())*100, 2);
    }

    public String toString(){

        String str = "Your Risk Per Trade(RPT) will be Rs. "+rpt()+"\n\n" +
                "Profit Per Stock = Rs. "+profitPerStock()+" ("+profitPerStockPercentage()+"%)\n\n" +
                "Loss Per Stock = Rs. "+lossPerStock()+" ("+lossPerStockPercentage()+"%)\n\n" +
                "RR Ratio = "+riskToRewardRatio()+"\n\n" +
                "Capital Required will be Rs. "+expectedCapital()+"\n\n" +
                "But you need to pay (15% Margin) will be Rs. "+expectedCapitalWithMargin()+"\n\n"+
                "Also Quantity will be "+expectedQuantity()+"\n\n" +
                "Total Profit suppose to Rs. "+totalProfit()+" ("+totalProfitPercentage()+"%)\n\n";

        return str;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
