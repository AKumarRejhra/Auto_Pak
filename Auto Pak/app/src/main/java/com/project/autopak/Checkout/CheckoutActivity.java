package com.project.autopak.Checkout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.project.autopak.R;


public class CheckoutActivity extends AppCompatActivity {

    public static String ItemName,ItemId,SellerEmail,BuyerEmail,CategoryName,CategoryId,imageUrl,price,companyName,
            companyAddress,companyContact,BuyerName,BuyerAddress,BuyerContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setTitle("Checkout");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ItemName = getIntent().getStringExtra("ItemName");
        ItemId = getIntent().getStringExtra("ItemId");
        SellerEmail = getIntent().getStringExtra("SellerEmail");
        BuyerEmail = getIntent().getStringExtra("BuyerEmail");
        CategoryName = getIntent().getStringExtra("CategoryName");
        CategoryId = getIntent().getStringExtra("CategoryId");
        imageUrl = getIntent().getStringExtra("imageUrl");
        price = getIntent().getStringExtra("price");
        companyName = getIntent().getStringExtra("companyName");
        companyAddress = getIntent().getStringExtra("companyAddress");
        companyContact = getIntent().getStringExtra("companyContact");
        BuyerName = getIntent().getStringExtra("BuyerName");
        BuyerAddress = getIntent().getStringExtra("BuyerAddress");
        BuyerContact = getIntent().getStringExtra("BuyerContact");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new AddressFragment());
        ft.commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
