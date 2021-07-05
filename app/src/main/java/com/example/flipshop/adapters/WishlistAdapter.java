package com.example.flipshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipshop.DashboardActivity;
import com.example.flipshop.R;
import com.example.flipshop.models.AddCartModel;
import com.example.flipshop.models.WishlistModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class WishlistAdapter extends FirebaseRecyclerAdapter<WishlistModel, WishlistAdapter.WishlistViewHolder> {

    DatabaseReference databaseReference;

    public WishlistAdapter(@NonNull FirebaseRecyclerOptions<WishlistModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WishlistViewHolder holder, int position, @NonNull WishlistModel model) {
        holder.p_name.setText(model.getProductName());
        holder.p_price.setText("₹ " + model.getpPrice() + ".00");


        holder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(model);
                removeFromWishList(model);
                notifyItemChanged(holder.getAdapterPosition());

            }
        });
        holder.delwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Wishlist").child(DashboardActivity.email.replaceAll("[.]*", ""))
                        .child(model.getProductID()).removeValue();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });


        holder.uploadId = model.getpCartkey();
        Picasso.get().load(model.getpImage()).into(holder.p_image);


    }

    private void removeFromWishList(WishlistModel model) {
        FirebaseDatabase.getInstance().getReference("Wishlist").child(DashboardActivity.email.replaceAll("[.]*", ""))
                .child(model.getProductID()).removeValue();
    }

    private void addToCart(WishlistModel model) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CartAll");
        String uploadId = databaseReference.push().getKey();

        AddCartModel add = new AddCartModel(model.getUserID(), model.getProductID(), model.getProductName(), model.getpImage(), model.getpPrice(), uploadId);
        databaseReference.child(DashboardActivity.email.replaceAll("[.]*", "")).child(model.getProductID()).setValue(add);

    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlistitem, parent, false);
        WishlistViewHolder wh = new WishlistViewHolder(view);


        return new WishlistAdapter.WishlistViewHolder(view);
    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView p_image;
        TextView p_name, p_price, rating;
        Button addtocart, delwish;

        String uploadId;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            p_image = itemView.findViewById(R.id.productImg);
            p_name = (TextView) itemView.findViewById(R.id.productName);
            p_price = (TextView) itemView.findViewById(R.id.productPrice);
            addtocart = (Button) itemView.findViewById(R.id.wishcart);
            delwish = (Button) itemView.findViewById(R.id.delwishlist);

        }
    }
}
