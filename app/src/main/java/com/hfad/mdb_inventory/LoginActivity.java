package com.hfad.mdb_inventory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    private CloudAuthenticator cloudAuthenticator;

    private EditText emailText;
    private EditText passwordText;
    private ProgressBar progressBar;
    private Button loginButton;

    /**
     * This is set to true when this activity is skipped when the user launches the app while signed in
      */
    private boolean didRedirectUserThroughLoginScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cloudAuthenticator = new CloudAuthenticator(this);

        if (cloudAuthenticator.isUserSignedIn()) {
            didRedirectUserThroughLoginScreen = true;
            //The user is already signed in, skip the login entry point and continue straight to authenticated entry
            Intent intent = new Intent(this, MainActivity.class);
            //replace this activity so the back button quits
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            finishActivity(0);
            return;
        }

        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.login);

        setupTextWatchers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if the user launched the app while signed in, this activity is transparently hidden. Pass this activity.
        if (didRedirectUserThroughLoginScreen) {
            this.finish();
        }
    }

    private void setupTextWatchers() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textFieldContentDidChange();
            }
        };

        emailText.addTextChangedListener(watcher);
        passwordText.addTextChangedListener(watcher);

        //call our did change once to apply our initial form configuration
        textFieldContentDidChange();
    }

    private void textFieldContentDidChange() {
        boolean isFormSubmittable = !(emailText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty());

        loginButton.setEnabled(isFormSubmittable);
    }

    public void onClickSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onClickLogin(View view) {
        progressBar.setVisibility(View.VISIBLE);
        cloudAuthenticator.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString(), new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,"Welcome back!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                }else {
                    Exception failureReason = task.getException();
                    if (failureReason == null) {
                        Toast.makeText(LoginActivity.this,"Unable to parse failure",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(LoginActivity.this, failureReason.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
