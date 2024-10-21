package org.example.cab302_project.test;

import org.example.cab302_project.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Java Unit tests which test the functionality of the getters and setters in the ingredient class
 */
public class IngredientsTest2 {
    private Ingredient ingredient;
    @BeforeEach

    /**
     * Sets up the test environment by initializing the ingredient object before each test is executed
     */
    public void setUp(){
        ingredient = new Ingredient(1,"Potato",7 ,6, false);
    }

    /**
     * tests if the ID of an ingredient is corrected retrieved
     */
    @Test
    public void TestGetID(){
        assertEquals(1 ,ingredient.getId());
    }

    /**
     * tests if an ingredient is corrected retrieved
     */
    @Test
    public void TestGetIngredient(){
        assertEquals("Potato" ,ingredient.getIngredient());
    }

    /**
     * tests if the quantity of an ingredient is corrected retrieved
     */
    @Test
    public void TestGetQuantity(){
        assertEquals(7 ,ingredient.getQuantity());
    }

    /**
     * tests if the minimum quantity for ingredient is corrected retrieved
     */
    @Test
    public void TestGetMinQuantity(){
        assertEquals(6 ,ingredient.getMinQuantity());
    }

    /**
     * Tests that quick access status is being correctly retrieved
     */
    @Test
    public void TestIsQuickaccess(){
        assertEquals(false ,ingredient.isQuick_access());
    }

    /**
     * Test that the name of an ingredient is correctly updated
     */
    @Test
    public void TestSetIngredient(){
        ingredient.setName("Lemon");
        assertEquals("Lemon" ,ingredient.getIngredient());
    }

    /**
     * Tests that the quantity of the ingredients is correctly retrieved
     */
    @Test
    public void TestSetQuantity(){
        ingredient.setQuantity(5);
        assertEquals(5 ,ingredient.getQuantity());
    }

    /**
     * Tests that the minimum quantity of the ingredient is being correctly updated.
     *
     */
    @Test
    public void TestSetMinQuantity(){
        ingredient.setMinQuantity(2);
        assertEquals(2 ,ingredient.getMinQuantity());
    }

    /**
     *
     * Test that the quick access status of an ingredient is correctly given
     */
    @Test
    public void TestSetQuickaccess(){
        ingredient.setQuick_access(true);
        assertEquals(true ,ingredient.isQuick_access());
    }
}
