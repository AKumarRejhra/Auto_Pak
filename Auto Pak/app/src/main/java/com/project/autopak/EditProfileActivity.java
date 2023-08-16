package com.project.autopak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    EditText edtName,edtEmail,edtType,edtcontact,edtaddress;
    Button btnUpdate;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtName = findViewById(R.id.name);
        edtEmail = findViewById(R.id.email);
        edtType = findViewById(R.id.type);
        edtcontact = findViewById(R.id.contact);
        edtaddress = findViewById(R.id.address);
        btnUpdate =  findViewById(R.id.btn_edit);

        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show

        String mail = sh.getString("email", "");

        FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("email").equalTo(mail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren()){
                    String name = ds.child("displayname").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String contact = ds.child("contact").getValue(String.class);
                    String address = ds.child("address").getValue(String.class);
                    String type = ds.child("type").getValue(String.class);
                    uid = ds.getKey();

                    edtName.setText(name);
                    edtEmail.setText(email);
                    edtcontact.setText(contact);
                    edtType.setText(type);
                    edtaddress.setText(address);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String email = edtEmail.getText().toString();
                String type = edtType.getText().toString();
                String contact = edtcontact.getText().toString();
                String address = edtaddress.getText().toString();

                if(name.isEmpty()) edtName.setError("Enter Name");
                else if(contact.isEmpty()) edtcontact.setError("Enter Contact");
                else if(address.isEmpty()) edtaddress.setError("Enter Address");
                else{
                    HashMap<String,String> map = new HashMap<>();
                    map.put("address",address);
                    map.put("contact",contact);
                    map.put("displayname",name);
                    map.put("email",email);
                    map.put("type",type);

                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(map);
                    Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}