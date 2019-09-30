package com.hfad.mdb_inventory;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView image;
    TextView item, location, price, purchaser;
    ItemClickListener itemClickListener;


    public MyHolder(@NonNull View itemView) {
        super(itemView);

        this.image = itemView.findViewById(R.id.card_image);
        this.item = itemView.findViewById(R.id.card_item);
        this.location = itemView.findViewById(R.id.card_location);
        this.price = itemView.findViewById(R.id.card_price);
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
