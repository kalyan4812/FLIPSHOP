package com.example.flipshop.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flipshop.DashboardActivity;
import com.example.flipshop.R;
import com.example.flipshop.adapters.ViewPagerAdapter;
import com.example.flipshop.admin.activity.AdminOrderConfirmation;
import com.example.flipshop.models.AddCartModel;
import com.example.flipshop.models.OrdersModel;
import com.example.flipshop.models.ProductUpload;
import com.example.flipshop.models.WishlistModel;
import com.example.flipshop.viewholder.ProductHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProductDetailsActivity extends AppCompatActivity {

    String key;
    ViewPager productView;
    DatabaseReference ref;
    FirebaseDatabase database;
    Integer stock;

    private DatabaseReference databaseReference;
    public static ProductDetailsActivity productDetailsActivity;
    ProgressDialog progressDialog;
    Query query;
    public static ArrayList<String> imagesall = new ArrayList<String>();
    private String brand, name, mrp, price, description, size, category, skey, uemail;
    String mycolor;
    Button cartitem;
    private String cImageUri1, cImageUri2, cImageUri3, cImageUri4;
    RecyclerView viewlikethis;
    ImageView wishlist;
    TextView pmrp, pprice, pname, pcolor, pbrand, pdesc, psize, stockav;
    Boolean likedItem = false;
    TextView ratingText;
    private double rating;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        setUpIds();
        showProgressDialog();


        key = getIntent().getStringExtra("pid");

        FirebaseDatabase.getInstance().getReference().child("Wishlist").child(DashboardActivity.email.replaceAll("[.]*", "")).child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot != null) {
                            WishlistModel wishlistModel = snapshot.getValue(WishlistModel.class);
                            if (wishlistModel != null) {
                                if (wishlistModel.getColorcode() == 1) {
                                    Log.i("info", "liked");
                                    likedItem = true;
                                    wishlist.setColorFilter(getResources().getColor(R.color.red));
                                } else {
                                    Log.i("info", "not liked");
                                    likedItem = false;
                                    wishlist.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
                                }
                            }


                        } else {
                            Log.i("info", "null and not liked");
                            likedItem = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        uemail = getIntent().getStringExtra("uemail").toString();

        imagesall.clear();


        ref = FirebaseDatabase.getInstance().getReference("product_info").child(key);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                brand = snapshot.child("brand").getValue().toString();
                name = snapshot.child("name").getValue().toString();
                mrp = snapshot.child("mrp").getValue().toString();
                price = snapshot.child("price").getValue().toString();
                description = snapshot.child("description").getValue().toString();
                stock = Integer.valueOf(snapshot.child("stock").getValue().toString());
                mycolor = snapshot.child("color").getValue().toString();
                size = snapshot.child("size").getValue().toString();
                skey = snapshot.child("key").getValue().toString();
                category = snapshot.child("category").getValue().toString();
                cImageUri1 = snapshot.child("cImageUri1").getValue().toString();
                cImageUri2 = snapshot.child("cImageUri2").getValue().toString();
                cImageUri3 = snapshot.child("cImageUri3").getValue().toString();
                cImageUri4 = snapshot.child("cImageUri4").getValue().toString();
                rating = snapshot.child("rating").getValue(Double.class);
                imagesall.add(cImageUri2);
                imagesall.add(cImageUri3);
                imagesall.add(cImageUri4);

                if (likedItem) {
                    ArrangeData(true);
                    likedItem = true;
                } else {
                    ArrangeData(false);
                    likedItem = false;
                }


                LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                viewlikethis.setLayoutManager(lm);


                query = FirebaseDatabase.getInstance().getReference("product_info").orderByChild("category").equalTo(category);

                getProductsList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /** < --- DO THIS AT FINAL ORDER----->
         if (ratingBar.getRating() == 0.0) {
         //ratingbar
         ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
        @Override public void onRatingChanged(RatingBar ratingBar, float mrating, boolean fromUser) {
        double new_rating = (rating + mrating) / (num_of_people_rated + 1);
        ref.child("num_of_people_rated").setValue(num_of_people_rated + 1);
        ref.child("rating").setValue(new_rating);

        }
        });
         }**/


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void setUpIds() {
        cartitem = findViewById(R.id.cart);
        viewlikethis = findViewById(R.id.recyclerView44);
        pmrp = findViewById(R.id.productmrp);
        pprice = findViewById(R.id.productprice);
        pname = findViewById(R.id.productname);
        wishlist = findViewById(R.id.wishlist);
        pcolor = findViewById(R.id.color_txt);
        pbrand = findViewById(R.id.brand_txt);
        pdesc = findViewById(R.id.description_txt);
        psize = findViewById(R.id.size_txt);
        stockav = findViewById(R.id.stock);
        productView = findViewById(R.id.viewPager);
        ratingBar = findViewById(R.id.ratingbar);
        ratingText = findViewById(R.id.rating);
    }

    public void getProductsList() {
        progressDialog.dismiss();
        FirebaseRecyclerOptions<ProductUpload> options = new FirebaseRecyclerOptions.Builder<ProductUpload>()
                .setQuery(query, ProductUpload.class)
                .build();

        FirebaseRecyclerAdapter<ProductUpload, ProductHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductUpload, ProductHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull final ProductUpload model) {

                holder.setView(ProductDetailsActivity.this, model.getName(), model.getcImageUri1(), model.getMrp(), model.getPrice(), model.getKey());
                Log.i("TAG", model.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), model.getKey(), Toast.LENGTH_LONG).show();
                        Intent intent_product = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                        intent_product.putExtra("pid", model.getKey().toString());
                        intent_product.putExtra("uemail", uemail);
                        startActivity(intent_product);

                    }
                });
            }

            @NonNull
            @Override
            public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product_list_item, parent, false);
                return new ProductHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        viewlikethis.setAdapter(firebaseRecyclerAdapter);


    }


    private void ArrangeData(Boolean liked) {

        pname.setText(name);
        pmrp.setText("MRP. " + mrp);
        pmrp.setPaintFlags(pmrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        pprice.setText("₹ " + price + ".00");
        pbrand.setText(brand);
        psize.setText(size);
        pcolor.setText(mycolor);
        pdesc.setText(description);

        if (stock > 0) {
            stockav.setTextColor(Color.GREEN);
            stockav.setText(R.string.in_stock);
        } else {
            stockav.setTextColor(Color.RED);
            stockav.setText(R.string.out_of_stock);
        }
        if (liked) {
            wishlist.setColorFilter(getResources().getColor(R.color.red));
        } else {
            wishlist.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
        }

        if (rating > 0.0) {
            Log.i("rating",rating+"");
            ratingText.setText(rating + "");
        } else {
            ratingText.setVisibility(View.GONE);
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        productView.setAdapter(viewPagerAdapter);
        progressDialog.dismiss();

    }


    public void addCart(View view) {
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CartAll");
        String uploadId = databaseReference.push().getKey();
        AddCartModel add = new AddCartModel(DashboardActivity.email, skey, name, cImageUri1, price, uploadId);
        databaseReference.child(DashboardActivity.email.replaceAll("[.]*", "")).child(key).setValue(add);
        Toast.makeText(this, "" + name + " added to cart", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    public void wishlist(View view) {
        if (likedItem) {
            likedItem = false;
            FirebaseDatabase.getInstance().getReference().child("Wishlist")
                    .child(DashboardActivity.email.replaceAll("[.]*", "")).child(key).removeValue();
            wishlist.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
            Toast.makeText(this, "" + name + " removed from wishlist", Toast.LENGTH_SHORT).show();

        } else {
            likedItem = false;
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Wishlist");
            String uploadId = databaseReference.push().getKey();
            int colorcode = 1;
            WishlistModel add = new WishlistModel(DashboardActivity.email, skey, name, cImageUri1, price, uploadId, colorcode);
            databaseReference.child(DashboardActivity.email.replaceAll("[.]*", "")).child(key).setValue(add);
            wishlist.setColorFilter(getResources().getColor(R.color.red));
            Toast.makeText(this, "" + name + " added to wishlist", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }


    }

    public void buynow(View view) {
        Intent intent = new Intent(getApplicationContext(), Buyitem.class);
        AddCartModel model = new AddCartModel(DashboardActivity.email, skey, name, cImageUri1, price,key);
        intent.putExtra("item",model);
        intent.putExtra("singleItem",true);
        startActivity(intent);
        /*
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        String uploadId = databaseReference.push().getKey();
        String ostatus = "0";
        OrdersModel add = new OrdersModel(uemail, skey, name, cImageUri1, price, uploadId, ostatus);
        databaseReference.child(DashboardActivity.email.replaceAll("[.]*", "")).child(key).setValue(add);
        Intent i = new Intent(this, AdminOrderConfirmation.class);
        i.putExtra("productname", name);
        i.putExtra("productimage", cImageUri1);
        Toast.makeText(this, "" + name + " added to Orders", Toast.LENGTH_SHORT).show();*/
    }
}
