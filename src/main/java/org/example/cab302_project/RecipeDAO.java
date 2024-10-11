package org.example.cab302_project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {
    private Connection connection;

    public RecipeDAO() {
        connection = DatabaseConnection.getInstance();
    }

    // Create Recipe table if it doesn't exist
    public void createRecipeTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS Recipe ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "Recipe VARCHAR NOT NULL "
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println("Error creating Recipe table: " + ex.getMessage());
        }
    }


    // Create RecipeIngredients table if it doesn't exist
    public void createRecipeIngredientTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS RecipeIngredients ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "Recipeid INTEGER, "
                            + "Ingredientid INTEGER, "
                            + "ingredientUsage INTEGER, "
                            + "FOREIGN KEY (Recipeid) REFERENCES Recipe(id) ON DELETE CASCADE, "
                            + "FOREIGN KEY (Ingredientid) REFERENCES Ingredients(id)"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println("Error creating RecipeIngredients table: " + ex.getMessage());
        }
    }

    // Insert a new recipe into the database
    public int InsertRecipe(Recipe recipe) {
        try {
            PreparedStatement insertRecipe = connection.prepareStatement(
                    "INSERT INTO Recipe (Recipe) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            insertRecipe.setString(1, recipe.getName());
            int affectedRows = insertRecipe.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating recipe failed, no rows affected.");
            }

            try (ResultSet generatedKeys = insertRecipe.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    recipe.setId(id);
                    return id;
                } else {
                    throw new SQLException("Creating recipe failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error inserting recipe: " + ex.getMessage());
        }
        return -1;
    }

    // Retrieve all recipes from the database
    public List<Recipe> getAll() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT id, Recipe FROM Recipe");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("Recipe");
                Recipe recipe = new Recipe(id, name);
                recipes.add(recipe);
                System.out.println("Retrieved recipe: ID = " + id + ", Name = " + name);
            }
        } catch (SQLException ex) {
            System.err.println("Error getting all recipes: " + ex.getMessage());
        }
        return recipes;
    }

    // gets all recipes where all ingredients are in stock
    public List<Recipe> getRecommendedRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            String query = "SELECT r.id, r.Recipe, " +
                    "GROUP_CONCAT(ri.Ingredientid || ':' || ri.ingredientUsage || ':' || COALESCE(i.Quantity, 0)) AS ingredients, " +
                    "SUM(CASE WHEN ri.ingredientUsage > COALESCE(i.Quantity, 0) THEN 1 ELSE 0 END) AS insufficient_ingredients " +
                    "FROM Recipe r " +
                    "LEFT JOIN RecipeIngredients ri ON r.id = ri.Recipeid " +
                    "LEFT JOIN Ingredients i ON ri.Ingredientid = i.id " +
                    "GROUP BY r.id";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("Recipe");
                String ingredients = rs.getString("ingredients");
                int insufficientIngredients = rs.getInt("insufficient_ingredients");

                System.out.println("Recipe: ID = " + id + ", Name = " + name +
                        ", Ingredients = " + ingredients +
                        ", Insufficient Ingredients = " + insufficientIngredients);

                if (insufficientIngredients == 0) {
                    Recipe recipe = new Recipe(id, name);
                    recipes.add(recipe);
                    System.out.println("Added to recommended recipes: " + name);
                } else {
                    System.out.println("Not recommended due to insufficient ingredients: " + name);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting recommended recipes: " + ex.getMessage());
            ex.printStackTrace();
        }
        return recipes;
    }

    // Debug method to print all recipes and their ingredients
    public void printAllRecipesAndIngredients() {
        try {
            String query = "SELECT r.id, r.Recipe, " +
                    "GROUP_CONCAT(i.Ingredient || ':' || ri.ingredientUsage || ':' || COALESCE(i.Quantity, 0)) AS ingredients " +
                    "FROM Recipe r " +
                    "LEFT JOIN RecipeIngredients ri ON r.id = ri.Recipeid " +
                    "LEFT JOIN Ingredients i ON ri.Ingredientid = i.id " +
                    "GROUP BY r.id";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("Recipe");
                String ingredients = rs.getString("ingredients");
                System.out.println("Recipe: ID = " + id + ", Name = " + name + ", Ingredients = " + ingredients);
            }
        } catch (SQLException ex) {
            System.err.println("Error printing all recipes and ingredients: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Insert a new recipe ingredient into the database
    public void InsertRecipeIngredient(RecipieIngredients ingredient) {
        try {
            PreparedStatement insertRecipeIngredient = connection.prepareStatement(
                    "INSERT INTO RecipeIngredients (Recipeid, Ingredientid, ingredientUsage) VALUES (?,?,?)"
            );
            insertRecipeIngredient.setInt(1, ingredient.getRecipeId());
            insertRecipeIngredient.setInt(2, ingredient.getIngredientId());  // Use getIngredientId() instead of getIngredient().getId()
            insertRecipeIngredient.setInt(3, ingredient.getAmount());

            int affectedRows = insertRecipeIngredient.executeUpdate();
            System.out.println("Inserted RecipeIngredient: RecipeId=" + ingredient.getRecipeId()
                    + ", IngredientId=" + ingredient.getIngredientId()
                    + ", Amount=" + ingredient.getAmount()
                    + ", Affected Rows=" + affectedRows);
        } catch (SQLException ex) {
            System.err.println("Error inserting recipe ingredient: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Retrieve all ingredients for a specific recipe
    public List<RecipieIngredients> getIngredientsForRecipe(int recipeId) {
        List<RecipieIngredients> ingredients = new ArrayList<>();
        try {
            String query = "SELECT ri.id, ri.Recipeid, ri.Ingredientid, ri.ingredientUsage, i.Ingredient " +
                    "FROM RecipeIngredients ri " +
                    "JOIN Ingredients i ON ri.Ingredientid = i.id " +
                    "WHERE ri.Recipeid = ?";
            System.out.println("Executing query: " + query + " with recipeId: " + recipeId);

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, recipeId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Ingredient ingredient = new Ingredient(
                        rs.getInt("Ingredientid"),
                        rs.getString("Ingredient"),
                        0, 0, false
                );
                RecipieIngredients ri = new RecipieIngredients(
                        rs.getInt("id"),
                        rs.getInt("Recipeid"),
                        ingredient,
                        rs.getInt("ingredientUsage")
                );
                ingredients.add(ri);
                System.out.println("Retrieved ingredient: " + ingredient.getIngredient() + " for recipe ID: " + recipeId);
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching ingredients for recipe: " + ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("Total ingredients found for recipe ID " + recipeId + ": " + ingredients.size());
        return ingredients;
    }

    // Delete a recipe from the database
    public boolean deleteRecipe(Recipe recipe) {
        try {
            connection.setAutoCommit(false);

            PreparedStatement deleteRecipe = connection.prepareStatement("DELETE FROM Recipe WHERE id = ?");
            deleteRecipe.setInt(1, recipe.getId());
            int rowsAffected = deleteRecipe.executeUpdate();

            connection.commit();

            if (rowsAffected == 0) {
                System.err.println("No recipe found with id: " + recipe.getId());
                return false;
            }
            return true;
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error deleting recipe: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Delete a recipe ingredient from the database
    public void deleteRecipeIngredient(RecipieIngredients Ingredient) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM RecipeIngredients WHERE id = ?");
            statement.setInt(1, Ingredient.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update a recipe ingredient in the database
    public boolean updateRecipeIngredient(RecipieIngredients ingredient) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE RecipeIngredients SET Ingredientid = ?, ingredientUsage = ? WHERE id = ?"
            );
            statement.setInt(1, ingredient.getIngredient().getId());
            statement.setInt(2, ingredient.getAmount());
            statement.setInt(3, ingredient.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("Error updating recipe ingredient: " + ex.getMessage());
            return false;
        }
    }

}
