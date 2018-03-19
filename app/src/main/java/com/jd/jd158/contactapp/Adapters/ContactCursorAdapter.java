package com.jd.jd158.contactapp.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.jd158.contactapp.Data.ContactContract;
import com.jd.jd158.contactapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by jd158 on 18/03/2018.
 */

public class ContactCursorAdapter  extends CursorRecyclerViewAdapter<ContactCursorAdapter.ViewHolder>{

    private final Context context;

    public ContactCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
        this.context = context;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mContactPicture;
        public TextView mContactName;

        public ViewHolder(View view) {
            super(view);
            mContactPicture = (ImageView) view.findViewById(R.id.contact_picture_image);
            mContactName = (TextView) view.findViewById(R.id.contact_name_txt);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_CONTACT_NAME));
        String photo = cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_CONTACT_PHOTO));
        viewHolder.mContactName.setText(name);

        Picasso.with(context)
                .load(photo)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(viewHolder.mContactPicture);
    }
}

