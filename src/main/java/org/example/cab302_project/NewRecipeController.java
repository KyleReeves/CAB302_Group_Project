package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Controller Class which handles the creation of a new recipe which provides CRUD methods to add,
 * edit and delete ingredients within a recipe
 */
public class NewRecipeController {

    @FXML
    private ListView<RecipieIngredients> ingredientList;

    @FXML
    private TextField recipeName;

    @FXML
    private ComboBox<Ingredient> ingredientComboBox;

    @FXML
    private ComboBox<String> quantityComboBox;

    @FXML
    private Button addIngredientButton;

    @FXML
    private Button updateIngredientButton;

    @FXML
    private Button backButton;

    // ObservableList to hold recipe ingredients
    private ObservableList<RecipieIngredients> ingredients = FXCollections.observableArrayList();
    // Index of the ingredient being edited
    private int editingIngredientIndex = -1;
    // DAOs for database operations
    private RecipeDAO recipeDAO;
    private IngredientsDAO ingredientsDAO;

    /**
     * Constructor for NewRecipe Controller Which intializes the IngredientsDAO for managing recipes and ingredients
     */
    public NewRecipeController() {
        recipeDAO = new RecipeDAO();
        ingredientsDAO = new IngredientsDAO();
    }

    /**
     * Intializes the controller by setting up combo boxes for ingredients and quantities.
     * Also sets up the ingredient list view, and loads all available ingredients from the database
     */
    @FXML
    public void initialize() {
        quantityComboBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        setupIngredientListView();
        ingredientList.setItems(ingredients);
        updateIngredientButton.setDisable(true);
        loadIngredients();
    }

    /**
     * Load all ingredients into the ingredient combo box
     */
    private void loadIngredients() {
        List<Ingredient> allIngredients = ingredientsDAO.getAll();
        ingredientComboBox.setItems(FXCollections.observableArrayList(allIngredients));
    }

    //

    /**
     * Set up the ingredient list view with custom cell factory for displaying ingredients with an edit and delete button
     */
    private void setupIngredientListView() {
        ingredientList.setCellFactory(new Callback<ListView<RecipieIngredients>, ListCell<RecipieIngredients>>() {
            @Override
            public ListCell<RecipieIngredients> call(ListView<RecipieIngredients> param) {
                return new ListCell<RecipieIngredients>() {
                    private final Button deleteButton = new Button("Delete");
                    private final Button editButton = new Button("Edit");

                    {
                        deleteButton.setOnAction(event -> {
                            RecipieIngredients ingredient = getItem();
                            ingredients.remove(ingredient);
                        });

                        editButton.setOnAction(event -> {
                            editingIngredientIndex = getIndex();
                            RecipieIngredients ingredient = getItem();
                            ingredientComboBox.setValue(ingredient.getIngredient());
                            quantityComboBox.setValue(String.valueOf(ingredient.getAmount()));
                            updateIngredientButton.setDisable(false);
                            addIngredientButton.setDisable(true);
                        });
                    }

                    @Override
                    protected void updateItem(RecipieIngredients ingredient, boolean empty) {
                        super.updateItem(ingredient, empty);
                        if (empty || ingredient == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(ingredient.getIngredient().getIngredient() + " (Qty: " + ingredient.getAmount() + ")");
                            setGraphic(new javafx.scene.layout.HBox(deleteButton, editButton));
                        }
                    }
                };
            }
        });
    }

    /**
     * Handles adding a new ingredient to the recipe
     * @param actionEvent the action event which is triggered when the "Add ingredient" button is clicked
     */
    @FXML
    public void handleAddIngredientClick(ActionEvent actionEvent) {
        Ingredient selectedIngredient = ingredientComboBox.getValue();
        String quantity = quantityComboBox.getValue();

        if (selectedIngredient != null && quantity != null) {
            RecipieIngredients newIngredient = new RecipieIngredients(0, selectedIngredient, Integer.parseInt(quantity));
            ingredients.add(newIngredient);
            ingredientComboBox.setValue(null);
            quantityComboBox.setValue(null);
        }
    }

    /**
     * Handle updating an existing ingredient in a recipe
     *
     * @param actionEvent the action event which is triggered when the "Update ingredient" button is clicked
     */
    @FXML
    public void handleUpdateIngredientClick(ActionEvent actionEvent) {
        Ingredient updatedIngredient = ingredientComboBox.getValue();
        String updatedQuantity = quantityComboBox.getValue();

        if (editingIngredientIndex >= 0 && updatedIngredient != null && updatedQuantity != null) {
            RecipieIngredients ingredient = ingredients.get(editingIngredientIndex);
            ingredient.setIngredient(updatedIngredient);
            ingredient.setAmount(Integer.parseInt(updatedQuantity));
            ingredients.set(editingIngredientIndex, ingredient);

            ingredientComboBox.setValue(null);
            quantityComboBox.setValue(null);
            editingIngredientIndex = -1;
            addIngredientButton.setDisable(false);
            updateIngredientButton.setDisable(true);
        }
    }

    //

    /**
     * Handles creating a new recipe by saving the recipe and its ingredients to the database
     *
     * @param actionEvent the action event which is triggered when the "create recipe" button is clicked
     *
     * @throws IOException handles errors related to loading the recipe management view
     */
    public void handleCreateRecipeClick(ActionEvent actionEvent) throws IOException {
        String recipeText = recipeName.getText();

        if (recipeText != null && !recipeText.trim().isEmpty() && !ingredients.isEmpty()) {
            Recipe newRecipe = new Recipe(recipeText);
            int recipeId = recipeDAO.InsertRecipe(newRecipe);

            if (recipeId != -1) {
                System.out.println("Created recipe with ID: " + recipeId);
                for (RecipieIngredients ingredient : ingredients) {
                    ingredient.setRecipeId(recipeId);
                    recipeDAO.InsertRecipeIngredient(ingredient);
                    System.out.println("Inserted ingredient: " + ingredient.getIngredient().getIngredient() + " for recipe ID: " + recipeId);
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/manage-recipes.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) recipeName.getScene().getWindow();
                Scene scene = new Scene(root, 650, 420);
                // Add stylesheet to the new scene
                scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to create recipe. Please try again.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Recipe name and ingredients must not be empty.");
            alert.showAndWait();
        }
    }

    // Handle back button click

    /**
     * handles the back button functionality, allowing users to go back a page
     *
     * @throws IOException handles errors loading the recipe management view
     */
    @FXML
    protected void backButton() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("manage-recipes.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 420);

        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
        stage.setTitle("Ingredient Tracker");
        stage.setScene(scene);
    }

}