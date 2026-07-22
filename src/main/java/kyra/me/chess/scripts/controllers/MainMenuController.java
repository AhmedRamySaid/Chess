package kyra.me.chess.scripts.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import kyra.me.chess.Chess;

import java.io.IOException;

public class MainMenuController {
    @FXML
    protected void onPlayButtonClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kyra/me/chess/scenes/play.fxml"));
        Parent node = fxmlLoader.load();

        Chess.primaryStage.getScene().setRoot(node);
    }

    @FXML
    protected void onSettingsButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kyra/me/chess/scenes/settings.fxml"));
        Parent node = fxmlLoader.load();

        Chess.primaryStage.getScene().setRoot(node);
    }
}