package kyra.me.chess.scripts.tile;

import javafx.event.Event;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import kyra.me.chess.scripts.Database;
import kyra.me.chess.scripts.GameManager;
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
                if (occupyingPiece == null){ return; }
                if (occupyingPiece.isWhite() != GameManager.isWhiteTurn) { return; }
                Database.setSelectedPiece(occupyingPiece);
            }
            else {
                if (occupyingPiece == null){ Database.getSelectedPiece().moveTo(this); return; }
                if (Database.getSelectedPiece().isWhite() == occupyingPiece.isWhite()) { Database.setSelectedPiece(occupyingPiece); return; }
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

    public void togglePlayableMoveOn(){
        playableMove.setOpacity(0.5);
    }
    public void togglePlayableMoveOff(){
        playableMove.setOpacity(0);
    }

    public void setColor(Color mainColor, Color offsetColor) {
        if (xPosition%2 == yPosition%2){
            this.setFill(offsetColor);
        }
        else{
            this.setFill(mainColor);
        }
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Tile){
            Tile other = (Tile) obj;
            return other.getXPosition() == this.getXPosition() && other.getYPosition() == this.getYPosition();
        }
        return false;
    }

    public void setOccupyingPiece(Piece piece) { occupyingPiece = piece; }
    public Piece getOccupyingPiece() { return occupyingPiece; }
    public StackPane getStackPane() { return stackPane; }
    public int getXPosition(){ return xPosition; }
    public int getYPosition(){ return yPosition; }
}
