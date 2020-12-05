package com.daytrading;

public class GetQuantityData {

    private String stopLoss;
    private String quantity;

    public GetQuantityData(String stopLoss, String quantity) {
        this.stopLoss = stopLoss;
        this.quantity = quantity;
    }

    public String getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(String stopLoss) {
        this.stopLoss = stopLoss;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
