package com.example.flipshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.flipshop.ui.activities.ProductDetailsActivity;
import com.example.flipshop.R;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;

    public ViewPagerAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return ProductDetailsActivity.imagesall.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_layout_viewpager, null);

        ImageView pImg=(ImageView)view.findViewById(R.id.productImg);
        //setimage
        Picasso.get().load(ProductDetailsActivity.imagesall.get(position)).into(pImg);

        ViewPager viewPager=(ViewPager)container;
        viewPager.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager=(ViewPager)container;
        View view=(View)object;
        viewPager.removeView(view);
    }
}
