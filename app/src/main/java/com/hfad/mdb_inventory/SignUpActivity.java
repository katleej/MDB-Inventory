package com.hfad.mdb_inventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.sign_up_button:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;


            default:
                break;
        }
    }
}
