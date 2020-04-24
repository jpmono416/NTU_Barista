package com.aad.ntubarista;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class OrderRVAdapter extends RecyclerView.Adapter<OrderRVAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<Map<String, Object>> orders;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemCLickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public OrderRVAdapter(Context context, ArrayList<Map<String, Object>> items)
    {
        this.mcontext = context;
        this.orders = items;
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

        return LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_all_orders_item, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ArrayList<String> myArr = (ArrayList<String>) orders.get(position).get("productNames");
        String names = "";

        for(String s : myArr)
        {
            names += s += ", ";
        }

        holder.txtNames.setText(names);
        holder.txtDate.setText(orders.get(position).get("date").toString());
        holder.txtPrice.setText(new StringBuilder().append("£").append(orders.get(position).get("totalPrice").toString()).toString());
        holder.txtUID.setText(orders.get(position).get("userId").toString());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    void setOrders(ArrayList<Map<String, Object>> orders) {
        this.orders = orders;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtNames;
        TextView txtDate;
        TextView txtPrice;
        TextView txtUID;
        ConstraintLayout parentLayout;


        public ViewHolder(@NonNull View ordersView, final OnItemClickListener listener)
        {
            super(ordersView);
            txtNames = ordersView.findViewById(R.id.txtProductNames);
            txtUID = ordersView.findViewById(R.id.txtUID);
            txtDate = ordersView.findViewById(R.id.txtDate);
            txtPrice = ordersView.findViewById(R.id.txtPrice);
            parentLayout = ordersView.findViewById(R.id.rv_layout);


            ordersView.setOnClickListener(new View.OnClickListener() {
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

