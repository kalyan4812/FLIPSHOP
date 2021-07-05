package com.example.flipshop.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipshop.DashboardActivity;
import com.example.flipshop.adapters.OrdersAdapter;
import com.example.flipshop.models.OrdersModel;
import com.example.flipshop.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrdersFragment extends Fragment {


    RecyclerView ordersRecycler;
    OrdersAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        ordersRecycler=(RecyclerView)root.findViewById(R.id.recyclerView11);
      //  adapter.getactivity(getContext());

        ordersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
          firebaseOperation();
        return root;
    }
    private void firebaseOperation() {
        Query query= FirebaseDatabase.getInstance().getReference().child("Orders").child(DashboardActivity.email.replaceAll("[.]*",""));

        FirebaseRecyclerOptions<OrdersModel> options=new FirebaseRecyclerOptions.Builder<OrdersModel>()
                .setQuery(query, OrdersModel.class)
                .build();
        adapter=new OrdersAdapter(options);
   //     adapter.getactivity(getContext());
        ordersRecycler.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
