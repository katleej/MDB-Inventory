package com.hfad.mdb_inventory;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView image;
    TextView name, type, attack, health, defense;
    ItemClickListener itemClickListener;


    public MyHolder(@NonNull View itemView) {
        super(itemView);

        this.image = itemView.findViewById(R.id.image);
        this.name = itemView.findViewById(R.id.name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClickListener(view, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
