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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Model> models;
    private FloatingActionButton floaty;
    private MyAdapter recycleViewAdapter;
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
        models = ModelArray.models;
        recycleViewAdapter = new MyAdapter(this, models);
        recyclerView.setAdapter(recycleViewAdapter);

        searchBar = findViewById(R.id.search);
        searchBar.setText(getSharedPreferences("SearchConstants", Context.MODE_PRIVATE).getString("last_search",""));

        beginModelFetch();
    }

    private void beginModelFetch() {
        progressBar.setVisibility(View.VISIBLE);
        cloudDatabase.getPurchases(new OnSuccessListener<ArrayList<Model>>() {
            @Override
            public void onSuccess(ArrayList<Model> models) {
                receiveModel(models);
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
        receiveSearchedModel(models,search);
        SharedPreferences.Editor editor = getSharedPreferences("SearchConstants", Context.MODE_PRIVATE).edit();
        editor.putString("last_search", search);
        editor.apply();
    }

    private void receiveSearchedModel(ArrayList<Model> data, String string) {
        this.models = data;
        ArrayList<Model> filtered_data = new ArrayList<Model>();
        for (int i = 0; i < data.size(); i++) {
            String item = models.get(i).getDescription();
            if (item.contains(string)) {
                filtered_data.add(models.get(i));
            }
        }

        recycleViewAdapter.models = filtered_data;
        recycleViewAdapter.notifyDataSetChanged();
    }

    private void receiveModel(ArrayList<Model> data) {
        this.models = data;
        recycleViewAdapter.models = data;
        recycleViewAdapter.notifyDataSetChanged();
    }

    private void insertNewModel(Model model) {
        progressBar.setVisibility(View.VISIBLE);
        cloudDatabase.pushPurchase(model, new OnCompleteListener<Void>() {
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

        models.add(model);
        receiveModel(recycleViewAdapter.models);
    }

    private void removeModel(Model model) {
        models.remove(model);
        receiveModel(models);

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
            Object modelQQ = data.getSerializableExtra("model");
            if (!(modelQQ instanceof Model)) {
                return;
            }
            Model model = (Model)modelQQ;
            insertNewModel(model);
        }else if (resultCode == IndividualPurchseActivity.DELETE_PURCHASE) {
            Object modelQQ = data.getSerializableExtra("model");
            if (!(modelQQ instanceof Model)) {
                return;
            }
            Model model = (Model)modelQQ;
            removeModel(model);
        }
    }
}
