package com.example.flipshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.flipshop.admin.activity.ProductDelete;
import com.example.flipshop.R;

import java.util.ArrayList;

public class ProductDeleteAdapter extends BaseAdapter {
    public static ArrayList<String> productsselected=new ArrayList<>();
    @Override
    public int getCount() {
        return ProductDelete.products.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.custom_product_delete,null);

        TextView tv1=v.findViewById(R.id.productnamefordeletion);
        CheckBox checkBox=v.findViewById(R.id.checkbox1);


        tv1.setText(ProductDelete.products.get(position));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    productsselected.add(ProductDelete.productkeys.get(position).toString());
                }
                else {
                    productsselected.remove(ProductDelete.productkeys.get(position).toString());
                }
            }
        });
        return v;
    }
}
