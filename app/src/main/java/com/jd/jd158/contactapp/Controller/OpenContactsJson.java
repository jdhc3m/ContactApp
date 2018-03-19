package com.jd.jd158.contactapp.Controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.jd.jd158.contactapp.Data.ContactContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jd158 on 17/03/2018.
 */

public class OpenContactsJson  extends AppCompatActivity {
    private static final String URL_IMAGE = "http://image.tmdb.org/t/p/";
    private final Activity activity;
    Context mContext;
    private static final String IMAGE_SIZE = "w780";

    static final String OWM_MESSAGE_CODE = "cod";

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

        // long localDate = System.currentTimeMillis();
        // long utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate);
        // long startDay = SunshineDateUtils.normalizeDate(utcDate);

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
            Uri urlImage = buildImageUrl(contactPic);

            //Contact c = new Contact(name, email, born, bio, contactPic, urlImage);

            ContentValues values = new ContentValues();
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_NAME, name);
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL, email);
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_BORN, born);
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_BIO, bio);
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_PHOTO, contactPic);


            Uri newUri = context.getContentResolver().insert(ContactContract.ContactEntry.CONTENT_URI,
                    values);
            // Show a toast message depending on whether or not the insertion was successful
/*            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(context, context.getString(R.string.editor_insert_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(context, context.getString(R.string.editor_insert_item_successful),
                        Toast.LENGTH_SHORT).show();
            }*/

            // Insert on the table
            //parsedMovieData[i] = urlImage.toString();
            parsedContactData[i] = jsonResultString.toString();

        }
        return parsedContactData;
    }


    /**
     * Parse the JSON and convert it into ContentValues that can be inserted into our database.
     *
     * @param context      An application context, such as a service or activity context.
     * @param movieJsonStr The JSON to parse into ContentValues.
     * @return An array of ContentValues parsed from the JSON.
     */
    public static ContentValues[] getFullWeatherDataFromJson(Context context, String movieJsonStr) {
        /** This will be implemented in a future lesson **/
        return null;
    }

    private static Uri buildImageUrl(String imagePath) {
        imagePath = imagePath.replace("/", "");
        Uri builtUri = Uri.parse(URL_IMAGE).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendPath(imagePath)
                .build();
        return builtUri;

    }

}
