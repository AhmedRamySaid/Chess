package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Queen extends Piece implements DiagonalMovingPiece{
    public Queen(boolean isWhite){
        this(null, isWhite);
    }
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
        createDiagonalMove(moves, 0, 1);
        createDiagonalMove(moves, 0, -1);
        createDiagonalMove(moves, 1, 0);
        createDiagonalMove(moves, -1, -0);

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

        createDiagonalAttack(0, 1);
        createDiagonalAttack(0, -1);
        createDiagonalAttack(1, 0);
        createDiagonalAttack(-1, 0);
    }
}
