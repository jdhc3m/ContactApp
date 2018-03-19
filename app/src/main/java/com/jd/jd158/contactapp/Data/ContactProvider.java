package com.jd.jd158.contactapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by jd158 on 17/03/2018.
 */

public class ContactProvider extends ContentProvider {
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = ContactProvider.class.getSimpleName();

    // Database Helper Object
    private ContactDbHelper mDbHelper;
    /**
     * URI matcher code for the content URI for the contact table
     */
    private static final int CONTACT = 100;
    
    /**
     * URI matcher code for the content URI for a single contact in the contact table
     */
    private static final int CONTACT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher
        sUriMatcher.addURI(ContactContract.CONTENT_AUTHORITY, ContactContract.PATH_CONTACT, CONTACT);

        sUriMatcher.addURI(ContactContract.CONTENT_AUTHORITY, ContactContract.PATH_CONTACT+ "/#", CONTACT_ID);

    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.

        // Initialize the variable using the ContactDbHelper contractor

        mDbHelper = new ContactDbHelper(getContext());

        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CONTACT:
                // For the CONTACT code, query the contacts table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the contacts table.
                // TODO: Perform database query on cotacts table

                cursor = database.query(ContactContract.ContactEntry.TABLE_NAME, projection, null, null,
                        null, null, sortOrder);
                break;
            case CONTACT_ID:
                // For the CONTACT_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.contacts/contacts/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = ContactContract.ContactEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the contacts table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(ContactContract.ContactEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor
        // So we know what content URI the Cursor was created forr
        // If the data at this URI changes, then we need to update to the cursor

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
                return insertInventory(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a contact into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertInventory(Uri uri, ContentValues values) {

        // Check that the name is not null
        String name = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Name Cannot be blank");
        }

        String email = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_NAME);
        String born = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL);
        String bio = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_BIO);
        String photo = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_PHOTO);
        
        // TODO: Insert a new contact into the Contact database table with the given ContentValues
        // Get readable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ContactContract.ContactEntry.TABLE_NAME,
                null,
                values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Notify all the listeners that the data has changed for the contact content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
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
                // For the CONTACT_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
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
        // If the {@link ContactEntry#COLUMN_CONTACT_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(ContactContract.ContactEntry.COLUMN_CONTACT_NAME)) {
            String name = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Name Cannot be blank");
            }
        }

        if (values.containsKey(ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL)) {
            String email = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL);
        }

        if (values.containsKey(ContactContract.ContactEntry.COLUMN_CONTACT_BORN)) {
            String born = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_BORN);
        }

        if (values.containsKey(ContactContract.ContactEntry.COLUMN_CONTACT_BIO)) {
            String email = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_BIO);
        }

        if (values.containsKey(ContactContract.ContactEntry.COLUMN_CONTACT_PHOTO)) {
            String email = values.getAsString(ContactContract.ContactEntry.COLUMN_CONTACT_PHOTO);
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(ContactContract.ContactEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            //Notify all the listeners that the data has changed for the contact content URI
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Returns the number of database rows affected by the update statement
        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CONTACT:
                // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                // Delete all rows that match the selection and selection args
                // For case CONTACT_ID:
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(ContactContract.ContactEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;

            case CONTACT_ID:
                // Delete a single row given by the ID in the URI
                selection = ContactContract.ContactEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                // Delete all rows that match the selection and selection args
                // For case CONTACT_ID:
                // Delete a single row given by the ID in the URI
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
