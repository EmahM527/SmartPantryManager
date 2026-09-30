package Model;


import java.util.ArrayList;

public class Recipe {

    private int id;
    private String name;
    private String ingredients;
    private String method;

    public Recipe(int id, String name, String ingredients, String method) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getMethod() {
        return method;
    }

    public ArrayList<String> getIngredientList() {

        ArrayList<String> list = new ArrayList<>();

        String[] parts = ingredients.split(",");

        for (String part : parts) {
            list.add(part.trim());
        }

        return list;
    }
}

