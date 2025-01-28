package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Rook extends Piece implements DiagonalMovingPiece{
    public Rook(Tile tile, boolean isWhite) {
        super(tile, isWhite);
        if (isWhite()){
            this.setImage(models[6]);
            if (occupiedTile.getYPosition() == 8){
                if (occupiedTile.getXPosition() == 1 || occupiedTile.getXPosition() == 8){
                    hasMoved = true;
                    return;
                }
            }
        } else {
            this.setImage(models[7]);
            if (occupiedTile.getYPosition() == 1){
                if (occupiedTile.getXPosition() == 1 || occupiedTile.getXPosition() == 8){
                    hasMoved = true;
                    return;
                }
            }
        }
        hasMoved = false;
    }

    @Override
    public void createMoves(List<Move> moves) {
        createDiagonalMove(moves, this, 0, 1);
        createDiagonalMove(moves, this, 0, -1);
        createDiagonalMove(moves, this, 1, 0);
        createDiagonalMove(moves, this, -1, -0);
    }
}
