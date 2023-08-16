package com.project.autopak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.autopak.Checkout.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String name,email,type,contact,address;
    FloatingActionButton fab;
    private RecyclerView recyclerView;
    CategoryAdapter
            adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the
    // Firebase Realtime Database
    //Arraylist for storing data
    ArrayList<category> list = new ArrayList<>();

    //Navigation view
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ProgressBar bar;

    int[] Images = {R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4};
    List<Integer> ids = new ArrayList<>();
    String id = "1";

    AdapterViewFlipper adapterViewFlipper;

    //Item Layout
    private RecyclerView recyclerView2;
    DatabaseReference mbase2;
    ArrayList<Items> list2 = new ArrayList<>();
    ItemsAdapter adapter2;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv);
       ids.add(1);
       ids.add(2);
       ids.add(3);
       ids.add(4);
       ids.add(5);
       ids.add(6);
       ids.add(7);
        Random rand = new Random();

       // Toast.makeText(this, ""+String.valueOf(ids.get(rand.nextInt(ids.size()))), Toast.LENGTH_SHORT).show();
        id =  String.valueOf(ids.get(rand.nextInt(ids.size())));

        adapterViewFlipper= findViewById(R.id.adapterviewflipper);

        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,Images);
        adapterViewFlipper.setAdapter(customAdapter);
        adapterViewFlipper.setFlipInterval(3000);
        adapterViewFlipper.setAutoStart(true);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView =  findViewById(R.id.navigationview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bar = findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationview);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        TextView tvmore = findViewById(R.id.tvmore);

        tvmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = textView.getText().toString();
                Intent intent = new Intent(MainActivity.this, ItemsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddProductActivity.class));
            }
        });


        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        name = sh.getString("name", "");
        contact = sh.getString("contact", "");
        address = sh.getString("address", "");
        email = sh.getString("email", "");
        type = sh.getString("type", "");

        Common.email = email;
        Common.address = address;
        Common.name = name;
        Common.phone = contact;



        View headerView = navigationView.getHeaderView(0);
        TextView navtv = (TextView) headerView.findViewById(R.id.tv);
        TextView navtvName = (TextView) headerView.findViewById(R.id.tv_name);
        navtvName.setText(name);
        String shortName =  name.length() < 2 ? name : name.substring(0, 2);
        navtv.setText(shortName.toUpperCase());
        if(type.equals("Admin")){
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.users).setVisible(true);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                if(item.getItemId()==R.id.home){
                    Toast.makeText(MainActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                }
                else if(item.getItemId()==R.id.orders){
                    Intent intent = new Intent(MainActivity.this,OrdersPage.class);
                    if(type.equals("Shopkeeper")){
                        intent.putExtra("from","buyer");
                    }
                    else if(type.equals("Wholeseller")) {
                        intent.putExtra("from","seller");
                    }
                    else{
                        intent.putExtra("from","admin");
                    }
                   startActivity(intent);

                }else if(item.getItemId()==R.id.wish){
                  startActivity(new Intent(MainActivity.this,WishlistPage.class));
                }else if(item.getItemId()==R.id.users){
                  startActivity(new Intent(MainActivity.this,UsersPage.class));
                }
                else if(item.getItemId()==R.id.myaccount){
                    startActivity(new Intent(MainActivity.this,EditProfileActivity.class));
                }
                else if(item.getItemId()==R.id.logout){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,ChooseActivity.class));
                    finish();
                }
                return false;
            }
        });


        if(type.equals("Shopkeeper") || type.equals("Admin")){
            fab.setVisibility(View.GONE);
        }

        // Create a instance of the database and get
        // its reference
        mbase
                = FirebaseDatabase.getInstance().getReference().child("Products").child("Categories");

        recyclerView = findViewById(R.id.recyclerview);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);

       //Fetching data from firebase
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String name = ds.child("name").getValue(String.class);
                    String id2 = ds.child("id").getValue(String.class);
                    String imageUrl = ds.child("imageUrl").getValue(String.class);
                    list.add(new category(id2,imageUrl,name));
                    if(id2.equals(id)){
                        textView.setText(name);
                    }
                }
                // Connecting object of required Adapter class to
                // the Adapter class itself
                adapter = new CategoryAdapter(list);
                // Connecting Adapter class with the Recycler view*/
                recyclerView.setAdapter(adapter);
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                bar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        mbase2
                = FirebaseDatabase.getInstance().getReference().child("Products").child("items");

        recyclerView2 = findViewById(R.id.recyclerview2);

        // To display the Recycler view linearly
        recyclerView2.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setHasFixedSize(true);

        //Fetching data from firebase
        mbase2.orderByChild("categoryid").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String name2 = ds.child("name").getValue(String.class);
                    String companyName = ds.child("companyName").getValue(String.class);
                    String email2 = ds.child("email").getValue(String.class);
                    String price = ds.child("price").getValue(String.class);
                    String address = ds.child("address").getValue(String.class);
                    String contact = ds.child("contact").getValue(String.class);
                    String id = ds.getKey();
                    String imageUrl = ds.child("imageUrl").getValue(String.class);
                    if(type.equals("Wholeseller")) {
                        if (companyName.equals(name) && email2.equals(email)) {

                            list2.add(new Items(name2, id, imageUrl, companyName, price, email2, address, contact));
                        }
                    }
                    else{
                        list2.add(new Items(name2,id,imageUrl,companyName,price,email2,address,contact));
                    }
                }
                // Connecting object of required Adapter class to
                // the Adapter class itself
                adapter2 = new ItemsAdapter(MainActivity.this,list2,name,String.valueOf(id),"front_item");
                // Connecting Adapter class with the Recycler view*/
                recyclerView2.setAdapter(adapter2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

               // Toast.makeText(ItemsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



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
        ArrayList<category> filteredlist = new ArrayList<category>();

        // running a for loop to compare elements.
        for (category item : list) {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.sort){
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

    public void AscendingOrder(){
        Collections.sort(list, new Comparator<category>(){
            public int compare(category obj1, category obj2) {
                // ## Ascending order
                return obj1.getName().compareToIgnoreCase(obj2.getName()); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void DescendingOrder(){
        Collections.sort(list, new Comparator<category>(){
            public int compare(category obj1, category obj2) {
                // ## Ascending order
                return obj2.getName().compareToIgnoreCase(obj1.getName()); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
            }
        });
        adapter.notifyDataSetChanged();
    }


}

class CustomAdapter extends BaseAdapter{

    Context context;
    int[] images;
    LayoutInflater layoutInflater;

    public  CustomAdapter(Context context,int[] images){
        this.context = context;
        this.images = images;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.view_flipper_layout,null);
        ImageView imageView = convertView.findViewById(R.id.imageview);
        imageView.setImageResource(images[position]);
        return convertView;
    }
}