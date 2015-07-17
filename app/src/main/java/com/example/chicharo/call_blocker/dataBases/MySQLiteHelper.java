package com.example.chicharo.call_blocker.dataBases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NUMBERS = "numbers";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NUMBER = "number";

    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_NAME = "name";

    public static final String TABLE_CONTACTS_NUMBERS = "contacts_numbers";
    public static final String COLUMN_PHONE_ID = "phone_id";
    public static final String COLUMN_CONTACT_ID = "contact_id";

    private static final String DATABASE_NAME = "OwnBlacklist.db";
    private static final int DATABASE_VERSION = 1;


    private static final String CREATE_TABLE_NUMBERS = "create table "
            + TABLE_NUMBERS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NUMBER
            + " string not null);";

    private static final String CREATE_TABLE_CONTACTS = "create table "
            + TABLE_CONTACTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " string not null);";

    private static final String CREATE_TABLE_CONTACTS_NUMBERS = "create table "
            + TABLE_CONTACTS_NUMBERS + "(" + COLUMN_PHONE_ID
            + " integer not null, " + COLUMN_CONTACT_ID
            + " integer not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_NUMBERS);
        database.execSQL(CREATE_TABLE_CONTACTS);
        database.execSQL(CREATE_TABLE_CONTACTS_NUMBERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMBERS);
        onCreate(db);
    }

}
