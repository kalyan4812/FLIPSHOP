package com.example.flipshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipshop.R;
import com.example.flipshop.ui.activities.ViewOrder;
import com.example.flipshop.models.OrdersModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class OrdersAdapter extends FirebaseRecyclerAdapter<OrdersModel,OrdersAdapter.OrdersViewHolder> {
 static    Context context;


//Intent myintent;
    public OrdersAdapter(@NonNull FirebaseRecyclerOptions<OrdersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrdersViewHolder holder, int position, @NonNull OrdersModel model) {
        holder.p_name.setText(model.getProductName());


        Picasso.get().load(model.getpImage()).into(holder.p_image);
        String orderstatus_string=model.getoStatus();
        if (orderstatus_string.equals("0")){
            holder.orderstatus.setText("PENDING..");
            holder.orderstatus.setTextColor(Color.BLUE);

        }else if (orderstatus_string.equals("1")){
            //accept
            holder.orderstatus.setText(" YOUR ORDER WAS ACCEPTED");
            holder.orderstatus.setTextColor(Color.GREEN);
        }else if(orderstatus_string.equals("2")){
            //reject
            holder.orderstatus.setText("Failed to place the order");
            holder.orderstatus.setTextColor(Color.RED);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          Toast.makeText(context,"OKKKKK",Toast.LENGTH_LONG).show();
               Intent myintent=new Intent(context, ViewOrder.class);
                myintent.putExtra("orderimage",model.getpImage());
                myintent.putExtra("ordername",model.getProductName());
                myintent.putExtra("orderstatus",model.getoStatus());
                context.startActivity(myintent);

            }
        });
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderslayout, parent, false);
     //   myintent=new Intent(parent.getContext(),ViewOrder.class);
        context=parent.getContext();
        return new OrdersAdapter.OrdersViewHolder(view);
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder{
        ImageView p_image;
        TextView p_name,orderstatus;
        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            p_image=(ImageView)itemView.findViewById(R.id.productImg);
            p_name=(TextView)itemView.findViewById(R.id.productName);
            orderstatus=(TextView)itemView.findViewById(R.id.orderstatus);
        }
    }
}
