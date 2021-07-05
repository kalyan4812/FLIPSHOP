package com.example.flipshop.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flipshop.HomeActivity;
import com.example.flipshop.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AdminLoginActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 101;
    Button googleBtn, loginBtn;
    EditText username, password;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference docRef;

    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        //set id

        googleBtn = findViewById(R.id.googlelink);
        loginBtn = findViewById(R.id.login);
        username = findViewById(R.id.usernameEd);
        password = findViewById(R.id.passwordEd);

        //instance
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = username.getText().toString();
                final String upass = password.getText().toString();

                if (!TextUtils.isEmpty(uname) && !TextUtils.isEmpty(upass)) {

                    docRef = firebaseFirestore.collection("admin").document(uname);

                    if (!(docRef == null)) {
                        docRef.addSnapshotListener(AdminLoginActivity.this, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                String acct_password = value.getString("Password");
                                if (upass.equals(acct_password)) {
                                    Toast.makeText(AdminLoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AdminLoginActivity.this, AdminDashActivity.class);
                                    intent.putExtra("uname", value.getString("Name"));
                                    intent.putExtra("uemail", value.getString("Email"));
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(AdminLoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Please enter Valid Email", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(AdminLoginActivity.this, "Either username or password incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }




    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(AdminLoginActivity.this, HomeActivity.class);
        intent.putExtra("uname", user.getDisplayName());
        intent.putExtra("uemail", user.getEmail());
        startActivity(intent);
    }

    public void OpenSignup(View view) {

        Intent intent = new Intent(AdminLoginActivity.this, AdminSignupActivity.class);
        startActivity(intent);
    }

}
