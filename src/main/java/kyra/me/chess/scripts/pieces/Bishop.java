package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Bishop extends Piece implements DiagonalMovingPiece{
    public Bishop(boolean isWhite) {
        this(null, isWhite);
    }
    public Bishop(Tile tile, boolean isWhite) {
        super(tile, isWhite);
        if (isWhite()){
            this.setImage(models[4]);
        } else {
            this.setImage(models[5]);
        }
    }

    @Override
    public void createMoves(List<Move> moves) {
        if (GameManager.isDoubleCheck) return;

        createDiagonalMove(moves, 1, 1);
        createDiagonalMove(moves, -1, 1);
        createDiagonalMove(moves, 1, -1);
        createDiagonalMove(moves, -1, -1);
    }

    @Override
    public void createAttacks(){
        createDiagonalAttack(1, 1);
        createDiagonalAttack(-1, 1);
        createDiagonalAttack(1, -1);
        createDiagonalAttack(-1, -1);
    }
}
