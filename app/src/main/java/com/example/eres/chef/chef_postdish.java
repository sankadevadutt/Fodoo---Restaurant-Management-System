package com.example.eres.chef;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eres.R;

public class chef_postdish extends Fragment {


    Button btnpost;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.chef_postdish,null);
        String phone = getArguments().getString("phone");
        btnpost = v.findViewById(R.id.btnpost);
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),com.example.eres.add_product.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
        });
        return v;

    }


}
