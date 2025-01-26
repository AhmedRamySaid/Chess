package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.Database;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class King extends Piece {
    public King(Tile tile, boolean isWhite) {
        super(tile, isWhite);
        if (isWhite()){
            this.setImage(models[10]);
        } else {
            this.setImage(models[11]);
        }
    }

    @Override
    public void createMoves(List<Move> moves, boolean isMoveCreation) {
        Tile t;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                t = Database.getTile(occupiedTile.getXPosition() + i, occupiedTile.getYPosition() + j);
                if (t == null) { continue; }
                if (t.getOccupyingPiece() != null) {
                    if (t.getOccupyingPiece().isWhite() != isWhite()) {
                        this.addMove(moves, t);
                    }
                }
                this.addMove(moves, t);
            }
        }
    }
}
