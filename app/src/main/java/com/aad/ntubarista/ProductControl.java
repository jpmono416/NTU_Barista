package com.aad.ntubarista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.Map;
import java.util.Objects;


abstract class ProductControl extends AppCompatActivity {

    protected ArrayList<Map<String, Object>> products = new ArrayList<>();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.collection("products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            products.clear();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                products.add(document.getData());
                            }
                            onProductsFetched(products);
                        }
                    }
                });
    }

    abstract void onProductsFetched(final ArrayList<Map<String, Object>> items);
}



