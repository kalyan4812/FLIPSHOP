package com.example.flipshop.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flipshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText name_ed, email_ed, password_ed, confirm_pass_ed;
    Button signupBtn;

    FirebaseAuth fAuth, firebaseAuth;
    FirebaseFirestore fstore;
    DocumentReference docRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //set ids
        name_ed=findViewById(R.id.usernameEd);
        email_ed=findViewById(R.id.emailEd);
        password_ed=findViewById(R.id.passwordEd);
        confirm_pass_ed=findViewById(R.id.confirmpasswordEd);
        signupBtn=findViewById(R.id.signup);


        //firebase instance
        fAuth=FirebaseAuth.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name_ed.getText().toString().equals("") && !email_ed.getText().toString().equals("") && !password_ed.getText().toString().equals("") && !confirm_pass_ed.getText().toString().equals("")){

                    String pass=password_ed.getText().toString();
                    String confirmpass=confirm_pass_ed.getText().toString();

                    String name_s=name_ed.getText().toString();
                    String email_s=email_ed.getText().toString();

                    if (pass.equals(confirmpass)){

                        Toast.makeText(SignupActivity.this, "All correct", Toast.LENGTH_SHORT).show();
                        progressDialog.show();
                        //firebase program to create account
                        docRef=fstore.collection("users").document(email_s);
                        UploadData(name_s,email_s,pass);


                    }else {
                        confirm_pass_ed.setError("Password Mismatched");
                        progressDialog.dismiss();
                    }


                }
                else{
                    Toast.makeText(SignupActivity.this, "All fields required. Please Fill the details", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void UploadData(String name_s, String email_s, String pass) {
        Map<String,Object> user=new HashMap<>();
        user.put("Name", name_s);
        user.put("Email", email_s);
        user.put("Password", pass);
        docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignupActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(SignupActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void OpenLogin(View view) {
        Intent intent=new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }



}
