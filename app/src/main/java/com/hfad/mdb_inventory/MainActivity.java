package com.hfad.mdb_inventory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Displays a list of purchases, allowing searching, adding, and drilling down for more info
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<PurchaseModel> purchaseModels;
    private FloatingActionButton floaty;
    private PurchaseAdapter recycleViewAdapter;
    private ProgressBar progressBar;
    private EditText searchBar;
    private CloudDatabase cloudDatabase;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_statistics:
                    Intent statIntents = new Intent(getApplicationContext(), StatisticsActivity.class);
                    startActivity(statIntents);
                    return true;
            }
            return false;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cloudDatabase = new CloudDatabase();

        //
        // UI creation/referencing
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //define floaty
        floaty = findViewById(R.id.floating_button);
        floaty.bringToFront();
        floaty.setOnClickListener(this);

        findViewById(R.id.search_button ).setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);

        //setting MainActivity with recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        purchaseModels = new ArrayList<>();
        recycleViewAdapter = new PurchaseAdapter(this, purchaseModels);
        recyclerView.setAdapter(recycleViewAdapter);

        searchBar = findViewById(R.id.search);
        searchBar.setText(getSharedPreferences("SearchConstants", Context.MODE_PRIVATE).getString("last_search",""));

        beginModelFetch();
    }

    private void beginModelFetch() {
        progressBar.setVisibility(View.VISIBLE);
        cloudDatabase.getPurchases(new OnSuccessListener<ArrayList<PurchaseModel>>() {
            @Override
            public void onSuccess(ArrayList<PurchaseModel> purchaseModels) {
                receiveModel(purchaseModels);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void beingModelSearch(String search) {
        receiveSearchedModel(purchaseModels,search);
        SharedPreferences.Editor editor = getSharedPreferences("SearchConstants", Context.MODE_PRIVATE).edit();
        editor.putString("last_search", search);
        editor.apply();
    }

    private void receiveSearchedModel(ArrayList<PurchaseModel> data, String string) {
        this.purchaseModels = data;
        ArrayList<PurchaseModel> filtered_data = new ArrayList<PurchaseModel>();
        for (int i = 0; i < data.size(); i++) {
            String item = purchaseModels.get(i).getDescription();
            if (item.contains(string)) {
                filtered_data.add(purchaseModels.get(i));
            }
        }

        recycleViewAdapter.purchaseModels = filtered_data;
        recycleViewAdapter.notifyDataSetChanged();
    }

    private void receiveModel(ArrayList<PurchaseModel> data) {
        this.purchaseModels = data;
        recycleViewAdapter.purchaseModels = data;
        recycleViewAdapter.notifyDataSetChanged();
    }

    private void insertNewModel(PurchaseModel purchaseModel) {
        progressBar.setVisibility(View.VISIBLE);
        cloudDatabase.pushPurchase(purchaseModel, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this,"Added!",Toast.LENGTH_SHORT).show();
                }else {
                    Exception failureReason = task.getException();
                    if (failureReason == null) {
                        Toast.makeText(MainActivity.this,"Unable to parse failure",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(MainActivity.this, failureReason.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        purchaseModels.add(purchaseModel);
        receiveModel(purchaseModels);
    }

    private void removeModel(PurchaseModel purchaseModel) {
        purchaseModels.remove(purchaseModel);
        receiveModel(purchaseModels);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_button:
                Intent intent = new Intent(this, AddNewActivity.class);
                startActivityForResult(intent,0);
            case R.id.search_button:
                String search_input = searchBar.getText().toString();
                beingModelSearch(search_input);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            Object modelQQ = data.getSerializableExtra("purchaseModel");
            if (!(modelQQ instanceof PurchaseModel)) {
                return;
            }
            PurchaseModel purchaseModel = (PurchaseModel)modelQQ;
            insertNewModel(purchaseModel);
        }else if (resultCode == IndividualPurchseActivity.DELETE_PURCHASE) {
            Object modelQQ = data.getSerializableExtra("purchaseModel");
            if (!(modelQQ instanceof PurchaseModel)) {
                return;
            }
            PurchaseModel purchaseModel = (PurchaseModel)modelQQ;
            removeModel(purchaseModel);
        }
    }
}
