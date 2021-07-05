package com.example.flipshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipshop.R;
import com.example.flipshop.models.AddCartModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class BuyitemAdapter extends FirebaseRecyclerAdapter<AddCartModel, BuyitemAdapter.buyViewHolder> {


    public static HashMap<String, Long> hashMap = new HashMap<>();



    public BuyitemAdapter(@NonNull FirebaseRecyclerOptions<AddCartModel> options) {
        super(options);
        hashMap.clear();
    }

    @Override
    protected void onBindViewHolder(@NonNull buyViewHolder holder, int position, @NonNull AddCartModel model) {
        holder.p_name.setText(model.getProductName());
        holder.p_price.setText("₹ " + model.getpPrice() + ".00");
        Picasso.get().load(model.getpImage()).into(holder.p_image);
        if (hashMap.containsKey(model.getProductID())) {
            //nothing to do
        } else {
            hashMap.put(model.getProductID(), Long.parseLong(model.getpPrice()));

        }

    }


    public static synchronized long totalCost() {
      long  cost = 0;
        for (long l : hashMap.values()) {
            cost = cost + l;
        }
        return cost;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {

        return super.getItemCount();
    }

    @NonNull
    @Override
    public buyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_buy_item, parent, false);
        return new buyViewHolder(v);
    }


    public class buyViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView p_image;
        TextView p_name, p_price;

        public buyViewHolder(@NonNull View itemView) {
            super(itemView);
            p_image =  itemView.findViewById(R.id.productImg);
            p_name = (TextView) itemView.findViewById(R.id.productName);
            p_price = (TextView) itemView.findViewById(R.id.productPrice);
        }
    }

}
