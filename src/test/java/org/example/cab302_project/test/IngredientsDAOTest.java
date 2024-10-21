package org.example.cab302_project;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;



/**
 * A small suite of tests which test the functionality of the IngredientsDAO class
 * this includes testing adding, deleting and updating functionality to ensure that the DAO is functionally correct
 *
 */
class IngredientsDAOTest {
    private static Connection connection;
    private IngredientsDAO ingredientsDAO;

    /**
     * Initialize IngredientsDAO to ensure the database tables exist before testing occurs,  also clears the ingredient
     * table to ensure a fresh test environment
     */
    @BeforeAll
    static void setUpClass() throws SQLException {
        DatabaseConnection.resetConnection();
        connection = DatabaseConnection.getInstance();
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection.setAutoCommit(false);
        ingredientsDAO = new IngredientsDAO();
        ingredientsDAO.createTable();
        clearTable();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        DatabaseConnection.closeConnection();
    }

    /**
     * Clears the ingredients table in the database to provide a clean slate for each test
     */

    private void clearTable() throws SQLException {
        connection.createStatement().execute("DELETE FROM Ingredients");
    }

    /**
     * Test if an ingredient can be inserted into the database and verifies the ingredients name, quantity,
     * Minimum quantity and if it has a quick access flag
     */
    @Test
    void testInsertAndGetIngredient() throws SQLException {
        Ingredient ingredient = new Ingredient("Test Ingredient", 100, 10, false);
        ingredientsDAO.Insert(ingredient);

        List<Ingredient> ingredients = ingredientsDAO.getAll();
        assertEquals(1, ingredients.size(), "Expected one ingredient after insertion");
        Ingredient insertedIngredient = ingredients.get(0);
        assertEquals("Test Ingredient", insertedIngredient.getIngredient());
        assertEquals(100, insertedIngredient.getQuantity());
        assertEquals(10, insertedIngredient.getMinQuantity());
        assertFalse(insertedIngredient.isQuick_access());
    }

    //@Test
    //void testUpdateIngredient() {
        //Ingredient ingredient = new Ingredient("Original Ingredient", 100, 10, false);
        //ingredientsDAO.Insert(ingredient);

        //List<Ingredient> ingredients = ingredientsDAO.getAll();
        //Ingredient toUpdate = ingredients.get(0);
        //toUpdate.setIngredient("Updated Ingredient");
        //toUpdate.setQuantity(200);
        //toUpdate.setMinQuantity(20);
        //toUpdate.setQuick_access(true);

        //ingredientsDAO.updateContact(toUpdate);

        //Ingredient updatedIngredient = ingredientsDAO.getById(toUpdate.getId());
        //assertNotNull(updatedIngredient);
        //assertEquals("Updated Ingredient", updatedIngredient.getIngredient());
        //assertEquals(200, updatedIngredient.getQuantity());
        //assertEquals(20, updatedIngredient.getMinQuantity());
        //assertTrue(updatedIngredient.isQuick_access());
    //}

    /**
     * Tests if an ingredient can be deleted from the database and verifies if the table is empty after deletion
     */
    @Test
    void testDeleteIngredient() throws SQLException {
        Ingredient ingredient = new Ingredient("Ingredient to Delete", 100, 10, false);
        ingredientsDAO.Insert(ingredient);

        List<Ingredient> ingredients = ingredientsDAO.getAll();
        assertEquals(1, ingredients.size(), "Expected one ingredient after insertion");

        ingredientsDAO.delete(ingredients.get(0));

        ingredients = ingredientsDAO.getAll();
        assertTrue(ingredients.isEmpty(), "Expected no ingredients after deletion");
    }
}