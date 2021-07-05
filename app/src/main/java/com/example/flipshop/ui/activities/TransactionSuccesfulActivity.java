package com.example.flipshop.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.flipshop.DashboardActivity;
import com.example.flipshop.R;

public class TransactionSuccesfulActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_succesful);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DashboardActivity.class));
    }
}