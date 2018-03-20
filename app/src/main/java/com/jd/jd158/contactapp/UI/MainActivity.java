package com.jd.jd158.contactapp.UI;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        LoaderManager.LoaderCallbacks<Cursor>{


    private static final int CONTACT_LOADER = 0;
    private RecyclerView mRecyclerView;
    private ContactCursorAdapter mContactCursorAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;


    @Override
    protected void onStart() {
        super.onStart();
        getLoaderManager().initLoader(CONTACT_LOADER, null, MainActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorMessageDisplay = findViewById(R.id.error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        configViews();
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactDetail.class);
                startActivity(intent);
            }
        });
        checkConnectionAndStartActions();

        mContactCursorAdapter.setOnItemClickListener(new ContactCursorAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                long id = mContactCursorAdapter.getItemId(position);
                Uri currentContactUri = ContentUris.withAppendedId(ContactContract.ContactEntry.CONTENT_URI, id);
                Intent intent = new Intent(MainActivity.this, ContactDetail.class);
                intent.setData(currentContactUri);
                startActivity(intent);

            }

        });

    }

    public void  checkConnectionAndStartActions() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            deleteAllRegister();
            new getContactTask().execute();
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            getLoaderManager().initLoader(CONTACT_LOADER, null, MainActivity.this);

        }
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
        protected void onPostExecute(String[] contactData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            getLoaderManager().initLoader(CONTACT_LOADER, null, MainActivity.this);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                String.valueOf(ContactContract.ContactEntry._ID),
                ContactContract.ContactEntry.COLUMN_CONTACT_NAME,
                ContactContract.ContactEntry.COLUMN_CONTACT_PHOTO,
        };
        String orderBy = ContactContract.ContactEntry.COLUMN_CONTACT_NAME;
        return new CursorLoader(
                this,
                ContactContract.ContactEntry.CONTENT_URI,
                projection,
                null,
                null,
                orderBy
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            mContactCursorAdapter.swapCursor(data);
        } else {
            mErrorMessageDisplay.setText(R.string.no_data);
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContactCursorAdapter.swapCursor(null);
    }

    private void configViews() {
        mErrorMessageDisplay = findViewById(R.id.error_message_display);
        mRecyclerView = findViewById(R.id.recyclerview_contact);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(false);
        mContactCursorAdapter = new ContactCursorAdapter(MainActivity.this, null, null);
        mRecyclerView.setAdapter(mContactCursorAdapter);
    }

    private void deleteAllRegister() {
        int rowsDeleted = getContentResolver().delete(ContactContract.ContactEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from contact database");
    }
}
