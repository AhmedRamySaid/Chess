package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.move.NormalMove;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Knight extends Piece {
    private static final int[][] KNIGHT_MOVES = {
            {-2, -1}, {-2, 1}, {2, -1}, {2, 1},
            {-1, -2}, {-1, 2}, {1, -2}, {1, 2}
    };

    public Knight(boolean isWhite) {
        this(null, isWhite);
    }
    public Knight(Tile tile, boolean isWhite){
        super(tile, isWhite);
        if (isWhite) {
            this.setImage(models[2]);
        } else {
            this.setImage(models[3]);
        }
    }

    @Override
    public void createMoves(List<Move> moves){
        if (GameManager.isDoubleCheck) return;

        if (occupiedTile.isUnderPin()) return;

        //Iterates through all possible combination of moves for a knight
        Tile t;
        for (int[] move : KNIGHT_MOVES) {
            t = Database.getTile(occupiedTile.getXPosition() + move[0],occupiedTile.getYPosition() + move[1]);
            this.addMove(moves, t);
        }
    }

    @Override
    public void createAttacks(){
        //Iterates through all possible combination of moves for a knight
        Tile t;
        for (int[] move : KNIGHT_MOVES) {
            t = Database.getTile(occupiedTile.getXPosition() + move[0],occupiedTile.getYPosition() + move[1]);
            if (t != null) {
                t.toggleUnderAttackOn();
                if (t.getOccupyingPiece() instanceof King){
                    if (t.getOccupyingPiece().isWhite != isWhite){
                        t.toggleUnderCheckOn();
                        GameManager.isDoubleCheck = GameManager.isCheck;
                        GameManager.isCheck = true;
                        occupiedTile.toggleUnderCheckOn();
                    }
                }
            }
        }
    }

    private void addMove(List<Move> moves, Tile endTile){
        if (endTile == null) return;
        if (endTile.getOccupyingPiece() != null) {
            if (endTile.getOccupyingPiece().isWhite() == isWhite()) {
                return;
            }
        }
        if (GameManager.isCheck && !endTile.isUnderCheck()) return;
        Move move = new NormalMove(occupiedTile, endTile);
        moves.add(move);
    }
}
