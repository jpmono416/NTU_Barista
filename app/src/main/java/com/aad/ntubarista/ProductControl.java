package com.aad.ntubarista;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

class ProductControl {

    private ArrayList<Map<String, Object>> products = new ArrayList<>();

    // Method returns future to make it reusable accross activities
    // otherwise listener won't complete before returning
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
        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        try
        {
            return productsFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            return new ArrayList<>();
        }
*/


    }
    /*
    private void removeItemByName(final String name)
    {
        db.collection("Items").whereEqualTo("Name", name).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot snapshot : task.getResult()){
                                db.collection("Items").document(name).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(PlaceOrder.this, "Deleted successfully",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PlaceOrder.this, "Couldn't delete, please try again.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}*/

}
