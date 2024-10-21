package org.example.cab302_project;

/**
 * Represents an Ingredient within the system with attributes such as an ID, name, quantity, minimum quantity and quick access
 * status
 */
public class Ingredient {
    private int id;
    private String Ingredient;
    private int Quantity;
    private int MinQuantity;
    private boolean quick_access;

    /**
     * Constructs an Ingredient object with an ID, name, quantity, minimum quantity, and quick access status
     *
     * @param id Unique identifier of the ingredient
     * @param Ingredient The name of the Ingredient
     * @param Quantity The Quantity of the Ingredient
     * @param MinQuantity The Minimum Quantity of the Ingredient to alert the user/create shopping list
     * @param quick_access Determines if it will be in the quick access menu
     */
    public Ingredient(int id,String Ingredient, int Quantity, int MinQuantity, boolean quick_access){
        this.id = id;
        this.Ingredient = Ingredient;
        this.Quantity = Quantity;
        this.MinQuantity = MinQuantity;
        this.quick_access = quick_access;
    }

    /**
     * Contructs an Ingredient object without an ID, but with name, quantity, minimum quantity, and quick access status.
     *
     * @param Ingredient The name of the Ingredient
     * @param Quantity The Quantity of the Ingredient
     * @param MinQuantity The Minimum Quantity of the Ingredient to alert the user/create shopping list
     * @param quick_access Determines if it will be in the quick access menu
     */
    public Ingredient(String Ingredient, int Quantity, int MinQuantity, boolean quick_access){
        this.Ingredient = Ingredient;
        this.Quantity = Quantity;
        this.MinQuantity = MinQuantity;
        this.quick_access = quick_access;
    }

    /**
     * Constructs an Ingredient object with Name, Quantity and Minimum Quantity
     *
      * @param Ingredient The name of the Ingredient
     * @param Quantity The Quantity of the Ingredient
     * @param MinQuantity The Minimum Quantity of the Ingredient to alert the user/create shopping list
     */
   public Ingredient(String Ingredient, int Quantity, int MinQuantity) {
        this.Ingredient = Ingredient;
        this.Quantity = Quantity;
        this.MinQuantity = MinQuantity;
    }

    /**
     * Gets the Unique Identifier of the Ingredient
     *
     * @return the Ingredients ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the Name of the Ingredient
     *
     * @return the ingredients Name
     */
    public String getIngredient() {
        return Ingredient;
    }

    /**
     * Gets the Quantity of the Ingredient
     *
     * @return the current quantity
     */
    public int getQuantity() {
        return Quantity;
    }

    /**
     * Gets the minimum quantity allowed for the ingredient before requiring restocking
     *
     * @return the minimum quantity
     */
    public int getMinQuantity() {
        return MinQuantity;
    }

    /**
     * Returns whether quick access functionality is enabled for the ingredient
     *
     * @return True if the quick access is enabled, or false otherwise
     */
    public boolean isQuick_access() {
        return quick_access;
    }

    /**
     * Sets the name for the ingredient
     *
     * @param Ingredient The new name of the Ingredient
     */
    public void setName(String Ingredient) {
        this.Ingredient = Ingredient;
    }

    /**
     * Sets the current quantity of the Ingredient
     *
     * @param Quantity The new quantity of the ingredient
     */
    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    /**
     * Sets the minimum quantity of the Ingredient
     *
     * @param MinQuantity minimum quantity amount for the ingredient
     */
    public void setMinQuantity(int MinQuantity) {
        this.MinQuantity = MinQuantity;
    }

    /**
     * Sets the quick access status of the ingredient
     *
     * @param quick_access The quick access status
     */
    public void setQuick_access(boolean quick_access) {
        this.quick_access = quick_access;
    }

    /**
     * Returns the string representation of the ingredient
     *
     * @return The name of the ingredient
     */
    @Override
    public String toString() {
        return this.Ingredient;
    }
}




