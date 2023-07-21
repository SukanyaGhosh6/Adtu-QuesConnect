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

import com.adtu.quesconnect.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText caemail,capassword,caname,caphone;    //ca stands for create account activity
    private Button create;
    private TextView login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
 //   private String name,email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Initializer();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String  name = caname.getText().toString().trim();
               final String  email = caemail.getText().toString().trim();
               String  password = capassword.getText().toString().trim();
                final String  phone = caphone.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    caemail.setError("Email is required");
                }
                if(TextUtils.isEmpty(password)){
                    capassword.setError("Password is required");
                }
                if(password.length() < 6){
                    capassword.setError("Password must be more 6 characters");
                }
                progressDialog.setMessage("Registering  ...");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    String uidtest = String.valueOf(firebaseAuth.getUid());

                                    User user = new User(
                                            name,email,phone,uidtest
                                    );
                             /*       FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user);  */
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(firebaseAuth.getUid()))
                                            .setValue(user);
                                    Verification();

                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateAccountActivity.this,"Error occurred",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }) ;

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
            }
        });
    }
    private void Initializer(){

        caemail = findViewById(R.id.caemail);
        capassword = findViewById(R.id.capassword);
        create = findViewById(R.id.create);
        login = findViewById(R.id.calogin);
        caname = findViewById(R.id.caname);
        caphone = findViewById(R.id.caphone);
    }
    private void Verification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CreateAccountActivity.this,"Verification mail has been sent",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                    }else {
                        Toast.makeText(CreateAccountActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}