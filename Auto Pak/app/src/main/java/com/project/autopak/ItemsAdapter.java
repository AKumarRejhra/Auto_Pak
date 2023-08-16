package com.project.autopak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    ArrayList<Items> list;
    String categoryName,categoryId;
    Context context;

   ArrayList<String> array = new ArrayList<>();
   String from ;




    public ItemsAdapter(Context context,ArrayList<Items> list,String categoryName,String categoryId,String from) {
        this.context = context;
        this.list = list;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.from = from;
        retrieveArray();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem;
        if(from.equals("item") || from.equals("wish")){
            listItem = layoutInflater.inflate(R.layout.items_layout, parent, false);
        }
        else{
            listItem = layoutInflater.inflate(R.layout.fron_item_layout, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Items myListData = list.get(position);
        holder.name.setText(myListData.name);
        holder.company.setText(myListData.companyName);
        holder.price.setText("Rs "+myListData.price);
        Glide.with(holder.itemView.getContext()).load(myListData.imageUrl).into(holder.img);

        for(int i = 0;i<array.size();i++){
            if(myListData.categoryid.equals(array.get(i))){
                holder.fav.setImageResource(R.drawable.ic_baseline_favorite_24);
            }
        }

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(array.contains(myListData.categoryid)){
                    holder.fav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    for(int i = 0;i<array.size();i++){
                        if(array.get(i).equals(myListData.categoryid)){

                            if(from.equals("wish")){
                                for(int j = 0;j<list.size();j++){
                                    if(list.get(j).categoryid.equals(array.get(i))){
                                        list.remove(j);
                                        notifyDataSetChanged();
                                    }
                                }

                            }

                            array.remove(i);
                        }
                    }
                    storeArray(array);
                }
                else{
                    holder.fav.setImageResource(R.drawable.ic_baseline_favorite_24);
                    array.add(myListData.categoryid);
                    storeArray(array);



                    }



            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(from.equals("wish")){

                    Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                    intent.putExtra("name", myListData.name);
                    intent.putExtra("id", myListData.categoryid);
                    intent.putExtra("price", myListData.price);
                    intent.putExtra("imageURL", myListData.imageUrl);
                    intent.putExtra("company", myListData.companyName);
                    intent.putExtra("email", myListData.email);
                    intent.putExtra("categoryId", myListData.id);
                    intent.putExtra("categoryName", myListData.categoryName);
                    intent.putExtra("CompanyAddress", myListData.address);
                    intent.putExtra("CompanyContact", myListData.contact);
                    holder.itemView.getContext().startActivity(intent);
                }
                else{

                    Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                    intent.putExtra("name", myListData.name);
                    intent.putExtra("id", myListData.categoryid);
                    intent.putExtra("price", myListData.price);
                    intent.putExtra("imageURL", myListData.imageUrl);
                    intent.putExtra("company", myListData.companyName);
                    intent.putExtra("email", myListData.email);
                    intent.putExtra("categoryId", categoryId);
                    intent.putExtra("categoryName", categoryName);
                    intent.putExtra("CompanyAddress", myListData.address);
                    intent.putExtra("CompanyContact", myListData.contact);
                    holder.itemView.getContext().startActivity(intent);
                }


                }


        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,company,price;
        ImageView img,fav;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageview);
            fav = itemView.findViewById(R.id.fav);
            name = itemView.findViewById(R.id.tv);
            company = itemView.findViewById(R.id.company);
            price = itemView.findViewById(R.id.rs);
        }
    }
    // method for filtering our recyclerview items.
    public void filterList(ArrayList<Items> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

   public void storeArray(ArrayList<String> array){
       //String array[]
//SharedPreferences prefs
       SharedPreferences sharedPreferences = context.getSharedPreferences("Pref",Context.MODE_PRIVATE);
       SharedPreferences.Editor edit = sharedPreferences.edit();
       edit.putInt("array_size", array.size());
       for(int i=0;i<array.size(); i++)
           edit.putString("array_" + i, array.get(i));
       edit.commit();
   }
   public  void retrieveArray(){
       SharedPreferences sharedPreferences = context.getSharedPreferences("Pref",Context.MODE_PRIVATE);
       int size = sharedPreferences.getInt("array_size", 0);
       array = new ArrayList<>();
       for(int i=0; i<size; i++){
           String value = sharedPreferences.getString("array_" + i, null);
           array.add(value);
       }


   }




}
