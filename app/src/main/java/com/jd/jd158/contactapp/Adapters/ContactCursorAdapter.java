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

public class ContactCursorAdapter  extends CursorRecyclerViewAdapter<ContactCursorAdapter.ViewHolder> implements View.OnClickListener{

    private Context context;
    private static ClickListener clickListener;

    public ContactCursorAdapter(Context context, Cursor cursor, CursorRecyclerViewAdapter mClickHandler){
        super(context,cursor, mClickHandler);
        this.context = context;

    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mContactPicture;
        public TextView mContactName;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mContactPicture = view.findViewById(R.id.contact_picture_image);
            mContactName = view.findViewById(R.id.contact_name_txt);

        }
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }


    public void setOnItemClickListener(ClickListener clickListener) {
        ContactCursorAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

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

