package com.example.chicharo.call_blocker.dataBases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContactsPhonesDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_PHONE_ID, MySQLiteHelper.COLUMN_CONTACT_ID};

    ContactsPhonesDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    ContactsPhonesDataSource(SQLiteDatabase database){
        this.database = database;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createContactPhone(long contactId, long numberId){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CONTACT_ID, contactId);
        values.put(MySQLiteHelper.COLUMN_PHONE_ID, numberId);
        database.insert(MySQLiteHelper.TABLE_CONTACTS_NUMBERS, null,
                values);
    }

    public List<Long> phonesOfContact(long contactId){
        List<Long> phonesOfContactsIds = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS_NUMBERS, allColumns, MySQLiteHelper.COLUMN_CONTACT_ID
                + " = " + contactId, null,null,null,null);
        while (cursor.moveToNext()){
            phonesOfContactsIds.add(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_PHONE_ID)));
        }
        return phonesOfContactsIds;
    }

    public List<Long> getAllPhones(){
        List<Long> allPhones = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS_NUMBERS, allColumns, MySQLiteHelper.COLUMN_PHONE_ID, null,null,null,null);
        while (cursor.moveToNext()){
            allPhones.add(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_PHONE_ID)));
        }
        return allPhones;
    }

    public void deleteAll(){
        database.delete(MySQLiteHelper.TABLE_CONTACTS_NUMBERS, null, null);
    }

    public void deletePhonesOfContact(long contactId){
        database.delete(MySQLiteHelper.TABLE_CONTACTS_NUMBERS,
                MySQLiteHelper.COLUMN_CONTACT_ID + " = " + contactId, null);
    }
}
