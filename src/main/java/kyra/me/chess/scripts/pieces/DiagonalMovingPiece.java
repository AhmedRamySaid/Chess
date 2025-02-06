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
        boolean isPinned = piece.getOccupiedTile().isUnderPin();
        int x = piece.getOccupiedTile().getXPosition() + xDirection;
        int y = piece.getOccupiedTile().getYPosition() + yDirection;
        int pinDirection;

        if (yDirection == 0) pinDirection = 1;
        else if (xDirection == 0) pinDirection = 2;
        else if (xDirection == yDirection) pinDirection = 3;
        else pinDirection = 4;
        if (piece.getPinDirection() != 0 && piece.getPinDirection() != pinDirection) { return; }

        while (x >= 1 && x <=8 && y >= 1 && y <=8){
            Tile t = Database.getTile(x, y);
            if (t.getOccupyingPiece() != null){
                if (t.getOccupyingPiece().isWhite() == piece.isWhite()) { return; }
                if (GameManager.isCheck && !t.isUnderCheck()) { return; }

                if (!isPinned || t.isUnderPin()){
                    Move move = new NormalMove(piece.occupiedTile, t);
                    moves.add(move);
                }
                return;
            }

            x += xDirection;
            y += yDirection;

            if (isPinned && !t.isUnderPin()) { return; }
            if (GameManager.isCheck && !t.isUnderCheck()) { continue; }

            Move move = new NormalMove(piece.occupiedTile, t);
            moves.add(move);
        }
    }

    default void createDiagonalAttack(int xDirection, int yDirection){
        boolean hitPieceFlag = false;
        boolean isCheck = false;
        boolean isPin = false;
        boolean isPinTemp = false;
        boolean isCheckTemp = true;
        Piece piece = (Piece)this;
        Tile tile;

        int pinDirection;
        if (yDirection == 0) pinDirection = 1;
        else if (xDirection == 0) pinDirection = 2;
        else if (xDirection == yDirection) pinDirection = 3;
        else pinDirection = 4;

        Piece enemyKing = Database.getPieces().stream().filter(t -> t instanceof King && t.isWhite != piece.isWhite).findFirst().orElse(null);
        if (enemyKing == null) { throw  new IllegalThreadStateException("King does not exist how did you get here"); }

        int x = piece.getOccupiedTile().getXPosition() + xDirection;
        int y = piece.getOccupiedTile().getYPosition() + yDirection;

        while (x >= 1 && x <=8 && y >= 1 && y <=8){
            tile = Database.getTile(x, y);

            if (tile.getOccupyingPiece() != null){
                if (tile.getOccupyingPiece() instanceof King){
                    //if isCheck is already true leave it true
                    //else if the first piece you hit is a king of the opposite color, isCheck is true
                    isCheck =  isCheck || ( isCheckTemp && (!hitPieceFlag && (tile.getOccupyingPiece().isWhite() != piece.isWhite)) );

                    //found a successful pin
                    isPin = isPin || (isPinTemp && tile.getOccupyingPiece().isWhite != piece.isWhite);
                } else{
                    //if the first piece you hit is a non-king of the opposite color, a pin might be possible
                    isPinTemp = !hitPieceFlag && (tile.getOccupyingPiece().isWhite() != piece.isWhite);

                    isCheckTemp = false;
                }

                if (!hitPieceFlag) tile.toggleUnderAttackOn();
                hitPieceFlag = true;
            }

            if (!hitPieceFlag) tile.toggleUnderAttackOn();
            x += xDirection;
            y += yDirection;
        }

        if (isCheck) {
            piece.getOccupiedTile().toggleUnderCheckOn();
            GameManager.isDoubleCheck = GameManager.isCheck;
            GameManager.isCheck = true;
        }
        if (isPin) piece.getOccupiedTile().toggleUnderPinOn();
        x = piece.getOccupiedTile().getXPosition() + xDirection;
        y = piece.getOccupiedTile().getYPosition() + yDirection;

        isCheckTemp = isCheck;
        //loop again to apply the pin or check
        while (x >= 1 && x <=8 && y >= 1 && y <=8){
            tile = Database.getTile(x, y);

            if (tile.getOccupyingPiece() != null){
                if (tile.getOccupyingPiece() instanceof King){
                    if (isCheck) tile.toggleUnderCheckOn();
                    if (isPin) tile.toggleUnderPinOn();

                    isCheck = false;
                    isPin = false;
                } else {
                    if (isPin) tile.getOccupyingPiece().setPinDirection(pinDirection);
                }
            }

            if (isCheck) tile.toggleUnderCheckOn();
            if (isPin) tile.toggleUnderPinOn();
            if (isCheckTemp) tile.toggleUnderAttackOn();
            x += xDirection;
            y += yDirection;
        }
    }
}
