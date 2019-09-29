package com.hfad.mdb_inventory;

import android.content.Intent;
import android.view.View;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    Context c;
    ArrayList<Model> models;
    View view;
    ViewGroup viewGroup;
    String item, price, image, purchaser, location, description, date;

    public MyAdapter(Context c, ArrayList<Model> models) {
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
        item = models.get(i).getItem();
        price = models.get(i).getPrice();
        image = models.get(i).getImageURL();
        description = models.get(i).getDescription();
        date = models.get(i).getDate();
        purchaser = "random purchaser"; //this must be fixed to ID later
        location = models.get(i).getLocation();

        myHolder.item.setText(item);
        myHolder.price.setText("$" + price);
        myHolder.purchaser.setText(purchaser);
        myHolder.location.setText(location);
        Glide.with(myHolder.image.getContext()).load(image).into(myHolder.image);


        myHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(c, IndividualPurchseActivity.class);
                intent.putExtra("item", item);
                intent.putExtra("price", price);
                intent.putExtra("date", date);
                intent.putExtra("description", description);
                intent.putExtra("purchaser", purchaser);
                intent.putExtra("location", location);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
