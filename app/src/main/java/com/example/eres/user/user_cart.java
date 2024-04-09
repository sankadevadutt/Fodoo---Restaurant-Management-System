package com.example.eres.user;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eres.R;
import com.example.eres.carthelper;
import com.example.eres.chef.chefhelper;
import com.example.eres.homeUser;
import com.example.eres.userproductdisp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class user_cart extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference dataa;
    String phone;
    private List<carthelper> cartitems;
    private usercartAdapter adapter;
    Button buy;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_cart,null);
        phone = getArguments().getString("phone");
        recyclerView = v.findViewById(R.id.recycle_);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataa = FirebaseDatabase.getInstance().getReference().child(phone);
        buy = v.findViewById(R.id.buy);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cart").child(phone);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartitems.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren())
                    {
                        carthelper ch = snapshot2.getValue(carthelper.class);
                        cartitems.add(ch);
                    }
                }
                adapter = new usercartAdapter(getContext(),cartitems,phone);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,List<String>> mp = new HashMap<>();
                List<String> hs = new ArrayList<>();
                for(carthelper ch : cartitems)
                {
                    if(!hs.contains(ch.getChefphone()))
                        hs.add(ch.getChefphone());
                }
                for(int i =0;i<hs.size();i++)
                {
                    mp.put(hs.get(i),new ArrayList<>());
                }
                for(carthelper ch : cartitems)
                {
                    mp.get(ch.getChefphone()).add(""+ch.getDish()+" * "+ch.getQuant());
                }
                for(String ph : mp.keySet())
                {
                    StringBuilder message = new StringBuilder("Hello manager I would like to buy the following items:\n");
                    for(String s : mp.get(ph))
                    {
                        message.append(s);
                        message.append("\n");
                    }
                    sendSMS(ph,message.toString());
                }
                FirebaseDatabase.getInstance().getReference("cart").child(phone).removeValue();
                Intent intent = new Intent(getActivity(), homeUser.class);
                intent.putExtra("phone",phone);
                intent.putExtra("Page","");
                startActivity(intent);
            }
        });
        cartitems = new ArrayList<>();
        return v;
    }
    private void sendSMS(String phonecontact,String message)
    {
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phonecontact,null,message,null,null);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
