package kyra.me.chess.scripts.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import kyra.me.chess.Chess;
import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.managers.GameState;
import kyra.me.chess.scripts.players.AI;
import kyra.me.chess.scripts.players.Player;
import kyra.me.chess.scripts.tile.Tile;

import java.io.IOException;

public class PlayController {

    @FXML
    private VBox profileList1;  // Left profile list (for Player 1)

    @FXML
    private VBox profileList2;  // Right profile list (for Player 2)

    @FXML
    private ImageView selectedProfileImage1;  // Image for Player 1
    @FXML
    private Label selectedProfileName1;  // Name for Player 1

    @FXML
    private ImageView selectedProfileImage2;  // Image for Player 2
    @FXML
    private Label selectedProfileName2;  // Name for Player 2

    public void initialize() {
        addProfiles();
    }

    private void addProfiles() {
        for (Player player: Database.getPlayers()){
            ImageView profileImage = new ImageView(player.getProfilePicture());
            profileImage.setFitWidth(50);
            profileImage.setFitHeight(50);
            Label nameLabel = new Label(player.getName());

            HBox profileEntry = new HBox(10, profileImage, nameLabel);
            profileEntry.setOnMouseClicked(event -> {
                selectProfile(selectedProfileName1, selectedProfileImage1, player);
                GameManager.playerOne = player;
            });
            profileList1.getChildren().add(profileEntry);


            ImageView profileImage2 = new ImageView(player.getProfilePicture());
            profileImage2.setFitWidth(50);
            profileImage2.setFitHeight(50);
            Label nameLabel2 = new Label(player.getName());

            HBox profileEntry2 = new HBox(10, profileImage2, nameLabel2);
            profileEntry2.setOnMouseClicked(event -> {
                selectProfile(selectedProfileName2, selectedProfileImage2, player);
                GameManager.playerTwo = player;
            });
            profileList2.getChildren().add(profileEntry2);
        }
    }

    private void selectProfile(Label selectedNameLabel, ImageView selectedImageView, Player player) {
        selectedNameLabel.setText(player.getName());
        selectedImageView.setImage(player.getProfilePicture());
    }

    @FXML
    protected void onStartButtonClicked() throws IOException {
        if (GameManager.playerOne == null || GameManager.playerTwo == null) { return; }

        Stage primaryStage = Chess.primaryStage;

        //Create the GridPane for the chessboard
        GridPane gridPane = new GridPane();
        Chess.board = gridPane;
        gridPane.setAlignment(Pos.CENTER);

        //Create a StackPane to hold the grid
        VBox centerPane = new VBox();
        Chess.sceneVBox = centerPane;
        centerPane.setAlignment(Pos.CENTER_RIGHT);
        centerPane.setStyle("-fx-background-color: cyan");

        //Set padding to scale with the smaller side length of the window
        NumberBinding binding = Bindings.min(primaryStage.widthProperty(), primaryStage.heightProperty());

        //Instantiate the tiles
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                StackPane stackPane = new StackPane();
                new Tile(i, j, stackPane);
                stackPane.prefWidthProperty().bind(binding.divide(12));
                stackPane.prefHeightProperty().bind(binding.divide(12));

                gridPane.add(stackPane, i-1, 8-j); // GridPane starts from {0,0} and the top left
            }
        }

        //adds the timers for both players
        Label playerOneTimerLabel = new Label();
        Label playerTwoTimerLabel = new Label();

        playerOneTimerLabel.setFont(new Font(36));
        playerTwoTimerLabel.setFont(new Font(36));
        setTimer(playerOneTimerLabel, false);
        setTimer(playerTwoTimerLabel, true);
        centerPane.getChildren().addAll(playerOneTimerLabel, gridPane, playerTwoTimerLabel);

        //Create a VBox to hold the profile info for Player 1 and Player 2
        VBox profileBox = createProfileBox(GameManager.playerOne, GameManager.playerTwo);
        Chess.playersProfiles = profileBox;
        profileBox.spacingProperty().bind(binding.divide(2));

        //Create a BorderPane to arrange the grid and the profiles side by side
        BorderPane layout = new BorderPane();
        layout.setCenter(centerPane); // Center the chess grid
        layout.setRight(profileBox); // Set the profile display to the right of the grid

        //Add space to the right of the profileBox to prevent sticking to the edge
        BorderPane.setMargin(profileBox, new Insets(10, 20, 10, 10));

        //Set the layout as the scene root
        primaryStage.setScene(new Scene(layout, 800, 600)); // Adjust the size as needed

        //Start the game
        GameManager.gameStart();
    }

    //Helper method to create the profile display
    private VBox createProfileBox(Player playerOne, Player playerTwo) {
        HBox profileOne = createProfileDisplay(playerOne);
        HBox profileTwo = createProfileDisplay(playerTwo);

        VBox profileBox = new VBox(profileTwo, profileOne);

        profileBox.setAlignment(Pos.CENTER);
        profileBox.setStyle("-fx-background-color: cyan;");
        return profileBox;
    }

    //Helper method to create the individual player profile display
    private HBox createProfileDisplay(Player player) {
        if (player == null) {
            return new HBox();
        }

        //Create the profile picture
        ImageView profileImage = new ImageView(player.getProfilePicture());
        profileImage.setFitWidth(75);
        profileImage.setFitHeight(75);

        //Create the name label
        Label nameLabel = new Label(player.getName());
        nameLabel.setStyle("-fx-font-size: 20");

        //Create an HBox for displaying the image and name
        HBox profileDisplay = new HBox(10, nameLabel, profileImage);
        profileDisplay.setAlignment(Pos.CENTER);
        profileDisplay.setStyle("-fx-background-color: cyan;"); // Match the background color of the profile box
        return profileDisplay;
    }

    //Helper method to set the timers for each player
    private void setTimer(Label timerLabel, boolean playerIsWhite) {
        // Initial duration for the timer (5 minutes and 1 second)
        Duration initialDuration = Duration.minutes(5);
        Duration[] remainingDuration = { initialDuration }; // Mutable state for remaining time

        //Create a Timeline to update the timer every second
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            if (playerIsWhite == GameManager.isWhiteTurnTemp) {
                // Decrease the remaining duration by 1 second
                remainingDuration[0] = remainingDuration[0].subtract(Duration.seconds(1));

                // Update the label
                timerLabel.setText(format(remainingDuration[0]));
            }
            //Stop the timer if the remaining duration is zero or negative
            if (remainingDuration[0].lessThanOrEqualTo(Duration.ZERO)) {
                timeline.stop();

                timerLabel.setText(format(Duration.ZERO));
                if (playerIsWhite){
                    GameManager.gameState = GameState.blackWon;
                } else {
                    GameManager.gameState = GameState.whiteWon;
                }
                GameManager.endGame();
            }
            if (remainingDuration[0].lessThanOrEqualTo(Duration.minutes(1))) {
                if(playerIsWhite && GameManager.playerOne instanceof AI){
                    ((AI) GameManager.playerOne).setTightOnTime(true);
                } else if (!playerIsWhite && GameManager.playerTwo instanceof AI){
                    ((AI) GameManager.playerTwo).setTightOnTime(true);
                }
            }
        });
        timeline.getKeyFrames().addAll(keyFrame);

        //Set the timeline to run indefinitely (until manually stopped)
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play(); // Start the timer
    }

    private String format(Duration duration) {
        long seconds = (long) duration.toSeconds();
        return String.format("%02d:%02d:%02d",
                seconds / 3600,      // Hours
                (seconds % 3600) / 60, // Minutes
                seconds % 60         // Seconds
        );
    }
}
