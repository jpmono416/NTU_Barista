package com.aad.ntubarista;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PlaceOrder extends ProductControl {
    ProductRVAdapter adapter;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        adapter = new ProductRVAdapter(this, products);

        RecyclerView recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        //List<String> prefList = pref.getStringSet("basket",);
        List<String> basket = new ArrayList<>();

        adapter.setOnItemCLickListener(new ProductRVAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(int position) {
               String name = products.get(position).get("name").toString();
               String basket = pref.getString("basket", "");
               //basket.add(name);

               basket += name += ",";
               editor = pref.edit();
               editor.putString("basket", "");
               editor.putString("basket", basket);
               editor.apply();

               Toast.makeText(PlaceOrder.this, "Added to your basket.",
                       Toast.LENGTH_SHORT).show();
           }
       });
        recyclerview.setAdapter(adapter);

    }

    public void onProductsFetched(final ArrayList<Map<String, Object>> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }
}