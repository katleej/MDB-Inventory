package com.hfad.mdb_inventory;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    Activity c;
    ArrayList<Model> models;
    View view;
    ViewGroup viewGroup;

    public MyAdapter(Activity c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.purchase_row, null);
        this.viewGroup = viewGroup;
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        final Model model = models.get(i);

        myHolder.item.setText(model.getItem());
        myHolder.price.setText("$" + model.getPrice());
        myHolder.location.setText(model.getLocation());
        Glide.with(myHolder.image.getContext()).load(model.getImageURL()).into(myHolder.image);


        myHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(c, IndividualPurchseActivity.class);
                intent.putExtra("model", model);
                c.startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
