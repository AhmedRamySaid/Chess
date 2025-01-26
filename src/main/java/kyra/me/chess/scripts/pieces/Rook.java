package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Rook extends Piece implements DiagonalMovingPiece{
    public Rook(Tile tile, boolean isWhite) {
        super(tile, isWhite);
        if (isWhite()){
            this.setImage(models[6]);
        } else {
            this.setImage(models[7]);
        }
    }

    @Override
    public void createMoves(List<Move> moves, boolean isMoveCreation) {
        pieceMoves.clear();
        createDiagonalMove(moves, isMoveCreation, this, 0, 1);
        createDiagonalMove(moves, isMoveCreation, this, 0, -1);
        createDiagonalMove(moves, isMoveCreation, this, 1, 0);
        createDiagonalMove(moves, isMoveCreation, this, -1, -0);
    }
}
