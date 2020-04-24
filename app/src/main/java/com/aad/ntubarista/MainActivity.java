package com.aad.ntubarista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String uid = pref.getString("userId", "");
        if(!uid.equals(""))
        {
            Toast.makeText(MainActivity.this, uid,
                    Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(MainActivity.this, "Fuck u lot",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Navigation
    public void redirToPlaceOrder(View v)
    {
        Intent placeOrderIntent = new Intent(MainActivity.this, PlaceOrder.class);
        MainActivity.this.startActivity(placeOrderIntent);
    }
    
    public void redirToPastOrders(View v)
    {
        Intent pastOrdersIntent = new Intent(MainActivity.this, PastOrders.class);
        MainActivity.this.startActivity(pastOrdersIntent);
    }
}
