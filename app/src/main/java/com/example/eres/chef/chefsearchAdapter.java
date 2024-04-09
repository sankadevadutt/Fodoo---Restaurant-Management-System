package com.example.eres.chef;

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
import com.example.eres.productdip;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class chefsearchAdapter extends RecyclerView.Adapter<chefsearchAdapter.ViewHolder> {
    private Context mcontext;
    private List<chefhelper> updatefoodList;
    private String phone;
    DatabaseReference databaseReference;

    public chefsearchAdapter(Context context, List<chefhelper>updatefoodList,String phone)
    {
        this.updatefoodList = updatefoodList;
        this.mcontext = context;
        this.phone = phone;
    }

    @NonNull
    @Override
    public chefsearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.chefmenu_update_delete,parent,false);
        return new chefsearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final chefhelper updatefood = updatefoodList.get(position);
        Glide.with(mcontext).load(updatefood.getImageURL()).into(holder.imageView);
        holder.DishName.setText(updatefood.getDishname());
        updatefood.getRandonUUID();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,com.example.eres.productdip.class);
                intent.putExtra("phone",phone);
                intent.putExtra("product",updatefood.getRandonUUID());
                mcontext.startActivity(intent);

            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,com.example.eres.productdip.class);
                intent.putExtra("phone",phone);
                intent.putExtra("product",updatefood.getRandonUUID());
                mcontext.startActivity(intent);
            }
        });
        holder.DishName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,com.example.eres.productdip.class);
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
        TextView DishName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.chef_product);
            DishName = itemView.findViewById(R.id.chef_dish);
        }
    }
}