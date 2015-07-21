package com.example.chicharo.call_blocker.dataBases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.chicharo.call_blocker.models.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class ContactsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allContactColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME };
    private PhonesDataSource phonesDataSource;
    private ContactsPhonesDataSource contactsPhonesDataSource;
    private Context context;

    public ContactsDataSource(Context context) {
        this.context = context;
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        phonesDataSource = new PhonesDataSource(context);
        phonesDataSource.open();
        contactsPhonesDataSource = new ContactsPhonesDataSource(context);
        contactsPhonesDataSource.open();
    }

    public void close() {
        dbHelper.close();
        phonesDataSource.close();
        contactsPhonesDataSource.close();
    }

    public ContactModel addBlockedContact(String name, List<String> numbers) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        long contactId = database.insert(MySQLiteHelper.TABLE_CONTACTS, null,
                values);
        values.clear();
        for(int i=0; i<numbers.size(); i++){
            long phoneId = phonesDataSource.blockNumber(numbers.get(i)).get_id();
            contactsPhonesDataSource.createContactPhone(contactId, phoneId);
        }
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS,
                allContactColumns, MySQLiteHelper.COLUMN_ID + " = " + contactId, null,
                null, null, null);
        cursor.moveToFirst();
        ContactModel newBlockedContact = cursorToContact(cursor);
        cursor.close();
        return newBlockedContact;
    }

    public void deleteAll(){
        database.delete(MySQLiteHelper.TABLE_CONTACTS, null, null);
        phonesDataSource.deleteAllNumbersWithContact();
        contactsPhonesDataSource.deleteAll();
    }

    public void deleteBlockedContact(long contactId) {
        database.delete(MySQLiteHelper.TABLE_CONTACTS, MySQLiteHelper.COLUMN_ID
                + " = " + contactId, null);
        phonesDataSource.deletePhonesOfContact(contactId);
        contactsPhonesDataSource.deletePhonesOfContact(contactId);
    }

    public List<ContactModel> getAllContacts() {
        List<ContactModel> contactList = new ArrayList<ContactModel>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS,
                allContactColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            ContactModel contact = cursorToContact(cursor);
            long contactPhoneId = contactsPhonesDataSource.phonesOfContact(contact.get_id()).get(0);
            contact.setPhoneNumber(phonesDataSource.getPhone(contactPhoneId).getNumber());
            contactList.add(contact);
        }
        // make sure to close the cursor
        cursor.close();
        return contactList;
    }

    public ContactModel cursorToContact(Cursor cursor){
        ContactModel contactModel = new ContactModel();
        contactModel.setContactName(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_NAME)));
        contactModel.set_id(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID)));
        return contactModel;
    }

}
