package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.Database;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Pawn extends Piece {

    public Pawn(Tile tile, boolean isWhite) {
        super(tile, isWhite);
        if (isWhite) {
            this.setImage(models[0]);
        } else {
            this.setImage(models[1]);
        }
        if (isWhite && occupiedTile.getYPosition() == 2) { hasMoved = false; }
        else if (!isWhite && occupiedTile.getYPosition() == 7) { hasMoved = false; }
        else { hasMoved = true; }
    }
    @Override
    public void createMoves(List<Move> moves, boolean isMoveCreation){
        int color = isWhite? 1: -1; //if white, the pawn goes up. if black, the pawn goes down
        Tile t = Database.getTile(occupiedTile.getXPosition(), occupiedTile.getYPosition() + color);
        if (t != null) {
            if (t.getOccupyingPiece() == null) {
                this.addMove(moves, t);
            }
        }

        t = Database.getTile(occupiedTile.getXPosition()+1, occupiedTile.getYPosition() + color);
        if (t != null) {
            if (t.getOccupyingPiece() != null) {
                if (t.getOccupyingPiece().isWhite() != isWhite()) {
                    this.addMove(moves, t);
                }
            }
        }

        t = Database.getTile(occupiedTile.getXPosition()-1, occupiedTile.getYPosition() + color);
        if (t != null) {
            if (t.getOccupyingPiece() != null) {
                if (t.getOccupyingPiece().isWhite() != isWhite()) {
                    this.addMove(moves, t);
                }
            }
        }

        if (!hasMoved){
            t = Database.getTile(occupiedTile.getXPosition(), occupiedTile.getYPosition() + 2 * color);
            if (t != null) {
                if (t.getOccupyingPiece() == null) {
                    this.addMove(moves, t);
                }
            }
        }
    }
}
