package com.jd.jd158.contactapp.UI;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jd.jd158.contactapp.Adapters.ContactCursorAdapter;
import com.jd.jd158.contactapp.Controller.OpenContactsJson;
import com.jd.jd158.contactapp.Data.ContactContract;
import com.jd.jd158.contactapp.R;
import com.jd.jd158.contactapp.Utils.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {


    private static final int CONTACT_LOADER = 0;
    private RecyclerView mRecyclerView;
    private ContactCursorAdapter mContactCursorAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configViews();
        new getContactTask().execute();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                String.valueOf(ContactContract.ContactEntry._ID),
                ContactContract.ContactEntry.COLUMN_CONTACT_NAME,
                ContactContract.ContactEntry.COLUMN_CONTACT_PHOTO,
        };

        // Returns a new CursorLoader
        return new CursorLoader(
                this,
                ContactContract.ContactEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mContactCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContactCursorAdapter.swapCursor(null);
    }


    public class getContactTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            URL contactsRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonContactsResponse = NetworkUtils
                        .getResponseFromHttpUrl(contactsRequestUrl);

                String[] simpleJsonContactsData = OpenContactsJson
                        .getContactsStringsFromJson(MainActivity.this, jsonContactsResponse);

                return simpleJsonContactsData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            getLoaderManager().initLoader(CONTACT_LOADER, null, MainActivity.this);

        }
    }

    private void configViews() {
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mRecyclerView = findViewById(R.id.recyclerview_contact);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(false);
        mContactCursorAdapter = new ContactCursorAdapter(MainActivity.this, null);
        mRecyclerView.setAdapter(mContactCursorAdapter);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

    }
}
