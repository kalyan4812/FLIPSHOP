package com.example.flipshop.admin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.flipshop.R;
import com.example.flipshop.adapters.ProductDeleteAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductDelete extends AppCompatActivity {
    Spinner sp;
    ArrayList<String> al = new ArrayList<String>();
    ArrayAdapter<String> ad;
    DatabaseReference dr;

    ProgressDialog progressDialog;
    ListView productview;
    Button delete;
    public static ArrayList<String> products = new ArrayList<>();
    public static ArrayList<String> productkeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_delete);

        productview = findViewById(R.id.listview4);
        delete = findViewById(R.id.buttondelete);
        delete.setVisibility(View.VISIBLE);
        productview.setVisibility(View.INVISIBLE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("PLEASE WAIT...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        sp = findViewById(R.id.categoryname);

        getSupportActionBar().setTitle("Delete Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // fd=FirebaseDatabase.getInstance();
        dr = FirebaseDatabase.getInstance().getReference();
        ad = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, al);
        sp.setAdapter(ad);
        getdata();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getdata() {
        progressDialog.show();
        dr.child("Category_img").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                al.clear();
                al.add("SELECT CATEGORY NAME");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    al.add(ds.getKey());
                }
                ad.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    getproducts(sp.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void delete(View view) {
        ArrayList<String> s = ProductDeleteAdapter.productsselected;
        if (s.size() > 0) {
            Toast.makeText(getApplicationContext(), s.size() + "", Toast.LENGTH_SHORT).show();

            progressDialog.show();
            for (int i = 0; i < s.size(); i++) {
                Toast.makeText(ProductDelete.this, s.get(i) + "", Toast.LENGTH_LONG).show();

                dr.child("product_info").child(s.get(i)).removeValue();
            }
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Products deleted", Toast.LENGTH_SHORT).show();

            new ProductDeleteAdapter().notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), "Please select items to be deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public void getproducts(String Category) {
        progressDialog.show();
        dr.child("product_info").orderByChild("category").equalTo(Category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    products.add(d.child("name").getValue().toString());
                    productkeys.add(d.getKey());
                    //Toast.makeText(getApplicationContext(),d.child("name").getValue().toString(),Toast.LENGTH_SHORT).show();
                }
                productview.setAdapter(new ProductDeleteAdapter());
                ProductDeleteAdapter.productsselected.clear();
                productview.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

