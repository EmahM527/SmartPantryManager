package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanagerapps.R;
import com.example.smartpantrymanagerapps.RecipeDetail;


import java.util.ArrayList;

import Model.Recipe;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Recipe> recipes;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_recipe, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Recipe recipe = recipes.get(position);

        holder.txtName.setText(recipe.getName());

        holder.txtIngredients.setText(
                "Ingredients: " + recipe.getIngredients()
        );

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    context,
                    RecipeDetail.class

            );

            intent.putExtra("recipe_id", recipe.getId());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtIngredients;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName =
                    itemView.findViewById(R.id.txtRecipeName);

            txtIngredients =
                    itemView.findViewById(R.id.txtRecipeIngredients);
        }
    }
}

