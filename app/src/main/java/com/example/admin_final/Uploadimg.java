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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class Uploadimg extends AppCompatActivity {
    Spinner spinner;

    ImageView gal_imageView;
    String category;
    Bitmap bitmap;
    DatabaseReference reference;
    StorageReference storageReference;
    String downloadUrl="";
    final int Req=1;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimg);
        getSupportActionBar().hide();
        reference= FirebaseDatabase.getInstance().getReference().child("galary");
        storageReference= FirebaseStorage.getInstance().getReference().child("galary");
        spinner=(Spinner) findViewById(R.id.spinning);
        gal_imageView=(ImageView) findViewById(R.id.img_galary_upt);
        pd=new ProgressDialog(this);
        String[] items=new String[]{"Select Category","Pravupad","GuruMoharaj","others"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void uploadImg(View view) {
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
            gal_imageView.setImageBitmap(bitmap);


        }
    }

    public void updatesImage(View view) {
        if(bitmap==null){
            Toast.makeText(getApplicationContext(),"Upload Image",Toast.LENGTH_LONG).show();
        } else if (category.equals("Select Category")) {
            Toast.makeText(getApplicationContext(),"Updatecategory",Toast.LENGTH_LONG).show();

        }
        else {
            pd.setTitle("Loading");
            pd.setMessage("Processing___________________________________");
            upload();

        }
    }

    private void upload() {
        pd.show();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage=baos.toByteArray();
        final StorageReference finalpath;
        finalpath=storageReference.child(finalImage+"jpg");
        final UploadTask uploadTask=finalpath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(Uploadimg.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(Uploadimg.this,"Something want Wrong",Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void uploadMessage() {
        reference=reference.child(category);
        final String uniqKey=reference.push().getKey();
        imageUpdate imageUpdate=new imageUpdate(downloadUrl);
        reference.child(uniqKey).setValue(imageUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Something wants wrong",Toast.LENGTH_LONG).show();


            }
        });
    }

    public void home(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}