package org.example.cab302_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.sql.Connection;

/**
 * The main application class for the ingredient Tracker application
 * This class extends the JavaFX Views, and serves as the entry point for the GUI and the program.
 * It sets up the home page and initializes the necessary DAOs to interact with the database.
 */
public class IngredientTrackerApplication extends Application {

    // The constants which define the window title and size
    /** The title of the application window. */
    public static final String TITLE = "Ingredient Tracker";
    /** The width of the application window. */
    public static final int WIDTH = 340;
    /** The height of the application window. */
    public static final int HEIGHT = 420;
    private IngredientsDAO ingredientsDAO = new IngredientsDAO();
    private RecipeDAO recipeDAO = new RecipeDAO();
    private UserDAO userDAO = new UserDAO();
    private Connection connection;

    /**
     * Initializes the application by initializing the appropriate DAOs to populate the necessary tables
     */
    @Override
    public void init() {
        ingredientsDAO.createTable();
        recipeDAO.createRecipeTable();
        recipeDAO.createRecipeIngredientTable();
        userDAO.createUserTable(); //create Tables for Users
    }

    /**
     * Starts the JavaFX application and sets the initial GUI/stage
     * loads the login screen from FXML, sets the scene and intializes functionality from LoginController
     *
     * @param stage the primary stage for this application, where the login screen is set
     * @throws IOException handles errors and exceptions when loading FXML files
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 340, 230);


        // Get the LoginController and set the UserDAO
        LoginController loginController = fxmlLoader.getController();
        loginController.setUserDAO(userDAO);

        // Adding the stylesheet to the scene
        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());

        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();

        try {
            connection = DatabaseConnection.getInstance();
        } catch (SQLException e) {
            System.err.println("Error establishing database connection in UserDAO: " + e.getMessage());
            e.printStackTrace();
        }


    }

    /**
     * The entry point and main method, which launches the JavaFX application
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch();
    }
}