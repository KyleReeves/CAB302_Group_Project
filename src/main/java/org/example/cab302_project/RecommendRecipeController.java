package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RecommendRecipeController {

    private RecipeDAO recipeDAO = new RecipeDAO();
    public static ObservableList<Recipe> RecipeList = FXCollections.observableArrayList();

    @FXML
    private Button backButton;

    @FXML
    private TableView<Recipe> RecipeTable;

    @FXML
    private TableColumn<Recipe, String> Recipe;

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

    // Initialize method: Set up table columns and load data
    @FXML
    public void initialize() {
        RecipeTable = new TableView<Recipe>();
        Recipe.setCellValueFactory(new PropertyValueFactory<>("Recipe"));
        loadRecipes();
    }

    public void loadRecipes() {
        try {
            RecipeList.clear();
            List<Recipe> recipes = recipeDAO.recomendedRecipes();
            System.out.println("Loaded " + recipes.size() + " ingredients from database");
            RecipeList.addAll(recipes);
            RecipeTable.setItems(RecipeList);
            System.out.println("Table items set, size: " + RecipeTable.getItems().size());
        } catch (Exception e) {
            System.err.println("Error loading ingredients: " + e.getMessage());
            e.printStackTrace();
            //showError("Error while loading ingredients", e.getMessage());
        }
        RecipeTable.refresh();
    }
}
