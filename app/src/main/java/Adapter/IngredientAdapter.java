package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanagerapps.AddEditIngredient;
import com.example.smartpantrymanagerapps.MainActivity;
import com.example.smartpantrymanagerapps.R;
import Database.DatabaseHelper;
import Adapter.IngredientAdapter;
import Model.Ingredient;

import org.tensorflow.lite.schema.Model;

import java.util.ArrayList;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Ingredient> ingredients;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    public IngredientAdapter(MainActivity context, ArrayList<Ingredient> ingredientList) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_ingredient, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Ingredient ingredient = ingredients.get(position);

        holder.txtName.setText(ingredient.getName());

        String details =
                ingredient.getQuantity() + " "
                        + ingredient.getUnit()
                        + " | Expiry: "
                        + ingredient.getExpiryDate();

        holder.txtDetails.setText(details);

        holder.btnEdit.setOnClickListener(v -> {

            Intent intent = new Intent(
                    context,
                    AddEditIngredient.class
            );

            intent.putExtra("id", ingredient.getId());
            intent.putExtra("name", ingredient.getName());
            intent.putExtra("quantity", ingredient.getQuantity());
            intent.putExtra("unit", ingredient.getUnit());
            intent.putExtra("expiry", ingredient.getExpiryDate());

            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Ingredient")
                    .setMessage(
                            "Are you sure you want to delete "
                                    + ingredient.getName() + "?"
                    )
                    .setPositiveButton("Delete", (dialog, which) -> {

                        DatabaseHelper db =
                                new DatabaseHelper(context);
                        int rowsDeleted = db.deleteIngredient(ingredient.getId());
                        boolean deleted = rowsDeleted > 0;

                        if (deleted){
                            ingredients.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context,ingredient.getName() + " deleted",Toast.LENGTH_LONG).show();

                        } else{
                            Toast.makeText(
                                    context,
                                    "Could not delete ingredient",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtDetails;
        Button btnEdit;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtIngredientName);
            txtDetails = itemView.findViewById(R.id.txtIngredientDetails);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

