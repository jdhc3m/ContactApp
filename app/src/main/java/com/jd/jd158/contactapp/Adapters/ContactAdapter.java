/*
package com.jd.jd158.contactapp.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.jd158.contactapp.Data.ContactContract;
import com.jd.jd158.contactapp.Data.ContactDbHelper;
import com.jd.jd158.contactapp.Model.Contact;
import com.jd.jd158.contactapp.R;
import com.squareup.picasso.Picasso;

import java.util.Scanner;

*/
/**
 * Created by jd158 on 17/03/2018.
 *//*


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactAdapterViewHolder> {

    private String[] mContactData;
    //ImageView mContactPicture;
    Context context;
    
    */
/*
    * An on-click handler that we've defined to make it easy for an Activity to interface with
    * our RecyclerView
    *//*

    private final ContactAdapterOnClickHandler mClickHandler;



    */
/**
     * The interface that receives onClick messages.
     *//*

    public interface ContactAdapterOnClickHandler {
        void onClick(String contactList);
    }

    */
/**
     * Creates a ContactAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     *//*

    public ContactAdapter(ContactAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    */
/**
     * Cache of the children views for a forecast list item.
     *//*

    public class ContactAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView mContactPicture;
        final TextView mContactName;
        ContactDbHelper mDbHelper = new ContactDbHelper(context);
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor mCursor = database.query(ContactContract.ContactEntry.TABLE_NAME, null, null, null,
                null, null, null);

        public ContactAdapterViewHolder(View view) {
            super(view);

            mContactPicture = (ImageView) view.findViewById(R.id.contact_picture_image);
            mContactName = (TextView) view.findViewById(R.id.contact_name_txt);
            view.setOnClickListener(this);
        }

        */
/**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         *//*

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String contactList = mContactData[adapterPosition];
            mClickHandler.onClick(contactList);
        }
    }
    
    @Override
    public ContactAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.contact_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ContactAdapterViewHolder(view);
    }

    */
/**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param contactAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     *//*

    @Override
    public void onBindViewHolder(ContactAdapterViewHolder contactAdapterViewHolder, int position) {


        String  contactDetail = mContactData[position];
        Scanner scan = new Scanner(contactDetail.toString());

        while (scan.hasNextLine() ){
            String oneLine = scan.nextLine();
            System.out.println(oneLine.length());
        }
        Contact c = new Contact();
        c.mName = contactAdapterViewHolder.mContactName;
        c.profilePic =
        
        Picasso.with(context).load(contactDetail).into(contactAdapterViewHolder.mContactPicture);
    }

    */
/**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     *//*

    @Override
    public int getItemCount() {
        if (null == mContactData) return 0;
        return mContactData.length;
    }

    public static void UpdateContact() {
        notifyDataSetChanged();
    }
        
    }
*/
