package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Queen extends Piece implements DiagonalMovingPiece{
    public Queen(Tile tile, boolean isWhite) {
        super(tile, isWhite);
        if (isWhite()){
            this.setImage(models[8]);
        } else {
            this.setImage(models[9]);
        }
    }

    @Override
    public void createMoves(List<Move> moves) {
        createDiagonalMove(moves, this, 0, 1);
        createDiagonalMove(moves, this, 0, -1);
        createDiagonalMove(moves, this, 1, 0);
        createDiagonalMove(moves, this, -1, -0);

        createDiagonalMove(moves, this, 1, 1);
        createDiagonalMove(moves, this, -1, 1);
        createDiagonalMove(moves, this, 1, -1);
        createDiagonalMove(moves, this, -1, -1);
    }
}
