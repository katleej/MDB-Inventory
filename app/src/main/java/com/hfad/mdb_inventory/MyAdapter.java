package com.hfad.mdb_inventory;

import android.content.Intent;
import android.view.View;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    Context c;
    ArrayList<Model> models;
    View view;
    ViewGroup viewGroup;
    String lower_name;

    public MyAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.box, null);
        this.viewGroup = viewGroup;
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        myHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(c, ClickIndividualActivity.class);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
