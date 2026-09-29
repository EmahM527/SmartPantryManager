package com.example.smartpantrymanagerapps;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Adapter.IngredientAdapter;
import Database.DatabaseHelper;
import Model.Ingredient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAddIngredient;
    Button btnRecipes;
    Button btnSettings;
    TextView txtEmpty;

    DatabaseHelper databaseHelper;

    ArrayList<Ingredient> ingredientList;

    IngredientAdapter adapter;

    private static final String CHANNEL_ID = "expiry_alerts";
    private static final int NOTIFICATION_ID = 1001;
    private static final int NOTIFICATION_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView =
                findViewById(R.id.recyclerViewIngredients);

        btnAddIngredient =
                findViewById(R.id.btnAddIngredient);

        btnRecipes =
                findViewById(R.id.btnRecipes);

        btnSettings =
                findViewById(R.id.btnSettings);

        txtEmpty =
                findViewById(R.id.txtEmpty);

        databaseHelper =
                new DatabaseHelper(this);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        createNotificationChannel();

        requestNotificationPermission();

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredient.class
            );

            startActivity(intent);
        });

        btnRecipes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipes.class
            );

            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    Settings.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadIngredients();
    }

    private void loadIngredients() {

        ingredientList =
                databaseHelper.getAllIngredients();

        adapter = new IngredientAdapter(
                this,
                ingredientList
        );

        recyclerView.setAdapter(adapter);

        if (ingredientList.isEmpty()) {

            txtEmpty.setVisibility(
                    TextView.VISIBLE
            );

        } else {

            txtEmpty.setVisibility(
                    TextView.GONE
            );
        }

        checkExpiringIngredients();
    }

    private void checkExpiringIngredients() {

        SharedPreferences preferences =
                getSharedPreferences(
                        "SmartPantrySettings",
                        MODE_PRIVATE
                );

        boolean expiryAlertsEnabled =
                preferences.getBoolean(
                        "expiryAlerts",
                        false
                );

        if (!expiryAlertsEnabled) {
            return;
        }

        ArrayList<String> expiringIngredients =
                new ArrayList<>();

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        dateFormat.setLenient(false);

        Date today = new Date();

        for (Ingredient ingredient : ingredientList) {

            String expiryDate =
                    ingredient.getExpiryDate();

            if (expiryDate == null ||
                    expiryDate.trim().isEmpty()) {

                continue;
            }

            try {

                Date expiry =
                        dateFormat.parse(expiryDate);

                long difference =
                        expiry.getTime() - today.getTime();

                long daysRemaining =
                        difference /
                                (1000 * 60 * 60 * 24);

                if (daysRemaining >= 0 &&
                        daysRemaining <= 3) {

                    expiringIngredients.add(
                            ingredient.getName()
                    );
                }

            } catch (Exception e) {

                // Ignore invalid or empty dates
            }
        }

        if (!expiringIngredients.isEmpty()) {

            StringBuilder message =
                    new StringBuilder();

            message.append(
                    "Expiring soon: "
            );

            for (int i = 0;
                 i < expiringIngredients.size();
                 i++) {

                message.append(
                        expiringIngredients.get(i)
                );

                if (i < expiringIngredients.size() - 1) {
                    message.append(", ");
                }
            }

            showExpiryNotification(
                    message.toString()
            );
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O) {

            CharSequence name =
                    "Expiry Alerts";

            String description =
                    "Notifications for ingredients that are expiring soon";

            int importance =
                    NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            name,
                            importance
                    );

            channel.setDescription(description);

            NotificationManager notificationManager =
                    getSystemService(
                            NotificationManager.class
                    );

            notificationManager.createNotificationChannel(
                    channel
            );
        }
    }

    private void requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.TIRAMISU) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.POST_NOTIFICATIONS
                        },
                        NOTIFICATION_PERMISSION_REQUEST
                );
            }
        }
    }

    private void showExpiryNotification(
            String message) {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.TIRAMISU) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        this,
                        CHANNEL_ID
                )
                        .setSmallIcon(
                                android.R.drawable.ic_dialog_alert
                        )
                        .setContentTitle(
                                "Smart Pantry Alert"
                        )
                        .setContentText(message)
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(message)
                        )
                        .setPriority(
                                NotificationCompat.PRIORITY_DEFAULT
                        )
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        notificationManager.notify(
                NOTIFICATION_ID,
                builder.build()
        );
    }
}
