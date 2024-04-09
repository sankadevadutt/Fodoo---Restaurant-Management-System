package com.example.eres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView ivback;
    TextInputLayout phn;
    TextInputEditText phone;
    Button btnsubmit;
    ProgressBar pb;
    FirebaseAuth mAuth;
    String phonenumber,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //To remove Action Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //Action bar removed

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        type = getIntent().getStringExtra("Type");

        ivback = findViewById(R.id.ivback);
        phn = findViewById(R.id.phn);
        phone = findViewById(R.id.phone);
        btnsubmit = findViewById(R.id.btnsubmit);
        pb = findViewById(R.id.pb);


        btnsubmit.setVisibility(View.VISIBLE);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });



        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonenumber = phone.getText().toString();
                Pattern p = Pattern.compile("[6-9][0-9]{9}$");
                Matcher m = p.matcher(phonenumber);
                if(phonenumber.isEmpty())
                {
                    phn.setErrorEnabled(true);
                    phn.setError("Field should not be empty");
                }
                else if(m.find())
                {
                    if(type.equals("User"))
                        send(phonenumber,type);
                    else if(type.equals("Manager"))
                    {
                        isManager();
                    }
                }else{
                    phn.setErrorEnabled(true);
                    phn.setError("Invalid phone number format");
                }
            }
        });


    }

    private void send(String phonenumber, String type) {
        btnsubmit.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        phn.setErrorEnabled(false);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phonenumber, 60, TimeUnit.SECONDS, MainActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        btnsubmit.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        btnsubmit.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        btnsubmit.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(),otp.class);
                        intent.putExtra("phone",phonenumber);
                        intent.putExtra("otp",s);
                        intent.putExtra("type",type);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        Boolean b2 = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(View.GONE);
            }
        },2000);
    }

    private void isManager() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Manager");
        Query checkuser = reference.orderByChild("Phone").equalTo(phonenumber);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    phn.setErrorEnabled(false);
                    send(phonenumber,type);
                }else{
                    phn.setErrorEnabled(true);
                    phn.setError("Account doesn't exist");
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Make sure that manager account is registered with admin");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                        }
                    });
                    alert.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}