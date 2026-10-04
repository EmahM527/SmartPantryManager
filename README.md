# Smart Pantry Manager

An Android application, written in Java, that helps users reduce food waste by tracking the ingredients they already have at home and suggesting recipes they can cook **using strictly those ingredients** — no shopping trip required.

## Demo Video

The 5–7 minute demonstration video is included in this repository: [SmartPantryApp_VIDEO.mp4]
https://github.com/EmahM527/SmartPantryManager/blob/main/SmartPantryApp_VIDEO.mp4
If the file doesn't preview inline due to its size, click **"View raw"** or use the download button on that page to watch it locally.


## Table of Contents

- [About](#about)
- [Features](#features)
- [Screens](#screens)
- [Tech Stack](#tech-stack)
- [Database](#database)
- [Data Model](#data-model)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Known Limitations / Out of Scope](#known-limitations--out-of-scope)
- [Author](#author)

## About

Most recipe apps assume you're willing to go shopping for whatever a recipe calls for — which doesn't help if the goal is to actually use up what's sitting in your fridge and cupboard before it spoils. **Smart Pantry Manager** flips that around: it only ever suggests a recipe if the user's pantry genuinely contains every ingredient it needs, in at least the required quantity.

This project was built as the practical assignment for **Mobile App Development 700**.

## Features

- **Pantry management** — add, edit, and delete ingredients (name, quantity, unit, expiry date).
- **Pantry list screen** — a `RecyclerView` showing all current ingredients, backed by a local database.
- **Recipe collection** — 15–20 recipes seeded into the database on first run, each with a name, required ingredients, and preparation steps.
- **Suggested Recipes screen** — runs the app's strict-matching logic against the current pantry and lists only the recipes the user can make *right now*.
- **Recipe detail screen** — shows the full ingredient list and method for a selected recipe.
- **Settings screen** — unit preference and expiring-soon alert toggle.
- **Empty-state feedback** — a clear message when no recipes match the current pantry, instead of a blank screen.
- **Input validation** — on all ingredient add/edit forms.

## Screens

| Screen | Purpose |
|---|---|
| `MainActivity` | Home / pantry list screen — shows all pantry items and links to every other feature. |
| `AddEditIngredientActivity` | Add a new ingredient or edit an existing one, with validation. |
| `SuggestedRecipesActivity` | Lists recipes that can be made with the current pantry contents. |
| `RecipeDetailActivity` | Shows a selected recipe's full ingredient list and preparation steps. |
| `SettingsActivity` | Unit preference and expiring-soon alert settings. |

## Tech Stack

- **Language:** Java
- **IDE:** Android Studio
- **UI:** XML layouts, `RecyclerView` with custom adapters, Material components
- **Navigation:** Explicit `Intent`s between Activities
- **Persistence:** SQLite (`SQLiteOpenHelper`)
- **Version control:** Git & GitHub

## Database

This project uses **SQLite**, accessed through a custom `DatabaseHelper` class built on `SQLiteOpenHelper`.

**Why SQLite:**
- It's fully local — no internet connection, backend, or third-party account is required to run or demo the app.
- It's the persistence approach covered directly in the module's data storage content, so it was the most defensible choice to build and explain in depth.
- Full CRUD is straightforward to implement and test against a single on-device database, which suited the scope of this assignment.
- Data genuinely persists between app sessions, satisfying the assignment's requirement that pantry data survive a close/reopen cycle.

The app performs full CRUD on pantry ingredients: **Create** (add ingredient), **Read** (pantry list, recipe list), **Update** (edit ingredient), and **Delete** (remove ingredient).

## Data Model

Three tables make up the local schema:

- **`PANTRY_ITEM`** — the user's current ingredients (`item_id`, `name`, `quantity`, `unit`, `expiry_date`).
- **`RECIPE`** — the seeded recipe collection (`recipe_id`, `name`, `steps`).
- **`RECIPE_INGREDIENT`** — each ingredient a recipe requires (`recipe_ing_id`, `recipe_id` FK, `ingredient_name`, `required_qty`, `unit`).

Ingredient names and units are normalised (lower-cased, singularised, unit-converted to a common base) before comparison, so the strict-matching logic isn't broken by trivial differences like "tomato" vs "tomatoes" or "1kg" vs "1000g".

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Giraffe or later recommended)
- Android SDK (API level as configured in `build.gradle`)
- An Android emulator or a physical device with USB debugging enabled

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/PantrySPM.git
   ```
2. Open the project in Android Studio: **File → Open** → select the cloned `PantrySPM` folder.
3. Let Gradle sync finish (Android Studio will prompt automatically).
4. Connect a device or start an emulator.
5. Click **Run ▶** (or `Shift + F10`).

On first launch, the app seeds its recipe collection automatically — no manual setup is required.

### Running Tests / Trying It Out

1. Add a few ingredients on the pantry screen.
2. Open **Suggested Recipes** to see which recipes currently qualify.
3. Remove one ingredient required by a suggested recipe and reopen the screen — that recipe should disappear, demonstrating the strict-matching rule.
4. Close and reopen the app to confirm pantry data has persisted.

## Project Structure

```
SmartPantryManager/
├── app/
│   └── src/main/
│       ├── java/com/example/smartpantrymanager/
│       │   ├── MainActivity.java
│       │   ├── AddEditIngredientActivity.java
│       │   ├── SuggestedRecipesActivity.java
│       │   ├── RecipeDetailActivity.java
│       │   ├── SettingsActivity.java
│       │   ├── adapter/
│       │   │   ├── IngredientAdapter.java
│       │   │   └── RecipeAdapter.java
│       │   ├── database/
│       │   │   └── DatabaseHelper.java
│       │   └── model/
│       │       ├── Ingredient.java
│       │       └── Recipe.java
│       └── res/
│           ├── layout/
│           └── values/
├── .gitignore
└── README.md
```

## Known Limitations / Out of Scope

Per the assignment brief, the following are intentionally **not** implemented:

- Google Maps, any mapping SDK, or device GPS/location features.
- Payment processing or real financial transactions.
- Publishing to the Google Play Store.

## Author

Cainos Emah Mtsweni
Student Number: 402307830
Mobile App Development 700
