package kyra.me.chess.scripts.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private Scene MainMenuScene;

    @FXML
    protected void onPlayButtonClicked() throws IOException {
        Stage primaryStage = (Stage)MainMenuScene.getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kyra/me/chess/scenes/play.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setScene(scene);
    }

    @FXML
    protected void onSettingsButtonClicked(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage)MainMenuScene.getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kyra/me/chess/scenes/settings.fxml"));
        Scene scene = fxmlLoader.load();

        primaryStage.setScene(scene);
    }
}