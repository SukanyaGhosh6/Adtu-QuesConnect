package com.adtu.quesconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.adtu.quesconnect.adapter.AdapterPDF;
import com.adtu.quesconnect.model.ModelC;
import com.adtu.quesconnect.model.ModelPDF;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course extends AppCompatActivity implements AdapterPDF.OnItemClickListener, PaymentResultListener {

    private TextView title,title1,description,duration,price,discount,tax,totalamount;
    private Button buy;
    private RecyclerView pdflist;
    int p,d,t,finalamount;

    private ScrollView l1,l3;
    private RelativeLayout l2;
    private LinearLayout razorpay,paytm;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;

    private AdapterPDF adapterClassListfirst;
    private List<ModelPDF> mUploads;
    private ValueEventListener mDBListener;

    private String f;
    private TextView amount,name;

    private FirebaseAuth firebaseAuth;
    private String key,dtitle,ddescription,SCREEN;

    public static final String PAYTM_PACKAGE_NAME = "net.one97.paytm";

    private Uri uri;
    private String approvalRefNo;
    private String status;
    String KEY,KEY2,KEY3,KEY4,KEY5,KEY6,KEY7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);
        Initializer();
        l1 = findViewById(R.id.l1);
        l3 = findViewById(R.id.l3);
        l2 = findViewById(R.id.l2);
        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();

        dtitle = bundle.getString("title");
        ddescription = bundle.getString("description");
        String dduration = bundle.getString("duration");
        final String dprice = bundle.getString("price");
        String ddiscount = bundle.getString("discount");
        String dtax = bundle.getString("tax");
        key = bundle.getString("key");
        SCREEN = bundle.getString("SCREEN");

        title.setText(String.valueOf(dtitle));
        title1.setText(String.valueOf(dtitle));
        description.setText(String.valueOf(ddescription));
        duration.setText(String.valueOf(dduration));
        price.setText(String.valueOf(dprice));
        discount.setText(String.valueOf(ddiscount));
        tax.setText(String.valueOf(dtax));


            p = Integer.parseInt(dprice);
            d = Integer.parseInt(ddiscount);
            t = Integer.parseInt(dtax);

        if(p == 0){
            price.setText("0");
            discount.setText("0");
            tax.setText("0");
            totalamount.setText("Free ");
        }else {
            finalamount = (p+t)-d;
            f = String.valueOf(finalamount);

            totalamount.setText(f);
        }


            amount.setText(f);
            name.setText(dtitle);


        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PRICE____________",dprice);
                if(dprice == null || dprice.equals("0")){
                    l1.setVisibility(View.INVISIBLE);
                    l1.setEnabled(false);
                    l3.setVisibility(View.VISIBLE);
                    l3.setEnabled(true);
                }else {


                    l1.setVisibility(View.INVISIBLE);
                    l1.setEnabled(false);
                    l2.setVisibility(View.VISIBLE);
                    l2.setEnabled(true);
                }
            }
        });

        razorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartPayment(dtitle,f);

            }
        });
     //******************************* Layout 2
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Courses").child(key).child("PDFs");
        if(SCREEN.equals("HOME")){
            databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(key).child("PDFs");
        }else {
            KEY = bundle.getString("KEY");
            KEY2 = bundle.getString("KEY2");
            KEY3 = bundle.getString("KEY3");
            KEY4 = bundle.getString("KEY4");
            KEY5 = bundle.getString("KEY5");
            KEY6 = bundle.getString("KEY6");
            databaseReference  = FirebaseDatabase.getInstance().getReference("QuesConnect").child(KEY).child(KEY2).child(KEY3).child(KEY4).child(KEY5).child(KEY6).child(key).child("PDFs");
  //          databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(key).child("PDFs");
        }


        pdflist.setHasFixedSize(true);
        pdflist.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        adapterClassListfirst = new AdapterPDF(Course.this,mUploads);
        pdflist.setAdapter(adapterClassListfirst);
        adapterClassListfirst.setOnItemClickListener(Course.this);
        mDBListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ModelPDF upload = postSnapshot.getValue(ModelPDF.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                adapterClassListfirst.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Course.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  UpiId =  "6000694368@paytm";  // Paytm UPID
                String msgNote = "PDF";
                uri = getUpiPaymentUri(UpiId,msgNote,f);
                payWithGpay(PAYTM_PACKAGE_NAME,1);
            }
        });

       /* ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/

    }
    private void payWithGpay(String paytmPackageName,int rcode) {
        if(isAppInstalled(this,paytmPackageName,rcode)){

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(paytmPackageName);
            startActivityForResult(intent,rcode);

        }
        else{
            Toast.makeText(getApplicationContext(),"Payment app is not installed. Please install and try again.", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isAppInstalled(Context context, String paytmPackageName,int rcode) {
        try{
            context.getPackageManager().getApplicationInfo(paytmPackageName,rcode);
            return true;
        }catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }

    private Uri getUpiPaymentUri(String upiId,String note,String finalMyValue) {
        return  new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("tn",note)
                .appendQueryParameter("am",finalMyValue)
                .appendQueryParameter("cu","INR")
                .build();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
            approvalRefNo = data.getStringExtra("txnRef");
        }
        if ((RESULT_OK == resultCode) && status.equals("success")) {
            Toast.makeText(getApplicationContext(), "Transaction successful. "+approvalRefNo, Toast.LENGTH_SHORT).show();
            //   msg.setText("Transaction successful of ₹" + finalMyValue);
            //   msg.setTextColor(Color.GREEN);
            // ******** Send payment money record to database ****************************
            l3.setVisibility(View.VISIBLE);
            l3.setEnabled(true);
            l1.setVisibility(View.INVISIBLE);
            l1.setEnabled(false);
            SendBuyRecord();
        }

        else{
            Toast.makeText(getApplicationContext(), "Transaction cancelled or failed please try again.", Toast.LENGTH_SHORT).show();
            //  msg.setText("Transaction Failed of ₹" + finalMyValue);
            //  msg.setTextColor(Color.RED);

        }


    }
    @Override
    public void onBackPressed() {

        if(l1.isEnabled()){
            finish();
        }if(l2.isEnabled()){
            l1.setVisibility(View.VISIBLE);
            l1.setEnabled(true);
            l2.setVisibility(View.INVISIBLE);
            l2.setEnabled(false);
        }if(l3.isEnabled()){
            l1.setVisibility(View.VISIBLE);
            l1.setEnabled(true);
            l3.setVisibility(View.INVISIBLE);
            l3.setEnabled(false);
        }
    }

    private void Initializer(){
        title = findViewById(R.id.title);
        title1 = findViewById(R.id.title1);
        duration = findViewById(R.id.duration);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        discount = findViewById(R.id.discount);
        tax = findViewById(R.id.tax);
        pdflist = findViewById(R.id.pdflist);
        buy = findViewById(R.id.buy);
        totalamount = findViewById(R.id.totalamount);
        razorpay = findViewById(R.id.razorpay);
        paytm = findViewById(R.id.paytm);
        amount  =findViewById(R.id.amount);
        name = findViewById(R.id.name);
    }

    @Override
    public void onItemClick(int position) {
        String url = mUploads.get(position).getPdfurl();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setPackage(null);
            startActivity(intent);
        }
    }

    public void StartPayment(String filename,String amt) {
        int amount = Integer.parseInt(amt);

        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        // checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: Rentomojo || HasGeek etc.
             */
            options.put("name", "ADTU QUEST");

            options.put("description", "PDF : "+filename);

            options.put("currency", "INR");

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            options.put("amount", amount+"00");

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Transaction Failed : "+e , Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), "Transaction Successful" , Toast.LENGTH_LONG).show();
        l3.setVisibility(View.VISIBLE);
        l3.setEnabled(true);
        l1.setVisibility(View.INVISIBLE);
        l1.setEnabled(false);
        SendBuyRecord();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.i("RAZORPAY_ERROR",s);
        Toast.makeText(getApplicationContext(), "Transaction Failed : "+s , Toast.LENGTH_LONG).show();
    }
    private void SendBuyRecord(){

        ModelC c = new ModelC();
        c.setK(key);
        c.setX(key);
        c.setT(dtitle);
        c.setDes(ddescription);
        FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Courses").push()
                .setValue(c);
    }
}

