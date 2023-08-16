package com.project.autopak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.autopak.Checkout.CheckoutActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class DetailsActivity extends AppCompatActivity {


    String name,id,imageUrl,price,company,email,categoryName,categoryId,companyContact,companyAddress,type;
    String loggedEmail,dateTime,loggedName,buyerAddress,buyerContact;
    TextView tvname,tvprice,tvcompany,tvaddress,tvcontact;
    ImageView imageView;

    TextView tvquantity;
    ImageView imgplus,imgminus;
    int value = 1;
    int p;

    LinearLayout linear_order,linear_edit_delete;

    Button btn_edit,btn_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        loggedEmail = sh.getString("email", "");
        loggedName = sh.getString("name", "");
        type = sh.getString("type", "");
        buyerAddress = sh.getString("address", "");
        buyerContact = sh.getString("contact", "");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name =  getIntent().getStringExtra("name");
        id =  getIntent().getStringExtra("id");
        imageUrl =  getIntent().getStringExtra("imageURL");
        price =  getIntent().getStringExtra("price");
        company =  getIntent().getStringExtra("company");
        email =  getIntent().getStringExtra("email");
        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");
        if(categoryName.equals("catname")){
            FirebaseDatabase.getInstance().getReference().child("Products").child("Categories").child(categoryId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    categoryName = snapshot.child("name").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        companyContact = getIntent().getStringExtra("CompanyContact");
        companyAddress = getIntent().getStringExtra("CompanyAddress");

        tvname = findViewById(R.id.itemName);
        tvprice = findViewById(R.id.price);
        tvcompany = findViewById(R.id.companyName);
        tvcontact = findViewById(R.id.companyContact);
        tvaddress = findViewById(R.id.companyAddress);
        tvquantity = findViewById(R.id.quantity);

        imageView = findViewById(R.id.image);

        tvname.setText(name);
        tvcompany.setText(company);
        tvaddress.setText(companyAddress);
        tvcontact.setText(companyContact);
        tvprice.setText(price);
        tvquantity.setText(String.valueOf(value));

        p = Integer.parseInt(price);

        imgminus = findViewById(R.id.minus);
        imgminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value>1){
                    value--;
                    int pc = p*value;
                    tvquantity.setText(String.valueOf(value));
                    tvprice.setText(String.valueOf(pc));
                }
            }
        });
        imgplus = findViewById(R.id.add);
        imgplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value<=100){
                    value++;
                   int pc = p*value;
                    tvquantity.setText(String.valueOf(value));
                    tvprice.setText(String.valueOf(pc));
                }
            }
        });

        Glide.with(this).load(imageUrl).into(imageView);

        Button btnOrder = findViewById(R.id.btnorder);
        linear_edit_delete = findViewById(R.id.linear_edit_delete);
        linear_order = findViewById(R.id.linear_order);
        if(type.equals("Wholeseller")){
            if(type.equals("Wholeseller") && loggedName.equals(company)){
                linear_edit_delete.setVisibility(View.VISIBLE);
                linear_order.setVisibility(View.GONE);
            }
            else{
                linear_edit_delete.setVisibility(View.GONE);
                linear_order.setVisibility(View.GONE);
            }

        }
        else if(type.equals("Admin")){
            linear_edit_delete.setVisibility(View.VISIBLE);
            linear_order.setVisibility(View.GONE);
        }

        else
        {
            linear_edit_delete.setVisibility(View.GONE);
            linear_order.setVisibility(View.VISIBLE);
        }
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(DetailsActivity.this, CheckoutActivity.class);
               intent.putExtra("ItemName",name);
                               intent.putExtra("ItemId",id);
                               intent.putExtra("SellerEmail",email);
                               intent.putExtra("BuyerEmail",loggedEmail);
                               intent.putExtra("CategoryName",categoryName);
                               intent.putExtra("CategoryId",categoryId);
                               intent.putExtra("imageUrl",imageUrl);
                               intent.putExtra("price",price);
                               intent.putExtra("companyName",company);
                               intent.putExtra("companyAddress",companyAddress);
                               intent.putExtra("companyContact",companyContact);
                               intent.putExtra("BuyerName",loggedName);
                               intent.putExtra("BuyerAddress",buyerAddress);
                               intent.putExtra("BuyerContact",buyerContact);
                              startActivity(intent);


            }
        });


        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Are you sure?")
                        .setMessage("Won't be able to recover this product!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // File deleted successfully
                                        //Log.d(TAG, "onSuccess: deleted file");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Uh-oh, an error occurred!
                                        //  Log.d(TAG, "onFailure: did not delete file");
                                    }
                                });

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                Query applesQuery = ref.child("Products").child("items").child(id);

                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().removeValue();
                                            Toast.makeText(v.getContext(),"Product Deleted",Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            finish();


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        //Log.e(TAG, "onCancelled", databaseError.toException());
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this,AddProductActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("id", id);
                intent.putExtra("price", price);
                intent.putExtra("imageURL", imageUrl);
                intent.putExtra("company", company);
                intent.putExtra("email", email);
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("CompanyAddress", companyAddress);
                intent.putExtra("CompanyContact", companyContact);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}