package com.hfad.mdb_inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class IndividualPurchseActivity extends AppCompatActivity {
    public static final int DELETE_PURCHASE = 191;
    PurchaseModel purchaseModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_purchse);

        TextView item = findViewById(R.id.individual_item);
        TextView date = findViewById(R.id.individual_date);
        TextView location = findViewById(R.id.individual_location);
        TextView price = findViewById(R.id.individual_price);
        TextView description = findViewById(R.id.individual_description);

        Intent intent = getIntent();
        purchaseModel = (PurchaseModel)intent.getSerializableExtra("purchaseModel");

        item.setText("Item: " + purchaseModel.getItem());
        date.setText("Date: " + purchaseModel.getDate());
        location.setText("From: " + purchaseModel.getLocation());
        price.setText("$" + purchaseModel.getPrice());
        String url = purchaseModel.getImageURL();
        if (url != null) {
            Glide.with(findViewById(R.id.individual_image).getContext()).load(url).into((ImageView)findViewById(R.id.individual_image));
        }
        description.setText(purchaseModel.getDescription());
    }

    public void deleteClicked(View view) {
        new CloudDatabase().deletePurchase(purchaseModel, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent deleteIntent = new Intent();
                    deleteIntent.putExtra("purchaseModel", purchaseModel);
                    IndividualPurchseActivity.this.setResult(DELETE_PURCHASE,deleteIntent);
                    IndividualPurchseActivity.this.finish();
                }else {
                    Exception failureReason = task.getException();
                    if (failureReason == null) {
                        Toast.makeText(IndividualPurchseActivity.this,"Unable to parse failure",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(IndividualPurchseActivity.this, failureReason.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
