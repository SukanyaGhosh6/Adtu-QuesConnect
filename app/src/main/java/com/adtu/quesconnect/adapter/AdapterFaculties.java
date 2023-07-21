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
import com.adtu.quesconnect.model.Model_new_datas;

import java.util.List;

public class AdapterFaculties extends RecyclerView.Adapter<AdapterFaculties.MyViewholder> {

    private Context mContext;
    private List<Model_new_datas> mUploads;
    private OnItemClickListener mListener;
    public AdapterFaculties(Context context, List<Model_new_datas> uploads) {
        mContext = context;
        mUploads = uploads;
    }
    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_data,parent,false);
        return new MyViewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        Model_new_datas model = mUploads.get(position);
        /*if(model.getFaculty()!=null){
            holder.title.setText(model.getFaculty());
        }*/
        holder.title.setText(model.getKey());

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
                    mListener.onFacultyItemClick(position);
                }
            }
        }
    }
    public interface OnItemClickListener {
        void onFacultyItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}

