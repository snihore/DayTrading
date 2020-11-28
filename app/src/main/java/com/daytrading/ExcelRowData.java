package com.daytrading;

public class ExcelRowData {

    private String stock;
    private String type;
    private String entry;
    private String exit;
    private String qty;
    private String PndL;
    private String PndLPer;
    private String PndLType;
    private String date;
    private String PndLwithoutBr;

    public ExcelRowData(String stock, String type, String entry, String exit, String qty, String pndL, String pndLPer, String pndLType, String date, String PndLwithoutBr) {
        this.stock = stock;
        this.type = type;
        this.entry = entry;
        this.exit = exit;
        this.qty = qty;
        this.PndL = pndL;
        this.PndLPer = pndLPer;
        this.PndLType = pndLType;
        this.date = date;
        this.PndLwithoutBr = PndLwithoutBr;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getExit() {
        return exit;
    }

    public void setExit(String exit) {
        this.exit = exit;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPndL() {
        return PndL;
    }

    public void setPndL(String pndL) {
        PndL = pndL;
    }

    public String getPndLPer() {
        return PndLPer;
    }

    public void setPndLPer(String pndLPer) {
        PndLPer = pndLPer;
    }

    public String getPndLType() {
        return PndLType;
    }

    public void setPndLType(String pndLType) {
        PndLType = pndLType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPndLwithoutBr() {
        return PndLwithoutBr;
    }

    public void setPndLwithoutBr(String pndLwithoutBr) {
        PndLwithoutBr = pndLwithoutBr;
    }
}
