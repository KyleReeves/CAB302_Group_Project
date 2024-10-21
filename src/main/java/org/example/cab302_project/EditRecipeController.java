package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * A Controller class for the Edit recipe View
 * This class handles the logic for the interactions between the recipe ingredients, allowing
 * users to add, update and remove ingredients from a recipe. It also performs
 * database operations with RecipeDAO and IngredientsDAO
 *
 */
public class EditRecipeController {
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
    // Recipe being edited
    private Recipe recipe;
    // Index of the ingredient being edited
    private int editingIngredientIndex = -1;
    // DAOs for database operations
    private RecipeDAO recipeDAO;
    private IngredientsDAO ingredientsDAO;

    /**
     * Contructor which initializes both the RecipeDAO and IngredientsDAO objects.
     */
    public EditRecipeController() {
        recipeDAO = new RecipeDAO();
        ingredientsDAO = new IngredientsDAO();
    }

    /**
     * Sets the recipe to be editing and populates the GUI with its data
     *
     * @param recipe The recipe object to be edited
     */
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipeName.setText(recipe.getName());
        loadRecipeIngredients();
        setupIngredientListView();
    }

    /**
     *  Initializes UI components such as the ComboBox for ingredients and quantity,
     *  loading the data for the Ingredients
     */
    @FXML
    public void initialize() {
        quantityComboBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        loadIngredients();
        setupIngredientListView();
    }

    /**
     * Loads all ingredients for the current recipe and displays them within the list to be viewed
     */
    private void loadIngredients() {
        List<Ingredient> allIngredients = ingredientsDAO.getAll();
        ingredientComboBox.setItems(FXCollections.observableArrayList(allIngredients));
    }

    /**
     * Load Ingredients for the current recipe and displays them within the list
     *
     */
    private void loadRecipeIngredients() {
        List<RecipieIngredients> recipeIngredients = recipeDAO.getIngredientsForRecipe(recipe.getId());
        ingredientList.setItems(FXCollections.observableArrayList(recipeIngredients));
        System.out.println("Loaded " + recipeIngredients.size() + " ingredients for recipe: " + recipe.getName());
    }

    /**
     * Sets up the ingredient list view with custom edit and delete buttons to delete a single ingredient within the list
     *
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
                            recipeDAO.deleteRecipeIngredient(ingredient);
                            loadRecipeIngredients();
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
     * Handles adding a new Ingredient to the recipe
     *
     * @param event The action event which is triggered when add button is clicked
     */
    @FXML
    public void handleAddIngredient(ActionEvent event) {
        Ingredient selectedIngredient = ingredientComboBox.getValue();
        String quantity = quantityComboBox.getValue();

        if (selectedIngredient != null && quantity != null) {
            RecipieIngredients newIngredient = new RecipieIngredients(recipe.getId(), selectedIngredient, Integer.parseInt(quantity));
            recipeDAO.InsertRecipeIngredient(newIngredient);
            loadRecipeIngredients();
            ingredientComboBox.setValue(null);
            quantityComboBox.setValue(null);
        }
    }

    /**
     * Handles saving the edited recipe
     *
     * @param event the action event triggered when the save button is clicked
     * @throws IOException handles issues present when loading a new scene
     */
    @FXML
    public void handleSaveRecipe(ActionEvent event) throws IOException {
        recipe.setName(recipeName.getText());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/manage-recipes.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) recipeName.getScene().getWindow();
        Scene scene = new Scene(root, 650, 420);
        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles updating an existing ingredient in the recipe
     *
     * @param actionEvent the action event triggered when the update ingredient is clicked
     */
    public void handleUpdateIngredient(ActionEvent actionEvent) {
        Ingredient updatedIngredient = ingredientComboBox.getValue();
        String updatedQuantity = quantityComboBox.getValue();

        if (editingIngredientIndex >= 0 && updatedIngredient != null && updatedQuantity != null) {
            RecipieIngredients ingredient = ingredientList.getItems().get(editingIngredientIndex);
            ingredient.setIngredient(updatedIngredient);
            ingredient.setAmount(Integer.parseInt(updatedQuantity));

            // Update ingredient in database
            boolean updated = recipeDAO.updateRecipeIngredient(ingredient);

            if (updated) {
                System.out.println("Successfully updated ingredient: " + ingredient.getIngredient().getIngredient() + " (Qty: " + ingredient.getAmount() + ")");
            } else {
                System.out.println("Failed to update ingredient");
            }

            loadRecipeIngredients();
            ingredientComboBox.setValue(null);
            quantityComboBox.setValue(null);
            editingIngredientIndex = -1;
            addIngredientButton.setDisable(false);
            updateIngredientButton.setDisable(true);
        }
    }

    /**
     * Handles the back button click, returning the user to the previous screen
     *
     * @throws IOException handles issues present with loading previous view
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