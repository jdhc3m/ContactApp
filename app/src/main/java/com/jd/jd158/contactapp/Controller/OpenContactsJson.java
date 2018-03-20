package com.jd.jd158.contactapp.Controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jd.jd158.contactapp.Data.ContactContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jd158 on 17/03/2018.
 */

public class OpenContactsJson  extends AppCompatActivity {

    private final Activity activity;

    public OpenContactsJson(Activity activity) {
        this.activity = activity;
    }

    public static String[] getContactsStringsFromJson(Context context, String contactsJsonStr)
            throws JSONException {

        final String NAME = "name";
        final String EMAIL = "email";
        final String BORN = "born";
        final String BIO = "bio";
        final String CONTACT_PIC = "photo";

        String[] parsedContactData = null;

        JSONArray contactArray = new JSONArray(contactsJsonStr);

        parsedContactData = new String[contactArray.length()];

        for (int i = 0; i < contactArray.length(); i++) {
            /* These are the values that will be collected */
            String name;
            String email;
            String born;
            String bio;
            String contactPic;

            /* Get the JSON object representing the day */
            JSONObject contactDetails = contactArray.getJSONObject(i);
            StringBuilder jsonResultString = new StringBuilder();


            name = contactDetails.getString(NAME) ;
            email = contactDetails.getString(EMAIL);
            born = contactDetails.getString(BORN);
            bio = contactDetails.getString(BIO);
            contactPic = contactDetails.getString(CONTACT_PIC);

            ContentValues values = new ContentValues();
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_NAME, name);
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL, email);
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_BORN, born);
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_BIO, bio);
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_PHOTO, contactPic);

           context.getContentResolver().insert(ContactContract.ContactEntry.CONTENT_URI,
                    values);
            parsedContactData[i] = jsonResultString.toString();

        }
        return parsedContactData;
    }

}
