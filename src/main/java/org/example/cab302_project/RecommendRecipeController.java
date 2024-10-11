package org.example.cab302_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class RecommendRecipeController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableView<Recipe> recipesTable;

    private RecipeDAO recipeDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recipeDAO = new RecipeDAO();
        setupRecipesTable();
        loadRecipes();
    }

    private void setupRecipesTable() {
        TableColumn<Recipe, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Recipe, String> nameColumn = new TableColumn<>("Recipe Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        recipesTable.getColumns().addAll(idColumn, nameColumn);
    }

    private void loadRecipes() {
        List<Recipe> recipes = recipeDAO.getAll();
        ObservableList<Recipe> observableRecipes = FXCollections.observableArrayList(recipes);
        recipesTable.setItems(observableRecipes);
    }

    @FXML
    protected void backButton() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(IngredientTrackerApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), IngredientTrackerApplication.WIDTH, IngredientTrackerApplication.HEIGHT);

        scene.getStylesheets().add(Objects.requireNonNull(IngredientTrackerApplication.class.getResource("FormStyles.css")).toExternalForm());
        stage.setTitle("Ingredient Tracker");
        stage.setScene(scene);
    }
}