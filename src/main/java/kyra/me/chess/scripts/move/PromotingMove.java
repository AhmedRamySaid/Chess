package kyra.me.chess.scripts.move;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import kyra.me.chess.Chess;
import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.pieces.*;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class PromotingMove extends Move {
    private final Piece promotedPiece;
    private final PromotingMove[] variations;
    private VBox selection;

    public PromotingMove(Tile start, Tile end, List<Move> moves) {
        super(start, end, MoveType.promotion);
        this.promotedPiece = new Queen(movingPiece.isWhite());
        this.promotedPiece.setOnMouseClicked(t -> {
            this.doMove();
            this.unshowSelection();
        });

        moves.add(this);
        variations = new PromotingMove[4];
        variations[0] = this;

        variations[1] = new PromotingMove(start, end, new Rook(movingPiece.isWhite()), moves, variations);
        variations[2] = new PromotingMove(start, end, new Bishop(movingPiece.isWhite()), moves, variations);
        variations[3] = new PromotingMove(start, end, new Knight(movingPiece.isWhite()), moves, variations);
    }
    public PromotingMove(Tile start, Tile end, Piece promotedPiece, List<Move> moves, PromotingMove[] variations) {
        super(start, end, MoveType.promotion);
        this.promotedPiece = promotedPiece;
        this.variations = variations;
        moves.add(this);

        promotedPiece.setOnMouseClicked(t -> {
            this.doMove();
            this.unshowSelection();
        });
    }

    @Override
    public void doMove() {
        super.doMove();

        movingPiece.destroy();
        promotedPiece.initialize(endingSquare);
        GameManager.turnStart();
    }

    @Override
    public void doMoveTemporary(){
        super.doMoveTemporary();

        Database.removePiece(movingPiece);
        Database.addPiece(promotedPiece);
        endingSquare.setOccupyingPiece(promotedPiece);
        promotedPiece.setOccupiedTile(endingSquare);
    }

    @Override
    public void undoMove(){
        Database.removePiece(promotedPiece);
        Database.addPiece(movingPiece);
        endingSquare.setOccupyingPiece(movingPiece);
        promotedPiece.setOccupiedTile(endingSquare);

        promotedPiece.setOccupiedTile(null);
        super.undoMove();
    }

    public void showSelection(){
        Scene scene = Chess.primaryStage.getScene();
        BorderPane root = (BorderPane)scene.getRoot();
        VBox vbox = new VBox();
        for (PromotingMove move : variations) {
            move.setSelection(vbox);
        }
        selection.getChildren().addAll(variations[0].promotedPiece, variations[1].promotedPiece, variations[2].promotedPiece, variations[3].promotedPiece);
        root.getChildren().add(selection);
    }

    private void setSelection(VBox selection) {
        this.selection = selection;
    }
    private void unshowSelection(){
        Scene scene = Chess.primaryStage.getScene();
        BorderPane root = (BorderPane)scene.getRoot();

        root.getChildren().remove(selection);
    }
}
