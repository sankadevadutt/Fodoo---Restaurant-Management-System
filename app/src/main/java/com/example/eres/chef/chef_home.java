package com.example.eres.chef;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eres.R;
import com.example.eres.UserHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chef_home extends Fragment{
    RecyclerView recyclerView;
    private List<chefhelper> updatefoodList;
    private chefhomeAdapter adapter;
    DatabaseReference databaseReference,dataa;
    SwipeRefreshLayout swipeRefreshLayout;
    String name,phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.chef_home,null);
        phone = getArguments().getString("phone");
        recyclerView = v.findViewById(R.id.recycle_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataa = FirebaseDatabase.getInstance().getReference().child(phone);
        dataa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userHelperClass = new UserHelperClass(UserHelperClass.class);
                chefDishes();
            }

            private void chefDishes() {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(phone);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        updatefoodList.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                chefhelper updatefood = snapshot1.getValue(chefhelper.class);
                                updatefoodList.add(updatefood);
                        }
                        adapter = new chefhomeAdapter(getContext(),updatefoodList,phone);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        updatefoodList = new ArrayList<>();
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.move);
        recyclerView.startAnimation(animation);

        return v;

    }
}
