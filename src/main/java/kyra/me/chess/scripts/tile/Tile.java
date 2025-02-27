package kyra.me.chess.scripts.tile;

import javafx.event.Event;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import kyra.me.chess.Chess;
import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.pieces.Piece;

public class Tile extends Rectangle {
    private final int xPosition;
    private final int yPosition;
    private Piece occupyingPiece;
    private Circle playableMove;
    private boolean isUnderAttack;
    private boolean isUnderPin;
    private boolean isUnderCheck;

    public Tile(int x, int y, StackPane pane) {
        pane.getChildren().add(this);
        xPosition = x;
        yPosition = y;

        playableMove = new Circle();
        playableMove.radiusProperty().bind(this.widthProperty().divide(6));
        playableMove.setFill(Color.RED);
        playableMove.setOpacity(0);
        isUnderAttack = false;

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

        this.setColor(Chess.tileMainColor, Chess.tileOffsetColor);
        this.widthProperty().bind(getStackPane().prefWidthProperty());
        this.heightProperty().bind(getStackPane().prefHeightProperty());
        Database.addTile(this);
        getStackPane().getChildren().add(playableMove);
    }

    public void togglePlayableMoveOn(){
        if (occupyingPiece == null) {
            playableMove.setOpacity(0.5);
        } else {
            setColor(Color.RED, Color.RED);
        }
    }
    public void togglePlayableMoveOff(){
        playableMove.setOpacity(0);
        setColor(Chess.tileMainColor, Chess.tileOffsetColor);
    }

    public void toggleUnderAttackOn(){
        isUnderAttack = true;
    }
    public void toggleUnderPinOn(){
        isUnderPin = true;
    }
    public void toggleUnderCheckOn(){
        isUnderCheck = true;
    }
    public void toggleAllOff(){
        isUnderAttack = false;
        isUnderPin = false;
        isUnderCheck = false;
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
    public StackPane getStackPane() { return (StackPane)getParent(); }
    public boolean isUnderAttack() { return isUnderAttack; }
    public boolean isUnderPin() { return isUnderPin; }
    public boolean isUnderCheck() { return isUnderCheck; }
    public int getXPosition(){ return xPosition; }
    public int getYPosition(){ return yPosition; }
}
