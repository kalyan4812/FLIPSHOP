package com.example.flipshop.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.flipshop.models.Category;
import com.example.flipshop.viewholder.HolderCategory;
import com.example.flipshop.ui.activities.LocationBottomSheet;
import com.example.flipshop.ui.activities.ProductsListActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

import com.example.flipshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements LocationBottomSheet.BottomSheetListener {
    AutoCompleteTextView actv;
    Button logoutBtn, b;
    FirebaseAuth mAuth;
    TextView message;
    RecyclerView recyclerView;
    DatabaseReference ref, ref2;
    FirebaseDatabase database;
    TextView locationname;
    String location;
    ImageButton locationicon;
    Button mic;

    private AdView mAdView;

    private AdView adsv;
    private FrameLayout frameLayoutAds;
    ImageSlider slider;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        setUpIds(root);

        initPreferences();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Category_img");


        setUpSlider();

        initRecycler();

        setUpAds();

        //button to acces current location.
        locationicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationBottomSheet lbs = new LocationBottomSheet();
                lbs.show(getChildFragmentManager(), "exampleBottomSheet");

            }
        });


        //mic button.
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "HI SPEAK SOMETHING....");
                try {
                    startActivityForResult(intent, 100);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }

    private void setUpAds() {
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("your device id should go here")).build();
        MobileAds.setRequestConfiguration(configuration);


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    private void initPreferences() {
        sharedPreferences = sharedPreferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
    }

    private void setUpIds(View root) {
        slider = root.findViewById(R.id.imageSlider);
        locationname = root.findViewById(R.id.locationname);
        locationicon = root.findViewById(R.id.locationicon);
        frameLayoutAds = root.findViewById(R.id.ads_frame);
        mic = root.findViewById(R.id.mic);
        actv = root.findViewById(R.id.actv);
        recyclerView = root.findViewById(R.id.recyclerView);
        mAdView = root.findViewById(R.id.adView);

    }

    private void initRecycler() {

        LinearLayoutManager managerCategory = new LinearLayoutManager(getContext());
        managerCategory.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(managerCategory);


    }

    private void setUpSlider() {
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.clear();
        FirebaseDatabase.getInstance().getReference().child("Category_offer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    if(d.child("subtitle").getValue().toString().equals("") || d.child("subtitle").getValue().toString().equals(" ")){
                        slideModels.add(new SlideModel(d.child("cImageUri").getValue().toString(),ScaleTypes.FIT));
                    }
                    else{
                        slideModels.add(new SlideModel(d.child("cImageUri").getValue().toString(),d.child("subtitle").getValue().toString(),ScaleTypes.FIT));


                    }




                }
                slider.setImageList(slideModels, ScaleTypes.FIT);
                slider.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                slider.startSliding(3000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error + "", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(ref, Category.class)
                .build();

        FirebaseRecyclerAdapter<Category, HolderCategory> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Category, HolderCategory>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HolderCategory holder, int position, @NonNull Category model) {

                holder.setView(getContext(), model.getcName(), model.getcImageUri());
                Log.i("TAG", model.getcName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), model.getcName(), Toast.LENGTH_LONG).show();
                        Intent pIntent = new Intent(getContext(), ProductsListActivity.class);
                        pIntent.putExtra("pName", model.getcName());
                        pIntent.putExtra("uemail", sharedPreferences.getString("uemail", ""));
                        startActivity(pIntent);
                    }
                });
            }

            @NonNull
            @Override
            public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_category, parent, false);
                return new HolderCategory(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> al = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                actv.setText(al.get(0));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = getActivity().getSharedPreferences("MYSHAREDPREFLOCATION", MODE_PRIVATE);
        location = prefs.getString("mylocationlkey", null);
        locationname.setText(location);
    }

    @Override
    public void onButtonClicked(String text) {
        locationname.setText("Deliver to : " +text);
        updateLocation("Deliver to : " +text);
    }

    private void updateLocation(String text) {
        location = text;
        SharedPreferences prefs = getActivity().getSharedPreferences("MYSHAREDPREFLOCATION", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mylocationlkey", location);
        editor.apply();

    }

    private void loadMobileAds() {
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("your device id should go here")).build();
        MobileAds.setRequestConfiguration(configuration);
    }

    private void getAdBanners() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSizenew = getSetSize();
        adsv.setAdSize(adSizenew);
        adsv.loadAd(adRequest);
    }

    private AdSize getSetSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float width = displayMetrics.widthPixels;
        float density = displayMetrics.density;
        int adwidth = (int) (width / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getContext(), adwidth);
    }

}
