package com.example.chicharo.call_blocker.models;

public class PhoneModel {
    private long _id;
    private String number;

    public void set_id(long _id) {
        this._id = _id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber(){
        return this.number;
    }

    public long get_id(){
        return this._id;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return number;
    }
}
