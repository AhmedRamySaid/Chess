package kyra.me.chess.scripts.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import kyra.me.chess.scripts.tile.Tile;

public class GameController {
    @FXML
    private GridPane Board;
    @FXML
    private Pane centerPane;

    public void initializer(Stage primaryStage) {
        Scene scene = new Scene(centerPane, 800, 600);

        //creates the tiles and adds them to a list
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                StackPane p = new StackPane();
                Tile tile= new Tile(i, j, p);
                Board.add(p, i-1, j-1);
                p.getChildren().add(tile);
            }
        }
        //scales the tiles size with the window
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {

        });
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {

        });
        primaryStage.setScene(scene);
    }
}
