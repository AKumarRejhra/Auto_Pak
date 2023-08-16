package com.project.autopak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    //Declare
    EditText name,email,password,address,contact,confirmPassword;
    Button mRegisterbtn;
    TextView mLoginPageBack;
    FirebaseAuth mAuth;
    DatabaseReference mdatabase;
    String Name,Email,Password,Number,Address;
    ProgressDialog mDialog;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        type = getIntent().getStringExtra("type");
        name = (EditText)findViewById(R.id.editName);
        email = (EditText)findViewById(R.id.editEmail);
        password = (EditText)findViewById(R.id.editPassword);


        password.setTransformationMethod(new PasswordTransformationMethod());
        address = (EditText)findViewById(R.id.editAddress);
        contact = (EditText)findViewById(R.id.editContact);
        confirmPassword = (EditText)findViewById(R.id.editConfirmPassword);
        confirmPassword.setTransformationMethod(new PasswordTransformationMethod());

        mRegisterbtn = (Button)findViewById(R.id.buttonRegister);
        mLoginPageBack = (TextView)findViewById(R.id.buttonLogin);
        // for authentication using FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegister();
            }
        });
        mLoginPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        mDialog = new ProgressDialog(this);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }


    private void UserRegister() {
        Name = name.getText().toString().trim();
        Email = email.getText().toString().trim();
        Number = contact.getText().toString().trim();
        Address = address.getText().toString().trim();
        Password = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();


        if (TextUtils.isEmpty(Name)){
            name.setError("Enter Name");
            return;
        }else if (TextUtils.isEmpty(Email)){
            email.setError("Enter Email");
            return;
        }else if (TextUtils.isEmpty(Password)){
            password.setError("Enter Password");
            return;
        }
        else if (TextUtils.isEmpty(Address)){
            address.setError("Enter Address");
            return;
        }
        else if (TextUtils.isEmpty(Number)){
            contact.setError("Enter Contact Number");
            return;
        }
        else if (Password.length()<6){
            password.setError("Password must be greater than 6 digits");
            return;
        }
        else if (!Password.equals(confirmPass)){
            confirmPassword.setError("Password does not match");
            return;
        }

        mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mDialog.dismiss();
                    OnAuth(task.getResult().getUser());
                    mAuth.signOut();
                    // Storing data into SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

                    // Creating an Editor object to edit(write to the file)
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    // Storing the key and its value as the data fetched from edittext
                    myEdit.putString("name", Name);
                    myEdit.putString("email", Email);
                    myEdit.putString("type", type);
                    myEdit.putString("address", Address);
                    myEdit.putString("contact", Number);

                    // Once the changes have been made,
                    // we need to commit to apply those changes made,
                    // otherwise, it will throw an error
                    myEdit.commit();
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    LoginActivity.activity.finish();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    intent.putExtra("choose",type);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(RegisterActivity.this,"error on creating user",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());
    }

    private void createAnewUser(String uid) {
        User user = BuildNewuser();
        mdatabase.child(uid).setValue(user);

    }


    private User BuildNewuser(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String token = sh.getString("token", "");
        return new User(
                getDisplayName(),
                getUserEmail(),
                type,getUserAddress(),getUserContact(),
                token

        );
    }

    public String getDisplayName() {
        return Name;
    }

    public String getUserEmail() {
        return Email;
    }

    public String getUserContact() {
        return Number;
    }
    public String getUserAddress() {
        return Address;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            intent.putExtra("choose",type);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        intent.putExtra("choose",type);
        startActivity(intent);
        finish();
    }
}