package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.move.NormalMove;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public interface DiagonalMovingPiece {
    default void createDiagonalMove(List<Move> moves, int xDirection, int yDirection){
        Piece piece = (Piece)this;
        int x = piece.getOccupiedTile().getXPosition() + xDirection;
        int y = piece.getOccupiedTile().getYPosition() + yDirection;

        while (x >= 1 && x <=8 && y >= 1 && y <=8){
            Tile t = Database.getTile(x, y);
            if (t.getOccupyingPiece() != null){
                if (t.getOccupyingPiece().isWhite() != piece.isWhite()) {
                    Move move = new NormalMove(piece.occupiedTile, t);
                    moves.add(move);
                }
                return;
            }

            Move move = new NormalMove(piece.occupiedTile, t);
            moves.add(move);
            x += xDirection;
            y += yDirection;
        }
    }

    default void createDiagonalAttack(int xDirection, int yDirection ){
        Piece piece = (Piece)this;
        int x = piece.getOccupiedTile().getXPosition() + xDirection;
        int y = piece.getOccupiedTile().getYPosition() + yDirection;

        while (x >= 1 && x <=8 && y >= 1 && y <=8){
            Tile t = Database.getTile(x, y);
            if (t.getOccupyingPiece() != null){
                t.toggleUnderAttackOn(piece.isWhite);
                return;
            }

            t.toggleUnderAttackOn(piece.isWhite);
            x += xDirection;
            y += yDirection;
        }
    }
}
