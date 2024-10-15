package org.example.cab302_project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Objects;

/**
 * The controller class which manages the main menu view for the ingredient tracker app. This class primarily provides
 * the functionality to go between the different menus, ensuring the styling and windows are appropriate per view transition.
 * This also provides the functionality to log out
 */
public class MenuController {


    @FXML
    private Button shoppingButton;

    @FXML
    private Button recommendRecipeButton;

    @FXML
    private Button manageIngredientsButton;

    @FXML
    private Button manageRecipesButton;

    @FXML
    private Button logoutButton;

    private UserDAO userDAO;


    /**
     * Redirects the user to the recommend recipe view
     *
     * @throws IOException handles error when loading recommend recipe fxml file
     */
    @FXML
    protected void recommendRecipeButton() throws IOException {

        Stage stage = (Stage) recommendRecipeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("recommend-recipe-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);
        stage.setTitle("Recommend Recipe");

        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());

        stage.setScene(scene);
    }

    /**
     * Redirects a user to the Manage Ingredients view
     *
     * @throws IOException handles errors if there is one loading the manage ingredients view
     */
    @FXML
    protected void manageIngredientsButton() throws IOException {
        Stage stage = (Stage) manageIngredientsButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("manage-ingredients-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, IngredientTrackerApplication.HEIGHT);
        stage.setTitle("Manage Ingredients");

        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());

        stage.setScene(scene);
    }

    /**
     * Redirects a user to the shopping list view
     *
     * @throws IOException handles errors in loading the shopping list fxml view
     */
    @FXML
    protected void ShoppingListButton() throws IOException {
        Stage stage = (Stage) shoppingButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("shopping-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 450);
        stage.setTitle("Shopping List");


        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());

        stage.setScene(scene);
    }

    /**
     * Redirects a user to the manage recipes view
     *
     * @throws IOException handles errors when loading the manage recipes fxml view
     */
    @FXML
    protected void manageRecipesButton() throws IOException {
        Stage stage = (Stage) manageRecipesButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("manage-recipes.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 560, 420);
        stage.setTitle("Manage Recipes");

        // Add stylesheet to the new scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());

        stage.setScene(scene);
    }


    /**
     * Setter for the UserDao for this controller
     *
     * @param userDAO
     */
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Handles the logout process. Which displays a confirmation alert, and if confirmed redirects the user back
     * To the login/registration view
     */
    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout");
        alert.setContentText("Are you sure you want to logout?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                // Load the login view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/login.fxml"));
                Parent loginView = loader.load();

                // Get the LoginController and set the UserDAO
                LoginController loginController = loader.getController();
                loginController.setUserDAO(this.userDAO);
                // Set the login scene
                Scene loginScene = new Scene(loginView, 340, 230);

                // Apply the CSS
                loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/example/cab302_project/FormStyles.css")).toExternalForm());

                Stage stage = (Stage) recommendRecipeButton.getScene().getWindow();
                stage.setScene(loginScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., show an error alert)
            }
        }

    }








}