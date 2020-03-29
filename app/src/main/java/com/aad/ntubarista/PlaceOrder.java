package com.aad.ntubarista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PlaceOrder extends AppCompatActivity {

    private ArrayList<Map<String, Object>> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                products.add(document.getData());
                            }
                        }
                        initRecyclerView(products);
                    }
                });
    }

    // TODO Function not in use, reference only
    public ArrayList<Map<String, Object>> getAllProducts() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        Map<String, Object> city = new HashMap<>();
        city.put("name", "Los Angeles");
        city.put("state", "CA");
        city.put("country", "USA");

        db.collection("cities").document("LA").set(city);


        final CompletableFuture<ArrayList<Map<String, Object>>> productsFuture = new CompletableFuture<>();
        db.collection("products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                products.add(document.getData());
                            }
                        }
                        productsFuture.complete(products);
                    }
                });
        try {
            return productsFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            return new ArrayList<>();
        }
    }

    private void initRecyclerView(final ArrayList<Map<String, Object>> items) {
        ProductRVAdapter adapter;
        RecyclerView recyclerview = findViewById(R.id.recycler_view);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        adapter = new ProductRVAdapter(this, items);
        recyclerview.setAdapter(adapter);
    }
}

