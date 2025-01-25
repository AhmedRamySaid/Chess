package kyra.me.chess.scripts.Controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import kyra.me.chess.scripts.GameManager;
import kyra.me.chess.scripts.pieces.Piece;
import kyra.me.chess.scripts.tile.Tile;

import java.io.IOException;
public class MainMenuController {
    @FXML
    private Scene MainMenuScene;

    @FXML
    protected void onPlayButtonClicked() throws IOException {
        Stage primaryStage = (Stage) MainMenuScene.getWindow();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        StackPane centerPane = new StackPane(gridPane);
        centerPane.setStyle("-fx-background-color: cyan");

        // Set padding to scale with window size
        NumberBinding binding = Bindings.min(primaryStage.widthProperty(), primaryStage.heightProperty());
        NumberBinding padding = binding.multiply(0.1); // 5% of the width for padding
        centerPane.setPadding(new Insets(0)); // Initial zero padding

        // Bind the padding to be proportional to the width of the centerPane
        centerPane.paddingProperty().bind(Bindings.createObjectBinding(
                () -> new Insets(padding.doubleValue(), padding.doubleValue(), padding.doubleValue(), padding.doubleValue()),
                padding
        ));

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                // Create the StackPane to hold the Tile
                StackPane stackPane = new StackPane();

                // Create the Tile (Rectangle)
                Tile tile = new Tile(i, j, stackPane);

                // Bind the StackPane size to the available space in the GridPane
                stackPane.prefWidthProperty().bind(binding.divide(10));
                stackPane.prefHeightProperty().bind(binding.divide(10));

                // Add the StackPane to the GridPane
                gridPane.add(stackPane, i-1, 8-j); //GridPane adds from the top-left and starts from (0,0)
            }
        }

        primaryStage.setScene(new Scene(centerPane,600,600));
        GameManager.gameStart();
    }

    @FXML
    protected void onSettingsButtonClicked(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage)MainMenuScene.getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kyra/me/chess/scenes/settings.fxml"));
        Scene scene = fxmlLoader.load();

        primaryStage.setScene(scene);
    }
}