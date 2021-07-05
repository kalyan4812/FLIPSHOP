package com.example.flipshop.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipshop.models.AddCartModel;
import com.example.flipshop.ui.activities.Buyitem;
import com.example.flipshop.adapters.CartAdapter;
import com.example.flipshop.DashboardActivity;
import com.example.flipshop.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class CartFragment extends Fragment {

    DatabaseReference databaseReference;
    Button buyNow;
    RecyclerView cartRecycler;
    CartAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        initRecycler(root);


        firebaseOperation();
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) cartRecycler.getLayoutManager();
                int totalItemCount = linearLayoutManager.getItemCount();

                if (totalItemCount >= 1) {
                    Intent intent = new Intent(getContext(), Buyitem.class);
                    intent.putExtra("numitems", totalItemCount);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "PLEASE ADD ITEMS TO BUY", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    private void initRecycler(View root) {
        cartRecycler = (RecyclerView) root.findViewById(R.id.recyclerView11);
        buyNow = (Button) root.findViewById(R.id.buyBtn);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void firebaseOperation() {
        Query query = FirebaseDatabase.getInstance().getReference().child("CartAll").child(DashboardActivity.email.replaceAll("[.]*", ""));


        FirebaseRecyclerOptions<AddCartModel> options = new FirebaseRecyclerOptions.Builder<AddCartModel>()
                .setQuery(query, AddCartModel.class)
                .build();
        adapter = new CartAdapter(options);
        cartRecycler.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

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