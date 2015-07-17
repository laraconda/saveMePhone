package com.example.chicharo.call_blocker.dataBases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.chicharo.call_blocker.models.ContactModel;
import com.example.chicharo.call_blocker.models.PhoneModel;

import java.util.ArrayList;
import java.util.List;

public class PhonesDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private ContactsPhonesDataSource contactsPhonesDataSource;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NUMBER };
    private Context context;

    public PhonesDataSource(Context context) {
        this.context = context;
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        contactsPhonesDataSource = new ContactsPhonesDataSource(context);
        contactsPhonesDataSource.open();
    }

    public void close() {
        dbHelper.close();
        contactsPhonesDataSource.close();
    }

    public PhoneModel blockNumber(String number) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NUMBER, number);
        long insertId = database.insert(MySQLiteHelper.TABLE_NUMBERS, null,
                values);
        return getPhone(insertId);
    }

    public void deletePhonesOfContact(long contactId){
        List<Long> phonesToDelete = contactsPhonesDataSource.phonesOfContact(contactId);
        for (int i=0; i<phonesToDelete.size(); i++){
            removeBlockedNumber(phonesToDelete.get(i));
        }
    }

    public PhoneModel getPhone(long id){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NUMBERS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        PhoneModel justBlockedNumber = cursorToPhone(cursor);
        cursor.close();
        return justBlockedNumber;
    }


    public void deleteAllNumbersWithContact() {
        List<Long> phonesToDelete = contactsPhonesDataSource.getAllPhones();
        for (int i = 0; i < phonesToDelete.size(); i++){
            removeBlockedNumber(phonesToDelete.get(i));
        }
    }

    public void removeBlockedNumber(long id) {
        database.delete(MySQLiteHelper.TABLE_NUMBERS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAll(){
        database.delete(MySQLiteHelper.TABLE_NUMBERS, MySQLiteHelper.COLUMN_ID, null);
    }

    public Boolean isInOwnBlackList(String number){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NUMBERS, allColumns, MySQLiteHelper.COLUMN_NUMBER
                + " = " + number, null,null,null,null);
        Boolean isInOwnBlacklist = cursor.getCount()!=0;
        cursor.close();
        return isInOwnBlacklist;
    }


    private PhoneModel cursorToPhone(Cursor cursor) {
        PhoneModel pm = new PhoneModel();
        pm.set_id(cursor.getLong(0));
        pm.setNumber(cursor.getString(1));
        return pm;
    }

}

