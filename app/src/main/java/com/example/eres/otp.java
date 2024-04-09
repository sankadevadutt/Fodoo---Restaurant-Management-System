package com.example.eres;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
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

public class otp extends AppCompatActivity {

    TextView tvmobile,tvresend;
    Button btnverify;
    TextInputLayout otp1,otp2,otp3,otp4,otp5,otp6;
    TextInputEditText ot1,ot2,ot3,ot4,ot5,ot6;
    String otp;
    String type;
    int i = 60;
    ProgressBar pb2;
    String phonenumber;
    ImageView ivback;

    FirebaseDatabase rootNode;
    DatabaseReference reference1;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("Type",type);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //To remove Action Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //Action bar removed

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        phonenumber = getIntent().getStringExtra("phone");
        otp = getIntent().getStringExtra("otp");
        type = getIntent().getStringExtra("type");

        tvmobile = findViewById(R.id.tvMobile);
        tvmobile.setText(phonenumber);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        ot1 = findViewById(R.id.ot1);
        ot2 = findViewById(R.id.ot2);
        ot3 = findViewById(R.id.ot3);
        ot4 = findViewById(R.id.ot4);
        ot5 = findViewById(R.id.ot5);
        ot6 = findViewById(R.id.ot6);
        tvresend = findViewById(R.id.tvResend);
        btnverify = findViewById(R.id.btnVerify);
        pb2 = findViewById(R.id.pb2);
        ivback = findViewById(R.id.ivback4);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("Type",type);
                startActivity(intent);
                finish();
            }
        });

        otpfields();
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(),homeUser.class);
                intent.putExtra("phone",phonenumber);
                intent.putExtra("Page","");
                startActivity(intent);
                finish();
                String otpentered;
                if(ot1.getText().toString().isEmpty()||
                        ot2.getText().toString().isEmpty()||
                        ot3.getText().toString().isEmpty()||
                        ot4.getText().toString().isEmpty()||
                        ot5.getText().toString().isEmpty()||
                        ot6.getText().toString().isEmpty()
                )Toast.makeText(otp.this, "Enter valid otp", Toast.LENGTH_SHORT).show();
                else
                {
                    otpentered = ot1.getText().toString()+
                            ot2.getText().toString()+
                            ot3.getText().toString()+
                            ot4.getText().toString()+
                            ot5.getText().toString()+
                            ot6.getText().toString();
                    if(otp==null)
                        Toast.makeText(otp.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                    else
                    {
                        pb2.setVisibility(View.VISIBLE);
                        btnverify.setVisibility(View.GONE);
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otp,otpentered);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    if(type.equals("Manager"))
                                    {
                                        Intent intent = new Intent(getApplicationContext(),homeManager.class);
                                        intent.putExtra("phone",phonenumber);
                                        intent.putExtra("Page","");
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        isUser();
                                    }
                                }
                                else
                                {
                                    pb2.setVisibility(View.GONE);
                                    btnverify.setVisibility(View.VISIBLE);
                                    Toast.makeText(otp.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });


        new CountDownTimer(60000,1000){
            @Override
            public void onTick(long l) {
                tvresend.setText(""+i);
                i--;
            }

            @Override
            public void onFinish() {
                tvresend.setText("Resend OTP");
            }
        }.start();





        tvresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+phonenumber, 60, TimeUnit.SECONDS, otp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otp = s;
                                i = 60;
                                new CountDownTimer(60000,1000){
                                    @Override
                                    public void onTick(long l) {
                                        tvresend.setText(""+i);
                                        i--;
                                    }

                                    @Override
                                    public void onFinish() {
                                        tvresend.setText("Resend OTP");
                                    }
                                }.start();
                            }
                        }
                );
            }
        });
    }





    private void isUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkuser = reference.orderByChild("phone").equalTo(phonenumber);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists())
                {
                    rootNode = FirebaseDatabase.getInstance();
                    reference1 = rootNode.getReference("Users");
                    UserHelperClass helperClass = new UserHelperClass(phonenumber,"");
                    reference1.child(phonenumber).setValue(helperClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    private void otpfields() {
        ot1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) ot2.requestFocus(); }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        ot2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) ot3.requestFocus();
                else ot1.requestFocus(); }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        ot3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) ot4.requestFocus();
                else ot2.requestFocus(); }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        ot4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) ot5.requestFocus();
                else ot3.requestFocus(); }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        ot5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) ot6.requestFocus();
                else ot4.requestFocus(); }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        ot6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) ;
                else ot5.requestFocus(); }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }


}