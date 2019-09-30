package com.hfad.mdb_inventory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IndividualPurchseActivity extends AppCompatActivity {

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
        Model model = (Model)intent.getSerializableExtra("model");

        item.setText("Item: " + model.getItem());
        date.setText("Date: " + model.getDate());
        location.setText("From: " + model.getLocation());
        price.setText("$" + model.getPrice());
        description.setText(model.getDescription());
    }
}
