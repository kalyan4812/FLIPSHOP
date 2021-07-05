package com.example.flipshop.viewholder;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipshop.DashboardActivity;
import com.example.flipshop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductHolder extends RecyclerView.ViewHolder {

    View view;
    DatabaseReference databaseReference;
    public Button cart;
    ImageView wish;

    public ProductHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;


    }

    public void setView(Context context, String pname, String pimgurl, String pmrp, String pprice, String keys) {
        TextView pName = view.findViewById(R.id.name_product);
        ImageView pImage = view.findViewById(R.id.image_product);
        TextView pMrp = view.findViewById(R.id.productmrpprice);
        TextView price = view.findViewById(R.id.productprice);
        cart = view.findViewById(R.id.cart);
        wish = view.findViewById(R.id.wish);
        //  Button cart=view.findViewById(R.id.cart);

        pName.setText(pname);
        Picasso.get().load(pimgurl.toString()).into(pImage);
        pMrp.setText(pmrp);
        pMrp.setPaintFlags(pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        price.setText("₹ " + pprice + ".00");
        FirebaseDatabase.getInstance().getReference().child("checkcolor").child(DashboardActivity.email.replaceAll("[.]*", ""))
                .child(keys).child("colorcode");


    }


}
