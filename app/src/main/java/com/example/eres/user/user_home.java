package com.example.eres.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eres.R;
import com.example.eres.UserHelperClass;
import com.example.eres.chef.chefhelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class user_home extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    RecyclerView recyclerView;
    private List<chefhelper> updatefoodList;
    private userhomeAdapter adapter;
    DatabaseReference databaseReference,dataa;
    SwipeRefreshLayout swipeRefreshLayout;
    String name,phone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.user_home,null);


        phone = getArguments().getString("phone");
        recyclerView = v.findViewById(R.id.recycle_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout = v.findViewById(R.id.swipelayout);



        updatefoodList = new ArrayList<chefhelper>();
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.move);
        recyclerView.startAnimation(animation);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.purple_500,R.color.blue);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                dataa = FirebaseDatabase.getInstance().getReference("Users").child(phone);
                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        customermenu();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



        return v;

    }

    @Override
    public void onRefresh() {
        customermenu();
    }

    private void customermenu() {
        swipeRefreshLayout.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updatefoodList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    for (DataSnapshot snapshot2:snapshot1.getChildren()){
                        chefhelper updatefood = snapshot2.getValue(chefhelper.class);
                        updatefoodList.add(updatefood);
                    }
                }
                adapter = new userhomeAdapter(getContext(),updatefoodList,phone);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
