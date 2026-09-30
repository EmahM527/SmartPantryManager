package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Model.Ingredient;
import Model.Recipe;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";

    // Increased because we are changing the recipe data structure.
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "quantity REAL NOT NULL," +
                        "unit TEXT NOT NULL," +
                        "expiry_date TEXT)"
        );

        db.execSQL(
                "CREATE TABLE recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "ingredients TEXT NOT NULL," +
                        "method TEXT NOT NULL)"
        );

        seedRecipes(db);
    }

    private void seedRecipes(SQLiteDatabase db) {

        addRecipe(db,
                "Vegetable Omelette",
                "egg:3:piece,milk:0.5:cup,tomato:1:piece,onion:1:piece,salt:1:teaspoon",
                "Beat the eggs and milk together. Add chopped tomato and onion. Season with salt. Cook in a heated pan until set.");

        addRecipe(db,
                "Tomato Pasta",
                "pasta:2:cup,tomato:2:cup,onion:1:piece,garlic:2:clove,salt:1:teaspoon",
                "Cook pasta until tender. Fry onion and garlic. Add tomato and seasoning. Mix with the cooked pasta.");

        addRecipe(db,
                "Chicken Rice",
                "chicken:2:piece,rice:2:cup,onion:1:piece,garlic:2:clove,salt:1:teaspoon",
                "Cook the rice. Fry chicken with onion and garlic. Add seasoning and serve with rice.");

        addRecipe(db,
                "Pancakes",
                "flour:2:cup,milk:1:cup,egg:2:piece,sugar:2:tablespoon,salt:0.5:teaspoon",
                "Mix flour, milk, eggs, sugar and salt. Pour small amounts into a heated pan and cook both sides.");

        addRecipe(db,
                "French Toast",
                "bread:4:piece,egg:2:piece,milk:0.5:cup,sugar:1:tablespoon",
                "Mix egg, milk and sugar. Dip bread into the mixture and fry both sides until golden.");

        addRecipe(db,
                "Chicken Sandwich",
                "bread:2:piece,chicken:1:piece,tomato:1:piece,lettuce:2:leaf,salt:0.5:teaspoon",
                "Cook and slice the chicken. Place chicken, tomato and lettuce between slices of bread. Add salt.");

        addRecipe(db,
                "Garlic Rice",
                "rice:2:cup,garlic:2:clove,onion:1:piece,salt:1:teaspoon",
                "Cook rice. Fry garlic and onion, then mix with the cooked rice and season.");

        addRecipe(db,
                "Tomato Egg Scramble",
                "egg:2:piece,tomato:1:cup,onion:1:piece,salt:0.5:teaspoon",
                "Fry onion and tomato. Add beaten eggs and stir until cooked.");

        addRecipe(db,
                "Chicken Pasta",
                "pasta:2:cup,chicken:1:piece,onion:1:piece,garlic:2:clove,salt:1:teaspoon",
                "Cook pasta. Cook chicken with onion and garlic. Combine with pasta and season.");

        addRecipe(db,
                "Vegetable Rice",
                "rice:2:cup,carrot:1:cup,peas:1:cup,onion:1:piece,salt:1:teaspoon",
                "Cook rice. Fry vegetables and onion. Mix vegetables with rice and season.");

        addRecipe(db,
                "Egg Sandwich",
                "bread:2:piece,egg:2:piece,mayonnaise:2:tablespoon,salt:0.5:teaspoon",
                "Boil eggs and chop them. Mix with mayonnaise and salt. Spread onto bread.");

        addRecipe(db,
                "Chicken Soup",
                "chicken:2:piece,carrot:1:cup,onion:1:piece,garlic:2:clove,salt:1:teaspoon",
                "Cook chicken with chopped vegetables, garlic and salt in water until tender.");

        addRecipe(db,
                "Tomato Soup",
                "tomato:2:cup,onion:1:piece,garlic:2:clove,salt:1:teaspoon",
                "Cook tomato, onion and garlic until soft. Blend until smooth and season.");

        addRecipe(db,
                "Rice and Beans",
                "rice:2:cup,beans:1:cup,onion:1:piece,tomato:1:cup,salt:1:teaspoon",
                "Cook rice separately. Cook beans with onion and tomato. Serve together.");

        addRecipe(db,
                "Simple Salad",
                "lettuce:2:cup,tomato:1:cup,onion:1:piece,cucumber:1:cup,salt:0.5:teaspoon",
                "Wash and chop the vegetables. Mix together and season lightly.");

        addRecipe(db,
                "Chicken Salad",
                "chicken:1:piece,lettuce:2:cup,tomato:1:cup,cucumber:1:cup,salt:0.5:teaspoon",
                "Cook and slice chicken. Combine with lettuce, tomato and cucumber. Add salt.");

        addRecipe(db,
                "Egg Fried Rice",
                "rice:2:cup,egg:2:piece,onion:1:piece,carrot:1:cup,peas:1:cup,salt:1:teaspoon",
                "Cook rice. Fry vegetables and onion. Add egg and cooked rice. Stir and season.");

        addRecipe(db,
                "Banana Pancakes",
                "banana:2:piece,flour:1:cup,milk:1:cup,egg:1:piece,sugar:1:tablespoon",
                "Mash banana. Mix with flour, milk, egg and sugar. Cook pancakes in a heated pan.");

        addRecipe(db,
                "Cheese Toast",
                "bread:2:piece,cheese:2:slice,butter:1:tablespoon,salt:0.25:teaspoon",
                "Butter the bread. Add cheese and a small amount of salt. Toast until the cheese melts.");

        addRecipe(db,
                "Potato Omelette",
                "potato:2:piece,egg:2:piece,onion:1:piece,salt:0.5:teaspoon",
                "Cook sliced potato and onion. Add beaten eggs and cook until the omelette is firm.");
    }

    private void addRecipe(
            SQLiteDatabase db,
            String name,
            String ingredients,
            String method) {

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("ingredients", ingredients);
        values.put("method", method);

        db.insert("recipes", null, values);
    }

    // CREATE

    public long addIngredient(Ingredient ingredient) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", ingredient.getName());
        values.put("quantity", ingredient.getQuantity());
        values.put("unit", ingredient.getUnit());
        values.put("expiry_date", ingredient.getExpiryDate());

        return db.insert("ingredients", null, values);
    }

    // READ

    public ArrayList<Ingredient> getAllIngredients() {

        ArrayList<Ingredient> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM ingredients ORDER BY name ASC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                Ingredient ingredient = new Ingredient();

                ingredient.setId(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        )
                );

                ingredient.setName(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        )
                );

                ingredient.setQuantity(
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow("quantity")
                        )
                );

                ingredient.setUnit(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("unit")
                        )
                );

                ingredient.setExpiryDate(
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("expiry_date")
                        )
                );

                list.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return list;
    }

    // UPDATE

    public int updateIngredient(Ingredient ingredient) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", ingredient.getName());
        values.put("quantity", ingredient.getQuantity());
        values.put("unit", ingredient.getUnit());
        values.put("expiry_date", ingredient.getExpiryDate());

        return db.update(
                "ingredients",
                values,
                "id=?",
                new String[]{
                        String.valueOf(ingredient.getId())
                }
        );
    }

    // DELETE

    public int deleteIngredient(int id) {

        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                "ingredients",
                "id=?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }

    // RECIPES

    public ArrayList<Recipe> getAllRecipes() {

        ArrayList<Recipe> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM recipes ORDER BY name ASC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow("id")
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("name")
                        );

                String ingredients =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("ingredients")
                        );

                String method =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow("method")
                        );

                list.add(
                        new Recipe(
                                id,
                                name,
                                ingredients,
                                method
                        )
                );

            } while (cursor.moveToNext());
        }

        cursor.close();

        return list;
    }

    public Recipe getRecipeById(int id) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM recipes WHERE id=?",
                new String[]{
                        String.valueOf(id)
                }
        );

        Recipe recipe = null;

        if (cursor.moveToFirst()) {

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    );

            String ingredients =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("ingredients")
                    );

            String method =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("method")
                    );

            recipe =
                    new Recipe(
                            id,
                            name,
                            ingredients,
                            method
                    );
        }

        cursor.close();

        return recipe;
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");

        onCreate(db);
    }
}

