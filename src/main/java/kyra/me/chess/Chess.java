package kyra.me.chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kyra.me.chess.scripts.pieces.Piece;
import kyra.me.chess.scripts.players.Player;
import kyra.me.chess.scripts.tile.Tile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Chess extends Application {
    List<Tile> tiles = new ArrayList<Tile>();
    List<Piece> pieces = new ArrayList<Piece>();
    Color tileMainColor =Color.LIGHTGRAY;
    Color tileOffsetColor = Color.BROWN;
    public static Stage primaryStage;
    public static GridPane board;
    public static VBox sceneVBox;
    public static VBox playersProfiles;

    @Override
    public void start(Stage primaryStage) throws IOException {
        //Creating profiles
        Player.generateProfiles();

        Chess.primaryStage = primaryStage;
        primaryStage.show();
        primaryStage.setTitle("Chess");
        FXMLLoader mainMenu = new FXMLLoader(getClass().getResource("/kyra/me/chess/scenes/main-menu.fxml"));
        Scene scene = mainMenu.load();
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
