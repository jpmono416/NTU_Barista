package com.aad.ntubarista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StaffMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);
    }


    // Navigation
    public void redirToAllOrders(View v)
    {
        Intent allOrdersIntent = new Intent(StaffMain.this, AllOrders.class);
        StaffMain.this.startActivity(allOrdersIntent);
    }

    public void redirToStoreMode(View v)
    {
        Intent storeModeIntent = new Intent(StaffMain.this, OrderTracker.class);
        StaffMain.this.startActivity(storeModeIntent);
    }

    public void redirToUpdateMenu(View v)
    {
        Intent updateMenuIntent = new Intent(StaffMain.this, UpdateMenu.class);
        StaffMain.this.startActivity(updateMenuIntent);
    }
}
