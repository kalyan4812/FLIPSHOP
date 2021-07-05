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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CartAdapter extends FirebaseRecyclerAdapter<AddCartModel, CartAdapter.cartViewHolder> {

    DatabaseReference dcart;
    public static int count;

    public CartAdapter(FirebaseRecyclerOptions<AddCartModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull cartViewHolder holder, int position, @NonNull AddCartModel model) {


        holder.p_name.setText(model.getProductName());
        holder.p_price.setText("₹ " + model.getpPrice() + ".00");
        holder.uploadId = model.getpCartkey();
        Picasso.get().load(model.getpImage()).into(holder.p_image);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromCart(model);
                notifyItemChanged(holder.getAdapterPosition());
                notifyDataSetChanged();
                getItemCount();
            }
        });

    }

    private void removeFromCart(AddCartModel model) {
        FirebaseDatabase.getInstance().getReference("CartAll").child(DashboardActivity.email.replaceAll("[.]*", ""))
                .child(model.getProductID()).removeValue();
    }

    public static int getcount() {

        return count;
    }

    @Override
    public int getItemCount() {
        count = super.getItemCount();
        return super.getItemCount();
    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitem, parent, false);
        getItemCount();
        return new cartViewHolder(view);
    }


    public class cartViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView p_image;
        TextView p_name, p_price;
        Button deleteBtn;
        String uploadId;

        public cartViewHolder(@NonNull View itemView) {
            super(itemView);

            p_image = itemView.findViewById(R.id.productImg);
            p_name = (TextView) itemView.findViewById(R.id.productName);
            p_price = (TextView) itemView.findViewById(R.id.productPrice);
            deleteBtn = (Button) itemView.findViewById(R.id.delcart);


        }
    }

}