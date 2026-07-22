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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

            //creates the profile options and adds them to the lists
            HBox profileEntry1 = new HBox(10, profileImage, nameLabel);
            profileEntry1.setOnMouseClicked(event -> {
                selectProfile(selectedProfileName1, selectedProfileImage1, player);
                GameManager.playerOne = player;
            });
            profileList1.getChildren().add(profileEntry1);


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

		// ── Board Grid ────────────────────────────────────────────────────────────
		GridPane gridPane = new GridPane();
		Chess.board = gridPane;
		gridPane.setAlignment(Pos.CENTER);

		// Subtle drop-shadow beneath the board
		DropShadow boardShadow = new DropShadow();
		boardShadow.setColor(Color.color(0, 0, 0, 0.6));
		boardShadow.setRadius(24);
		boardShadow.setOffsetY(8);
		gridPane.setEffect(boardShadow);

		// ── Tiles ─────────────────────────────────────────────────────────────────
		NumberBinding binding = Bindings.min(primaryStage.widthProperty(), primaryStage.heightProperty());

		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				StackPane stackPane = new StackPane();
				new Tile(i, j, stackPane);
				stackPane.prefWidthProperty().bind(binding.divide(11));
				stackPane.prefHeightProperty().bind(binding.divide(11));
				gridPane.add(stackPane, i - 1, 8 - j);
			}
		}

		// ── Timers ────────────────────────────────────────────────────────────────
		Label playerOneTimerLabel = createTimerLabel();
		Label playerTwoTimerLabel = createTimerLabel();
		setTimer(playerOneTimerLabel, true);
		setTimer(playerTwoTimerLabel, false );

		// ── Board Column (timers + board) ─────────────────────────────────────────
		VBox centerPane = new VBox(10);
		Chess.sceneVBox = centerPane;
		centerPane.setAlignment(Pos.CENTER);
		centerPane.setPadding(new Insets(28));

		// Board label strip (rank/file decorations optional — just spacing here)
		centerPane.getChildren().addAll(playerTwoTimerLabel, gridPane, playerOneTimerLabel);

		// ── Profiles ──────────────────────────────────────────────────────────────
		VBox profileBox = createProfileBox(GameManager.playerOne, GameManager.playerTwo);
		Chess.playersProfiles = profileBox;
		profileBox.setSpacing(40);
		profileBox.setAlignment(Pos.CENTER);
		profileBox.setPadding(new Insets(32, 28, 32, 20));
		profileBox.setMinWidth(210);
		profileBox.setMaxWidth(260);
		profileBox.setStyle(
				"-fx-background-color: #1a1210;" +
						"-fx-border-color: #7a5c2e;" +
						"-fx-border-width: 0 0 0 1;"   // left gold separator line
		);

		// ── Root Layout ───────────────────────────────────────────────────────────
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: #110e0c;"); // deep espresso bg
		layout.setCenter(centerPane);
		layout.setRight(profileBox);

		BorderPane.setMargin(centerPane, new Insets(0, 0, 0, 16));
		BorderPane.setMargin(profileBox, new Insets(0, 0, 0, 0));

		primaryStage.getScene().setRoot(layout);

		// ── Start ─────────────────────────────────────────────────────────────────
		GameManager.gameStart();
	}

	// ── Helper: styled timer label ────────────────────────────────────────────────
	private Label createTimerLabel() {
		Label label = new Label();
		label.setFont(Font.font("Courier New", FontWeight.BOLD, 34));
		label.setTextFill(Color.web("#c9a84c"));        // warm gold
		label.setStyle(
				"-fx-background-color: #1e1814;" +
						"-fx-background-radius: 6;" +
						"-fx-padding: 6 18 6 18;"
		);
		return label;
	}

    //Helper method to create the profile display
    private VBox createProfileBox(Player playerOne, Player playerTwo) {
        HBox profileOne = createProfileDisplay(playerOne);
        HBox profileTwo = createProfileDisplay(playerTwo);

        VBox profileBox = new VBox(profileTwo, profileOne);

        profileBox.setAlignment(Pos.CENTER);
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
        return profileDisplay;
    }

    //Helper method to set the timers for each player
    private void setTimer(Label timerLabel, boolean playerIsWhite) {
        Duration initialDuration = Duration.minutes(5);
        Duration[] remainingDuration = { initialDuration }; //Mutable state for remaining time

        //Creates a Timeline to update the timer every second
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            if (GameManager.gameState != GameState.normal) {
                timeline.stop();
                timeline.getKeyFrames().clear();
            }

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
