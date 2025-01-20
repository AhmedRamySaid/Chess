package kyra.me.chess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import kyra.me.chess.pieces.Piece;
import kyra.me.chess.tile.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Chess extends Application {
    List<Tile> tiles = new ArrayList<Tile>();
    List<Piece> pieces = new ArrayList<Piece>();
    Color tileMainColor =Color.LIGHTGRAY;
    Color tileOffsetColor = Color.BROWN;

    @Override
    public void start(Stage primaryStage){
        mainMenu(primaryStage);
    }

    public void mainMenu(Stage primaryStage){
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: lightblue;");
        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle("Chess");
        primaryStage.setScene(scene);
        primaryStage.show();

        Button playButton = new Button("Play");
        playButton.setFont(new Font(24));
        playButton.setOnAction(e -> {
            gameScene(primaryStage);
        });
        Button settingsButton = new Button("Settings");
        settingsButton.setFont(new Font(24));
        settingsButton.setOnAction(e -> {
            settingsScene(primaryStage);
        });

        pane.setCenter(playButton);
        //pane.setCenter(settingsButton);
    }
    public void gameScene(Stage primaryStage){
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: lightblue;");
        Scene scene = new Scene(pane, 800, 600);

        //creates the tiles and adds them to a list
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                Tile tile= new Tile(i, j, tileMainColor, tileOffsetColor, primaryStage);
                pane.getChildren().add(tile.getTile());
                tiles.add(tile);
            }
        }
        //scales the tiles size with the window
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            for(Tile tile: tiles){
                tile.sizeSetter(primaryStage);
                tile.locationSetter(primaryStage);
            }
        });
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            for(Tile tile: tiles){
                tile.sizeSetter(primaryStage);
                tile.locationSetter(primaryStage);
            }
        });
        primaryStage.setScene(scene);
    }
    public void settingsScene(Stage primaryStage){
        StackPane mainPane = new StackPane();
        Pane backPane = new Pane();

        Button backButton = new Button("Back");
        backButton.setFont(new Font(24));
        backButton.setOnAction(e -> {
            mainMenu(primaryStage);
        });
        mainPane.getChildren().add(backPane);

        mainPane.setStyle("-fx-background-color: lightblue;");
        Scene scene = new Scene(mainPane, 800, 600);

        TextField FEN = new TextField();
        mainPane.getChildren().add(FEN);

        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
