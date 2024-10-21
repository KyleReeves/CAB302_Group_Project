package org.example.cab302_project;


/**
 * Represents an ingredient which is used in a Recipe in. Each ingredient is linked to a recipe and has a quantity
 * specific to that recipe
 */
public class RecipieIngredients {
    private int id;
    private int recipeId;
    private int ingredientId;  // Add this line
    private Ingredient ingredient;
    private int amount;


    /**
     * Constructs a RecipieIngredient object with given parameters
     *
     * @param id The ID of this recipe's ingredient
     * @param recipeId the ID of the recipe this ingredient belongs to
     * @param ingredient The ingredient objecting which represents the ingredient data
     * @param amount the amount of the ingredient used in the recipe
     */
    public RecipieIngredients(int id, int recipeId, Ingredient ingredient, int amount) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientId = ingredient.getId();  // Add this line
        this.ingredient = ingredient;
        this.amount = amount;
    }

    /**
     * Constructs a RecipieIngredient object without an ID
     *
     * @param recipeId the ID of the recipe this ingredient belongs to
     * @param ingredient The ingredient objecting which represents the ingredient data
     * @param amount the amount of the ingredient used in the recipe
     */
    public RecipieIngredients(int recipeId, Ingredient ingredient, int amount) {
        this.recipeId = recipeId;
        this.ingredientId = ingredient.getId();  // Add this line
        this.ingredient = ingredient;
        this.amount = amount;
    }


    /**
     * Gets the ID of the ingredient associated with this recipe
     *
     * @return the ID of the ingredient
     */
    public int getIngredientId() {
        return ingredientId;
    }

    /**
     * Gets the ID of the recipe ingredient
     *
     * @return the ID of this recipe ingredient
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the recipe ingredient
     * @param id The recipe ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the ID of the recipe this ingredient belongs to
     *
     * @return The recipe ID
     */
    public int getRecipeId() {
        return recipeId;
    }

    /**
     * Sets the ID of the recipe this ingredient belongs to.
     *
     * @param recipeId The recipe ID to be set.
     */
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    /**
     * Gets the Ingredient object associated with this recipe ingredient.
     *
     * @return The Ingredient object.
     */
    public Ingredient getIngredient() {
        return ingredient;
    }

    /**
     * Sets the Ingredient object for this recipe ingredient.
     *
     * @param ingredient The Ingredient object to be set.
     */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    /**
     * Gets the quantity of this ingredient used in the recipe.
     *
     * @return The quantity of the ingredient.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the quantity of this ingredient used in the recipe.
     *
     * @param amount The quantity to be set.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

}
