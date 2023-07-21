package com.adtu.quesconnect.adapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adtu.quesconnect.HomeActivity;
import com.adtu.quesconnect.R;
import com.adtu.quesconnect.model.ModelC;
import com.adtu.quesconnect.model.ModelCourses;
import com.adtu.quesconnect.model.ModelPDF;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyCourses extends AppCompatActivity implements AdapterC.OnItemClickListener,AdapterPDF.OnItemClickListener{

    private   RecyclerView courselist;
    private  DatabaseReference databaseReference;
    private  FirebaseDatabase firebaseDatabase;
    private  FirebaseDatabase fbd;


    private ModelCourses modelClass1;
    private AdapterC adapterClassListfirst;
    private List<ModelC> mUploads;
    private ValueEventListener mDBListener;

    private AdapterPDF adapterPDF;
    private List<ModelPDF> list;
    private ValueEventListener listener;
    private  FirebaseAuth firebaseAuth;

    private   RecyclerView pdflist;

    private  LinearLayout l1,l2;
    private  TextView name,Description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_courses);
        courselist = findViewById(R.id.rviown);
        name = findViewById(R.id.name);
        Description = findViewById(R.id.description);
        pdflist = findViewById(R.id.pdflist);
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Courses");

        courselist.setHasFixedSize(true);
        courselist.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        adapterClassListfirst = new AdapterC(MyCourses.this,mUploads);
        courselist.setAdapter(adapterClassListfirst);
        adapterClassListfirst.setOnItemClickListener(MyCourses.this);
        mDBListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ModelC upload = postSnapshot.getValue(ModelC.class);
                    upload.setK(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                adapterClassListfirst.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyCourses.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(l1.isEnabled()){
            finish();
            startActivity(new Intent(MyCourses.this, HomeActivity.class));
        }
        if(l2.isEnabled()){
            l2.setVisibility(View.INVISIBLE);
            l2.setEnabled(false);
            l1.setVisibility(View.VISIBLE);
            l1.setEnabled(true);

        }
    }
    @Override
    public void onNItemClick(int position) {
        l1.setVisibility(View.INVISIBLE);
        l1.setEnabled(false);
        l2.setVisibility(View.VISIBLE);
        l2.setEnabled(true);

        String t,x,des;


        t = mUploads.get(position).getT();
        x = mUploads.get(position).getX();
        des = mUploads.get(position).getDes();

     //   Log.d("HERE_YOU_HAVE_IT____",duration);
       // Log.d("HERE_YOU_HAVE_IT____",description);



        name.setText(t);
        Description.setText(des);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Courses").child(x).child("PDFs");
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Courses").child(x).child("PDFs");

        pdflist.setHasFixedSize(true);
        pdflist.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapterPDF = new AdapterPDF(MyCourses.this,list);
        pdflist.setAdapter(adapterPDF);
        adapterPDF.setOnItemClickListener(MyCourses.this);
        listener = dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ModelPDF upload = postSnapshot.getValue(ModelPDF.class);
                    upload.setKey(postSnapshot.getKey());
                    list.add(upload);
                }
                adapterPDF.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyCourses.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        String url = list.get(position).getPdfurl();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setPackage(null);
            startActivity(intent);
        }    }
}

