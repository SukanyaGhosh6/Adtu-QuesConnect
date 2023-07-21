package com.adtu.quesconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private EditText loginemail,loginpassword;
    private Button login;
    private TextView create,passwordreset;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Initializer();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){


            finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = loginemail.getText().toString().trim();
                String userPassword = loginpassword.getText().toString().trim();

                if (TextUtils.isEmpty(userEmail)) {
                    loginemail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(userPassword)) {
                    loginpassword.setError("Password is required");
                    return;
                }
                progressDialog.setMessage("Logging you in...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    EmailVerification();
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this,"logged in failed ",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
                    }
                });
        passwordreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, Password_Reset.class));
            }
        });
            }

    private void Initializer(){
        loginemail = findViewById(R.id.loginemail);
        loginpassword = findViewById(R.id.loginpassword);
        login = findViewById(R.id.loginbutton);
        create = findViewById(R.id.createacc);
        passwordreset = findViewById(R.id.passwordreset);
    }
    private void EmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean verified = firebaseUser.isEmailVerified();
        if(verified){



            finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }else {
            Toast.makeText(LoginActivity.this,"Verify your email first ",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
