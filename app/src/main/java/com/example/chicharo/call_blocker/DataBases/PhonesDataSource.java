package com.example.chicharo.call_blocker.DataBases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.chicharo.call_blocker.Models.PhoneModel;

import java.util.ArrayList;
import java.util.List;

public class PhonesDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NUMBER };

    public PhonesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public PhoneModel createPhone(String number) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NUMBER, number);
        long insertId = database.insert(MySQLiteHelper.TABLE_PHONE, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PHONE,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        PhoneModel newPhone = cursorToPhone(cursor);
        cursor.close();
        return newPhone;
    }

    public void deleteComment(PhoneModel phone) {
        long id = phone.get_id();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_PHONE, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAll(){
        database.delete(MySQLiteHelper.TABLE_PHONE, MySQLiteHelper.COLUMN_ID, null);
    }

    public Boolean isInOwnBlackList(String number){
        Cursor c = database.query(MySQLiteHelper.TABLE_PHONE, allColumns, MySQLiteHelper.COLUMN_NUMBER
                + " = " + number, null,null,null,null);
        return c.getCount()!=0;
    }

    public List<PhoneModel> getAllComments() {
        List<PhoneModel> Lphones = new ArrayList<PhoneModel>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PHONE,
                allColumns, null, null, null, null, null);
        Log.d("Database","Size :"+cursor.getCount());

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PhoneModel phones = cursorToPhone(cursor);
            Lphones.add(phones);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Lphones;
    }

    private PhoneModel cursorToPhone(Cursor cursor) {
        PhoneModel pm = new PhoneModel();
        pm.set_id(cursor.getLong(0));
        pm.setNumber(cursor.getString(1));
        return pm;
    }

}

