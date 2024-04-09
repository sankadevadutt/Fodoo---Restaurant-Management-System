package com.example.eres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.eres.user.user_cart;
import com.example.eres.user.user_home;
import com.example.eres.user.user_logout;
import com.example.eres.user.user_search;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class homeUser extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //To remove Action Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //Action bar removed


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        phone = getIntent().getStringExtra("phone");
        String name = getIntent().getStringExtra("Page");

        BottomNavigationView navigationView = findViewById(R.id.user_bottom_navigation);
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("phone",phone);
        if(!name.isEmpty())
        {
            if(name.equalsIgnoreCase("Homepage")){
                fragment = new user_home();
                fragment.setArguments(bundle);
            }else if(name.equals("cart")){
                fragment = new user_cart();
                fragment.setArguments(bundle);
            }
        }else{
            fragment = new user_home();
            fragment.setArguments(bundle);
        }
        loaduserFragment(fragment);
        navigationView.setOnItemSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();



    }
    private boolean loaduserFragment(Fragment fragment) {
        if(fragment!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_user,fragment).commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId())
        {
            case R.id.userhome:
                fragment = new user_home();
                break;
            case R.id.tdsearch:
                fragment = new user_search();
                break;
            case R.id.usercart:
                fragment = new user_cart();
                break;
            case R.id.logout:
                fragment = new user_logout();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("phone",phone);
        fragment.setArguments(bundle);
        return loaduserFragment(fragment);
    }
}