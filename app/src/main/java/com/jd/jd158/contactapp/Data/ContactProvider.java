package com.jd.jd158.contactapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.jd.jd158.contactapp.R;

/**
 * Created by jd158 on 17/03/2018.
 */

public class ContactProvider extends ContentProvider {

    public static final String LOG_TAG = ContactProvider.class.getSimpleName();

    private ContactDbHelper mDbHelper;
    /**
     * URI matcher code for the content URI for the contact table
     */
    private static final int CONTACT = 100;

    /**
     * URI matcher code for the content URI for a single contact in the contact table
     */
    private static final int CONTACT_ID = 101;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ContactContract.CONTENT_AUTHORITY, ContactContract.PATH_CONTACT, CONTACT);
        sUriMatcher.addURI(ContactContract.CONTENT_AUTHORITY, ContactContract.PATH_CONTACT+ "/#", CONTACT_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new ContactDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case CONTACT:
                cursor = database.query(ContactContract.ContactEntry.TABLE_NAME, projection, null, null,
                        null, null, sortOrder);
                break;
            case CONTACT_ID:
                selection = ContactContract.ContactEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ContactContract.ContactEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(R.string.cannot_query_uri) + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CONTACT:
                return insertContact(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a contact into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertContact(Uri uri, ContentValues values) {

        String name = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Name Cannot be blank");
        }

        // Get readable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ContactContract.ContactEntry.TABLE_NAME,
                null,
                values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CONTACT:
                return updateContact(uri, contentValues, selection, selectionArgs);
            case CONTACT_ID:
                selection = ContactContract.ContactEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateContact(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update contacts in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more contacts).
     * Return the number of rows that were successfully updated.
     */
    /**
     * Update contacts in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more contacts).
     * Return the number of rows that were successfully updated.
     */
    private int updateContact(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ContactContract.ContactEntry.COLUMN_CONTACT_NAME)) {
            String name = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Name Cannot be blank");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(ContactContract.ContactEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CONTACT:
                rowsDeleted = database.delete(ContactContract.ContactEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;

            case CONTACT_ID:
                selection = ContactContract.ContactEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ContactContract.ContactEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }
    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CONTACT:
                return ContactContract.ContactEntry.CONTENT_LIST_TYPE;
            case CONTACT_ID:
                return ContactContract.ContactEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


}
