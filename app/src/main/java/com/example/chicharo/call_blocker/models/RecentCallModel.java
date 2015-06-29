package com.example.chicharo.call_blocker.models;

public class RecentCallModel {
    private String header;
    private String date;
    private String number;

    public String getHeader() {
        return header;
    }

    public String getDate() {
        return date;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
