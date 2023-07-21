package com.adtu.quesconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adtu.quesconnect.model.ModelCourses;
import com.adtu.quesconnect.R;

import java.util.List;

public class AdapterCourse extends RecyclerView.Adapter<AdapterCourse.MyViewholder> {

    private Context mContext;
    private List<ModelCourses> mUploads;
    private OnItemClickListener mListener;
    public AdapterCourse(Context context, List<ModelCourses> uploads) {
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
        ModelCourses uploadCurrent = mUploads.get(position);
        holder.title.setText(uploadCurrent.getName());
        holder.shortdesc.setText(uploadCurrent.getDescription());
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

