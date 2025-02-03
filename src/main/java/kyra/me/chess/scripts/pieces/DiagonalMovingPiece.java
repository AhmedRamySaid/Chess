package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.managers.GameManager;
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

    default void createDiagonalAttack(int xDirection, int yDirection){
        boolean hitPieceFlag = false;
        boolean isCheck = false;
        boolean isPin = false;
        boolean isPinTemp = false;
        boolean isCheckTemp = true;
        Piece piece = (Piece)this;
        Piece enemyKing = Database.getPieces().stream().filter(t -> t instanceof King && t.isWhite != piece.isWhite).findFirst().orElse(null);
        if (enemyKing == null) { GameManager.endGame(); }

        int x = piece.getOccupiedTile().getXPosition() + xDirection;
        int y = piece.getOccupiedTile().getYPosition() + yDirection;
        if (piece instanceof Queen && xDirection == 1 && yDirection == -1){
            System.out.println();
        }
        while (x >= 1 && x <=8 && y >= 1 && y <=8){
            Tile t = Database.getTile(x, y);

            if (t.getOccupyingPiece() != null){
                if (t.getOccupyingPiece() instanceof King){
                    //if isCheck is already true leave it true
                    //else if the first piece you hit is a king of the opposite color, isCheck is true
                    isCheck =  isCheck || ( isCheckTemp && (!hitPieceFlag && (t.getOccupyingPiece().isWhite() != piece.isWhite)) );

                    //found a successful pin
                    isPin = isPinTemp;
                } else{
                    //if the first piece you hit is a non-king of the opposite color, a pin might be possible
                    isPinTemp = !hitPieceFlag && (t.getOccupyingPiece().isWhite() != piece.isWhite);

                    isCheckTemp = false;
                }

                if (!hitPieceFlag) t.toggleUnderAttackOn(piece.isWhite);
                hitPieceFlag = true;
            }

            if (!hitPieceFlag) t.toggleUnderAttackOn(piece.isWhite);
            x += xDirection;
            y += yDirection;
        }

        x = piece.getOccupiedTile().getXPosition() + xDirection;
        y = piece.getOccupiedTile().getYPosition() + yDirection;

        //loop again to apply the pin or check
        while (x >= 1 && x <=8 && y >= 1 && y <=8){
            Tile t = Database.getTile(x, y);

            if (t.getOccupyingPiece() != null){
                if (t.getOccupyingPiece() instanceof King){
                    if (isCheck) t.toggleUnderCheckOn();
                    if (isPin) t.toggleUnderPinOn();

                    isCheck = false;
                    isPin = false;
                }
            }

            if (isCheck) t.toggleUnderCheckOn();
            if (isPin) t.toggleUnderPinOn();
            x += xDirection;
            y += yDirection;
        }
    }
}
