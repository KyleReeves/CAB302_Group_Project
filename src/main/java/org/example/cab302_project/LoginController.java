package org.example.cab302_project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Controller class for handling the login and registration logic in the Ingredient Tracker Application
 * Interacts with UserDAO to authenticate users and register new users
 * Manages the navigation to the menu view after a login is successful
 */
public class LoginController {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label messageLabel;

    private UserDAO userDAO;


    /**
     * Sets the UserDAO for this controller. Injects the UserDAO dependency from the main application
     *
     * @param userDAO the UserDAO object which handles user related database operations
     */
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Handles the login Process when the enters and submits their username and password
     * Checks if UserDAO is inialized, then attemts to authenticate the user
     * If a login is successful, it redirects to the menu view. Otherwise, an error message appears
     */
    @FXML
    public void handleLogin() {
        if (userDAO == null) {
            messageLabel.setText("Error: UserDAO not initialized");
            return;
        }
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userDAO.loginUser(username, password);
        if (user != null) {
            messageLabel.setText("Login successful!");
            navigateToMenuView();
        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }

    /**
     * Handles the registration process when a user submits their desired username and password
     * attempts to register the user, and if successful, Displays a success message, otherwise displays an
     * error message
     */
    @FXML
    private void handleRegister() {
        if (userDAO == null) {
            messageLabel.setText("Error: UserDAO not initialized");
            return;
        }
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (userDAO.registerUser(username, password)) {
            messageLabel.setText("Registration successful! You can now login.");
        } else {
            messageLabel.setText("Registration failed. Username might be taken.");
        }
    }

    /**
     * Redirects users to the menu view once a login is successful
     * It loads the menu view from the FXML file, and sets up the menuController
     * while applying the appropriate stylesheet
     */
    private void navigateToMenuView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cab302_project/menu-view.fxml"));
            Parent menuView = loader.load();

            // Get the MenuController and set the UserDAO
            MenuController menuController = loader.getController();
            menuController.setUserDAO(this.userDAO);

            Scene menuScene = new Scene(menuView, IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);

            // Apply the CSS
            menuScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/example/cab302_project/FormStyles.css")).toExternalForm());

            // Get the current stage
            Stage stage = (Stage) usernameField.getScene().getWindow();

            // Set the new scene
            stage.setScene(menuScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error loading menu view.");
        }
    }
}