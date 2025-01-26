package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Bishop extends Piece implements DiagonalMovingPiece{
    public Bishop(Tile tile, boolean isWhite) {
        super(tile, isWhite);
        if (isWhite()){
            this.setImage(models[4]);
        } else {
            this.setImage(models[5]);
        }
    }

    @Override
    public void createMoves(List<Move> moves, boolean isMoveCreation) {
        pieceMoves.clear();
        createDiagonalMove(moves, isMoveCreation, this, 1, 1);
        createDiagonalMove(moves, isMoveCreation, this, -1, 1);
        createDiagonalMove(moves, isMoveCreation, this, 1, -1);
        createDiagonalMove(moves, isMoveCreation, this, -1, -1);
    }
}
