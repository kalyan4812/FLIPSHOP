package com.example.flipshop.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.flipshop.models.Category;
import com.example.flipshop.viewholder.HolderCategory;
import com.example.flipshop.ui.activities.ProductsListActivity;
import com.example.flipshop.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

public class ShopbycategoryFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference ref;
    FirebaseDatabase database;
    SharedPreferences sharedPreferences;
    FirebaseRecyclerAdapter<Category, HolderCategory> firebaseRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shopbycategory, container, false);


        //database setup.
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Category_img");

        initRecylerView(root);
        initPreferences();
        onLoad();

        return root;
    }

    private void initRecylerView(View root) {
        recyclerView = root.findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sm);

    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    public void onLoad() {
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(ref, Category.class)
                .build();

         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Category, HolderCategory>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HolderCategory holder, int position, @NonNull Category model) {

                holder.setView(getContext(), model.getcName(), model.getcImageUri());
                Log.i("TAG", model.getcName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), model.getcName(), Toast.LENGTH_LONG).show();
                        Intent pIntent = new Intent(getContext(), ProductsListActivity.class);
                        pIntent.putExtra("pName", model.getcName());
                        pIntent.putExtra("uemail", sharedPreferences.getString("uemail", ""));
                        startActivity(pIntent);
                    }
                });
            }

            @NonNull
            @Override
            public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopbycategoryview, parent, false);
                return new HolderCategory(view);
            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }

    private void initPreferences() {
        sharedPreferences = sharedPreferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
    }
}