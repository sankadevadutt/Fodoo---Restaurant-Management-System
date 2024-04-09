package com.example.eres.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eres.R;
import com.example.eres.chef.chefhelper;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class searchadapter extends RecyclerView.Adapter<searchadapter.ViewHolder>{
    private Context mcontext;
    private List<chefhelper> updatefoodList;
    private String phone;
    DatabaseReference databaseReference;

    public searchadapter(Context mcontext, List<chefhelper> updatefoodList, String phone) {
        this.mcontext = mcontext;
        this.updatefoodList = updatefoodList;
        this.phone = phone;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_menudish,parent,false);
        return new searchadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final chefhelper updatefood = updatefoodList.get(position);
        Glide.with(mcontext).load(updatefood.getImageURL()).into(holder.imageView);
        holder.DishName.setText(updatefood.getDishname());
        holder.Price.setText("Rs: "+updatefood.getPrice());
        updatefood.getRandonUUID();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,com.example.eres.userproductdisp.class);
                intent.putExtra("phone",phone);
                intent.putExtra("product",updatefood.getRandonUUID());
                mcontext.startActivity(intent);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,com.example.eres.userproductdisp.class);
                intent.putExtra("phone",phone);
                intent.putExtra("product",updatefood.getRandonUUID());
                mcontext.startActivity(intent);
            }
        });
        holder.Price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,com.example.eres.userproductdisp.class);
                intent.putExtra("phone",phone);
                intent.putExtra("product",updatefood.getRandonUUID());
                mcontext.startActivity(intent);
            }
        });
        holder.DishName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,com.example.eres.userproductdisp.class);
                intent.putExtra("phone",phone);
                intent.putExtra("product",updatefood.getRandonUUID());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return updatefoodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView DishName,Price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.home_product);
            DishName = itemView.findViewById(R.id.home_dish);
            Price = itemView.findViewById(R.id.home_price);
        }
    }
}
