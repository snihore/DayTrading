package com.daytrading;

public class PndLData {
    private String PndL;
    private String PndLType;
    private String date;

    public PndLData(String pndL, String pndLType, String date) {
        PndL = pndL;
        PndLType = pndLType;
        date = date;
    }

    public String getPndL() {
        return PndL;
    }

    public void setPndL(String pndL) {
        PndL = pndL;
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
}
