package com.example.flipshop.admin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.flipshop.R;
import com.google.firebase.database.FirebaseDatabase;

public class AdminDashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);
        getSupportActionBar().setTitle("Admin Panel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void openAdminCatAdd(View view) {

        Intent intent = new Intent(AdminDashActivity.this, AdminCategoryAdd.class);
        startActivity(intent);
    }

    public void productadd(View view) {
        Intent intent = new Intent(AdminDashActivity.this, AdminProductAdd.class);
        startActivity(intent);
    }

    public void orderrequests(View view) {
        Intent i = new Intent(this, AdminOrderConfirmation.class);
        startActivity(i);
    }

    public void openAdminAddadd(View view) {
        Intent intent = new Intent(AdminDashActivity.this, AdminOffer.class);
        startActivity(intent);
    }

    public void adddelete(View view) {
        FirebaseDatabase.getInstance().getReference().child("Category_offer").removeValue();
        Toast.makeText(getApplicationContext(),"All adds are deleted..",Toast.LENGTH_SHORT).show();
    }

    public void productdelete(View view) {
        Intent intent = new Intent(AdminDashActivity.this, ProductDelete.class);
        startActivity(intent);
    }

    public void categorydelete(View view) {
        Intent intent = new Intent(AdminDashActivity.this, CategoryDelete.class);
        startActivity(intent);
    }
}
