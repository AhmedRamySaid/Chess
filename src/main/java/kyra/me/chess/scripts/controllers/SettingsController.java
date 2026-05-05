package kyra.me.chess.scripts.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import kyra.me.chess.Chess;

import java.io.IOException;

public class SettingsController {
    @FXML
    protected void onBackButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kyra/me/chess/scenes/main-menu.fxml"));
        Scene scene = fxmlLoader.load();
        Chess.primaryStage.setScene(scene);
        Chess.primaryStage.setFullScreen(true);
    }
}