package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.move.NormalMove;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Knight extends Piece {
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
        int[] values = {1, 2};

        // Iterate through {1, 2}, {-1, 2}, {1, -2}, {-1, -2}
        for (int i = 0; i < 4; i++) {
            Tile t = Database.getTile(occupiedTile.getXPosition() + values[0], occupiedTile.getYPosition() + values[1]);
            this.addMove(moves, t);
            t = Database.getTile(occupiedTile.getXPosition() + values[1], occupiedTile.getYPosition() + values[0]);
            this.addMove(moves, t);

            // Perform the transformation
            if (i % 2 == 0) {
                values[0] = -values[0];  // Toggle the first value
            } else {
                values[1] = -values[1];  // Toggle the second value
            }
        }
    }

    @Override
    public void createAttacks(){
        int[] values = {1, 2};

        // Iterate through {1, 2}, {-1, 2}, {1, -2}, {-1, -2}
        for (int i = 0; i < 4; i++) {
            Tile t = Database.getTile(occupiedTile.getXPosition() + values[0], occupiedTile.getYPosition() + values[1]);
            if (t != null) { t.toggleUnderAttackOn(isWhite); }
            t = Database.getTile(occupiedTile.getXPosition() + values[1], occupiedTile.getYPosition() + values[0]);
            if (t != null) { t.toggleUnderAttackOn(isWhite); }

            // Perform the transformation
            if (i % 2 == 0) {
                values[0] = -values[0];  // Toggle the first value
            } else {
                values[1] = -values[1];  // Toggle the second value
            }
        }
    }

    private void addMove(List<Move> moves, Tile endTile){
        if (endTile == null) { return; }
        if (endTile.getOccupyingPiece() != null) {
            if (endTile.getOccupyingPiece().isWhite() == isWhite()) {
                return;
            }
        }
        Move move = new NormalMove(occupiedTile, endTile);
        moves.add(move);
    }
}
