package kyra.me.chess.scripts.tile;

import javafx.event.Event;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import kyra.me.chess.scripts.Database;
import kyra.me.chess.scripts.pieces.Piece;

public class Tile extends Rectangle {
    private final int xPosition;
    private final int yPosition;
    private Piece occupyingPiece;
    private Circle playableMove;
    StackPane stackPane;

    public Tile(int x, int y, StackPane p){
        stackPane = p;
        xPosition = x;
        yPosition = y;

        playableMove = new Circle();
        playableMove.radiusProperty().bind(widthProperty().divide(6));
        playableMove.setFill(Color.RED);
        playableMove.setOpacity(0);

        setOnMouseEntered(event -> {
            this.setOpacity(0.5);
        });
        setOnMouseExited(event -> {
            this.setOpacity(1);
        });
        setOnMouseClicked((Event event) -> {
            if (Database.getSelectedPiece() == null){
                Database.setSelectedPiece(this.occupyingPiece);
            }
            else {
                Database.getSelectedPiece().moveTo(this);
            }
        });

        playableMove.setOnMouseEntered(event -> {
            this.setOpacity(0.5);
        });
        playableMove.setOnMouseExited(event -> {
            this.setOpacity(1);
        });
        playableMove.setOnMouseClicked((Event event) -> {
            if (Database.getSelectedPiece() == null){
                Database.setSelectedPiece(this.occupyingPiece);
            }
            else {
                Database.getSelectedPiece().moveTo(this);
            }
        });

        this.setColor(Color.WHITE, Color.BLACK);
        this.widthProperty().bind(stackPane.prefWidthProperty());
        this.heightProperty().bind(stackPane.prefHeightProperty());
        Database.addTile(this);
        stackPane.getChildren().addAll(this, playableMove);
    }

    public void playableMoveToggle(){
        if (playableMove.opacityProperty().get() == 0){
            playableMove.setOpacity(0.5);
        }
        else {
            playableMove.setOpacity(0);
        }
    }

    public void setColor(Color mainColor, Color offsetColor) {
        if (xPosition%2 == yPosition%2){
            this.setFill(offsetColor);
        }
        else{
            this.setFill(mainColor);
        }
    }

    public void setOccupyingPiece(Piece piece) {
        occupyingPiece = piece;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public int getXPosition(){
        return xPosition;
    }
    public int getYPosition(){
        return yPosition;
    }
}
