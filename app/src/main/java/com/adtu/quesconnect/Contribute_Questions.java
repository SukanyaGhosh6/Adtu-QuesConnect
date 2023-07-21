package com.adtu.quesconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.adtu.quesconnect.model.Model_new_datas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Contribute_Questions extends AppCompatActivity {

    AppCompatButton submit;
    TextInputEditText faculty,degree,department,semester,examType,year,subject;
    private Button select;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;

    private static final int pick = 1;
    private Uri pdfuri;
    private StorageTask mUploadTask;
    ProgressDialog progressDialog;
    String displayName;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contribute_questions);

        submit = findViewById(R.id.submit);
        faculty = findViewById(R.id.faculty);
        degree = findViewById(R.id.degree);
        department = findViewById(R.id.department);
        semester = findViewById(R.id.semester);
        examType = findViewById(R.id.examType);
        year = findViewById(R.id.year);
        subject = findViewById(R.id.subject);
        select = findViewById(R.id.selectpdfs);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Free PDFs");
        databaseReference = FirebaseDatabase.getInstance().getReference("Free PDFs");

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                pickPDF();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(faculty.getText().toString())) {
                    faculty.setError("This field can't be empty");
                    return;
                }
                if (TextUtils.isEmpty(degree.getText().toString())) {
                    degree.setError("This field can't be empty");
                    return;
                }
                if (TextUtils.isEmpty(department.getText().toString())) {
                    department.setError("This field can't be empty");
                    return;
                }
                if (TextUtils.isEmpty(semester.getText().toString())) {
                    semester.setError("This field can't be empty");
                    return;
                }
                if (TextUtils.isEmpty(examType.getText().toString())) {
                    examType.setError("This field can't be empty");
                    return;
                }
                if (TextUtils.isEmpty(year.getText().toString())) {
                    year.setError("This field can't be empty");
                    return;
                }
                if (TextUtils.isEmpty(subject.getText().toString())) {
                    subject.setError("This field can't be empty");
                    return;
                }else {
                    uploadFile();
                }
            }
        });
    }
    private void pickPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pick && resultCode == RESULT_OK && data != null) {
            pdfuri = data.getData();
            String uriString = pdfuri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();

            displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplicationContext().getContentResolver().query(pdfuri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }
            select.setText("Selected : "+displayName);
        } else {
            Toast.makeText(Contribute_Questions.this, "Please select a file ", Toast.LENGTH_LONG).show();
        }
    }
    private void uploadFile( ) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading .....");
        progressDialog.setProgress(0);
        progressDialog.show();
        progressDialog.setCancelable(false);
        if (pdfuri != null) {
            StorageReference fileReference = storageReference.child(displayName);
            mUploadTask = fileReference.putFile(pdfuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Contribute_Questions.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();


                            Model_new_datas model = new Model_new_datas();
                            model.setFaculty(faculty.getText().toString());
                            model.setDegree(degree.getText().toString());
                            model.setDepartment(department.getText().toString());
                            model.setSemester(semester.getText().toString());
                            model.setExamType(examType.getText().toString());
                            model.setYear(year.getText().toString());
                            model.setSubject(subject.getText().toString());
                            model.setPdfurl(downloadUrl.toString());


                            String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                            String currentTime = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());

                            FirebaseDatabase.getInstance().getReference("Contributions").child(FirebaseAuth.getInstance().getUid()).child(currentDate+currentTime).setValue(model)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Contribute_Questions.this, "Submitted", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Contribute_Questions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            int current = (int)(100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressDialog.setProgress(current);
                            if(current == 100){
                                progressDialog.dismiss();
                                Toast.makeText(Contribute_Questions.this, "Upload successful", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        } else {


            Model_new_datas model = new Model_new_datas();
            model.setFaculty(faculty.getText().toString());
            model.setDegree(degree.getText().toString());
            model.setDepartment(department.getText().toString());
            model.setSemester(semester.getText().toString());
            model.setExamType(examType.getText().toString());
            model.setYear(year.getText().toString());
            model.setSubject(subject.getText().toString());


            String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());

            FirebaseDatabase.getInstance().getReference("Contributions").child(FirebaseAuth.getInstance().getUid()).child(currentDate+currentTime).setValue(model)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(Contribute_Questions.this, "Submitted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
    }
}