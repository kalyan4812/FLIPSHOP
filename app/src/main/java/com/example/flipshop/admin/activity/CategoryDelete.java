package com.example.flipshop.admin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.flipshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CategoryDelete extends AppCompatActivity {
    Spinner sp;
    ArrayList<String> al=new ArrayList<String>();
    ArrayAdapter<String> ad;
    String categoryfordeletion;

    DatabaseReference dr;
    private StorageReference storageReference;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_delete);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("PLEASE WAIT...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        sp=findViewById(R.id.categoryname);

        getSupportActionBar().setTitle("Delete Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // fd=FirebaseDatabase.getInstance();
        dr= FirebaseDatabase.getInstance().getReference();
        ad=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,al);
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
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    al.add(ds.getKey());
                }
                ad.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void delete(View view) {
        if(!(sp.getSelectedItem().toString()=="SELECT CATEGORY NAME")) {
            dr.child("Category_img").child(sp.getSelectedItem().toString()).removeValue();
        }
        else{
            Toast.makeText(getApplicationContext(),"please select a category for deletion",Toast.LENGTH_LONG).show();
        }
    }
}
