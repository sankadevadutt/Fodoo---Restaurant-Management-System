package com.example.eres.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eres.MainActivity2;
import com.example.eres.R;
import com.example.eres.homeManager;
import com.example.eres.homeUser;

public class chef_logout extends Fragment {
    String phone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chef_logout,null);
        phone = getArguments().getString("phone");
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Logout")
                .setMessage("Do you want to logout")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), MainActivity2.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), homeManager.class);
                        intent.putExtra("phone",phone);
                        intent.putExtra("Page","");
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);




        return v;

    }
}