package com.aad.ntubarista;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<Map<String, Object>> items;
    private OnItemClickListener mListener;
    private Boolean staffMode = false;
    public void setStaffModeOn(){
        this.staffMode = true;
    }
    public void setStaffModeOff(){
        this.staffMode = false;
    }


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
        View view = getLayout(parent);
        ViewHolder holder = new ViewHolder(view, mListener);
        return holder;
    }

    private View getLayout(ViewGroup parent)
    {
        if(staffMode) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_menu_update_item, parent, false);
        }
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_products_item, parent, false);
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

    void setItems(ArrayList<Map<String, Object>> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtName;
        TextView txtDescription;
        TextView txtPrice;
        ImageView imgBasket;
        ConstraintLayout parentLayout;



        public ViewHolder(@NonNull View productView, final OnItemClickListener listener)
        {
            super(productView);
            txtName = productView.findViewById(R.id.txtDate);
            txtDescription = productView.findViewById(R.id.txtProductNames);
            txtPrice = productView.findViewById(R.id.txtPrice);
            imgBasket = productView.findViewById(R.id.basket);
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

