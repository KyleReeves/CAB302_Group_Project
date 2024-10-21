package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * The Controller class responsible for the logic related to managing ingredients in the Ingredient Tracker Applcation
 * Provides CRUD functionality for the creating,reading, editing, and deleting ingredients.
 * Interacts with the IngredientsDAO database.
 */
public class ManageIngredientsController {

    public VBox quickAccessSection;

    @FXML
    private Button backButton;

    @FXML
    private Button addIngredientButton;

    @FXML
    private TableView<Ingredient> ingredientsTable;

    @FXML
    private TableColumn<Ingredient, String> ingredientColumn;

    @FXML
    private TableColumn<Ingredient, Integer> quantityColumn;

    @FXML
    private TableColumn<Ingredient, Integer> minQuantityColumn;

    @FXML
    private TableColumn<Ingredient, Boolean> quickAccessColumn;

    @FXML
    private Button plusButton;

    @FXML
    private Button minusButton;


    @FXML
    private TableColumn<Ingredient, Boolean> checkboxColumn;

    public static ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    private IngredientsDAO ingredientsDAO;


    public ManageIngredientsController() {
        ingredientsDAO = new IngredientsDAO();
    }

    /**
     * Sets up table columns, and loads data
     *
     */
    @FXML
    public void initialize() {
        setupTableColumns();
        loadIngredients();
        loadQuickAccessIngredients();
        setupButtonDisabling();
    }

    /**
     * Set up the table columns with appropriate cell value factories which display ingredient information
     */
    private void setupTableColumns() {
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    /**
     * Configures the button disabling logic, based on if an ingredient is selected or not
     */
    private void setupButtonDisabling() {
        plusButton.setDisable(true);
        minusButton.setDisable(true);

        ingredientsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean disableButtons = (newSelection == null);
            plusButton.setDisable(disableButtons);
            minusButton.setDisable(disableButtons);
        });
    }

    /**
     * Load all ingredients from the database, and populates the table
     */
    public void loadIngredients() {
        try {
            ingredientList.clear();
            List<Ingredient> ingredients = ingredientsDAO.getAll();
            System.out.println("Loaded " + ingredients.size() + " ingredients from database");
            ingredientList.addAll(ingredients);
            ingredientsTable.setItems(ingredientList);
            System.out.println("Table items set, size: " + ingredientsTable.getItems().size());
        } catch (Exception e) {
            System.err.println("Error loading ingredients: " + e.getMessage());
            e.printStackTrace();
            showError("Error while loading ingredients", e.getMessage());
        }
    }

    /**
     * Method to dynamically load Quick Access items
     */
    public void loadQuickAccessIngredients() {
        VBox quickAccessVBox = new VBox(10);
        List<Ingredient> quickAccessIngredients = ingredientsDAO.getQuickAccessIngredients();

        for (Ingredient ingredient : quickAccessIngredients) {
            Label ingredientLabel = new Label(ingredient.getIngredient());
            ingredientLabel.setStyle("-fx-text-fill: white;");

            HBox buttonBox = new HBox(5);
            Button subtractButton = new Button("-");
            Button addButton = new Button("+");

            subtractButton.setStyle("-fx-background-color: #ff5c5c; -fx-text-fill: white;");
            addButton.setStyle("-fx-background-color: #00d970; -fx-text-fill: white;");

            // subtract
            subtractButton.setOnAction(event -> {
                updateIngredientQuantity(ingredient, -1);
            });

            // add
            addButton.setOnAction(event -> {
                updateIngredientQuantity(ingredient, 1);
            });

            buttonBox.getChildren().addAll(subtractButton, addButton);
            quickAccessVBox.getChildren().addAll(ingredientLabel, buttonBox);
        }

        quickAccessSection.getChildren().setAll(quickAccessVBox); // Add to section dynamically
    }

    /**
     * Handle the addition of ingredients
     *
     * @throws IOException handles errors when loading new ingredients view
     */
    @FXML
    protected void addIngredientButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(IngredientTrackerApplication.class.getResource("new-ingredient-view.fxml"));
        Parent root = loader.load();

        NewIngredientController newIngredientController = loader.getController();
        newIngredientController.setManageIngredientsController(this);

        Stage stage = new Stage();
        Scene scene = new Scene(root, IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        scene.getStylesheets().add(IngredientTrackerApplication.class.getResource("FormStyles.css").toExternalForm());
        stage.setTitle("Add New Ingredient");
        stage.setScene(scene);
        stage.showAndWait();

        loadIngredients();
        loadQuickAccessIngredients();
    }

    /**
     * Handles editing a selected ingredient from the table
     * Opens a form to edit the ingredient, then reloads table and quick access when its closed
     */
    @FXML
    private void handleEditIngredient() {
        Ingredient selectedIngredient = ingredientsTable.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-ingredient-view.fxml"));
                Parent root = loader.load();

                EditIngredientController editController = loader.getController();
                editController.setIngredient(selectedIngredient);
                editController.setManageIngredientsController(this);

                Stage stage = new Stage();
                stage.setTitle("Edit Ingredient");
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } catch (IOException e) {
                showError("Error", "Could not open edit window: " + e.getMessage());
            }
        } else {
            showError("No Selection", "Please select an ingredient to edit.");
        }
    }

    //

    /**
     * Updates the quantity of an ingredient
     *
     * @param ingredient the ingredient which will be updated
     * @param delta the amount which the quantity will change by
     */
    private void updateIngredientQuantity(Ingredient ingredient, int delta) {

        int newQuantity = ingredient.getQuantity() + delta;
        if (newQuantity < 0) {
            newQuantity = 0;
        }
        ingredient.setQuantity(newQuantity);

        // update database
        ingredientsDAO.update(ingredient);

        updateTableView(ingredient);
    }

    /**
     * Updates the table to show the updated quantity of the ingredient
     *
     * @param updatedIngredient the ingredient with updated data
     */
    private void updateTableView(Ingredient updatedIngredient) {

        for (Ingredient ingredient : ingredientList) {
            if (ingredient.getId() == updatedIngredient.getId()) {
                ingredient.setQuantity(updatedIngredient.getQuantity());
                break;
            }
        }
        // refresh table
        ingredientsTable.refresh();
    }

    /**
     * Handles the deletion of the selected ingredient from the table
     * Gives the user a confirmation prompt before it gets deleted
     */
    @FXML
    protected void handleDeleteSelected() {
        Ingredient selectedIngredient = ingredientsTable.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Ingredient");
            alert.setHeaderText("Are you sure you want to delete this ingredient?");
            alert.setContentText(selectedIngredient.getIngredient());

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean deleted = ingredientsDAO.delete(selectedIngredient);
                    if (deleted) {
                        ingredientList.remove(selectedIngredient);
                        loadIngredients(); // Refresh the table
                        loadQuickAccessIngredients(); // Refresh quick access section
                        showInfo("Ingredient Deleted", "The ingredient has been successfully deleted.");
                    } else {
                        showError("Delete Failed", "Failed to delete the ingredient. Please try again.");
                    }
                }
            });
        } else {
            showError("No Selection", "Please select an ingredient to delete.");
        }
    }

    /**
     * Increments the quantity of the selected ingredient by 1
     */
    @FXML
    private void handlePlusButton() {
        updateSelectedIngredientQuantity(1);
    }

    /**
     * Decrements the quantity of the selected ingredient by 1
     */
    @FXML
    private void handleMinusButton() {
        updateSelectedIngredientQuantity(-1);
    }

    /**
     * Updates the quantity of the selected ingredient by the specified delta
     *
     * @param delta the amount to change the quantity by
     */
    private void updateSelectedIngredientQuantity(int delta) {
        Ingredient selectedIngredient = ingredientsTable.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            int newQuantity = Math.max(0, selectedIngredient.getQuantity() + delta);
            selectedIngredient.setQuantity(newQuantity);
            ingredientsDAO.update(selectedIngredient);
            ingredientsTable.refresh();
            loadQuickAccessIngredients(); // Refresh quick access section if needed
        }
    }

    /**
     * displays the information in a pop-up alert
     *
     * @param title title of pop up
     * @param message message in pop up
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles going back to the previous page when the back button is clicked, ensuring title and formatting remain consistent
     *
     * @throws IOException handles errors when loading views from file
     */
    @FXML
    protected void backButton() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);

        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
        stage.setTitle("Ingredient Tracker");
        stage.setScene(scene);
    }

    /**
     * Displays an error message in a pop-up alert
     *
     * @param title title of pop up
     * @param message message in pop up
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Loads all ingredients from the database and refreshes the table view
     */
    private void loadAllIngredients() {
        ingredientList.clear();
        ingredientList.addAll(ingredientsDAO.getAll());
        ingredientsTable.refresh();
    }

    /**
     * closes database connection when the program exits or database operations are completed
     */
    public void closeDatabaseConnection() {
        ingredientsDAO.close();
        System.out.println("Database connection closed.");
    }

}


