package com.example.flipshop.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipshop.DashboardActivity;
import com.example.flipshop.R;
import com.example.flipshop.models.OrdersModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class AdminOrderConfirmationAdapter extends FirebaseRecyclerAdapter<OrdersModel, AdminOrderConfirmationAdapter.AdminOrderConfirmationHolder> {


    DatabaseReference databaseReference;

    public AdminOrderConfirmationAdapter(@NonNull FirebaseRecyclerOptions<OrdersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdminOrderConfirmationHolder holder, int position, @NonNull OrdersModel model) {
        if (model.getoStatus().equals("0")) {
            holder.p_name.setText(model.getProductName());
            holder.customermail.setText(model.getUserID());

            Picasso.get().load(model.getpImage()).into(holder.p_image);
            holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.acceptBtn.setText("ACCEPTED");
                    holder.acceptBtn.setEnabled(false);


                    acceptOrder(model);

                }
            });
            holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.acceptBtn.setText("REJECTED");
                    holder.acceptBtn.setEnabled(false);

                    rejectOrder(model);
                }
            });
        }

    }

    private void rejectOrder(OrdersModel model) {
        FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(model.getUserID()).child(model.getProductID())
                .child("oStatus").setValue("2");


        FirebaseDatabase.getInstance().getReference().child("admin").child("Orders")
                .child(model.getProductID())
                .removeValue();

    }

    private void acceptOrder(OrdersModel model) {
        FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(model.getUserID()).child(model.getProductID())
                .child("oStatus").setValue("1");

        FirebaseDatabase.getInstance().getReference().child("admin").child("Orders")
                .child(model.getProductID())
                .removeValue();
    }

    @NonNull
    @Override
    public AdminOrderConfirmationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_admin_order_confirmation, parent, false);

        return new AdminOrderConfirmationHolder(view);
    }

    public class AdminOrderConfirmationHolder extends RecyclerView.ViewHolder {
        ImageView p_image;
        TextView p_name, customermail;
        Button acceptBtn, rejectBtn, moreBtn;

        public AdminOrderConfirmationHolder(@NonNull View itemView) {
            super(itemView);
            p_image = (ImageView) itemView.findViewById(R.id.productImg);
            p_name = (TextView) itemView.findViewById(R.id.productName);
            customermail = (TextView) itemView.findViewById(R.id.customerMail);
            acceptBtn = (Button) itemView.findViewById(R.id.accept);
            rejectBtn = (Button) itemView.findViewById(R.id.reject);
            moreBtn = (Button) itemView.findViewById(R.id.moredetails);
        }
    }
}
