package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Rook extends Piece implements DiagonalMovingPiece{
    public Rook(boolean isWhite){
        this(null, isWhite);
    }
    public Rook(Tile tile, boolean isWhite) {
        super(tile, isWhite);
        if (isWhite()){
            this.setImage(models[6]);
            if (tile == null) { hasMoved = true; return; }
            if (occupiedTile.getYPosition() == 8){
                if (occupiedTile.getXPosition() == 1 || occupiedTile.getXPosition() == 8){
                    hasMoved = true;
                    return;
                }
            }
        } else {
            this.setImage(models[7]);
            if (tile == null) { hasMoved = true; return; }
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
        if (GameManager.isDoubleCheck) return;

        createDiagonalMove(moves, 0, 1);
        createDiagonalMove(moves, 0, -1);
        createDiagonalMove(moves, 1, 0);
        createDiagonalMove(moves, -1, 0);
    }

    @Override
    public void createAttacks(){
        createDiagonalAttack(0, 1);
        createDiagonalAttack(0, -1);
        createDiagonalAttack(1, 0);
        createDiagonalAttack(-1, 0);
    }
}
