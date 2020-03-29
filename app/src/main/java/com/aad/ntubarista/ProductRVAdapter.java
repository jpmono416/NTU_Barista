package com.aad.ntubarista;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<Map<String, Object>> items;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemCLickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ProductRVAdapter(Context context, ArrayList<Map<String, Object>> items)
    {
        this.mcontext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_products_item, parent, false);
        ViewHolder holder = new ViewHolder(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(items.get(position).get("name").toString());
        holder.txtDescription.setText(items.get(position).get("description").toString());
        holder.txtPrice.setText(new StringBuilder().append("£").append(items.get(position).get("price").toString()).toString());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtName;
        TextView txtDescription;
        TextView txtPrice;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View productView, final OnItemClickListener listener)
        {
            super(productView);
            txtName = productView.findViewById(R.id.txtName);
            txtDescription = productView.findViewById(R.id.txtDescription);
            txtPrice = productView.findViewById(R.id.txtPrice);
            parentLayout = productView.findViewById(R.id.rv_layout);

            productView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}

