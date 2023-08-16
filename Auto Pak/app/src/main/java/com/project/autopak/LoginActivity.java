package com.project.autopak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    //Declare
    EditText Email, Password;
    Button LogInButton, RegisterButton;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    String email, password;
    ProgressDialog dialog;
    public static final String userEmail = "";

    public static final String TAG = "LOGIN";

    public static Activity activity;
    String choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;
        //Initilize
        if(getIntent().getStringExtra("choose")!=null){

        choose = getIntent().getStringExtra("choose");
        }
        else{
            startActivity(new Intent(LoginActivity.this,ChooseActivity.class));
            finish();
        }

        dialog = new ProgressDialog(this);
        //Check if user has already login, then open Main activity
        if(FirebaseAuth.getInstance().getCurrentUser() !=null){
            // Retrieving the value using its keys the file name
            // must be same in both saving and retrieving the data
            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

            // The value will be default as empty string because for
            // the very first time when the app is opened, there is nothing to show
            String type = sh.getString("type", "");
            if(type.equals(choose)){
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                finish();

            }


        }


        LogInButton = (Button) findViewById(R.id.btn_login);

        RegisterButton = (Button) findViewById(R.id.btn_signup);

        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        Password.setTransformationMethod(new PasswordTransformationMethod());
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling EditText is empty or no method.
                userSign();


            }
        });

        // Adding click listener to register button.
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Opening new user registration activity using intent on button click.
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("type",choose);
                startActivity(intent);


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }

    }

    @Override
    public void onBackPressed() {
        LoginActivity.super.finish();
    }


    private void userSign() {
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Email.setError("Enter Email");
            return;
        } else if (TextUtils.isEmpty(password)) {
            Password.setError("Enter Password");
            return;
        }
        dialog.setMessage("Logging in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        if(choose.equals("Shopkeeper") || choose.equals("Wholeseller")){
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        dialog.dismiss();

                        Toast.makeText(LoginActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();

                    } else {
                        String uid = FirebaseAuth.getInstance().getUid();
                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot ds) {
                                // for(DataSnapshot ds:snapshot.getChildren()){

                                String type = ds.child("type").getValue(String.class);
                                String name = ds.child("displayname").getValue(String.class);
                                String email = ds.child("email").getValue(String.class);
                                String address = ds.child("address").getValue(String.class);
                                String contact = ds.child("contact").getValue(String.class);

                                    SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

                                    String token = sh.getString("token", "");
                                    HashMap<String,Object> map = new HashMap<>();
                                    map.put("token",token);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).updateChildren(map);
                                //Check type of user
                                if(type.equals(choose)){
                                    dialog.dismiss();
                                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                    // Storing data into SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

                                    // Creating an Editor object to edit(write to the file)
                                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                    // Storing the key and its value as the data fetched from edittext
                                    myEdit.putString("name", name);
                                    myEdit.putString("email", email);
                                    myEdit.putString("type", type);
                                    myEdit.putString("address", address);
                                    myEdit.putString("contact", contact);

                                    // Once the changes have been made,
                                    // we need to commit to apply those changes made,
                                    // otherwise, it will throw an error
                                    myEdit.commit();
                                    startActivity(i);
                                    finish();

                                }

                            }
                            //  }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                        //   checkIfEmailVerified();

                    }
                }
            });
        }
        else{
            //Check Admin Details

            //Email: admin@gmail.com    password: admin12
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        dialog.dismiss();

                        Toast.makeText(LoginActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        String uid = FirebaseAuth.getInstance().getUid();
                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot ds) {
                                // for(DataSnapshot ds:snapshot.getChildren()){

                                String type = ds.child("type").getValue(String.class);
                                String name = ds.child("displayname").getValue(String.class);
                                String email = ds.child("email").getValue(String.class);
                                String address = ds.child("address").getValue(String.class);
                                String contact = ds.child("contact").getValue(String.class);
                                //Check type of user
                                if(type.equals(choose)){
                                    dialog.dismiss();
                                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                    // Storing data into SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

                                    // Creating an Editor object to edit(write to the file)
                                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                    // Storing the key and its value as the data fetched from edittext
                                    myEdit.putString("name", name);
                                    myEdit.putString("email", email);
                                    myEdit.putString("type", type);
                                    myEdit.putString("address", address);
                                    myEdit.putString("contact", contact);

                                    // Once the changes have been made,
                                    // we need to commit to apply those changes made,
                                    // otherwise, it will throw an error
                                    myEdit.commit();
                                    startActivity(i);
                                    finish();

                                }

                            }
                            //  }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                        //   checkIfEmailVerified();

                    }
                }
            });

        }


    }

    //This function helps in verifying whether the email is verified or not.
    private void checkIfEmailVerified() {
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified = users.isEmailVerified();
        if (!emailVerified) {
            Toast.makeText(this, "Verify the Email Id", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            finish();
        } else {
            Email.getText().clear();

            Password.getText().clear();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra(userEmail, email);

            startActivity(intent);

        }
    }
}