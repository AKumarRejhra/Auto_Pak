package com.project.autopak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    ArrayList<User> list;
    Context context;
   String from ;




    public UserAdapter(Context context, ArrayList<User> list,String from) {
        this.context = context;
        this.list = list;
        this.from = from;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem;

            listItem = layoutInflater.inflate(R.layout.users_layout, parent, false);


        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User myListData = list.get(position);
        holder.name.setText(myListData.name.substring(0,2).toUpperCase());
        holder.displayName.setText(myListData.name);
        holder.email.setText(myListData.Email);
        holder.contact.setText(myListData.contact);
        holder.address.setText(myListData.address);







//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(from.equals("wish")){
//
//                    Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
//                    intent.putExtra("name", myListData.name);
//                    intent.putExtra("id", myListData.categoryid);
//                    intent.putExtra("price", myListData.price);
//                    intent.putExtra("imageURL", myListData.imageUrl);
//                    intent.putExtra("company", myListData.companyName);
//                    intent.putExtra("email", myListData.email);
//                    intent.putExtra("categoryId", myListData.id);
//                    intent.putExtra("categoryName", myListData.categoryName);
//                    intent.putExtra("CompanyAddress", myListData.address);
//                    intent.putExtra("CompanyContact", myListData.contact);
//                    holder.itemView.getContext().startActivity(intent);
//                }
//                else{
//
//                    Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
//                    intent.putExtra("name", myListData.name);
//                    intent.putExtra("id", myListData.categoryid);
//                    intent.putExtra("price", myListData.price);
//                    intent.putExtra("imageURL", myListData.imageUrl);
//                    intent.putExtra("company", myListData.companyName);
//                    intent.putExtra("email", myListData.email);
//                    intent.putExtra("categoryId", categoryId);
//                    intent.putExtra("categoryName", categoryName);
//                    intent.putExtra("CompanyAddress", myListData.address);
//                    intent.putExtra("CompanyContact", myListData.contact);
//                    holder.itemView.getContext().startActivity(intent);
//                }
//
//
//                }
//
//
//        });


    }



    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,displayName,email,contact,address;

        public ViewHolder(View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.displayName);
            email = itemView.findViewById(R.id.email);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contact);
            address = itemView.findViewById(R.id.address);
        }
    }
    // method for filtering our recyclerview items.
    public void filterList(ArrayList<User> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }







}
