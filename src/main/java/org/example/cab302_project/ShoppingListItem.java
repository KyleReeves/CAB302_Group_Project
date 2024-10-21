package org.example.cab302_project;

import java.util.Objects;


/**
 * The item or ingredient representation from the shopping list, which consists of an ingredient and its quantity
 *
 */
public class ShoppingListItem {
    private Ingredient ingredient;
    private int quantity;

    /**
     * Constructs a ShoppingListItem with a specified ingredient and a quantity
     *
     * @param ingredient the ingredient that the shopping list item represents
     * @param quantity the amount of an ingredient to be purchased
     */
    public ShoppingListItem(Ingredient ingredient, int quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    /**
     *  Gets the Ingredient associated with the shopping list
     * @return the ingredient associated with the shopping list
     */
    public Ingredient getIngredient() {
        return ingredient;
    }

    /**
     * Gets the quantity of the ingredient to be purchased.
     * @return The quantity of the ingredient.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the ingredient to be purchased.
     *
     * @param quantity the amount of ingredient to be purcased
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Checks if this ShoppingListItem is equal to another.
     * They are equal if they have the same ingredient
     *
     * @param o the object to compare with
     * @return true if objects are equal, false if they arent
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingListItem that = (ShoppingListItem) o;
        return Objects.equals(ingredient, that.ingredient);
    }

    /**
     * Generates a hash code for the shoppinglistitem based on the ingredient
     *
     * @return the hash code value for the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(ingredient);
    }
}