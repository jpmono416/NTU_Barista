package com.aad.ntubarista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderTracker extends OrderControl {
    private Integer listPosition = 0;
    OrderRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracker);

        adapter = new OrderRVAdapter(this, orders);

        RecyclerView recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        //txtStatus = ordersView.findViewById(R.id.txtCurrentStatus);
        adapter.setOnItemCLickListener(new OrderRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                listPosition = position;
                String status = orders.get(position).get("status").toString();
                TextView txtStatus = findViewById(R.id.txtCurrentStatus);
                txtStatus.setText(status);
            }
        });

        recyclerview.setAdapter(adapter);

    }

    @Override
    void onProductsFetched(ArrayList<Map<String, Object>> items) {

        /* This stuff makes the app crash for some reason. If I use a second temporary var
                the same thing happens

        // Enter only active items into the list
        for(Map<String, Object> elem : items)
        {
            if(elem.get("status").equals("collected"))
            {
                items.remove(elem);
            }
         */
        adapter.setOrders(items);
        adapter.notifyDataSetChanged();
    }

    public void onCheckPressed(View v)
    {
        Map<String, Object> obj = orders.get(listPosition);

        switch (obj.get("status").toString())
        {

            case "in progress":
                // set as ready
                obj.put("status", "ready");
            break;

            case "ready":
                obj.put("status", "collected");
                // set as collected
            break;

            default:

            break;

        }

        orders.set(listPosition, obj);
        Toast.makeText(OrderTracker.this, "Order is: " + obj.get("status"),
                Toast.LENGTH_SHORT).show();
    }

    private void autoSave()
    {
        // WIP
    }
    private void saveDoc(Map<String, Object> current)
    {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("orders")
                .whereEqualTo("date", current.get("date"))
                .whereEqualTo("userId", current.get("userId"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                db.collection("orders").document(document.getId())
                                        .update("status", document.get("status"));

                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        }
                    }});
    }
}