package com.example.chicharo.call_blocker.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chicharo.call_blocker.Models.contactModel;
import com.example.chicharo.call_blocker.R;

import java.util.List;


public class BlockedContactsAdapter extends RecyclerView.Adapter<BlockedContactsAdapter.ContactViewHolder> {

    private List<contactModel> blockedContactsList;

    public BlockedContactsAdapter(List<contactModel> blockedContactsList){
        this.blockedContactsList = blockedContactsList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.blocked_contacts_card, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        contactModel contactmodel = blockedContactsList.get(position);
        holder.contactName.setText(contactmodel.getContactName());
        holder.contactNumber.setText(contactmodel.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return blockedContactsList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView contactNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView)itemView.findViewById(R.id.blocked_contact_title);
            contactNumber = (TextView)itemView.findViewById(R.id.blocked_contact_number);
        }
    }

}
