package com.example.flipshop.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flipshop.R;
import com.squareup.picasso.Picasso;

public class HolderCategory  extends RecyclerView.ViewHolder {
    View view;
    public HolderCategory(@NonNull View itemView) {
        super(itemView);
        view=itemView;
    }

    public void setView(Context context, String name, String url){
        TextView nametx=view.findViewById(R.id.name_category);
        ImageView imgcat=view.findViewById(R.id.image_category);
        nametx.setText(name);
        Picasso.get().load(url.toString()).into(imgcat);

    }

}
