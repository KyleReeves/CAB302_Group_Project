package org.example.cab302_project;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Second set of Unit tests for testing the functionality of RecipeDAO and IngredientDAO classes
 * Ensures correct database operations such as insertion, deletion and linking recipes and ingredients
 */
class RecipeDAOTest2 {
    private static Connection testConnection;
    private RecipeDAO recipeDAO;
    private IngredientsDAO ingredientsDAO;

    /**
     * Initializes a test database connection with an instance for testing
     *
     * @throws SQLException handles if the test connection cannot be established
     */
    @BeforeAll
    static void setUpClass() throws SQLException {
        testConnection = DriverManager.getConnection("jdbc:sqlite:test_database.db");
        DatabaseConnection.setTestInstance(testConnection);
    }

    /**
     * Sets up the RecipeDAO and IngredientsDAO instances and creates the necessary tables before each test
     *
     * @throws SQLException handles errors which occur during the creation of a table
     */
    @BeforeEach
    void setUp() throws SQLException {
        recipeDAO = new RecipeDAO();
        ingredientsDAO = new IngredientsDAO();
        recipeDAO.createRecipeTable();
        recipeDAO.createRecipeIngredientTable();
        ingredientsDAO.createTable();
        clearAllTables();
    }

    /**
     * Clears all tables after each test to ensure they're fresh
     *
     * @throws SQLException handles errors which occur during the creation of a table
     */
    @AfterEach
    void tearDown() throws SQLException {
        clearAllTables();
    }

    /**
     * Closes the test database after tests are executed
     * @throws SQLException handles errors which occur during the creation of a table
     */
    @AfterAll
    static void tearDownClass() throws SQLException {
        testConnection.close();
    }

    /**
     *  Clears data from Recipe, RecipeIngredients and Ingredients tables
     *
     * @throws SQLException handles errors which occur during the creation of a table
     */
    private void clearAllTables() throws SQLException {
        testConnection.createStatement().execute("DELETE FROM Recipe");
        testConnection.createStatement().execute("DELETE FROM RecipeIngredients");
        testConnection.createStatement().execute("DELETE FROM Ingredients");
    }

    /**
     * Tests to verify the insertion of a recipe into the database
     */
    @Test
    void testInsertRecipe() {
        Recipe recipe = new Recipe("Spaghetti Carbonara");
        int id = recipeDAO.InsertRecipe(recipe);
        assertTrue(id > 0, "Spaghetti Carbonara recipe should be inserted with a valid ID");

        // Verify the recipe was actually inserted
        List<Recipe> recipes = recipeDAO.getAll();
        assertEquals(1, recipes.size(), "There should be one recipe in the database");
        assertEquals("Spaghetti Carbonara", recipes.get(0).getName(), "The inserted recipe should be Spaghetti Carbonara");
        assertEquals(id, recipes.get(0).getId(), "The ID should match the one returned by InsertRecipe");
    }

    /**
     * Tests which retrieve all recipes from the database
     * Ensures that both inserted recipes are correctly retrieved
     */
    @Test
    void testGetAllRecipes() {
        Recipe recipe1 = new Recipe("Chicken Parmesan");
        Recipe recipe2 = new Recipe("Vegetable Stir Fry");
        int id1 = recipeDAO.InsertRecipe(recipe1);
        int id2 = recipeDAO.InsertRecipe(recipe2);

        List<Recipe> recipes = recipeDAO.getAll();
        assertEquals(2, recipes.size(), "Should retrieve both inserted recipes");
        assertTrue(recipes.stream().anyMatch(r -> r.getName().equals("Chicken Parmesan") && r.getId() == id1), "Should contain Chicken Parmesan with correct ID");
        assertTrue(recipes.stream().anyMatch(r -> r.getName().equals("Vegetable Stir Fry") && r.getId() == id2), "Should contain Vegetable Stir Fry with correct ID");
    }

    /**
     * Test which ensures that tables are empty at the start of the testing
     */
    @Test
    void testDatabaseIsEmptyAtStart() {
        List<Recipe> recipes = recipeDAO.getAll();
        List<Ingredient> ingredients = ingredientsDAO.getAll();

        assertTrue(recipes.isEmpty(), "Recipe table should be empty at start");
        assertTrue(ingredients.isEmpty(), "Ingredients table should be empty at start");
    }
}