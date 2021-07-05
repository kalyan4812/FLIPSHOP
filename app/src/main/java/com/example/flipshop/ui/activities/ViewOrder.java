package com.example.flipshop.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flipshop.R;
import com.google.api.Distribution;
import com.squareup.picasso.Picasso;

public class ViewOrder extends AppCompatActivity {
    String ordername;
    String orderimage;
    String orderstatus;
    ImageView p_image;
    TextView p_name, Orderstatus;
    LinearLayout feedbacklayout;
    RatingBar orderrating;
    Button submitfeedback;
    float ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        ordername = getIntent().getStringExtra("ordername").toString();
        orderimage = getIntent().getStringExtra("orderimage").toString();
        orderstatus = getIntent().getStringExtra("orderstatus").toString();
        feedbacklayout = findViewById(R.id.feedbacklayout);
        feedbacklayout.setVisibility(View.INVISIBLE);
        orderrating = findViewById(R.id.orderrating);
        submitfeedback = findViewById(R.id.submitfeedback);
        p_image = (ImageView) findViewById(R.id.productImg);
        p_name = (TextView) findViewById(R.id.productName);
        Orderstatus = (TextView) findViewById(R.id.orderstatus);
        Picasso.get().load(orderimage).into(p_image);
        p_name.setText(ordername);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (orderstatus.equals("0")) {
            Orderstatus.setText("Please wait some time till order is confirmed");

        } else if (orderstatus.equals("1")) {
            //accept
            Orderstatus.setText("YOUR ORDER WAS CONFIRMED");

        } else if (orderstatus.equals("2")) {
            //reject
            Orderstatus.setText("ORDER REQUEST WAS FAILED.PLEASE TRY AGAIN");
        }
        if (Orderstatus.equals("ORDER WAS DELIVERED")) {
            //   float ratings;
            feedbacklayout.setVisibility(View.VISIBLE);
            orderrating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    ratings = rating;
                }
            });
            submitfeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ratings > 0) {
                        feedbacklayout.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "THANK YOU FOR YOUR FEEDBACK", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "PLEASE RATE US BEFORE SUBMIT", Toast.LENGTH_LONG).show();
                    }
                }
            });

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

    public void buyitagain(View view) {

    }
}
