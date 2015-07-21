package com.example.chicharo.call_blocker.models;


import java.util.ArrayList;
import java.util.List;

public class ContactModel {

    private long _id;
    private String systemId;
    private String contactName;
    private List<String> phoneNumbers = new ArrayList<>();

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumbers.get(0);
    }

    public List<String> getPhoneNumbers(){
        return phoneNumbers;
    }

    public void addPhoneNumber(String number){
        this.phoneNumbers.add(number);
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }


}
