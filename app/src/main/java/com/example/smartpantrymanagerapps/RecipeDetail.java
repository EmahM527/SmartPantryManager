package com.example.smartpantrymanagerapps;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Database.DatabaseHelper;
import Model.Recipe;

public class RecipeDetail extends AppCompatActivity {

    TextView txtRecipeName;
    TextView txtRecipeIngredients;
    TextView txtRecipeMethod;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        txtRecipeName =
                findViewById(R.id.txtRecipeName);

        txtRecipeIngredients =
                findViewById(R.id.txtRecipeIngredients);

        txtRecipeMethod =
                findViewById(R.id.txtRecipeMethod);

        databaseHelper =
                new DatabaseHelper(this);

        int recipeId =
                getIntent().getIntExtra(
                        "recipe_id",
                        -1
                );

        if (recipeId == -1) {

            Toast.makeText(
                    this,
                    "Recipe not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        Recipe recipe =
                databaseHelper.getRecipeById(recipeId);

        if (recipe != null) {

            txtRecipeName.setText(
                    recipe.getName()
            );

            txtRecipeIngredients.setText(
                    recipe.getIngredients()
            );

            txtRecipeMethod.setText(
                    recipe.getMethod()
            );
        }
    }
}