package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Represents recipes in the application, each recipe has an ID, name and a list of ingredients
 */
public class Recipe {
    private int id;
    private String name;
    private ObservableList<RecipieIngredients> ingredients;

    /**
     * Constructs a new recipe with the given name
     * Sets the ID to -1 to indicate that a recipe has not yet been saved to the database
     *
     * @param name the name of the recipe
     */
    public Recipe(String name) {
        this.id = -1; // Use -1 to indicate a new, unsaved recipe
        this.name = name;
        this.ingredients = FXCollections.observableArrayList();
    }

    /**
     * Constructs a new recipe with a given ID and name
     *
     * @param id ID of a recipe
     * @param name name of a recipe
     */
    public Recipe(int id, String name) {
        this.id = id;
        this.name = name;
        this.ingredients = FXCollections.observableArrayList();
    }

    /**
     * Gets the ID of the recipe
     *
     * @return the ID of a recipe
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the recipe
     *
     * @param id the ID of a recipe
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the recipe name
     * @return the name of the recipe
     */
    public String getName() {
        return name;
    }

    /**
     * sets the recipe name
     *
     * @param name the name of the recipe
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the list of ingredients for a recipe
     *
     * @return a list of ingredients for a specified recipe
     */
    public ObservableList<RecipieIngredients> getIngredients() {
        return ingredients;
    }

    /**
     * Adds an ingredient to the ingredients list
     *
     * @param ingredient the ingredient to be added to the recipe
     */
    public void addIngredient(RecipieIngredients ingredient) {
        ingredients.add(ingredient);
    }

    /**
     * Removes an ingredient from the recipes ingredients list
     *
     * @param ingredient the ingredient to be removed
     */
    public void removeIngredient(String ingredient) {
        ingredients.remove(ingredient);
    }

    /**
     * Overrides the toString() method to display the recipe name in the ListView
     * Used to display the recipes name in the listview
     *
     * @return the name of the recipe
     */
    @Override
    public String toString() {
        return name;
    }
}

