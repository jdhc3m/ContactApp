package com.jd.jd158.contactapp.UI;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jd.jd158.contactapp.Data.ContactContract;
import com.jd.jd158.contactapp.R;
import com.squareup.picasso.Picasso;


public class ContactDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    EditText mNameEditText;
    EditText mEmailEditText;
    EditText mBornEditText;
    EditText mBioEditText;
    ImageView mPhotoImageView;
    private boolean mItemHasChanged = false;

    private Uri mUri;
    private static final int EXISTING_ITEM_LOADER = 0;
    private static final String LOG_TAG = ContactDetail.class.getSimpleName();


    /**
     * Content URI for the existing contact (null if it's a new contact)
     */
    private Uri mCurrentItemUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        mNameEditText =  findViewById(R.id.contact_detail_name_txt);
        mEmailEditText =  findViewById(R.id.contact_detail_email_txt);
        mBornEditText =  findViewById(R.id.contact_detail_born_txt);
        mBioEditText =  findViewById(R.id.contact_detail_bio_txt);
        mPhotoImageView =  findViewById(R.id.contact_detail_photo_imageView);

        mNameEditText.setOnTouchListener(mTouchListener);
        mEmailEditText.setOnTouchListener(mTouchListener);
        mBornEditText.setOnTouchListener(mTouchListener);
        mBioEditText.setOnTouchListener(mTouchListener);

        Intent intent = getIntent();
        Uri currentItemUri = intent.getData();
        mCurrentItemUri = currentItemUri;
        if (currentItemUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_contact));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_update_contact));
        }
        getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);

    }

    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_CONTACT_NAME,
                ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL,
                ContactContract.ContactEntry.COLUMN_CONTACT_BORN,
                ContactContract.ContactEntry.COLUMN_CONTACT_BIO,
                ContactContract.ContactEntry.COLUMN_CONTACT_PHOTO,
        };

        if (mCurrentItemUri == null) {
            return null;
        } else {
            String orderBy = ContactContract.ContactEntry.COLUMN_CONTACT_NAME;
            // This loader will execute the ContentProvider's query method on a background thread
            return new CursorLoader(this,
                    mCurrentItemUri,
                    projection,
                    null,
                    null,
                    orderBy);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            // Find the columns of contact attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_NAME);
            int emailColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL);
            int bornColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_BORN);
            int bioColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_BIO);
            int photoColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_PHOTO);

            String name = cursor.getString(nameColumnIndex);
            String email = cursor.getString(emailColumnIndex);
            String born = cursor.getString(bornColumnIndex);
            String bio = cursor.getString(bioColumnIndex);
            String photo = cursor.getString(photoColumnIndex);
            try {
                mUri = Uri.parse(cursor.getString(photoColumnIndex));
            }catch (Exception e) {
                Log.e(LOG_TAG, "Failed to load image.", e);

            }

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mEmailEditText.setText(email);
            mBornEditText.setText(born);
            mBioEditText.setText(bio);
            Picasso.with(this)
                    .load(photo)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(mPhotoImageView);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void saveItems() {

        String nameString = mNameEditText.getText().toString().trim();
        String emailString = mEmailEditText.getText().toString().trim();
        String bornString = mBornEditText.getText().toString().trim();
        String bioString = mBioEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nameString)) {
            showNameCannotBeBlank();
        } else {

            ContentValues values = new ContentValues();
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_NAME, nameString);
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL, emailString);
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_BORN, bornString);
            values.put(ContactContract.ContactEntry.COLUMN_CONTACT_BIO, bioString);

            // Insert the new row, returning the primary key value of the new row
            if (mCurrentItemUri == null) {
                Uri newUri = getContentResolver().insert(ContactContract.ContactEntry.CONTENT_URI,
                        values);
                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.editor_insert_item_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_item_successful),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.editor_insert_item_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_item_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                String nameString = mNameEditText.getText().toString().trim();
                if (TextUtils.isEmpty(nameString)) {
                    showNameCannotBeBlank();
                    return true;
                } else {
                    saveItems();
                    finish();
                    return true;
                }
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(ContactDetail.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(ContactDetail.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteContact();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showNameCannotBeBlank() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.blank_name_msg);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the contact in the database.
     */
    private void deleteContact() {
        if (mCurrentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_contact_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_contact_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}
