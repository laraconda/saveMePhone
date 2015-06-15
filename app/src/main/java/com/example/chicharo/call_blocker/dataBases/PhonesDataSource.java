package com.example.chicharo.call_blocker.dataBases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.chicharo.call_blocker.models.contactModel;

import java.util.ArrayList;
import java.util.List;

public class PhonesDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_NUMBER };

    public PhonesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public contactModel createBlockedContact(String number, String name) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_NUMBER, number);
        long insertId = database.insert(MySQLiteHelper.TABLE_PHONE, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PHONE,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        contactModel newBlockedContact = cursorToContact(cursor);
        cursor.close();
        return newBlockedContact;
    }

    public void deleteBlockedContact(contactModel contact) {
        long id = contact.get_id();
        database.delete(MySQLiteHelper.TABLE_PHONE, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAll(){
        database.delete(MySQLiteHelper.TABLE_PHONE, MySQLiteHelper.COLUMN_ID, null);
    }

    public Boolean isInOwnBlackList(String number){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PHONE, allColumns, MySQLiteHelper.COLUMN_NUMBER
                + " = " + number, null,null,null,null);
        Boolean isInOwnBlacklist = cursor.getCount()!=0;
        cursor.close();
        return isInOwnBlacklist;
    }

    public List<contactModel> getAllContacts() {
        List<contactModel> contactList = new ArrayList<contactModel>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PHONE,
                allColumns, null, null, null, null, null);
        Log.d("Database","Size :"+cursor.getCount());

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            contactModel phones = cursorToContact(cursor);
            contactList.add(phones);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return contactList;
    }

    private contactModel cursorToContact(Cursor cursor) {
        contactModel cm = new contactModel();
        cm.set_id(cursor.getLong(0));
        cm.setContactName(cursor.getString(1));
        cm.setPhoneNumber(cursor.getString(2));
        return cm;
    }

}

