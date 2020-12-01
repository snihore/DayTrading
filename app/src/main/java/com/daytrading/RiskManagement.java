package com.daytrading;

import android.content.Context;

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
    private Context context;

    public RiskManagement(Context context, long investment, double rptPercentage, double entryPrice, double exitPrice, double stopLoss) {
        this.context = context;
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

    public long expectedQuantity03(long capital){

        double capitalFull = (capital*100)/ Conf.getMargin(context); // 15% MARGIN, Now 25%

        double res = capitalFull/entryPrice;

        return (long) Math.floor(res);
    }

    public long expectedCapital(){

        return (long) Math.ceil(expectedQuantity()*entryPrice);
    }

    public long expectedCapital02(long quantity){
        return (long) Math.ceil(quantity*entryPrice);
    }

    public long expectedCapital03(long capital){
        double capitalFull = (capital*100)/ Conf.getMargin(context); // 15% MARGIN, Now 25%
        return (long) Math.ceil(capitalFull);
    }

    public long expectedCapitalWithMargin(){

        double res = expectedQuantity()*entryPrice;

        return (long) Math.ceil((res*Conf.getMargin(context))/100); // 15% MARGIN ONLY PAY, Now 25%
    }
    public long expectedCapitalWithMargin02(long quantity){

        double res = quantity*entryPrice;

        return (long) Math.ceil((res*Conf.getMargin(context))/100); // 15% MARGIN ONLY PAY, Now 25%
    }


    public double totalProfit(){

        double profit = profitPerStock();

        return round(profit*expectedQuantity(), 2);

    }

    public double totalLoss(){

        double loss = lossPerStock();

        return round(loss*expectedQuantity(), 2);

    }

    public double totalProfit02(long quantity){

        double profit = profitPerStock();

        return round(profit*quantity, 2);

    }

    public double totalLoss02(long quantity){

        double loss = lossPerStock();

        return round(loss*quantity, 2);

    }

    public double totalProfit03(long capital){

        double profit = profitPerStock();

        return round(profit*expectedQuantity03(capital), 2);

    }

    public double totalLoss03(long capital){

        double loss = lossPerStock();

        return round(loss*expectedQuantity03(capital), 2);

    }

    public double totalProfitPercentage(){

        double profit = totalProfit();

        return round((profit/expectedCapitalWithMargin())*100, 2);
    }

    public double totalLossPercentage(){

        double loss = totalLoss();

        return round((loss/expectedCapitalWithMargin())*100, 2);
    }

    public double totalProfitPercentage02(long quantity){

        double profit = totalProfit02(quantity);

        return round((profit/expectedCapitalWithMargin02(quantity))*100, 2);
    }

    public double totalLossPercentage02(long quantity){

        double loss = totalLoss02(quantity);

        return round((loss/expectedCapitalWithMargin02(quantity))*100, 2);
    }

    public double totalProfitPercentage03(long capital){

        double profit = totalProfit03(capital);

        return round((profit/capital)*100, 2);
    }

    public double totalLossPercentage03(long capital){

        double loss = totalLoss03(capital);

        return round((loss/capital)*100, 2);
    }

    public String toString(){

        String str = "Your Risk Per Trade(RPT) will be Rs. "+rpt()+"\n\n" +
                "Profit Per Stock = Rs. "+profitPerStock()+" ("+profitPerStockPercentage()+"%)\n\n" +
                "Loss Per Stock = Rs. "+lossPerStock()+" ("+lossPerStockPercentage()+"%)\n\n" +
                "RR Ratio = "+riskToRewardRatio()+"\n\n" +
                "Capital Required will be Rs. "+expectedCapital()+"\n\n" +
                "But you need to pay ("+Conf.getMargin(context)+"% Margin) will be Rs. "+expectedCapitalWithMargin()+"\n\n"+
                "Also Quantity will be "+expectedQuantity()+"\n\n" +
                "Total Profit suppose to Rs. "+totalProfit()+" ("+totalProfitPercentage()+"%)\n\n"+
                "Total Loss suppose to Rs. "+totalLoss()+" ("+totalLossPercentage()+"%)\n\n";

        return str;
    }
    public String toString02(long quantity){

        String str = "Your Risk Per Trade(RPT) will be Rs. "+rpt()+"\n\n" +
                "Profit Per Stock = Rs. "+profitPerStock()+" ("+profitPerStockPercentage()+"%)\n\n" +
                "Loss Per Stock = Rs. "+lossPerStock()+" ("+lossPerStockPercentage()+"%)\n\n" +
                "RR Ratio = "+riskToRewardRatio()+"\n\n" +
                "Capital Required will be Rs. "+expectedCapital02(quantity)+"\n\n" +
                "But you need to pay ("+Conf.getMargin(context)+"% Margin) will be Rs. "+expectedCapitalWithMargin02(quantity)+"\n\n"+
                "Also Quantity will be "+quantity+"\n\n" +
                "Total Profit suppose to Rs. "+totalProfit02(quantity)+" ("+totalProfitPercentage02(quantity)+"%)\n\n"+
                "Total Loss suppose to Rs. "+totalLoss02(quantity)+" ("+totalLossPercentage02(quantity)+"%)\n\n";

        return str;
    }
    public String toString03(long capital){

        String str = "Your Risk Per Trade(RPT) will be Rs. "+rpt()+"\n\n" +
                "Profit Per Stock = Rs. "+profitPerStock()+" ("+profitPerStockPercentage()+"%)\n\n" +
                "Loss Per Stock = Rs. "+lossPerStock()+" ("+lossPerStockPercentage()+"%)\n\n" +
                "RR Ratio = "+riskToRewardRatio()+"\n\n" +
                "Capital Required will be Rs. "+expectedCapital03(capital)+"\n\n" +
                "But you need to pay ("+Conf.getMargin(context)+"% Margin) will be Rs. "+capital+"\n\n"+
                "Also Quantity will be "+expectedQuantity03(capital)+"\n\n" +
                "Total Profit suppose to Rs. "+totalProfit03(capital)+" ("+totalProfitPercentage03(capital)+"%)\n\n"+
                "Total Loss suppose to Rs. "+totalLoss03(capital)+" ("+totalLossPercentage03(capital)+"%)\n\n";

        return str;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
