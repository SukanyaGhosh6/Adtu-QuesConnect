package com.adtu.quesconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.adtu.quesconnect.adapter.AdapterCourse;
import com.adtu.quesconnect.model.ModelCourses;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class New_Data_PDFs extends AppCompatActivity
        implements AdapterCourse.OnItemClickListener{

    private RecyclerView courselist;

    private List<ModelCourses> coursesList;
    AdapterCourse adapterCourseList;
    ValueEventListener valueEventListener;
    String KEY,KEY2,KEY3,KEY4,KEY5,KEY6,KEY7;
    int counter;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_data_pdfs);

        Bundle bundle = getIntent().getExtras();
        KEY = bundle.getString("KEY");
        KEY2 = bundle.getString("KEY2");
        KEY3 = bundle.getString("KEY3");
        KEY4 = bundle.getString("KEY4");
        KEY5 = bundle.getString("KEY5");
        KEY6 = bundle.getString("KEY6");
        databaseReference  = FirebaseDatabase.getInstance().getReference("QuesConnect").child(KEY).child(KEY2).child(KEY3).child(KEY4).child(KEY5).child(KEY6);

        courselist = findViewById(R.id.courselist);
        courselist.setHasFixedSize(true);
        courselist.setLayoutManager(new LinearLayoutManager(this));
        coursesList = new ArrayList<>();
        adapterCourseList = new AdapterCourse(New_Data_PDFs.this,coursesList);
        courselist.setAdapter(adapterCourseList);
        adapterCourseList.setOnItemClickListener(New_Data_PDFs.this);
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coursesList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ModelCourses upload = postSnapshot.getValue(ModelCourses.class);
                    upload.setKey(postSnapshot.getKey());
                    coursesList.add(upload);
                }
                adapterCourseList.notifyDataSetChanged();
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
    public void onNItemClick(int position) {

        String title,description,duration,price,discount,tax,key;
        title = coursesList.get(position).getName();
        description = coursesList.get(position).getDescription();
        duration = coursesList.get(position).getDuration();
        price = coursesList.get(position).getPrice();
        discount = coursesList.get(position).getDiscount();
        tax = coursesList.get(position).getTax();
        key = coursesList.get(position).getKey();

        if(price != null){
            Intent intent = new Intent(getApplicationContext(),Course.class);
            intent.putExtra("title",title);
            intent.putExtra("description",description);
            intent.putExtra("duration",duration);
            intent.putExtra("price",price);
            intent.putExtra("discount",discount);
            intent.putExtra("tax",tax);
            intent.putExtra("key",key);
            intent.putExtra("SCREEN","NEW");

            intent.putExtra("KEY",KEY);
            intent.putExtra("KEY2",KEY2);
            intent.putExtra("KEY3",KEY3);
            intent.putExtra("KEY4",KEY4);
            intent.putExtra("KEY5",KEY5);
            intent.putExtra("KEY6",KEY6);

            startActivity(intent);
        }if(price == null){
            Intent intent = new Intent(getApplicationContext(),Course.class);
            intent.putExtra("title",title);
            intent.putExtra("description",description);
            intent.putExtra("duration",duration);
            intent.putExtra("price","0");
            intent.putExtra("discount","0");
            intent.putExtra("tax","0");
            intent.putExtra("key",key);
            intent.putExtra("SCREEN","NEW");

            intent.putExtra("KEY",KEY);
            intent.putExtra("KEY2",KEY2);
            intent.putExtra("KEY3",KEY3);
            intent.putExtra("KEY4",KEY4);
            intent.putExtra("KEY5",KEY5);
            intent.putExtra("KEY6",KEY6);

            startActivity(intent);
        }

    }
}