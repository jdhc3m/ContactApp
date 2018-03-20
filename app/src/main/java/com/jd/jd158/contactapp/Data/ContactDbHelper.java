package com.jd.jd158.contactapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jd158 on 17/03/2018.
 */

public class ContactDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = ContactDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "shelter.db";

    private static final int DATABASE_VERSION = 3;

    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the contact table
        String SQL_CREATE_CONTACT_TABLE = "CREATE TABLE " + ContactContract.ContactEntry.TABLE_NAME + " ("
                + ContactContract.ContactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContactContract.ContactEntry.COLUMN_CONTACT_NAME + " TEXT NOT NULL, "
                + ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL + " TEXT DEFAULT 0, "
                + ContactContract.ContactEntry.COLUMN_CONTACT_BORN + " TEXT DEFAULT 0, "
                + ContactContract.ContactEntry.COLUMN_CONTACT_BIO + " TEXT DEFAULT 0,"
                + ContactContract.ContactEntry.COLUMN_CONTACT_PHOTO + " TEXT DEFAULT 0);"
                ;


        // Execute the SQL statement
        db.execSQL(SQL_CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContactContract.ContactEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
