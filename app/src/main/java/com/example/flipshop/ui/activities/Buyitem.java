package com.example.flipshop.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flipshop.DashboardActivity;
import com.example.flipshop.MyApplication;
import com.example.flipshop.R;
import com.example.flipshop.adapters.BuyitemAdapter;
import com.example.flipshop.models.AddCartModel;
import com.example.flipshop.models.OrdersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Buyitem extends AppCompatActivity {
    private static final int UPI_PAYMENT = 1001;
    RecyclerView myRecycler;
    BuyitemAdapter adapter;
    TextView numitems, totalcost;
    Button payment;
    int numofitems, cost;
    boolean singleItem = false;
    LinearLayout singleItemLayout;
    TextView singleItemName, singleItemPrice;
    ImageView singleItemPic;
    AddCartModel singleItemModel;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyitem);

        initViews();
        bindData();


        if (getIntent() != null && getIntent().getBooleanExtra("singleItem", false) && getIntent().getSerializableExtra("item") != null) {
            singleItem = getIntent().getBooleanExtra("singleItem", false);
            myRecycler.setVisibility(View.GONE);
            AddCartModel model = (AddCartModel) getIntent().getSerializableExtra("item");
            singleItemModel = model;
            singleItemName.setText(model.getProductName());
            singleItemPrice.setText("₹ " + model.getpPrice() + ".00");
            Picasso.get().load(model.getpImage()).into(singleItemPic);
            singleItemLayout.setVisibility(View.VISIBLE);
            numitems.setText("No of items : " + 1);
            totalcost.setText("Total Cost : " + model.getpPrice());


        } else {
            firebaseOperation();
        }


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpProgressDialog();
                doWork();
              /*  if (singleItem) {
                    proceedPaymentUsingUpi(Long.parseLong(singleItemPrice.getText().toString()) + "", "xxx@ybl", "FlipShop", "Safe Payment");
                } else {
                   proceedPaymentUsingUpi(BuyitemAdapter.totalCost() + "", "xxxx@ybl", "FlipShop", "Safe Payment");
                }*/

                Toast.makeText(Buyitem.this, "IN PROGRESS.....", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void bindData() {
        numofitems = getIntent().getIntExtra("numitems", 0);
        numitems.setText("No of items : " + numofitems);
        totalcost.setText("Total cost : Please wait..");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (numofitems == 1) {
            getSupportActionBar().setTitle("Confirm Order");
        } else {
            getSupportActionBar().setTitle("Confirm Orders");
        }
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

    private void initViews() {
        numitems = findViewById(R.id.numitems);
        totalcost = findViewById(R.id.totalcost);
        payment = findViewById(R.id.gotopayment);
        myRecycler = findViewById(R.id.recyclerView11);
        singleItemLayout = findViewById(R.id.single_item_layout);
        singleItemName = findViewById(R.id.productName);
        singleItemPrice = findViewById(R.id.productPrice);
        singleItemPic = findViewById(R.id.productImg);
    }

    private void firebaseOperation() {
        Query query = FirebaseDatabase.getInstance().getReference().child("CartAll").child(DashboardActivity.email.replaceAll("[.]*", ""));


        FirebaseRecyclerOptions<AddCartModel> options = new FirebaseRecyclerOptions.Builder<AddCartModel>()
                .setQuery(query, AddCartModel.class)
                .build();
        adapter = new BuyitemAdapter(options);
        myRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myRecycler.setAdapter(adapter);
        myRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);
                if (state.getItemCount() == numofitems) {
                    Log.i("check", "inflation done");
                    totalcost.setText("Total cost : " + BuyitemAdapter.totalCost());
                }


            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    private void proceedPaymentUsingUpi(String amount, String upiId, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", "1")
                .appendQueryParameter("tn", System.currentTimeMillis() + "")
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(Buyitem.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (MyApplication.isInternetAvailable()) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                setUpProgressDialog();
                doWork();

                // Toast.makeText(Buyitem.this, "Transaction successful.", Toast.LENGTH_SHORT).show();

                Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(Buyitem.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Buyitem.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Buyitem.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void doWork() {
        if (!singleItem) {
            addItemsToOrders();


        } else {
            addItemToOrders(singleItemModel);
            progressDialog.dismiss();
            startActivity(new Intent(Buyitem.this, TransactionSuccesfulActivity.class));

        }
    }

    private void goBack() {
        super.onBackPressed();
    }

    private void refreshCart() {
        FirebaseDatabase.getInstance().getReference().child("CartAll").child(DashboardActivity.email.replaceAll("[.]*", "")).removeValue();

    }

    private void addItemsToOrders() {
        FirebaseDatabase.getInstance().getReference().child("CartAll").child(DashboardActivity.email.replaceAll("[.]*", ""))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (ds != null && ds.getValue() != null)
                                addItemToOrders(ds.getValue(AddCartModel.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        refreshCart();
        progressDialog.dismiss();

        startActivity(new Intent(Buyitem.this, TransactionSuccesfulActivity.class));
    }

    private void addItemToOrders(AddCartModel value) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        String uploadId = databaseReference.push().getKey();
        String ostatus = "0";
        OrdersModel add = new OrdersModel(DashboardActivity.email.replaceAll("[.]*", ""), value.getProductID(), value.getProductName(), value.getpImage(), value.getpPrice(), uploadId, ostatus);
        databaseReference.child(DashboardActivity.email.replaceAll("[.]*", "")).child(value.getProductID()).setValue(add);
        addInAdmin(value);

    }

    private void addInAdmin(AddCartModel value) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("admin").child("Orders");
        String uploadId = databaseReference.push().getKey();
        String ostatus = "0";
        OrdersModel add = new OrdersModel(DashboardActivity.email.replaceAll("[.]*", ""), value.getProductID(), value.getProductName(), value.getpImage(), value.getpPrice(), uploadId, ostatus);
        databaseReference.child(value.getProductID()).setValue(add);
    }

    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }


}
