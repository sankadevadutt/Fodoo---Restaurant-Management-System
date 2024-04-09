package com.example.eres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eres.chef.chefhelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class userproductdisp extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String phone,product,chefphone="";
    ImageButton disp;
    TextView Dishame,value,restame,contact,descrpt;
    Button buy;
    ImageView ivback;
    Spinner spinnerquant;
    int count=0;
    String[]quant = {"1","2","3","4","5","6","7","8","9","10"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //To remove Action Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //Action bar removed

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userproductdisp);


        phone = getIntent().getStringExtra("phone");
        product = getIntent().getStringExtra("product");

        disp = findViewById(R.id.disp);
        Dishame = findViewById(R.id.Dishame);
        value = findViewById(R.id.value);
        restame = findViewById(R.id.Restname);
        contact = findViewById(R.id.conta);
        descrpt = findViewById(R.id.descbod);
        buy = findViewById(R.id.buy);
        ivback = findViewById(R.id.ivbac4);
        spinnerquant = findViewById(R.id.spinnerquant);

        spinnerquant.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.color_spinner_layout, quant);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinnerquant.setAdapter(adapter);



        ActivityCompat.requestPermissions(userproductdisp.this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);


        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),homeUser.class);
                intent.putExtra("phone",phone);
                intent.putExtra("Page","");
                startActivity(intent);
                finish();
            }
        });


        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Products");
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren())
                    {
                        chefhelper updatefood = snapshot2.getValue(chefhelper.class);
                        if(updatefood.getRandonUUID().equals(product))
                        {
                            chefphone = updatefood.getPhone();
                            Dishame.setText(updatefood.getDishname());
                            descrpt.setText(updatefood.getDescription());
                            value.setText("Rs: "+updatefood.getPrice());
                            Glide.with(userproductdisp.this).load(updatefood.getImageURL()).into(disp);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(!chefphone.isEmpty()){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Manager").child(chefphone);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    restame.setText(snapshot.child("ResName").getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cart").child(phone).child(chefphone).child(product);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            count = Integer.parseInt(snapshot.child("quant").getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count+=Integer.parseInt(spinnerquant.getSelectedItem().toString());
                        carthelper ch = new carthelper(product,String.valueOf(count),chefphone,Dishame.getText().toString(),value.getText().toString().substring(4));
                        FirebaseDatabase.getInstance().getReference("cart").child(phone).child(chefphone).child(product).setValue(ch).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(),Dishame.getText().toString()+" is added to cart",Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    }
                }, 1000);

                Intent intent = new Intent(getApplicationContext(),homeUser.class);
                intent.putExtra("phone",phone);
                intent.putExtra("Page","");
                startActivity(intent);
                finish();

            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}