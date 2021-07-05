package com.example.flipshop.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.flipshop.R;
import com.example.flipshop.ui.auth.LoginActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    EditText displayNameEt, nameEt, emailEt;
    CircleImageView userPicIv;


    String imgPath;
    Bitmap bitmap;
    AppCompatImageView pickImage;
    Button save;
    NestedScrollView nestedScrollView;
    Button logout;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayNameEt = view.findViewById(R.id.display_name);
        nameEt = view.findViewById(R.id.ed_name);
        nameEt.setEnabled(false);
        emailEt = view.findViewById(R.id.ed_phone);
        emailEt.setEnabled(false);
        userPicIv = view.findViewById(R.id.userImgView);
        pickImage = view.findViewById(R.id.openCamera);
        logout = view.findViewById(R.id.logout);
        save = view.findViewById(R.id.save);
        nestedScrollView = view.findViewById(R.id.scrollView);
        initPreferences();
        bindData();

        displayNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displayNameEt.setCursorVisible(true);
                save.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logout.setVisibility(View.GONE);
                save(view);


            }
        });
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setVisibility(View.VISIBLE);
                openPicker(v);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().clear().apply();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();

            }
        });
    }

    private void save(View view) {
        displayNameEt.setCursorVisible(false);
        save.setText("Please wait...");
        sharedPreferences.edit().putString("display_name", displayNameEt.getText().toString()).apply();
        sharedPreferences.edit().putString("profile_pic", imgPath).commit();
        save.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);


    }

    private void initPreferences() {
        sharedPreferences = getActivity().getSharedPreferences("User_Details", MODE_PRIVATE);
    }

    private void bindData() {
        emailEt.setText(sharedPreferences.getString("uemail", ""));
        nameEt.setText(sharedPreferences.getString("uname", ""));
        displayNameEt.setText(sharedPreferences.getString("display_name", ""));

        if (!sharedPreferences.getString("profile_pic", "").equals("")) {
            String image = sharedPreferences.getString("profile_pic", "");
            Log.i("check", image);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeFile(image, options);
            userPicIv.setImageBitmap(bitmap);
            userPicIv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }
        else{
            Log.i("check", "empty image");
        }


    }

    public void openPicker(View v) {
        ImagePicker.Companion.with(this)
                .cropSquare()            //Crop image(Optional), Check Customization for more option
                .compress(1024)      //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                //.saveDir(new File(Environment.getExternalStorageDirectory(), "CricFrik"))
                .galleryMimeTypes(
                        new String[]{
                                "image/png",
                                "image/jpg",
                                "image/jpeg"
                        }
                )
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imgPath = data.getData().getPath();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                bitmap = BitmapFactory.decodeFile(imgPath, options);
                userPicIv.setImageBitmap(bitmap);
                userPicIv.setScaleType(ImageView.ScaleType.CENTER_CROP);


            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getContext(), "Unable to pick the image.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Unable to pick the image.", Toast.LENGTH_SHORT).show();
        }

    }
}
