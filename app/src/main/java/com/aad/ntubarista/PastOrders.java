package com.aad.ntubarista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PastOrders extends OrderControl {
    OrderRVAdapter adapter;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_orders);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        adapter = new OrderRVAdapter(this, orders);

        RecyclerView recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        adapter.setOnItemCLickListener(new OrderRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String name = orders.get(position).get("productNames").toString();
                //basket.add(name);

                Toast.makeText(PastOrders.this, name,
                        Toast.LENGTH_SHORT).show();
            }
        });
        recyclerview.setAdapter(adapter);
    }

    @Override
    void onProductsFetched(ArrayList<Map<String, Object>> items) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //String userId = mAuth.getCurrentUser().getUid();
        String userId = pref.getString("userId", "");
        if(userId.equals(""))
        {
            userId = mAuth.getCurrentUser().getUid();
        }


        // Enter only active orders into the list
        if(items != null && !items.isEmpty())
        {
            List<Map<String, Object>> removeList = new ArrayList<>();

            for(Map<String, Object> elem : items)
            {
                String tempUID = (String) elem.get("userId");

                //Format
                if(tempUID.charAt(tempUID.length() -1) == '.')
                {
                    tempUID = tempUID.substring(0, tempUID.length() -1);
                }

                if(!tempUID.equals(userId))
                {
                    removeList.add(elem);
                }
            }

            // This way rather than removing on the go prevents crashing
            items.removeAll(removeList);
        }

        adapter.setOrders(items);
        adapter.notifyDataSetChanged();
    }
}
