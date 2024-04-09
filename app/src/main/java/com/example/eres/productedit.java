package com.example.eres;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eres.chef.chefhelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class productedit extends AppCompatActivity {

    ImageButton addimage;
    TextInputLayout dish,desc,quantity,price;
    String Dishname,Description,Quantity,Price,RandonUid,dburi;
    Button postsubmit;
    Uri imageuri;
    private Uri cropimageuri;
    FirebaseStorage storage;
    StorageReference storageReference,ref;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,dataa;
    String phone,product;
    ImageView ivback3;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //To remove Action Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //Action bar removed

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productedit);

        databaseReference = firebaseDatabase.getInstance().getReference("Products");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        phone = getIntent().getStringExtra("phone");
        product = getIntent().getStringExtra("product");
        addimage = findViewById(R.id.addimage);
        postsubmit = findViewById(R.id.postsubmit);
        dish = findViewById(R.id.dish);
        desc = findViewById(R.id.desc);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        ivback3 = findViewById(R.id.ivbac3);

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
                    Glide.with(productedit.this).load(chef.getImageURL()).into(addimage);
                    dburi = chef.getImageURL();
                    RandonUid = chef.getRandonUUID();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
            ivback3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),productdip.class);
                    intent.putExtra("phone",phone);
                    intent.putExtra("product",product);
                    startActivity(intent);
                    finish();
                }
            });

        try{
            dataa = firebaseDatabase.getInstance().getReference("Manager").child(phone);
            dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    addimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onSelectImageclick(view);
                        }
                    });
                    postsubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Dishname = dish.getEditText().getText().toString().trim();
                            Description = desc.getEditText().getText().toString().trim();
                            Quantity = quantity.getEditText().getText().toString().trim();
                            Price = price.getEditText().getText().toString().trim();
                            if(isValid())
                            {
                                if(imageuri!=null)
                                    uploadImage();
                                else
                                    updatedesc(dburi);
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Log.d("Error",e.getMessage());
        }








    }

    private void updatedesc(String dburi) {
        chefhelper chefhelper = new chefhelper(Dishname,Quantity,Description,Price,dburi,RandonUid,phone);
        firebaseDatabase.getInstance().getReference("Products").child(phone).child(product).setValue(chefhelper).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(productedit.this, "Dish updated Succesfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),homeManager.class);
                intent.putExtra("phone",phone);
                intent.putExtra("Page","");
                startActivity(intent);
                finish();
            }
        });
    }

    private void uploadImage() {
        if(imageuri!=null)
        {
            progressDialog = new ProgressDialog(productedit.this);
            progressDialog.setTitle("Uploading.....");
            progressDialog.show();
            ref = storageReference.child(RandonUid);
            ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            chefhelper helper = new chefhelper(Dishname,Quantity,Description,Price,String.valueOf(uri),RandonUid,phone);
                            firebaseDatabase.getInstance().getReference("Products").child(phone).child(RandonUid).setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Dish posted succesfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),homeManager.class);
                                    intent.putExtra("phone",phone);
                                    intent.putExtra("Page","");
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = 100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded "+(int) progress+"%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }else {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValid() {
        Pattern p = Pattern.compile("^[a-zA-Z\\s\\.]*$");
        Pattern p2 = Pattern.compile("^[0-9]*$");
        Matcher mdish = p.matcher(Dishname);
        Matcher mdesc = p.matcher(Description);
        Matcher mquan = p2.matcher(Quantity);
        Matcher mprice = p2.matcher(Price);
        boolean isvaliddesc=false,isvaliddish=false,isvalidquant=false,isvalidprice=false;

        if(TextUtils.isEmpty(Description)){
            isvaliddesc = false;
            desc.setErrorEnabled(true);
            desc.setError("Description is required");
        }else{
            isvaliddesc = true;
            desc.setErrorEnabled(false);
        }

        if(TextUtils.isEmpty(Dishname)){
            isvaliddish = false;
            dish.setErrorEnabled(true);
            dish.setError("Dish name is required");
        }else if(!mdish.find()){
            isvaliddish = false;
            dish.setErrorEnabled(true);
            dish.setError("Only characters are allowed");
        }else{
            isvaliddish = true;
            dish.setErrorEnabled(false);
        }

        if(TextUtils.isEmpty(Price)){
            isvalidprice = false;
            price.setErrorEnabled(true);
            price.setError("Price is required");
        }else if(!mprice.find()){
            isvalidprice = false;
            price.setErrorEnabled(true);
            price.setError("Only numbers are allowed");
        }else{
            isvalidprice = true;
            price.setErrorEnabled(false);
        }

        if(!mquan.find()) {
            isvalidquant = false;
            quantity.setErrorEnabled(true);
            quantity.setError("Only numbers are allowed");
        }else{
            isvalidquant = true;
            quantity.setErrorEnabled(false);
        }

        boolean isvalid = (isvaliddesc && isvaliddish && isvalidprice && isvalidquant)?true:false;
        return isvalid;
    }

    private void startCropImageActivity(Uri imageuri){
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }
    private void onSelectImageclick(View v){
        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressWarnings("Depricated")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (cropimageuri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(cropimageuri);
        } else {
            Toast.makeText(this, "Cancelling! Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NewApi")
    @SuppressWarnings("Depricated")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            imageuri = CropImage.getPickImageResultUri(this,data);
            if(CropImage.isReadExternalStoragePermissionsRequired(this,imageuri)){
                cropimageuri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }else{
                startCropImageActivity(imageuri);
            }
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                addimage.setImageURI(result.getUri());
                imageuri = result.getUri();
                Toast.makeText(this,"Cropped Successfully!",Toast.LENGTH_SHORT).show();
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(this,"Failed To Crop"+result.getError(),Toast.LENGTH_SHORT).show();

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}