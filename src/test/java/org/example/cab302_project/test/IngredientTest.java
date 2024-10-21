package org.example.cab302_project.test;

import org.example.cab302_project.Ingredient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit testing responsible for testing functionality within the ingredient class
 */
public class IngredientTest {


    /**
     * Tests the getters in Ingredients to ensure they correctly return the values expected of an ingredient object
     *
     * This creates an ingredient with a predetermined value and checks if the name, quantity, minimum quantity and quick
     * access status are accurately retrieved
     */
    @Test
    public void testGetIngredientInfo(){
        Ingredient ingredient = new Ingredient("test1", 5, 1, false);
        assertEquals("test1", ingredient.getIngredient());
        assertEquals(5, ingredient.getQuantity());
        assertEquals(1, ingredient.getMinQuantity());
        assertFalse(ingredient.isQuick_access());
    }

}
