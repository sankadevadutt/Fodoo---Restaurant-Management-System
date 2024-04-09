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

import com.example.eres.chef.chef_home;
import com.example.eres.chef.chef_postdish;
import com.example.eres.chef.chef_search;
import com.example.eres.user.chef_logout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class homeManager extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //To remove Action Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //Action bar removed


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_manager);


        phone = getIntent().getStringExtra("phone");
        BottomNavigationView navigationView = findViewById(R.id.chef_bottom_navigation);
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("phone",phone);
        String name = getIntent().getStringExtra("Page");
        if(!name.isEmpty())
        {
            if(name.equalsIgnoreCase("Homepage")){
                fragment = new chef_home();
                fragment.setArguments(bundle);
            }else if(name.equalsIgnoreCase("PostDish")){
                fragment = new chef_postdish();
                fragment.setArguments(bundle);
            }
        }else{
            fragment = new chef_home();
            fragment.setArguments(bundle);
        }
        loadchefFragment(fragment);
        navigationView.setOnItemSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    }

    private boolean loadchefFragment(Fragment fragment) {
        if(fragment!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId())
        {
            case R.id.chefhome:
                fragment = new chef_home();
                break;
            case R.id.tdsearch:
                fragment = new chef_search();
                break;
            case R.id.postdish:
                fragment = new chef_postdish();
                break;
            case R.id.log:
                fragment = new chef_logout();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("phone",phone);
        fragment.setArguments(bundle);
        return loadchefFragment(fragment);
    }
}