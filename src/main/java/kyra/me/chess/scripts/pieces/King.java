package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.move.CastlingMove;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.move.NormalMove;
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
        Tile tile;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                tile = Database.getTile(occupiedTile.getXPosition() + i, occupiedTile.getYPosition() + j);
                if (tile == null) { continue; }
                if (tile.isUnderAttack()) { continue; }
                if (tile.getOccupyingPiece() != null) {
                    if (tile.getOccupyingPiece().isWhite() != isWhite()) {
                        Move move = new NormalMove(occupiedTile, tile);
                        moves.add(move);
                    }
                } else {
                    Move move = new NormalMove(occupiedTile, tile);
                    moves.add(move);
                }
            }
        }

        if (occupiedTile.isUnderAttack()) { return; }
        if (!hasMoved) {
            int x = getOccupiedTile().getXPosition() - 1;

            //long castling
            while (x >= 1){
                tile = Database.getTile(x, getOccupiedTile().getYPosition());
                //if it reaches the last square and the piece there is a rook who is the same color as the king and has not moved:
                // make a castling move
                if (x == 1 && tile.getOccupyingPiece() instanceof Rook && tile.getOccupyingPiece().isWhite() == isWhite() && !tile.getOccupyingPiece().hasMoved) {
                    Move move = new CastlingMove( occupiedTile, Database.getTile(3, getOccupiedTile().getYPosition()),
                            (Rook)tile.getOccupyingPiece(), Database.getTile(4, getOccupiedTile().getYPosition()) );
                    moves.add(move);
                }
                //if there is a non-empty square in the middle, don't create the move
                if (tile.getOccupyingPiece() != null){ break; }
                if (tile.isUnderAttack()) { break; }

                x--;
            }

            //short castling
            x = getOccupiedTile().getXPosition() + 1;
            while (x <= 8){
                tile = Database.getTile(x, getOccupiedTile().getYPosition());
                if (x == 8 && tile.getOccupyingPiece() instanceof Rook && tile.getOccupyingPiece().isWhite() == isWhite() && !tile.getOccupyingPiece().hasMoved) {
                    Move move = new CastlingMove( occupiedTile, Database.getTile(7, getOccupiedTile().getYPosition()),
                            (Rook)tile.getOccupyingPiece(), Database.getTile(6, getOccupiedTile().getYPosition()) );
                    moves.add(move);
                }
                if (tile.getOccupyingPiece() != null){ break; }
                if (tile.isUnderAttack()) { break; }

                x++;
            }
        }
    }

    @Override
    public void createAttacks(){
        Tile tile;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                tile = Database.getTile(occupiedTile.getXPosition() + i, occupiedTile.getYPosition() + j);
                if (tile == null) { continue; }
                tile.toggleUnderAttackOn(isWhite);
            }
        }
    }
}
