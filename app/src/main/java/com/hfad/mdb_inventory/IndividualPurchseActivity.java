package com.hfad.mdb_inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class IndividualPurchseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_purchse);

        TextView item = (TextView) findViewById(R.id.individual_item);
        TextView date = (TextView) findViewById(R.id.individual_date);
        TextView location = (TextView) findViewById(R.id.individual_location);
        TextView price = (TextView) findViewById(R.id.individual_price);
        TextView description = (TextView) findViewById(R.id.individual_description);

        Intent intent = getIntent();
        String _item = intent.getStringExtra("item");
        String _price = intent.getStringExtra("price");
        String _description = intent.getStringExtra("description");
        String _date = intent.getStringExtra("date");
        String _location = intent.getStringExtra("location");

        item.setText("Item: " + _item);
        date.setText("Date: " + _date);
        location.setText("From: " + _location);
        price.setText("$" + _price);
        description.setText(_description);
    }
}
