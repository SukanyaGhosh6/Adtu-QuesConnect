package com.adtu.quesconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adtu.quesconnect.R;
import com.adtu.quesconnect.model.ModelC;

import java.util.List;

public class AdapterC extends RecyclerView.Adapter<AdapterC.MyViewholder> {

    private Context mContext;
    private List<ModelC> mUploads;
    private OnItemClickListener mListener;
    public AdapterC(Context context, List<ModelC> uploads) {
        mContext = context;
        mUploads = uploads;
    }
    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice_board,parent,false);
        return new MyViewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        ModelC uploadCurrent = mUploads.get(position);
        holder.title.setText(uploadCurrent.getT());
        holder.shortdesc.setText(uploadCurrent.getDes());
    }
    @Override
    public int getItemCount() {
        return mUploads.size();
    }
    public interface Clickedlistener{
        void onClick(View v,int position);
    }

    public class MyViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,shortdesc;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            shortdesc = itemView.findViewById(R.id.shortdesc);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onNItemClick(position);
                }
            }
        }
    }
    public interface OnItemClickListener {
        void onNItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
