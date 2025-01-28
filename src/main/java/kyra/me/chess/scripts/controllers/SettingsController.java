package kyra.me.chess.scripts.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {
    public Scene SettingsScene;

    @FXML
    protected void onBackButtonClicked(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage)SettingsScene.getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kyra/me/chess/scenes/main-menu.fxml"));
        Scene scene = fxmlLoader.load();
        primaryStage.setScene(scene);
    }
}