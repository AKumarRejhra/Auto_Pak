package com.project.autopak.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.autopak.R;
import com.project.autopak.User;
import com.project.autopak.UserAdapter;

import java.util.ArrayList;


public class WholesellerFragment extends Fragment {

    private RecyclerView recyclerView;
    DatabaseReference mbase;
    ArrayList<User> list = new ArrayList<>();
    ProgressBar bar;
    UserAdapter adapter;

    public WholesellerFragment() {
        // Required empty public constructor
    }


    public static WholesellerFragment newInstance(String param1, String param2) {
        WholesellerFragment fragment = new WholesellerFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wholeseller, container, false);
        bar = view.findViewById(R.id.progressBar);
        mbase
                = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView = view.findViewById(R.id.recyclerview);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        recyclerView.setHasFixedSize(true);

        //Fetching data from firebase
        mbase.orderByChild("type").equalTo("Wholeseller").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String address = ds.child("address").getValue(String.class);
                    String contact = ds.child("contact").getValue(String.class);
                    String displayname = ds.child("displayname").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String type = ds.child("type").getValue(String.class);
                    String token = "";
                    if(ds.hasChild("token")){
                        token = ds.child("token").getValue(String.class);
                    }

                    if(type.equals("Wholeseller")) {

                        list.add(new User(displayname,email,type,address,contact,token));
                    }

                }
                // Connecting object of required Adapter class to
                // the Adapter class itself
                adapter = new UserAdapter(getActivity(),list,"Wholeseller");
                // Connecting Adapter class with the Recycler view*/
                recyclerView.setAdapter(adapter);
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                bar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}