package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.move.MoveType;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class King extends Piece {
    public King(Tile tile, boolean isWhite) {
        super(tile, isWhite);
        if (isWhite()){
            this.setImage(models[10]);
            if (occupiedTile.getXPosition() == 5 && occupiedTile.getYPosition() == 1){
                hasMoved = false;
            } else { hasMoved = true; }
        } else {
            this.setImage(models[11]);
            if (occupiedTile.getXPosition() == 5 && occupiedTile.getYPosition() == 8){
                hasMoved = false;
            } else { hasMoved = true; }
        }
    }

    @Override
    public void createMoves(List<Move> moves) {
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
                        Move move = new Move(occupiedTile, t);
                        moves.add(move);
                    }
                } else {
                    Move move = new Move(occupiedTile, t);
                    moves.add(move);
                }
            }
        }

        //WARNING: THIS PART IS NOT COMPLETE. MUST CHECK FOR A LONG-PIECE ATTACK
        if (!hasMoved) {
            int x = getOccupiedTile().getXPosition() - 1;

            //long castling
            while (x >= 1){
                t = Database.getTile(x, getOccupiedTile().getYPosition());
                if (x == 1 && t.getOccupyingPiece() instanceof Rook && t.getOccupyingPiece().isWhite() == isWhite() && !t.getOccupyingPiece().hasMoved) {
                    Move move = new Move(occupiedTile, Database.getTile(3, getOccupiedTile().getYPosition()), MoveType.castling,
                            (Rook)t.getOccupyingPiece(), Database.getTile(4, getOccupiedTile().getYPosition()));
                    moves.add(move);
                }
                if (t.getOccupyingPiece() != null){
                    break;
                }
                x--;
            }

            //short castling
            x = getOccupiedTile().getXPosition() + 1;
            while (x <= 8){
                t = Database.getTile(x, getOccupiedTile().getYPosition());
                if (x == 8 && t.getOccupyingPiece() instanceof Rook && t.getOccupyingPiece().isWhite() == isWhite() && !t.getOccupyingPiece().hasMoved) {
                    Move move = new Move(occupiedTile, Database.getTile(7, getOccupiedTile().getYPosition()), MoveType.castling,
                            (Rook)t.getOccupyingPiece(), Database.getTile(6, getOccupiedTile().getYPosition()));
                    moves.add(move);
                }
                if (t.getOccupyingPiece() != null){
                    break;
                }
                x++;
            }
        }
    }
}
