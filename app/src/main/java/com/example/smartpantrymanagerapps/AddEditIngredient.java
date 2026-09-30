package com.example.smartpantrymanagerapps;



import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Database.DatabaseHelper;
import Model.Ingredient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddEditIngredient extends AppCompatActivity {

    EditText edtName;
    EditText edtQuantity;
    EditText edtUnit;
    EditText edtExpiry;

    Button btnSave;

    TextView txtTitle;

    DatabaseHelper databaseHelper;

    int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        edtName = findViewById(R.id.edtName);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtUnit = findViewById(R.id.edtUnit);
        edtExpiry = findViewById(R.id.edtExpiry);

        btnSave = findViewById(R.id.btnSave);

        txtTitle = findViewById(R.id.txtTitle);

        databaseHelper = new DatabaseHelper(this);

        if (getIntent().hasExtra("id")) {

            ingredientId = getIntent().getIntExtra("id", -1);

            txtTitle.setText("Edit Ingredient");

            edtName.setText(
                    getIntent().getStringExtra("name")
            );

            edtQuantity.setText(
                    String.valueOf(
                            getIntent().getDoubleExtra("quantity", 0)
                    )
            );

            edtUnit.setText(
                    getIntent().getStringExtra("unit")
            );

            edtExpiry.setText(
                    getIntent().getStringExtra("expiry")
            );

            btnSave.setText("Update Ingredient");
        }

        btnSave.setOnClickListener(v -> saveIngredient());
    }

    private void saveIngredient() {


        String name = edtName.getText().toString().trim();
        String quantityText = edtQuantity.getText().toString().trim();
        String unit = edtUnit.getText().toString().trim();
        String expiry = edtExpiry.getText().toString().trim();

        if (name.isEmpty()) {

            edtName.setError("Enter ingredient name");
            edtName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {

            edtQuantity.setError("Enter quantity");
            edtQuantity.requestFocus();
            return;
        }

        if (unit.isEmpty()) {

            edtUnit.setError("Enter unit");
            edtUnit.requestFocus();
            return;
        }
        if (!expiry.isEmpty()) {

            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dateFormat.setLenient(false);

            try {
                dateFormat.parse(expiry);
            } catch (ParseException e) {
                edtExpiry.setError("Enter a valid date (yyyy-MM-dd)");
                edtExpiry.requestFocus();
                return;
            }
        }


        double quantity;

        try {

            quantity = Double.parseDouble(quantityText);

        } catch (NumberFormatException e) {

            edtQuantity.setError("Enter a valid number");
            return;
        }

        if (quantity <= 0) {

            edtQuantity.setError("Quantity must be greater than zero");
            return;
        }

        Ingredient ingredient = new Ingredient(
                ingredientId,
                name,
                quantity,
                unit,
                expiry
        );

        if (ingredientId == -1) {

            databaseHelper.addIngredient(ingredient);

            Toast.makeText(
                    this,
                    "Ingredient added",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            databaseHelper.updateIngredient(ingredient);

            Toast.makeText(
                    this,
                    "Ingredient updated",
                    Toast.LENGTH_SHORT
            ).show();
        }

        finish();
    }
}