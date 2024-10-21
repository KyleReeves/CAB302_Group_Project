package org.example.cab302_project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * This Data Access Object class is responsible for managing shopping lists in the database
 * with CRUD methods to create, read update and delete lists and their ingredients
 */
public class ShoppingListDAO {
    private Connection connection;


    /**
     * Constructor which initializes the database connected to create the lists
     */
    public ShoppingListDAO() {
        try {
            connection = DatabaseConnection.getInstance();
        } catch (SQLException e) {
            System.err.println("Error establishing database connection in UserDAO: " + e.getMessage());
            e.printStackTrace();
        }
        createShoppingListTables();
    }

    /**
     * Creates the tables for storing shopping lists and their ingredients in the database
     */
    private void createShoppingListTables() {
        try {
            Statement statement = connection.createStatement();
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS ShoppingLists (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "name TEXT UNIQUE NOT NULL" +
                            ")"
            );
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS ShoppingListItems (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "list_id INTEGER, " +
                            "ingredient_id INTEGER, " +
                            "quantity INTEGER, " +
                            "FOREIGN KEY (list_id) REFERENCES ShoppingLists(id), " +
                            "FOREIGN KEY (ingredient_id) REFERENCES Ingredients(id)" +
                            ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a shopping list and its associated items to the database, ifa list with a given name already exists then
     * it instead updates the list
     *
     * @param listName the name of the shopping list to save
     * @param shoppingList the list of shoppingListItem objects which need to be saved
     */
    public void saveShoppingList(String listName, List<ShoppingListItem> shoppingList) {
        try {
            connection.setAutoCommit(false);

            // Insert or get the list ID
            int listId;
            PreparedStatement listStatement = connection.prepareStatement(
                    "INSERT OR IGNORE INTO ShoppingLists (name) VALUES (?)"
            );
            listStatement.setString(1, listName);
            listStatement.executeUpdate();

            PreparedStatement getListIdStatement = connection.prepareStatement(
                    "SELECT id FROM ShoppingLists WHERE name = ?"
            );
            getListIdStatement.setString(1, listName);
            ResultSet listIdResult = getListIdStatement.executeQuery();
            if (listIdResult.next()) {
                listId = listIdResult.getInt("id");
            } else {
                throw new SQLException("Failed to get or create list ID");
            }

            // Clear existing items for this list
            PreparedStatement clearStatement = connection.prepareStatement(
                    "DELETE FROM ShoppingListItems WHERE list_id = ?"
            );
            clearStatement.setInt(1, listId);
            clearStatement.executeUpdate();

            // Insert new items
            PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO ShoppingListItems (list_id, ingredient_id, quantity) VALUES (?, ?, ?)"
            );

            for (ShoppingListItem item : shoppingList) {
                insertStatement.setInt(1, listId);
                insertStatement.setInt(2, item.getIngredient().getId());
                insertStatement.setInt(3, item.getQuantity());
                insertStatement.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes a shopping list and its associated items from the databae
     *
     * @param listName the name of the shopping list which will be deleted
     * @return returns true if the list was deleted successfully, false if it wasn't
     */
    public boolean deleteShoppingList(String listName) {
        System.out.println("Attempting to delete shopping list: " + listName);
        try {
            connection.setAutoCommit(false);

            // First, get the list ID
            PreparedStatement getListIdStatement = connection.prepareStatement(
                    "SELECT id FROM ShoppingLists WHERE name = ?"
            );
            getListIdStatement.setString(1, listName);
            ResultSet listIdResult = getListIdStatement.executeQuery();

            if (listIdResult.next()) {
                int listId = listIdResult.getInt("id");
                System.out.println("Found list with ID: " + listId);

                // Delete items associated with this list
                PreparedStatement deleteItemsStatement = connection.prepareStatement(
                        "DELETE FROM ShoppingListItems WHERE list_id = ?"
                );
                deleteItemsStatement.setInt(1, listId);
                int itemsDeleted = deleteItemsStatement.executeUpdate();
                System.out.println("Deleted " + itemsDeleted + " items from the list");

                // Delete the list itself
                PreparedStatement deleteListStatement = connection.prepareStatement(
                        "DELETE FROM ShoppingLists WHERE id = ?"
                );
                deleteListStatement.setInt(1, listId);
                int listsDeleted = deleteListStatement.executeUpdate();
                System.out.println("Deleted " + listsDeleted + " lists");

                connection.commit();
                System.out.println("Shopping list deletion committed successfully");
                return true;
            } else {
                System.out.println("No list found with name: " + listName);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting shopping list: " + e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("Transaction rolled back due to error");
            } catch (SQLException rollbackEx) {
                System.out.println("Error during rollback: " + rollbackEx.getMessage());
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error resetting auto-commit: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads a shopping list by name and returns the ingredients/items
     *
     * @param listName the name of the shopping list
     * @return a list of the shopping list objects from the shopping list
     */
    public List<ShoppingListItem> loadShoppingList(String listName) {
        List<ShoppingListItem> shoppingList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT sli.ingredient_id, sli.quantity, i.Ingredient, i.MinQuantity, i.quick_access " +
                            "FROM ShoppingListItems sli " +
                            "JOIN ShoppingLists sl ON sli.list_id = sl.id " +
                            "JOIN Ingredients i ON sli.ingredient_id = i.id " +
                            "WHERE sl.name = ?"
            );
            statement.setString(1, listName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient(
                        resultSet.getInt("ingredient_id"),
                        resultSet.getString("Ingredient"),
                        0,  // We don't need the current quantity for shopping list
                        resultSet.getInt("MinQuantity"),
                        resultSet.getBoolean("quick_access")
                );
                int quantity = resultSet.getInt("quantity");
                shoppingList.add(new ShoppingListItem(ingredient, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shoppingList;
    }

    /**
     * Retrieves the names of all shopping lists from the database
     *
     * @return A list of all shopping list names
     */
    public List<String> getShoppingListNames() {
        List<String> listNames = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name FROM ShoppingLists");
            while (resultSet.next()) {
                listNames.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listNames;
    }
}