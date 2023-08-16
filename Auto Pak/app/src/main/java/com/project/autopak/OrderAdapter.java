package com.project.autopak;

import android.content.Intent;
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
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    ArrayList<ordermodel> list;
    String from ;

    public OrderAdapter(ArrayList<ordermodel> list,String from) {
        this.list = list;
        this.from = from;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.order_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ordermodel myListData = list.get(position);
        if(from.equals("buyer")){

        holder.name.setText(myListData.getCompanyName());

        }
        else{
            holder.name.setText(myListData.getBuyerName());
        }

        holder.itemName.setText(myListData.getItemName());
        holder.date.setText(myListData.getDateTime());
        holder.itemCategory.setText(myListData.getCategoryName());
        holder.price.setText(myListData.getPrice());

        Glide.with(holder.itemView.getContext()).load(myListData.imageUrl).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(holder.itemView.getContext(), ItemsActivity.class);
//                intent.putExtra("name", myListData.name);
//                intent.putExtra("id", myListData.id);
//                holder.itemView.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName,itemCategory,name,price,date;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageview);
            itemName = itemView.findViewById(R.id.itemName);
            itemCategory = itemView.findViewById(R.id.categoryName);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.dateTime);
        }
    }
    // method for filtering our recyclerview items.
    public void filterList(ArrayList<ordermodel> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}
