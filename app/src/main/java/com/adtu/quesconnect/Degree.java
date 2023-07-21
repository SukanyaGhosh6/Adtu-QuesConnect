package com.adtu.quesconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.adtu.quesconnect.adapter.AdapterFaculties;
import com.adtu.quesconnect.model.Model_new_datas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Degree extends AppCompatActivity implements AdapterFaculties.OnItemClickListener {

    RecyclerView recyclerView;
    private List<Model_new_datas> list;
    AdapterFaculties adapter;
    ValueEventListener eventListener;
    String KEY,KEY2,KEY3,KEY4,KEY5,KEY6,KEY7;
    int counter;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.degree);
        recyclerView = findViewById(R.id.faculties);

        Bundle bundle = getIntent().getExtras();
        counter = Integer.parseInt(bundle.getString("counter"));

        if(counter==1){
            KEY = bundle.getString("KEY");
            databaseReference  = FirebaseDatabase.getInstance().getReference("QuesConnect").child(KEY);
        }else if(counter == 2){
            KEY = bundle.getString("KEY");
            KEY2 = bundle.getString("KEY2");
            databaseReference  = FirebaseDatabase.getInstance().getReference("QuesConnect").child(KEY).child(KEY2);
        }else if(counter == 3){
            KEY = bundle.getString("KEY");
            KEY2 = bundle.getString("KEY2");
            KEY3 = bundle.getString("KEY3");
            databaseReference  = FirebaseDatabase.getInstance().getReference("QuesConnect").child(KEY).child(KEY2).child(KEY3);
        }else if(counter == 4){
            KEY = bundle.getString("KEY");
            KEY2 = bundle.getString("KEY2");
            KEY3 = bundle.getString("KEY3");
            KEY4 = bundle.getString("KEY4");
            databaseReference  = FirebaseDatabase.getInstance().getReference("QuesConnect").child(KEY).child(KEY2).child(KEY3).child(KEY4);
        }else if(counter == 5){
            KEY = bundle.getString("KEY");
            KEY2 = bundle.getString("KEY2");
            KEY3 = bundle.getString("KEY3");
            KEY4 = bundle.getString("KEY4");
            KEY5 = bundle.getString("KEY5");
            databaseReference  = FirebaseDatabase.getInstance().getReference("QuesConnect").child(KEY).child(KEY2).child(KEY3).child(KEY4).child(KEY5);
        }else if(counter == 6){
            KEY = bundle.getString("KEY");
            KEY2 = bundle.getString("KEY2");
            KEY3 = bundle.getString("KEY3");
            KEY4 = bundle.getString("KEY4");
            KEY5 = bundle.getString("KEY5");
            KEY6 = bundle.getString("KEY6");
            databaseReference  = FirebaseDatabase.getInstance().getReference("QuesConnect").child(KEY).child(KEY2).child(KEY3).child(KEY4).child(KEY5).child(KEY6);
        }




        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new AdapterFaculties(Degree.this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(Degree.this);
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model_new_datas upload = postSnapshot.getValue(Model_new_datas.class);
                    upload.setKey(postSnapshot.getKey());
                    list.add(upload);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onFacultyItemClick(int position) {

        if(counter==1){
            Intent intent = new Intent(getApplicationContext(),Degree.class);
            intent.putExtra("KEY",KEY);
            intent.putExtra("KEY2",list.get(position).getKey());
            intent.putExtra("counter", String.valueOf(counter+1));
            startActivity(intent);
        }else if(counter == 2){
            Intent intent = new Intent(getApplicationContext(),Degree.class);
            intent.putExtra("KEY",KEY);
            intent.putExtra("KEY2",KEY2);
            intent.putExtra("KEY3",list.get(position).getKey());
            intent.putExtra("counter", String.valueOf(counter+1));
            startActivity(intent);
        }else if(counter == 3){
            Intent intent = new Intent(getApplicationContext(),Degree.class);
            intent.putExtra("KEY",KEY);
            intent.putExtra("KEY2",KEY2);
            intent.putExtra("KEY3",KEY3);
            intent.putExtra("KEY4",list.get(position).getKey());
            intent.putExtra("counter", String.valueOf(counter+1));
            startActivity(intent);
        }else if(counter == 4){
            Intent intent = new Intent(getApplicationContext(),Degree.class);
            intent.putExtra("KEY",KEY);
            intent.putExtra("KEY2",KEY2);
            intent.putExtra("KEY3",KEY3);
            intent.putExtra("KEY4",KEY4);
            intent.putExtra("KEY5",list.get(position).getKey());
            intent.putExtra("counter", String.valueOf(counter+1));
            startActivity(intent);
        }else if(counter == 5){
            Intent intent = new Intent(getApplicationContext(),New_Data_PDFs.class);
            intent.putExtra("KEY",KEY);
            intent.putExtra("KEY2",KEY2);
            intent.putExtra("KEY3",KEY3);
            intent.putExtra("KEY4",KEY4);
            intent.putExtra("KEY5",KEY5);
            intent.putExtra("KEY6",list.get(position).getKey());
            intent.putExtra("counter", String.valueOf(counter+1));
            startActivity(intent);
        }
    }
}