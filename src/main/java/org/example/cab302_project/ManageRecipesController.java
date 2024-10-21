package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import java.io.*;
import java.util.List;
import java.util.Objects;


/**
 * Controller Class which handles the logic for managing recipes in the ingredient Tracker application
 * Provides CRUD functionality for the creating,reading, editing, and deleting recipes .
 */
public class ManageRecipesController {

    @FXML
    private ListView<Recipe> recipeList;

    @FXML
    private ListView<String> ingredientListView;

    @FXML
    private Button newRecipeButton;

    @FXML
    private Button editRecipeButton;

    @FXML
    private Button deleteRecipeButton;

    @FXML
    private Button backButton;

    @FXML
    private Button addIngredientButton;

    private RecipeDAO recipeDAO;
    private IngredientsDAO ingredientsDAO;


    // Initialize method: Load recipes and set up listeners

    /**
     * Constructor for managerecipes controller which intializes the RecipeDAO and IngredientsDAO to be able
     * to manage recipe-related and ingredient related database operations
     */
    public ManageRecipesController() {
        recipeDAO = new RecipeDAO();
        ingredientsDAO = new IngredientsDAO();
    }

    /**
     * Initializes the controller by loading all recipes and setting up listeners for recipe selection changes.
     * If a recipe is selected, it has its ingredients displayed with appropriate edit and delete buttons enabled
     */
    @FXML
    public void initialize() {
        loadRecipes();
        recipeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected recipe: " + newValue.getName() + ", ID: " + newValue.getId());
                loadIngredientsForRecipe(newValue);
                editRecipeButton.setDisable(false);
                deleteRecipeButton.setDisable(false);
            } else {
                ingredientListView.getItems().clear();
                editRecipeButton.setDisable(true);
                deleteRecipeButton.setDisable(true);
            }
        });
    }

    /**
     * Loads all recipes from the database and populates the recipe list view.
     */
    private void loadRecipes() {
        List<Recipe> recipes = recipeDAO.getAll();
        ObservableList<Recipe> observableRecipes = FXCollections.observableArrayList(recipes);
        recipeList.setItems(observableRecipes);
        System.out.println("Loaded " + recipes.size() + " recipes");
        for (Recipe recipe : recipes) {
            System.out.println("Loaded Recipe: " + recipe.getName() + ", ID: " + recipe.getId());
        }
    }

    /**
     * Loads the ingredients for a selected recipe and fills the ingredient list view
     *
     * @param recipe The selected recipe which the ingredients are loaded for
     */
    private void loadIngredientsForRecipe(Recipe recipe) {
        List<RecipieIngredients> ingredients = recipeDAO.getIngredientsForRecipe(recipe.getId());
        ingredientListView.getItems().clear();
        for (RecipieIngredients ri : ingredients) {
            ingredientListView.getItems().add(ri.getIngredient().getIngredient() + " (Qty: " + ri.getAmount() + ")");
        }
        System.out.println("Loaded " + ingredients.size() + " ingredients for recipe: " + recipe.getName());
    }

    /**
     * Handle new recipe button click by opening a new recipe form
     *
     * @param event the action event triggered when the new recipe button is clicked
     * @throws IOException handling of errors with the new recipe view
     */
    @FXML
    public void handleNewRecipeButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/new-recipe.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 650, 420);
        stage.setTitle("New Recipes");
        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }


    /**
     *  Handle edit recipe button click by oopening the edit recipe view, once recipe has been edited, refreshes
     *  the recipe list
     *
     * @param event the action event triggered by clicking the edit recipe button
     * @throws IOException handles errors related to loading the edit recipe view
     */
    @FXML
    public void handleEditRecipeClick(ActionEvent event) throws IOException {
        Recipe selectedRecipe = recipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/edit-recipe.fxml"));
            Parent root = loader.load();
            EditRecipeController controller = loader.getController();
            controller.setRecipe(selectedRecipe);

            Stage stage = (Stage) editRecipeButton.getScene().getWindow();
            Scene scene = new Scene(root, 650, 420);
            stage.setTitle("Edit Recipes");
            // Add stylesheet to the new scene
            scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Handles deleting a selected recipe by removing it from the database, once deleted,
     * it refreshes the recipe and ingredient lists
     *
     * @param event The action event which is triggered when the delete recipe button is clicked.
     */
    @FXML
    public void handleDeleteRecipeClick(ActionEvent event) {
        Recipe selectedRecipe = recipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            System.out.println("Attempting to delete recipe: " + selectedRecipe.getName() + ", ID: " + selectedRecipe.getId());
            boolean deleted = recipeDAO.deleteRecipe(selectedRecipe);
            if (deleted) {
                System.out.println("Successfully deleted recipe: " + selectedRecipe.getName());
            } else {
                System.out.println("Failed to delete recipe: " + selectedRecipe.getName());
            }
            loadRecipes();
            ingredientListView.getItems().clear();
        }
    }

    /**
     * Opens a window/view for the addition of a new ingredient to a selected recipe
     *
     * @throws IOException handles errors when loading the new ingredient view
     */
    @FXML
    public void handleAddIngredientClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/new-ingredient-view.fxml"));
        Parent root = loader.load();

        NewIngredientController newIngredientController = loader.getController();
        newIngredientController.setManageRecipesController(this);

        Stage stage = new Stage();
        Scene scene = new Scene(root, IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        scene.getStylesheets().add(IngredientTrackerApplication.class.getResource("FormStyles.css").toExternalForm());
        stage.setTitle("Add New Ingredient");
        stage.setScene(scene);
        stage.showAndWait();

        refreshIngredients();
    }

    /**
     * Refreshes the ingredient list for the selected recipe
     */
    public void refreshIngredients() {
        Recipe selectedRecipe = recipeList.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            loadIngredientsForRecipe(selectedRecipe);
        }
    }

    //

    /**
     * Once clicked, navigates a user back to the main menu
     * @throws IOException
     */
    @FXML
    protected void backButton() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        scene.getStylesheets().add(IngredientTrackerApplication.class.getResource("FormStyles.css").toExternalForm());
        stage.setTitle("Ingredient Tracker");
        stage.setScene(scene);
    }
}