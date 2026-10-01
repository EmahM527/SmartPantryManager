package com.example.smartpantrymanagerapps;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Adapter.RecipeAdapter;
import Database.DatabaseHelper;
import Model.Ingredient;
import Model.Recipe;

public class SuggestedRecipes extends AppCompatActivity {

    RecyclerView recyclerViewRecipes;
    TextView txtNoRecipes;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggested_recipes);

        recyclerViewRecipes =
                findViewById(R.id.recyclerViewRecipes);

        txtNoRecipes =
                findViewById(R.id.txtNoRecipes);

        databaseHelper =
                new DatabaseHelper(this);

        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        ArrayList<Ingredient> pantry =
                databaseHelper.getAllIngredients();

        ArrayList<Recipe> allRecipes =
                databaseHelper.getAllRecipes();

        ArrayList<Recipe> matchingRecipes =
                new ArrayList<>();

        /*
         * Store pantry ingredients using:
         * ingredient name -> quantity
         *
         * Quantities are converted to a common base
         * where possible.
         */
        Map<String, Double> pantryMap =
                new HashMap<>();

        Map<String, String> pantryUnits =
                new HashMap<>();

        for (Ingredient ingredient : pantry) {

            String name =
                    normalizeIngredient(
                            ingredient.getName()
                    );

            String unit =
                    normalizeUnit(
                            ingredient.getUnit()
                    );

            double quantity =
                    convertToBaseQuantity(
                            ingredient.getQuantity(),
                            unit
                    );

            String key =
                    name + "|" + getUnitType(unit);

            double current =
                    pantryMap.containsKey(key)
                            ? pantryMap.get(key)
                            : 0;

            pantryMap.put(
                    key,
                    current + quantity
            );

            pantryUnits.put(
                    key,
                    unit
            );
        }

        for (Recipe recipe : allRecipes) {

            if (recipeMatchesPantry(
                    recipe,
                    pantryMap
            )) {

                matchingRecipes.add(recipe);
            }
        }
        RecipeAdapter adapter =
                new RecipeAdapter(
                        this,
                       matchingRecipes

                );

        recyclerViewRecipes.setAdapter(adapter);

        if (matchingRecipes.isEmpty()) {

            txtNoRecipes.setVisibility(
                    TextView.VISIBLE
            );

        } else {

            txtNoRecipes.setVisibility(
                    TextView.GONE
            );
        }
    }

    private boolean recipeMatchesPantry(
            Recipe recipe,
            Map<String, Double> pantryMap) {

        for (String required :
                recipe.getIngredientList()) {

            String[] parts =
                    required.split(":");

            if (parts.length != 3) {
                return false;
            }

            String ingredientName =
                    normalizeIngredient(parts[0]);

            double requiredQuantity;

            try {

                requiredQuantity =
                        Double.parseDouble(parts[1]);

            } catch (NumberFormatException e) {

                return false;
            }

            String requiredUnit =
                    normalizeUnit(parts[2]);

            double requiredBaseQuantity =
                    convertToBaseQuantity(
                            requiredQuantity,
                            requiredUnit
                    );

            String unitType =
                    getUnitType(requiredUnit);

            String key =
                    ingredientName + "|" + unitType;

            if (!pantryMap.containsKey(key)) {
                return false;
            }

            double pantryQuantity =
                    pantryMap.get(key);

            /*
             * The pantry must contain at least
             * the required quantity.
             */
            if (pantryQuantity < requiredBaseQuantity) {
                return false;
            }
        }

        return true;
    }

    private String normalizeIngredient(
            String ingredient) {

        String value =
                ingredient
                        .trim()
                        .toLowerCase(Locale.ROOT);

        if (value.endsWith("ies")) {

            value =
                    value.substring(
                            0,
                            value.length() - 3
                    ) + "y";

        } else if (value.endsWith("es")) {

            value =
                    value.substring(
                            0,
                            value.length() - 2
                    );

        } else if (value.endsWith("s")) {

            value =
                    value.substring(
                            0,
                            value.length() - 1
                    );
        }

        return value;
    }

    private String normalizeUnit(String unit) {

        String value =
                unit
                        .trim()
                        .toLowerCase(Locale.ROOT);

        switch (value) {

            case "kgs":
            case "kg":
            case "kilogram":
            case "kilograms":
                return "kg";

            case "g":
            case "gram":
            case "grams":
                return "g";

            case "l":
            case "liter":
            case "litre":
            case "liters":
            case "litres":
                return "l";

            case "ml":
            case "milliliter":
            case "milliliters":
            case "millilitre":
            case "millilitres":
                return "ml";

            case "cups":
            case "cup":
                return "cup";

            case "tbsp":
            case "tablespoon":
            case "tablespoons":
                return "tablespoon";

            case "tsp":
            case "teaspoon":
            case "teaspoons":
                return "teaspoon";

            case "pieces":
            case "piece":
            case "pcs":
            case "pc":
                return "piece";

            case "cloves":
            case "clove":
                return "clove";

            case "slices":
            case "slice":
                return "slice";

            case "leaves":
            case "leaf":
                return "leaf";

            default:
                return value;
        }
    }

    private String getUnitType(String unit) {

        switch (unit) {

            case "kg":
            case "g":
                return "weight";

            case "l":
            case "ml":
                return "volume";

            case "cup":
            case "tablespoon":
            case "teaspoon":
                return "volume";

            default:
                return unit;
        }
    }

    private double convertToBaseQuantity(
            double quantity,
            String unit) {

        switch (unit) {

            // Weight → grams
            case "kg":
                return quantity * 1000;

            case "g":
                return quantity;

            // Liquid → millilitres
            case "l":
                return quantity * 1000;

            case "ml":
                return quantity;

            // Kitchen volume → teaspoons
            case "cup":
                return quantity * 48;

            case "tablespoon":
                return quantity * 3;

            case "teaspoon":
                return quantity;

            // Countable items remain as they are
            case "piece":
            case "clove":
            case "slice":
            case "leaf":
            default:
                return quantity;
        }
    }
}