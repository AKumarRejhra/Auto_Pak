package com.project.autopak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

public class WishlistPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    DatabaseReference mbase;
    ArrayList<Items> list = new ArrayList<>();
    ItemsAdapter adapter;
    ProgressBar bar;
    String catId;

    ArrayList<String> array = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_page);

        getSupportActionBar().setTitle("Wishlist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bar = findViewById(R.id.progressBar);

        retrieveArray();


        mbase
                = FirebaseDatabase.getInstance().getReference().child("Products").child("items");

        recyclerView = findViewById(R.id.recyclerview);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        if(array.size()==0 || array==null) bar.setVisibility(View.GONE);

        for(int i = 0;i<array.size();i++){
            fetchItems(array.get(i));

        }





    }

    private void fetchItems(String id){
        //Fetching data from firebase
        mbase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    String name = snapshot.child("name").getValue(String.class);
                    catId = snapshot.child("categoryid").getValue(String.class);
                    String companyName = snapshot.child("companyName").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String price = snapshot.child("price").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String contact = snapshot.child("contact").getValue(String.class);
                    String id = snapshot.getKey();
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    list.add(new Items(name,id,imageUrl,companyName,price,email,address,contact,"catname",catId));

                    // Connecting object of required Adapter class to

                    adapter = new ItemsAdapter(WishlistPage.this,list,"catname",catId,"wish");
                    // Connecting Adapter class with the Recycler view*/
                    recyclerView.setAdapter(adapter);
                    bar.setVisibility(View.GONE);






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                bar.setVisibility(View.GONE);
                Toast.makeText(WishlistPage.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
                return obj2.getPrice().compareToIgnoreCase(obj1.getPrice()); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void AscendingOrder(){
        Collections.sort(list, new Comparator<Items>(){
            public int compare(Items obj1, Items obj2) {
                // ## Ascending order
                return obj1.getPrice().compareToIgnoreCase(obj2.getPrice()); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });
        adapter.notifyDataSetChanged();
    }
    public  void retrieveArray(){
        SharedPreferences sharedPreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);
        int size = sharedPreferences.getInt("array_size", 0);
        array = new ArrayList<>();
        for(int i=0; i<size; i++){
            String value = sharedPreferences.getString("array_" + i, null);
            array.add(value);
        }


    }


}