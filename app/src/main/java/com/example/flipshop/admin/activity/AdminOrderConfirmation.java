package com.example.flipshop.admin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.flipshop.admin.adapter.AdminOrderConfirmationAdapter;
import com.example.flipshop.R;
import com.example.flipshop.models.OrdersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AdminOrderConfirmation extends AppCompatActivity {

    RecyclerView ordersRecycler;
    AdminOrderConfirmationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_confirmation);
        getSupportActionBar().setTitle("Order Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ordersRecycler=(RecyclerView)findViewById(R.id.recyclerView11);

        ordersRecycler.setLayoutManager(new LinearLayoutManager(this));
        firebaseOperation();
    }

    private void firebaseOperation() {
        Query query= FirebaseDatabase.getInstance().getReference().child("admin").child("Orders");

        FirebaseRecyclerOptions<OrdersModel> options=new FirebaseRecyclerOptions.Builder<OrdersModel>()
                .setQuery(query, OrdersModel.class)
                .build();
        adapter=new AdminOrderConfirmationAdapter(options);
        ordersRecycler.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
