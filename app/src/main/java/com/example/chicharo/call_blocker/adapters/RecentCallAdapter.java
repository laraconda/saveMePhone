package com.example.chicharo.call_blocker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chicharo.call_blocker.R;
import com.example.chicharo.call_blocker.models.RecentCallModel;

import java.util.List;

public class RecentCallAdapter extends RecyclerView.Adapter<RecentCallAdapter.RecentCallViewHolder>{

    private List<RecentCallModel> recentCallModelList;
    onItemClickListener mItemClickListener;

    public RecentCallAdapter(List<RecentCallModel> recentCallModelList){
        this.recentCallModelList = recentCallModelList;
    }

    @Override
    public RecentCallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.bivalue_card, parent, false);
        return new RecentCallViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecentCallViewHolder holder, int position) {
        RecentCallModel recentCallModel = recentCallModelList.get(position);
        holder.contactName.setText(recentCallModel.getHeader());
        holder.date.setText(recentCallModel.getDate());
    }

    @Override
    public int getItemCount() {
        return recentCallModelList.size();
    }

    public class RecentCallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView contactName;
        TextView date;

        public RecentCallViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView)itemView.findViewById(R.id.blocked_contact_title);
            date = (TextView)itemView.findViewById(R.id.blocked_contact_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface onItemClickListener{
        public void onItemClick(View v, int position);
    }

    public void SetOnItemClickListener(final onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
