package com.adtu.quesconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adtu.quesconnect.model.ModelPDF;
import com.adtu.quesconnect.R;

import java.util.List;

public class AdapterPDF extends RecyclerView.Adapter<AdapterPDF.viewHolder> {
    private Context mContext;
    private List<ModelPDF> mUploads;
    private OnItemClickListener mListener;
    public AdapterPDF(Context context, List<ModelPDF> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ModelPDF uploadCurrent = mUploads.get(position);

        holder.pdfname.setText(uploadCurrent.getName());
        holder.price.setText("");
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView pdfname,price;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            pdfname = itemView.findViewById(R.id.pdfname);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
