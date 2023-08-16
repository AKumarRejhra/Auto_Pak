package com.project.autopak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ItemsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    DatabaseReference mbase;
    ArrayList<Items> list = new ArrayList<>();
    String id,name;
    ItemsAdapter adapter;
    ProgressBar bar;

    String company_Name,companyEmail,companyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bar = findViewById(R.id.progressBar);
        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        company_Name = sh.getString("name", "");
        companyEmail = sh.getString("email", "");
        companyType = sh.getString("type", "");


        mbase
                = FirebaseDatabase.getInstance().getReference().child("Products").child("items");

        recyclerView = findViewById(R.id.recyclerview);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        //Fetching data from firebase
        mbase.orderByChild("categoryid").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String name = ds.child("name").getValue(String.class);
                    String companyName = ds.child("companyName").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String price = ds.child("price").getValue(String.class);
                    String address = ds.child("address").getValue(String.class);
                    String contact = ds.child("contact").getValue(String.class);
                    String id = ds.getKey();
                    String imageUrl = ds.child("imageUrl").getValue(String.class);
                    if(companyType.equals("Wholeseller")) {
                        if (companyName.equals(company_Name) && email.equals(companyEmail)) {

                            list.add(new Items(name, id, imageUrl, companyName, price, email, address, contact));
                        }
                    }
                    else{
                        list.add(new Items(name,id,imageUrl,companyName,price,email,address,contact));
                    }
                }
                // Connecting object of required Adapter class to
                // the Adapter class itself
                adapter = new ItemsAdapter(ItemsActivity.this,list,name,id,"item");
                // Connecting Adapter class with the Recycler view*/
                recyclerView.setAdapter(adapter);
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                bar.setVisibility(View.GONE);
                Toast.makeText(ItemsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        else if(item.getItemId()==R.id.sort){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.sort_dialog, null);
            dialogBuilder.setView(dialogView);

            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = dialogBuilder.create();

            TextView editTextAsc = (TextView) dialogView.findViewById(R.id.asc);
            TextView editTextDesc = (TextView) dialogView.findViewById(R.id.desc);
            editTextAsc.setText("Price Low-High");
            editTextDesc.setText("Price High-Low");
            editTextDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DescendingOrder();
                    alertDialog.dismiss();

                }
            });
            editTextAsc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AscendingOrder();
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_view, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        // getting search view of our item.
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });
        return true;
    }
    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Items> filteredlist = new ArrayList<Items>();

        // running a for loop to compare elements.
        for (Items item : list) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
           // Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }
    public void DescendingOrder(){
        Collections.sort(list, new Comparator<Items>(){
            public int compare(Items obj1, Items obj2) {
                // ## Ascending order
                //return obj2.getPrice().compareToIgnoreCase(obj1.getPrice()); // To compare string values
                 //return Integer.valueOf(obj1.getPrice()).compareTo(Integer.valueOf(obj2.getPrice())); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                return Integer.valueOf(obj2.getPrice()).compareTo(Integer.valueOf(obj1.getPrice())); // To compare integer values
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void AscendingOrder(){
        Collections.sort(list, new Comparator<Items>(){
            public int compare(Items obj1, Items obj2) {
                // ## Ascending order
               // return obj1.getPrice().compareToIgnoreCase(obj2.getPrice()); // To compare string values
                 return Integer.valueOf(obj1.getPrice()).compareTo(Integer.valueOf(obj2.getPrice())); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });
        adapter.notifyDataSetChanged();
    }

}