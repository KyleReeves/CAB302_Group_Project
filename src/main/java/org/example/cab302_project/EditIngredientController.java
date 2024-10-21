package org.example.cab302_project;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


/**
 * A controller class for adding an assortment of functionality to the 'Edit Ingredients' GUI/View
 * Such as the functionality for minimum quantity, quick access settings. Interacts directly with INGREDIENTSDAO for
 * database operations and updates ManageIngredientsController upon changes.
 */
public class EditIngredientController {

    @FXML
    private TextField ingredientNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField minQuantityField;

    @FXML
    private CheckBox quickAccessCheckBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private IngredientsDAO ingredientsDAO;
    private Ingredient ingredient;
    private ManageIngredientsController manageIngredientsController;



    /**
     * Initializes the IngredientsDAO
     */
    public void initialize() {
        ingredientsDAO = new IngredientsDAO();
    }

    /**
     *  Sets the ingredient to be edited within the form, and populates the fields
     *
     * @param ingredient The ingredient object which will be edited
     */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        populateFields();
    }

    /**
     * Sets the parent controller ManageIngredients, allowing the ingredient list to be updated after edits are made
     *
     * @param controller is the instance which sets ManageIngredientsController as the parent controller
     */
    public void setManageIngredientsController(ManageIngredientsController controller) {
        this.manageIngredientsController = controller;
    }

    /**
     * Populates the fields with the current data of the ingredients which can be edited.
     *
     */
    private void populateFields() {
        ingredientNameField.setText(ingredient.getIngredient());
        quantityField.setText(String.valueOf(ingredient.getQuantity()));
        minQuantityField.setText(String.valueOf(ingredient.getMinQuantity()));
        quickAccessCheckBox.setSelected(ingredient.isQuick_access());
    }

    /**
     * Handles the saving of changes made to ingredients within the edit GUI.
     * Updates the database with the changes to the ingredients and refreshes the parent controllers ingredient list.
     */
    @FXML
    private void handleSave() {
        try {
            ingredient.setName(ingredientNameField.getText());
            ingredient.setQuantity(Integer.parseInt(quantityField.getText()));
            ingredient.setMinQuantity(Integer.parseInt(minQuantityField.getText()));
            ingredient.setQuick_access(quickAccessCheckBox.isSelected());

            ingredientsDAO.update(ingredient);
            manageIngredientsController.loadIngredients();
            manageIngredientsController.loadQuickAccessIngredients();
            closeWindow();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for Quantity and Min Quantity.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred while saving the ingredient: " + e.getMessage());
        }
    }

    /**
     * Cancels the edit operation and closes the window
     *
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Closes the edit ingredient window
     */
    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the error alerts by displaying a message box with the title and content of an error
     *
     * @param title The title of the error alert
     * @param content The content of the error alert
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}