package com.example.eres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eres.chef.chefhelper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class productdip extends AppCompatActivity {

    ImageView ivback,ivedit,addimage;
    String phone,product;
    TextInputLayout dish,desc,quantity,price;
    DatabaseReference databaseReference;
    Button del;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //To remove Action Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //Action bar removed

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdip);
        phone = getIntent().getStringExtra("phone");
        product = getIntent().getStringExtra("product");


        ivback = findViewById(R.id.ivbac);
        ivedit = findViewById(R.id.ivedit);
        dish = findViewById(R.id.dishn);
        desc = findViewById(R.id.desc);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        addimage = findViewById(R.id.addimage);
        del = findViewById(R.id.dele);


        databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(phone).child(product);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chefhelper chef = snapshot.getValue(chefhelper.class);
                if(chef!=null)
                {
                    dish.getEditText().setText(chef.getDishname());
                    desc.getEditText().setText(chef.getDescription());
                    quantity.getEditText().setText(chef.getQuantity());
                    price.getEditText().setText(chef.getQuantity());
                    Glide.with(productdip.this).load(chef.getImageURL()).into(addimage);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(productdip.this);
                builder.setMessage("Are you sure you want to delete the dish");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference("Products").child(phone).child(product).removeValue();
                        AlertDialog.Builder food = new AlertDialog.Builder(productdip.this);
                        food.setMessage("Dish has been deleted");
                        food.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getApplicationContext(),homeManager.class);
                                intent.putExtra("phone",phone);
                                intent.putExtra("Page","");
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alert = food.create();
                        alert.show();
                        Button positive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                        positive.setTextColor(Color.WHITE);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE),negat = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                positive.setTextColor(Color.WHITE);
                negat.setTextColor(Color.WHITE);
            }
        });

        ivedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),productedit.class);
                intent.putExtra("phone",phone);
                intent.putExtra("product",product);
                startActivity(intent);
                finish();
            }
        });
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),homeManager.class);
                intent.putExtra("phone",phone);
                intent.putExtra("Page","");
                startActivity(intent);
                finish();
            }
        });
    }
}