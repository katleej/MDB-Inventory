package com.hfad.mdb_inventory;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class AddNewActivity extends AppCompatActivity implements View.OnClickListener {
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog datePickerDialog;
    EditText item, date, location, price, description;
    ImageView image;
    Button save_button;
    ImageButton image_button;
    String uploadedImageURL;
    private ProgressBar progressBar;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<EditText> editTextArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        //Calling all Views
        image = findViewById(R.id.image);
        item = findViewById(R.id.item);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        image_button = findViewById(R.id.add_image_button);
        save_button = findViewById(R.id.save);
        progressBar = findViewById(R.id.progressBar);
        image_button.setOnClickListener(this);
        save_button.setOnClickListener(this);

        //add all editTexts in an array to make sure all the items are filled
        editTextArrayList.add(item);
        editTextArrayList.add(date);
        editTextArrayList.add(location);
        editTextArrayList.add(price);
        editTextArrayList.add(description);


        //Setting Calendar on date
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int _year = myCalendar.get(Calendar.YEAR);
                int _month = myCalendar.get(Calendar.MONTH);
                int _day = myCalendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AddNewActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                date.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, _year,_month,_day);
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_image_button:
                //getting image from one's gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                break;
            case R.id.save:
                //if there are any empty fields, make the user fill all of them
                if (!checkingInputs()) {
                    Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                } else {
                    //if all fields are complete, create a new model
                    Model model = new Model();
                    model.setItem(item.getText().toString());
                    model.setLocation(location.getText().toString());
                    model.setDescription(description.getText().toString());
                    model.setPrice(price.getText().toString());
                    model.setDate(date.getText().toString());
                    model.setImageURL(uploadedImageURL);

                    Intent goBackToMain = new Intent();
                    goBackToMain.putExtra("model",model);
                    setResult(RESULT_OK,goBackToMain);
                    finish();
                }
        }
    }

    public boolean checkingInputs() {
        for (int i = 0; i < editTextArrayList.size(); i++) {
            if (editTextArrayList.get(i).getText().toString().equals("")) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                progressBar.setVisibility(View.VISIBLE);
                new CloudStorage().upload(imageUri, new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        image.setImageBitmap(selectedImage);
                        uploadedImageURL = s;
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddNewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Incorrect requestcode", Toast.LENGTH_SHORT).show();
        }
    }

}
