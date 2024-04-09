package com.example.eres.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eres.MainActivity2;
import com.example.eres.R;
import com.example.eres.carthelper;
import com.example.eres.homeUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class usercartAdapter  extends RecyclerView.Adapter<usercartAdapter.ViewHolder>{

    Context context;
    List<carthelper> cartitems;
    String phone;
    public usercartAdapter(Context context, List<carthelper> cartitems, String phone) {
        this.context = context;
        this.cartitems = cartitems;
        this.phone = phone;
    }

    @NonNull
    @Override
    public usercartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.usercart,parent,false);
        return new usercartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final carthelper ch = cartitems.get(position);
        holder.cartval.setText(String.valueOf(Integer.parseInt(ch.getQuant())*Integer.parseInt(ch.getPrice())));
        holder.cartquan.setText(ch.getQuant());
        holder.dn.setText(ch.getDish());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("Do you want to delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase.getInstance().getReference("cart").child(phone).child(ch.getChefphone()).child(ch.getProduct()).removeValue();
                                Intent intent = new Intent(context, homeUser.class);
                                intent.putExtra("phone",phone);
                                intent.putExtra("Page","cart");
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(context, homeUser.class);
                                intent.putExtra("phone",phone);
                                intent.putExtra("Page","cart");
                                context.startActivity(intent);
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dn,cartquan,cartval;
        ImageButton del;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dn = itemView.findViewById(R.id.dn);
            cartquan = itemView.findViewById(R.id.cartquan);
            cartval = itemView.findViewById(R.id.cartval);
            del = itemView.findViewById(R.id.del);
        }
    }
}
