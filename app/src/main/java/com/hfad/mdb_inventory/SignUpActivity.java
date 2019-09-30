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

/**
 * Allows the creation of new accounts, forwards to MainActivity
 */
public class SignUpActivity extends AppCompatActivity {
    private CloudAuthenticator cloudAuthenticator;

    private EditText nameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private ProgressBar progressBar;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        cloudAuthenticator = new CloudAuthenticator(this);

        nameText = findViewById(R.id.name);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        confirmPasswordText = findViewById(R.id.passwordConfirmed);
        progressBar = findViewById(R.id.progressBar);
        signUpButton = findViewById(R.id.sign_up_button);

        setupTextWatchers();
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

        nameText.addTextChangedListener(watcher);
        emailText.addTextChangedListener(watcher);
        passwordText.addTextChangedListener(watcher);
        confirmPasswordText.addTextChangedListener(watcher);

        //call our did change once to apply our initial form configuration
        textFieldContentDidChange();
    }

    private void textFieldContentDidChange() {
        boolean isFormSubmittable = !(nameText.getText().toString().isEmpty() || emailText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty() || confirmPasswordText.getText().toString().isEmpty()
                && passwordText.getText().toString().equals(confirmPasswordText.getText().toString()));

        signUpButton.setEnabled(isFormSubmittable);
    }


    public void onClickSignUp(View view) {
        progressBar.setVisibility(View.VISIBLE);

        cloudAuthenticator.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString(), new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    new CloudDatabase().finishUserRegistration(cloudAuthenticator.getCurrentUser(),nameText.getText().toString());
                    Toast.makeText(SignUpActivity.this,"Welcome!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Exception failureReason = task.getException();
                    if (failureReason == null) {
                        Toast.makeText(SignUpActivity.this,"Unable to parse failure",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(SignUpActivity.this, failureReason.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
