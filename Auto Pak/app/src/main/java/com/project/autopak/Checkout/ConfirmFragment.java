package com.project.autopak.Checkout;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.project.autopak.Checkout.localstorage.LocalStorage;
import com.project.autopak.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmFragment extends Fragment {
    LocalStorage localStorage;
    Gson gson;
    RecyclerView recyclerView;
    TextView back, placeOrder;
    TextView total, shipping, totalAmount;
    Double _total, _shipping, _totalAmount;
    ProgressDialog progressDialog;
    String orderNo;
    String id;
    String token = "";

    public ConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);
        localStorage = new LocalStorage(getContext());
        recyclerView = view.findViewById(R.id.cart_rv);
        totalAmount = view.findViewById(R.id.total_amount);
        total = view.findViewById(R.id.total);
        shipping = view.findViewById(R.id.shipping_amount);
        back = view.findViewById(R.id.back);
        placeOrder = view.findViewById(R.id.place_order);
        progressDialog = new ProgressDialog(getContext());
        gson = new Gson();
//        Random rnd = new Random();
//        orderNo = "Order #" + (100000 + rnd.nextInt(900000));

        _total = Double.parseDouble(CheckoutActivity.price);
        _shipping = 0.0;
        _totalAmount = _total + _shipping;
        total.setText(_total + "");
        shipping.setText(_shipping + "");
        totalAmount.setText(_totalAmount + "");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait....");
                progressDialog.show();
                closeProgress();

            }
        });

        //get seller token
        FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("email").equalTo(CheckoutActivity.SellerEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String value = ds.getKey();

                    if(snapshot.child(value).hasChild("token")){
                        token = snapshot.child(value).child("token").getValue(String.class);

                    }


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void closeProgress() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                              String  dateTime = sdf.format(new Date());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
                //Send notification through FCM

                senPushdNotification("New Order Received","Auto Pak",token);
                HashMap<String,String> map  = new HashMap<>();
                map.put("ItemName",CheckoutActivity.ItemName);
                map.put("ItemId",CheckoutActivity.ItemId);
                map.put("SellerEmail",CheckoutActivity.SellerEmail);
                map.put("BuyerEmail",CheckoutActivity.BuyerEmail);
                map.put("CategoryName",CheckoutActivity.CategoryName);
                map.put("CategoryId",CheckoutActivity.CategoryId);
                map.put("DateTime",dateTime);
                map.put("imageUrl",CheckoutActivity.imageUrl);
                map.put("price",CheckoutActivity.price);
                map.put("companyName",CheckoutActivity.companyName);
                map.put("companyAddress",CheckoutActivity.companyAddress);
                map.put("companyContact",CheckoutActivity.companyContact);
                map.put("BuyerName",CheckoutActivity.BuyerName);
                map.put("BuyerAddress",CheckoutActivity.BuyerAddress);
                map.put("BuyerContact",CheckoutActivity.BuyerContact);
                FirebaseDatabase.getInstance().getReference().child("Orders").push().setValue(map);
                Toast.makeText(getActivity(), "Order is Placed!", Toast.LENGTH_SHORT).show();
                getActivity().finish();



            }
        }, 3000); // 5000 milliseconds

    }
    public static void senPushdNotification(final String body, final String title, final String fcmToken) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject notificationJson = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    notificationJson.put("body", body);
                    notificationJson.put("title", title);
                    notificationJson.put("priority", "high");
                    dataJson.put("customId", "02");
                    dataJson.put("badge", 1);
                    dataJson.put("alert", "Alert");
                    json.put("notification", notificationJson);
                    json.put("data", dataJson);
                    json.put("to", fcmToken);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=AAAA_CwQPto:APA91bFzYXz_gSkwONaetl4Zeq5hgoT4MQSWaTIo3Svrq0SXWmE5gmjMkDyrD4r1Jd-80pgYQzoyeJEBaqAwkPUWSSIs2iVBwsatTCgEyC43nk3MJ_PXHBbuJYc1N41M-qCsk5Al174L")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    Log.i("TAG", finalResponse);
                } catch (Exception e) {

                    Log.i("TAG", e.getMessage());
                }
                return null;
            }
        }.execute();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Confirm");
    }


}
