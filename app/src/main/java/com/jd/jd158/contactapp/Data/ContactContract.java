package com.jd.jd158.contactapp.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jd158 on 17/03/2018.
 */

public class ContactContract {

    public static final String CONTENT_AUTHORITY = "com.jd.jd158.contactapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CONTACT = "contacts";

    private ContactContract() {}

    public static abstract class ContactEntry implements BaseColumns {

        public static final String TABLE_NAME = "contacts";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CONTACT_NAME = "name";
        public static final String COLUMN_CONTACT_EMAIL= "email";
        public static final String COLUMN_CONTACT_BORN= "born";
        public static final String COLUMN_CONTACT_BIO= "bio";
        public static final String COLUMN_CONTACT_PHOTO = "photo";


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CONTACT);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of inventorys.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACT;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single inventory.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACT;

    }
}
