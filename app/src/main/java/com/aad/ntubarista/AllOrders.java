package com.aad.ntubarista;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Map;

public class AllOrders extends OrderControl {

    OrderRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

        adapter = new OrderRVAdapter(this, orders);

        RecyclerView recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        recyclerview.setAdapter(adapter);

    }

    @Override
    void onProductsFetched(ArrayList<Map<String, Object>> orders) {
        adapter.setOrders(orders);
        adapter.notifyDataSetChanged();
    }
}
