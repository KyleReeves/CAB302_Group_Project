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
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RecommendRecipeController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableView<Recipe> recipesTable;

    @FXML
    private TableColumn<Recipe, String> recipeNameColumn;

    @FXML
    private TableColumn<Recipe, String> ingredientsColumn;

    private RecipeDAO recipeDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recipeDAO = new RecipeDAO();
        setupRecipesTable();
        loadRecommendedRecipes();
    }

    private void setupRecipesTable() {
        recipeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ingredientsColumn.setCellValueFactory(cellData -> {
            Recipe recipe = cellData.getValue();
            String ingredientsList = recipe.getIngredients().stream()
                    .map(ri -> ri.getIngredient().getIngredient() + " (" + ri.getAmount() + ")")
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(ingredientsList);
        });
    }

    private void loadRecommendedRecipes() {
        List<Recipe> recommendedRecipes = recipeDAO.getRecommendedRecipes();

        for (Recipe recipe : recommendedRecipes) {
            List<RecipieIngredients> ingredients = recipeDAO.getIngredientsForRecipe(recipe.getId());
            recipe.getIngredients().setAll(ingredients);
        }

        ObservableList<Recipe> observableRecipes = FXCollections.observableArrayList(recommendedRecipes);
        recipesTable.setItems(observableRecipes);

        System.out.println("Debug: Recommended recipes count: " + recommendedRecipes.size());
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
