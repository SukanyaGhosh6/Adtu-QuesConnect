package com.adtu.quesconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;

import com.adtu.quesconnect.adapter.AdapterCourse;
import com.adtu.quesconnect.adapter.AdapterFaculties;
import com.adtu.quesconnect.model.ModelCourses;
import com.adtu.quesconnect.model.ModelHotTopic;
import com.adtu.quesconnect.model.Model_new_datas;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity
        implements AdapterCourse.OnItemClickListener,AdapterFaculties.OnItemClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private ImageView hottopic;


    private String position;
    private String cat;


    private ShimmerFrameLayout shimmer;

    private ScrollView l2;
    private  RecyclerView courselist;

    private List<ModelCourses> coursesList;
    AdapterCourse adapterCourseList;
    ValueEventListener valueEventListener;

    RecyclerView recyclerView;
    private List<Model_new_datas> list;
    AdapterFaculties adapter;
    ValueEventListener eventListener;
    ArrayList<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        hottopic = findViewById(R.id.hottopic);
        courselist = findViewById(R.id.courselist);
        recyclerView = findViewById(R.id.faculties);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        shimmer = findViewById(R.id.shimmer);




        databaseReference = FirebaseDatabase.getInstance().getReference("Courses");
        courselist = findViewById(R.id.courselist);
        courselist.setHasFixedSize(true);
        courselist.setLayoutManager(new LinearLayoutManager(this));
        coursesList = new ArrayList<>();
        adapterCourseList = new AdapterCourse(HomeActivity.this,coursesList);
        courselist.setAdapter(adapterCourseList);
        adapterCourseList.setOnItemClickListener(HomeActivity.this);
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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new AdapterFaculties(HomeActivity.this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(HomeActivity.this);
        eventListener = FirebaseDatabase.getInstance().getReference("QuesConnect").addValueEventListener(new ValueEventListener() {
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


        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Hot Topic").child("Hot Topic");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelHotTopic user = snapshot.getValue(ModelHotTopic.class);

                if (user != null) {
                    // title.setText(user.getTitle());
                    position = user.getPos();
                    cat = user.getCat();
                    Picasso.get().load(user.getImageUrl()).into(hottopic);
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.INVISIBLE);
                    l2 = findViewById(R.id.l2);
                    l2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ImageView dots = findViewById(R.id.dots);
        dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        PopupMenu popup = new PopupMenu(HomeActivity.this, view);
                        for (String item : items) {
                            popup.getMenu().add(Menu.NONE, items.indexOf(item), items.indexOf(item), item);
                        }
                        popup.getMenu().add(Menu.NONE, items.size(), items.size(), "Logout");

                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int itemId = item.getItemId();

                                if (itemId == items.size()) {
                                    firebaseAuth.signOut();
                                    finish();
                                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                                    return true;
                                }
                                return true;
                            }
                        });

            }
        });

        AppCompatButton contribute = findViewById(R.id.contribute);
        contribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Contribute_Questions.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
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
            intent.putExtra("SCREEN","HOME");
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
            intent.putExtra("SCREEN","HOME");
            startActivity(intent);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        shimmer.startShimmer();
    }

    @Override
    public void onFacultyItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(),Degree.class);
        intent.putExtra("KEY",list.get(position).getKey());
        intent.putExtra("counter","1");
        startActivity(intent);
    }
}
//************************************************** Quiz sets


