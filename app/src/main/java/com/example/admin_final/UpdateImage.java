package com.example.admin_final;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateImage extends AppCompatActivity {
    private final int Req=1;
    Bitmap bitmap;
    ImageView imageView;
    DatabaseReference reference;
    StorageReference storageReference;
    EditText sendsms;
    String downloadUrl="";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_image);
        getSupportActionBar().hide();
        imageView=(ImageView) findViewById(R.id.img_update1);
        sendsms=(EditText) findViewById(R.id.sms_send);
        reference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        pd=new ProgressDialog(this);
    }
    public void updateImg(View view) {
        GelaryOpen();

    }

    private void GelaryOpen() {
        Intent pickImage=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,Req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Req && resultCode ==RESULT_OK){
            Uri uri=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageView.setImageBitmap(bitmap);


        }
    }

    public void updates(View view) {
        if(sendsms.getText().toString().isEmpty()){
            sendsms.setError("Empty");
            sendsms.requestFocus();

        }
        else if (bitmap==null) {
            uploadMessage();

        }
        else {
            uploadImage();

        }
    }

    private void uploadImage() {
        pd.setTitle("Wait");
        pd.setMessage("Processing____________");
        pd.show();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage=baos.toByteArray();
        final StorageReference finalpath;
        finalpath=storageReference.child("Sms").child(finalImage+"jpg");
        final UploadTask uploadTask=finalpath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(UpdateImage.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            finalpath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=String.valueOf(uri);
                                    uploadMessage();
                                    pd.dismiss();

                                }
                            });



                        }
                    });


                }
                else {
                    Toast.makeText(UpdateImage.this,"Something want Wrong",Toast.LENGTH_LONG).show();

                }

            }
        });


    }

    private void uploadMessage() {
        reference =reference.child("Update_notice");
        String title=sendsms.getText().toString();
        final String uniqKey=reference.push().getKey();
        Calendar cal_date=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        String date=dateFormat.format(cal_date.getTime());


        Calendar cal_time=Calendar.getInstance();
        SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm a");
        String time=timeFormat.format(cal_time.getTime());

        NoticeData noticeData=new NoticeData(title,downloadUrl,date,time,uniqKey);
        reference.child(uniqKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(UpdateImage.this,"Upload Sucessfully",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UpdateImage.this,"Something Error",Toast.LENGTH_LONG).show();


            }
        });





    }

    public void home(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}