package com.project.autopak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    Spinner spinner;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listId = new ArrayList<>();
    String name,categoryId,price,email,companyName,imageUrl;
    EditText edtname,edtprice;
    ImageView imageView;
    boolean isImageChoosed = false;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    // Uri indicates, where the image will be picked from
    private Uri filePath;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        //to show back button on top
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = findViewById(R.id.spinner);
        edtname = findViewById(R.id.nameofitem);
        edtprice = findViewById(R.id.pricefitem);
        imageView = findViewById(R.id.imageview);
        Button btnAdd = findViewById(R.id.btn_add);

        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        companyName = sh.getString("name", "");
        email = sh.getString("email", "");



        list.add("Choose Category");
        listId.add("0");
        if(getIntent().getStringExtra("id")!=null){
            name =  getIntent().getStringExtra("name");
            id =  getIntent().getStringExtra("id");
            imageUrl =  getIntent().getStringExtra("imageURL");
            price =  getIntent().getStringExtra("price");
             edtprice.setText(price);
                edtname.setText(name);


            Glide.with(AddProductActivity.this).load(imageUrl).into(imageView);
            TextView tv = findViewById(R.id.textView);
            tv.setText("Update Product");
            btnAdd.setText("Update");


        }


        FirebaseDatabase.getInstance().getReference().child("Products").child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String name = ds.child("name").getValue(String.class);
                    String id = ds.child("id").getValue(String.class);
                    list.add(name);
                    listId.add(id);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddProductActivity.this, android.R.layout.simple_spinner_dropdown_item
                        ,list);
                spinner.setAdapter(adapter);
                if(getIntent().getStringExtra("categoryId")!=null){
                    String  categoryId = getIntent().getStringExtra("categoryId");
                    int category_id = Integer.parseInt(categoryId);
                //    category_id--;
                spinner.setSelection(category_id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProducts();
            }
        });

Button btnChoose = findViewById(R.id.btn_choose);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 200);
            }
        });

       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               categoryId = listId.get(position);
               ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });


        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




    }

    private void AddProducts() {
    name = edtname.getText().toString();
    price = edtprice.getText().toString();
    if(name.isEmpty()){
        edtname.setError("Enter Name Of Item");
    }
    else if(price.isEmpty()){
        edtprice.setError("Enter Price Of Item");
    }
    else if(categoryId.equals("0")){
        Toast.makeText(this, "Choose Category", Toast.LENGTH_SHORT).show();
    }
    else if(!isImageChoosed && getIntent().getStringExtra("id")==null){
        Toast.makeText(this, "Choose Image", Toast.LENGTH_SHORT).show();
    }
    else{

        uploadImage();

    }

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                // Get the Uri of data
                filePath = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(filePath);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
                isImageChoosed = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddProductActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(AddProductActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null && getIntent().getStringExtra("id") !=null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "items/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {


                                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                imageUrl = task.getResult().toString();
                                                HashMap<String,Object> map = new HashMap<>();
                                                map.put("categoryid",categoryId);
                                                map.put("imageUrl",imageUrl);
                                                map.put("name",name);
                                                map.put("price",price);
                                                FirebaseDatabase.getInstance().getReference().child("Products").child("items").child(id).updateChildren(map);
                                                // Image uploaded successfully
                                                // Dismiss dialog
                                                progressDialog.dismiss();
                                                Toast
                                                        .makeText(AddProductActivity.this,
                                                                "Item Updated!!",
                                                                Toast.LENGTH_SHORT)
                                                        .show();

                                                finish();



                                            } else {
                                                // Handle failures
                                            }
                                        }
                                    });

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddProductActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }

                            });
        }

       else if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "items/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {


                                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                imageUrl = task.getResult().toString();
                                                HashMap<String,String> map = new HashMap<>();
                                                map.put("categoryid",categoryId);
                                                map.put("companyName",companyName);
                                                map.put("imageUrl",imageUrl);
                                                map.put("name",name);
                                                map.put("price",price);
                                                map.put("email",email);
                                                FirebaseDatabase.getInstance().getReference().child("Products").child("items").push().setValue(map);
                                                // Image uploaded successfully
                                                // Dismiss dialog
                                                progressDialog.dismiss();
                                                Toast
                                                        .makeText(AddProductActivity.this,
                                                                "New Item Added!!",
                                                                Toast.LENGTH_SHORT)
                                                        .show();

                                                finish();



                                            } else {
                                                // Handle failures
                                            }
                                        }
                                    });

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddProductActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }

    else if(imageUrl!=null){
            HashMap<String,Object> map = new HashMap<>();
            map.put("categoryid",categoryId);
            map.put("name",name);
            map.put("price",price);
            FirebaseDatabase.getInstance().getReference().child("Products").child("items").child(id).updateChildren(map);
            // Image uploaded successfully
            // Dismiss dialog
            Toast
                    .makeText(AddProductActivity.this,
                            "Item Updated!!",
                            Toast.LENGTH_SHORT)
                    .show();

            finish();

        }
    }

    //to go back previous activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

}