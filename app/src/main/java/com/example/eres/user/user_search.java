package com.example.eres.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eres.R;
import com.example.eres.chef.chefhelper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class user_search extends Fragment implements AdapterView.OnItemSelectedListener{
    RecyclerView recyclerView;
    private List<chefhelper> updatefoodList;
    private searchadapter adapter;
    DatabaseReference databaseReference,dataa;
    SwipeRefreshLayout swipeRefreshLayout;
    String name,phone;
    TextInputLayout usersearch;
    AutoCompleteTextView sear;
    Button btnsearch;
    List<String> dishn = new ArrayList<>();
    @Override
    public void onResume() {
        super.onResume();
        sear.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.dropdownitem,dishn);
        adapter.setDropDownViewResource(R.layout.dropdownitem);
        sear.setAdapter(adapter);


    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_search,null);
        phone = getArguments().getString("phone");
        recyclerView = v.findViewById(R.id.recycle_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout = v.findViewById(R.id.swipelayout);
        usersearch = v.findViewById(R.id.tdsearch);
        btnsearch = v.findViewById(R.id.btnsearch);
        sear = v.findViewById(R.id.sear);

        updatefoodList = new ArrayList<chefhelper>();
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.move);
        recyclerView.startAnimation(animation);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren())
                    {
                        chefhelper ch = snapshot2.getValue(chefhelper.class);
                        dishn.add(ch.getDishname());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usersearch.getEditText().getText().toString().isEmpty())
                {
                    usersearch.setErrorEnabled(true);
                    usersearch.setError("Empty");
                }else if(!dishn.contains(usersearch.getEditText().getText().toString()))
                {
                    usersearch.setErrorEnabled(true);
                    usersearch.setError("Product "+usersearch.getEditText().getText().toString()+" doesn't exist");
                }else {
                    usersearch.setErrorEnabled(false);
                    dataa = FirebaseDatabase.getInstance().getReference("Products");
                    dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            updatefoodList.clear();
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                for (DataSnapshot snapshot2:snapshot1.getChildren()){
                                    chefhelper updatefood = snapshot2.getValue(chefhelper.class);
                                    if(updatefood.getDishname().equals(usersearch.getEditText().getText().toString()))
                                        updatefoodList.add(updatefood);
                                }
                            }
                            adapter = new searchadapter(getContext(),updatefoodList,phone);
                            recyclerView.setAdapter(adapter);
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
