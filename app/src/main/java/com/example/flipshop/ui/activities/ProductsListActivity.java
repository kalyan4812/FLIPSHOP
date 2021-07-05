package com.example.flipshop.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flipshop.DashboardActivity;
import com.example.flipshop.R;
import com.example.flipshop.models.AddCartModel;
import com.example.flipshop.models.ProductUpload;
import com.example.flipshop.viewholder.ProductHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ProductsListActivity extends AppCompatActivity {

    private String pName, uemail;

    RecyclerView recyclerView;

    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    Query query;
    EditText searchQuery;
    FirebaseRecyclerAdapter<ProductUpload, RecyclerView.ViewHolder> firebaseRecyclerAdapter;
    String queryText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        searchQuery = findViewById(R.id.search_bar);

        getIntentData();

        setUpToolBar();

        setUpProgressDialog();


        initRecycler();

        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 queryText = s.toString();
                Log.i("check",queryText+"");
                if (!queryText.isEmpty()) {
                 if(firebaseRecyclerAdapter!=null){
                     Log.i("check","not null");
                     firebaseRecyclerAdapter.notifyDataSetChanged();
                 }
                  //  getProductsList(queryText);

                } else {

                    if(firebaseRecyclerAdapter!=null){
                        firebaseRecyclerAdapter.notifyDataSetChanged();
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                firebaseRecyclerAdapter.notifyDataSetChanged();
             Log.i("checks",s.toString());

            }
        });
        // getItems based on category selected.
        query = FirebaseDatabase.getInstance().getReference("product_info").orderByChild("category").equalTo(pName);


        getProductsList();


    }

    private void getIntentData() {
        pName = getIntent().getStringExtra("pName").toString();
        uemail = getIntent().getStringExtra("uemail").toString();

    }

    private void setUpToolBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(pName);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.recyclerView23);
        StaggeredGridLayoutManager sm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sm);
    }

    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void getProductsList() {

        progressDialog.dismiss();
        FirebaseRecyclerOptions<ProductUpload> options = new FirebaseRecyclerOptions.Builder<ProductUpload>()
                .setQuery(query, ProductUpload.class)
                .build();

         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductUpload, RecyclerView.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull final ProductUpload model) {

                if(holder instanceof ProductHolder) {
                    ProductHolder holder1=(ProductHolder) holder;

                    holder1.setView(ProductsListActivity.this, model.getName(), model.getcImageUri1(), model.getMrp(), model.getPrice(), model.getKey());

                    holder1.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent_product = new Intent(ProductsListActivity.this, ProductDetailsActivity.class);
                            intent_product.putExtra("pid", model.getKey());
                            intent_product.putExtra("uemail", uemail);
                            startActivity(intent_product);
                        }
                    });

                    holder1.cart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addItemToCart(model);

                        }
                    });
                }
                else {
                    //
                }

            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType==1) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product_list_item, parent, false);
                    return new ProductHolder(view);
                }
                else{
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_item, parent, false);
                    return new EmptyViewHolder(view);
                }
            }

            @Override
            public int getItemViewType(int position) {


                ProductUpload product = getItem(position);

                if(product.getName().toLowerCase().trim().contains(queryText.toLowerCase().trim())){
                    return 1;
                }
                else{
                    return  2;
                }

            }
        };

        firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }
    private void addItemToCart(ProductUpload model) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CartAll");
        String uploadId = databaseReference.push().getKey();
        AddCartModel add = new AddCartModel(DashboardActivity.email, model.getKey(), model.getName(), model.getcImageUri1(), model.getPrice(), uploadId);
        databaseReference.child(DashboardActivity.email.replaceAll("[.]*", "")).child(model.getKey()).setValue(add);
        Toast.makeText(getApplicationContext(), model.getName() + " is added to cart", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}