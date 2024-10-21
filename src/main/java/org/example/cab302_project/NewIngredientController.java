package org.example.cab302_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Controller class for handling the logic relating to the addition of new ingredients in the ingredient tracker application
 * This class interacts with the IngredientsDAO for database operations, and updates the parent controllers when a new ingredient
 * is added
 */
public class NewIngredientController {

    @FXML
    private TextField ingredientName;

    @FXML
    private TextField quantity;

    @FXML
    private TextField minQuantity;

    @FXML
    private CheckBox quickAccess;

    @FXML
    private Button backButton;

    @FXML
    private Button createButton;

    private IngredientsDAO ingredientsDAO;
    private ManageIngredientsController manageIngredientsController;
    private ManageRecipesController manageRecipesController;

    /**
     * A constructor which initializes the ingredientsDAO for managing database operations relating to ingredients
     */
    public NewIngredientController() {
        ingredientsDAO = new IngredientsDAO();
    }

    //

    /**
     * Initializes the controller by setting up input validation for the quantity fields by ensuring
     * minimum quantity fields only accept numeric inputs
     */
    @FXML
    public void initialize() {
        // Existing input validation code
        quantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantity.setText(oldValue);
            }
        });

        minQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minQuantity.setText(oldValue);
            }
        });
    }


    /**
     * Set the parent controllers
     *
     * @param controller the instance of ManageIngredientsController, setting it as the parent controller
     */
    public void setManageIngredientsController(ManageIngredientsController controller) {
        this.manageIngredientsController = controller;
    }

    /**
     * Set the parent controllers
     *
     * @param controller the instance of ManageRecipesController, setting it as the parent controller
     */
    public void setManageRecipesController(ManageRecipesController controller) {
        this.manageRecipesController = controller;
    }

    /**
     * Handles the logic for adding a new ingredient in the database
     *
     */
    @FXML
    public void addIngredient() {
        String ingredientNameValue = ingredientName.getText().trim();
        String quantityString = quantity.getText().trim();
        String minQuantityString = minQuantity.getText().trim();
        boolean isQuickAccess = quickAccess.isSelected();

        if (ingredientNameValue.isEmpty() || quantityString.isEmpty() || minQuantityString.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled.");
            return;
        }

        if (ingredientsDAO.ingredientExists(ingredientNameValue)) {
            showAlert(Alert.AlertType.ERROR, "Error", "An ingredient with this name already exists.");
            return;
        }

        try {
            int quantityValue = Integer.parseInt(quantityString);
            int minQuantityValue = Integer.parseInt(minQuantityString);

            Ingredient newIngredient = new Ingredient(ingredientNameValue, quantityValue, minQuantityValue, isQuickAccess);
            ingredientsDAO.Insert(newIngredient);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Ingredient added successfully.");

            if (manageIngredientsController != null) {
                manageIngredientsController.loadIngredients();
                manageIngredientsController.loadQuickAccessIngredients();
            }
            if (manageRecipesController != null) {
                manageRecipesController.refreshIngredients();
            }

            closeWindow();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Quantity and Minimum Quantity must be valid numbers.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the ingredient: " + e.getMessage());
        }
    }

    /**
     * Displays alert messages to the user
     *
     * @param alertType The type of alter
     * @param title the title of the alert
     * @param content the message content for the alter
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * handles what the backbutton should do, in this case closes the window
     *
     */
    @FXML
    protected void backButton() {
        closeWindow();
    }

    /**
     * Handle closing the add ingredient window
     */
    private void closeWindow() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }
}