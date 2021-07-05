package com.example.flipshop.admin.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.flipshop.models.ProductUpload;
import com.example.flipshop.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AdminProductAdd extends AppCompatActivity {
    Spinner sp;
    ArrayList<String> al = new ArrayList<String>();
    ArrayAdapter<String> ad;
    LinearLayout lnew;

    DatabaseReference dr, dr1;
    EditText brand, name, mrp, price, color, stock, size, description;
    Button photo1, photo2, photo3, photo4;
    ImageView img1, img2, img3, img4;
    private StorageReference storageReference;
    // private DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    private Uri imageUri1, imageUri2, imageUri3, imageUri4;
    String imguri1, imguri2, imguri3, imguri4;
    String category;
    String downloadUrl1, downloadUrl2, downloadUrl3, downloadUrl4;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_add);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Data Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getSupportActionBar().setTitle("Add Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // fd=FirebaseDatabase.getInstance();

        //ids ////////////////////////////////////////////////////////////////////////////
        brand = findViewById(R.id.brand);
        name = findViewById(R.id.name);
        mrp = findViewById(R.id.mrp);
        price = findViewById(R.id.price);
        color = findViewById(R.id.color);
        stock = findViewById(R.id.stock);
        size = findViewById(R.id.size);
        description = findViewById(R.id.description);

        photo1 = findViewById(R.id.pick_pic_1);
        photo2 = findViewById(R.id.pick_pic_2);
        photo3 = findViewById(R.id.pick_pic_3);
        photo4 = findViewById(R.id.pick_pic_4);
        img1 = findViewById(R.id.selected_pic_1);
        img2 = findViewById(R.id.selected_pic_2);
        img3 = findViewById(R.id.selected_pic_3);
        img4 = findViewById(R.id.selected_pic_4);
        ////////////////////////////////////////////////////////////////////////////////

        lnew = findViewById(R.id.linearnew);
        sp = findViewById(R.id.categoryname);
        // fd=FirebaseDatabase.getInstance();
        dr1 = FirebaseDatabase.getInstance().getReference();
        ad = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, al);
        sp.setAdapter(ad);
        getdata();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    lnew.setVisibility(View.VISIBLE);
                } else {
                    lnew.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////////////
        storageReference = FirebaseStorage.getInstance().getReference("product_info");
        dr = FirebaseDatabase.getInstance().getReference("product_info");

        // key=dr.push().getKey();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getdata() {
        progressDialog.show();
        dr1.child("Category_img").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                al.clear();
                al.add("SELECT CATEGORY NAME");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    al.add(ds.getKey());
                }
                ad.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void picupload(View view) {

        Dexter.withActivity(AdminProductAdd.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), code(view));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }


                }).check();
    }

    public void productupload(View view) {
        String sbrand = brand.getText().toString();
        String sname = name.getText().toString();
        String smrp = mrp.getText().toString();
        String sprice = price.getText().toString();
        String sstock = stock.getText().toString();
        String scolor = color.getText().toString();
        String ssize = size.getText().toString();
        String sdescription = description.getText().toString();
        String colorcode = "0";
        category = sp.getSelectedItem().toString();
        if (!TextUtils.isEmpty(sbrand) && !TextUtils.isEmpty(sname) && !TextUtils.isEmpty(smrp) && !TextUtils.isEmpty(sprice) && !TextUtils.isEmpty(sstock) && !TextUtils.isEmpty(scolor) &&
                !TextUtils.isEmpty(ssize) && !TextUtils.isEmpty(sdescription) && imageUri1 != null && imageUri2 != null && imageUri3 != null && imageUri4 != null) {

            uploadProduct(colorcode, key, category, sname, sbrand, smrp, sprice, sstock, ssize, scolor, sdescription, imageUri1, imageUri2, imageUri3, imageUri4);

        } else {
            Toast.makeText(AdminProductAdd.this, "All Details Required", Toast.LENGTH_SHORT).show();
        }


    }

    private void uploadProduct(String colorcode, String key, String category, String sname, String sbrand, String smrp, String sprice, String sstock, String ssize, String scolor, String sdescription, Uri imageUri1, Uri imageUri2, Uri imageUri3, Uri imageUri4) {
        progressDialog.show();
        if (imageUri1 != null && imageUri2 != null && imageUri3 != null && imageUri4 != null) {
            final StorageReference filereference1 = storageReference.child(System.currentTimeMillis() + "." + fileExt(imageUri1));
            final StorageReference filereference2 = storageReference.child(System.currentTimeMillis() + "." + fileExt(imageUri2));
            final StorageReference filereference3 = storageReference.child(System.currentTimeMillis() + "." + fileExt(imageUri3));
            final StorageReference filereference4 = storageReference.child(System.currentTimeMillis() + "." + fileExt(imageUri4));
            final UploadTask uploadTask1 = filereference1.putFile(imageUri1);
            uploadTask1.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(AdminProductAdd.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AdminProductAdd.this, "Product-1 Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                    Task<Uri> urlTask = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            downloadUrl1 = filereference1.getDownloadUrl().toString();
                            return filereference1.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadUrl1 = task.getResult().toString();

                                //image 2
                                final UploadTask uploadTask2 = filereference2.putFile(imageUri2);
                                uploadTask2.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String message = e.toString();
                                        Toast.makeText(AdminProductAdd.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(AdminProductAdd.this, "Product-2 Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                                        Task<Uri> urlTask = uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                            @Override
                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                if (!task.isSuccessful()) {
                                                    throw task.getException();
                                                }

                                                downloadUrl2 = filereference2.getDownloadUrl().toString();
                                                return filereference2.getDownloadUrl();
                                            }
                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    downloadUrl2 = task.getResult().toString();

                                                    //image 3

                                                    final UploadTask uploadTask3 = filereference3.putFile(imageUri3);
                                                    uploadTask3.addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            String message = e.toString();
                                                            Toast.makeText(AdminProductAdd.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                        }
                                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            Toast.makeText(AdminProductAdd.this, "Product-3 Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                                                            Task<Uri> urlTask = uploadTask3.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                @Override
                                                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                    if (!task.isSuccessful()) {
                                                                        throw task.getException();
                                                                    }

                                                                    downloadUrl3 = filereference3.getDownloadUrl().toString();
                                                                    return filereference3.getDownloadUrl();
                                                                }
                                                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Uri> task) {
                                                                    if (task.isSuccessful()) {
                                                                        downloadUrl3 = task.getResult().toString();

                                                                        //image 4

                                                                        final UploadTask uploadTask4 = filereference4.putFile(imageUri4);
                                                                        uploadTask4.addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                String message = e.toString();
                                                                                Toast.makeText(AdminProductAdd.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                                                progressDialog.dismiss();
                                                                            }
                                                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                Toast.makeText(AdminProductAdd.this, "Product-4 Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                                                                                Task<Uri> urlTask = uploadTask4.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                                    @Override
                                                                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                                        if (!task.isSuccessful()) {
                                                                                            throw task.getException();
                                                                                        }

                                                                                        downloadUrl4 = filereference4.getDownloadUrl().toString();
                                                                                        return filereference4.getDownloadUrl();
                                                                                    }
                                                                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Uri> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            downloadUrl4 = task.getResult().toString();

                                                                                            Toast.makeText(AdminProductAdd.this, "All Images Successfully...", Toast.LENGTH_SHORT).show();

                                                                                            ProductData(colorcode, key, category, sbrand, sname, smrp, sprice, ssize, sstock, scolor, sdescription, downloadUrl1, downloadUrl2, downloadUrl3, downloadUrl4);
                                                                                        }
                                                                                    }
                                                                                });


                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });


                                                        }
                                                    });
                                                }
                                            }
                                        });


                                    }
                                });
                            }
                        }
                    });


                }
            });


        }
    }

    // String key=dr.getKey();
    void ProductData(String colorcode, String key, String category, String sbrand, String sname, String smrp, String sprice, String ssize, String sstock, String scolor, String sdescription, String imguri1, String imguri2, String imguri3, String imguri4) {
        String uploadId = dr.push().getKey();

        ProductUpload upload = new ProductUpload(0, 0.0, colorcode, uploadId, category, sbrand, sname, smrp, sprice, sdescription, sstock, scolor, ssize, imguri1, imguri2, imguri3, imguri4);

        dr.child(uploadId).setValue(upload);
        progressDialog.dismiss();

        Toast.makeText(getApplicationContext(), "Product Uploaded", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img1.setImageBitmap(bitmap);
                imageUri1 = data.getData();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img2.setImageBitmap(bitmap);
                imageUri2 = data.getData();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null) {

            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img3.setImageBitmap(bitmap);
                imageUri3 = data.getData();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 4 && resultCode == RESULT_OK && data != null) {

            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img4.setImageBitmap(bitmap);
                imageUri4 = data.getData();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String fileExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public int code(View v) {
        int requestcode = 0;
        switch (v.getId()) {
            case R.id.pick_pic_1:
                requestcode = 1;
                break;
            case R.id.pick_pic_2:
                requestcode = 2;
                break;
            case R.id.pick_pic_3:
                requestcode = 3;
                break;

            case R.id.pick_pic_4:
                requestcode = 4;
                break;
        }
        return requestcode;
    }


}
