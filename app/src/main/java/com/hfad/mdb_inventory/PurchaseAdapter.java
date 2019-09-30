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

/**
 * Displays an array of purchases as cards
 */
public class PurchaseAdapter extends RecyclerView.Adapter<MyHolder> {
    Activity c;
    ArrayList<PurchaseModel> purchaseModels;
    View view;
    ViewGroup viewGroup;

    public PurchaseAdapter(Activity c, ArrayList<PurchaseModel> purchaseModels) {
        this.c = c;
        this.purchaseModels = purchaseModels;
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
        final PurchaseModel purchaseModel = purchaseModels.get(i);

        myHolder.item.setText(purchaseModel.getItem());
        myHolder.price.setText("$" + purchaseModel.getPrice());
        myHolder.location.setText(purchaseModel.getLocation());
        Glide.with(myHolder.image.getContext()).load(purchaseModel.getImageURL()).into(myHolder.image);


        myHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(c, IndividualPurchseActivity.class);
                intent.putExtra("purchaseModel", purchaseModel);
                c.startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return purchaseModels.size();
    }

}
