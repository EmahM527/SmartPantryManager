package com.example.smartpantrymanagerapps;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    Switch switchExpiry;
    Spinner spinnerUnits;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        switchExpiry =
                findViewById(R.id.switchExpiry);

        spinnerUnits =
                findViewById(R.id.spinnerUnits);

        // SharedPreferences stores the user's settings
        preferences =
                getSharedPreferences(
                        "SmartPantrySettings",
                        MODE_PRIVATE
                );

        // -----------------------------
        // Expiring-Soon Alerts
        // -----------------------------

        boolean expiryEnabled =
                preferences.getBoolean(
                        "expiryAlerts",
                        false
                );

        switchExpiry.setChecked(expiryEnabled);

        switchExpiry.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    preferences.edit()
                            .putBoolean(
                                    "expiryAlerts",
                                    isChecked
                            )
                            .apply();
                }
        );

        // -----------------------------
        // Units Preference
        // -----------------------------

        String[] units = {
                "Metric",
                "Imperial"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnits.setAdapter(adapter);

        // Load previously selected unit
        String savedUnit =
                preferences.getString(
                        "unitsPreference",
                        "Metric"
                );

        if (savedUnit.equals("Imperial")) {
            spinnerUnits.setSelection(1);
        } else {
            spinnerUnits.setSelection(0);
        }

        // Save selected unit
        spinnerUnits.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            android.view.View view,
                            int position,
                            long id) {

                        String selectedUnit =
                                units[position];

                        preferences.edit()
                                .putString(
                                        "unitsPreference",
                                        selectedUnit
                                )
                                .apply();
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {
                    }
                }
        );
    }
}

