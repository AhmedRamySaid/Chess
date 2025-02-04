package kyra.me.chess.scripts.pieces;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.move.MoveType;
import kyra.me.chess.scripts.move.NormalMove;
import kyra.me.chess.scripts.move.PromotingMove;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Pawn extends Piece {

    public Pawn(Tile tile, boolean isWhite) {
        super(tile, isWhite);
        if (isWhite) {
            this.setImage(models[0]);
        } else {
            this.setImage(models[1]);
        }
        if (isWhite && occupiedTile.getYPosition() == 2) hasMoved = false;
        else if (!isWhite && occupiedTile.getYPosition() == 7) hasMoved = false;
        else { hasMoved = true; }
    }
    @Override
    public void createMoves(List<Move> moves){
        if (GameManager.isDoubleCheck) return;

        int color = isWhite? 1: -1; //if white, the pawn goes up. if black, the pawn goes down

        Tile t = Database.getTile(occupiedTile.getXPosition(), occupiedTile.getYPosition() + color);
        createForwardMove(moves, t);

        //create attacks
        t = Database.getTile(occupiedTile.getXPosition()+1, occupiedTile.getYPosition() + color);
        if (t != null) {
            if (t.getOccupyingPiece() != null) {
                if (t.getOccupyingPiece().isWhite() != isWhite()) {
                    if (isWhite() && (pinDirection == 0 || pinDirection == 3)) createPawnMove(moves, t);
                    else if (!isWhite() && (pinDirection == 0 || pinDirection == 4)) createPawnMove(moves, t);
                }
            }
        }

        t = Database.getTile(occupiedTile.getXPosition()-1, occupiedTile.getYPosition() + color);
        if (t != null) {
            if (t.getOccupyingPiece() != null) {
                if (t.getOccupyingPiece().isWhite() != isWhite()) {
                    if (isWhite() && (pinDirection == 0 || pinDirection == 4)) createPawnMove(moves, t);
                    else if (!isWhite() && (pinDirection == 0 || pinDirection == 3)) createPawnMove(moves, t);
                }
            }
        }

        //check if last move is a double pawn move
        if (GameManager.lastMove == null) { return; }
        if (GameManager.lastMove.getType() != MoveType.doublePawn) { return; }
        //en passant is possible if they are on the same y and the x difference is 1
        if (GameManager.lastMove.getEndingSquare().getYPosition() == this.occupiedTile.getYPosition() &&
                Math.abs(GameManager.lastMove.getEndingSquare().getXPosition() - this.occupiedTile.getXPosition()) == 1) {

            t = Database.getTile(GameManager.lastMove.getEndingSquare().getXPosition(), occupiedTile.getYPosition() + color);
            if (GameManager.isCheck && !t.isUnderCheck()) { return; }

            enPassantMoveMaker(t, moves);
        }
    }

    public void createAttacks(){
        int color = isWhite? 1: -1; //if white, the pawn goes up. if black, the pawn goes down
        Tile t;

        t = Database.getTile(occupiedTile.getXPosition()+1, occupiedTile.getYPosition() + color);
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

        t = Database.getTile(occupiedTile.getXPosition()-1, occupiedTile.getYPosition() + color);
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

    private void createForwardMove(List<Move> moves, Tile t){
        int color = isWhite? 1: -1;
        if (t == null) return;
        if (pinDirection == 1 || pinDirection == 3 || pinDirection == 4) return;
        if (t.getOccupyingPiece() != null) return;
        createPawnMove(moves, t);

        if (hasMoved) return;
        t = Database.getTile(occupiedTile.getXPosition(), occupiedTile.getYPosition() + 2 * color);
        if (t == null) return;

        if (t.getOccupyingPiece() == null && !(GameManager.isCheck && !t.isUnderCheck())) {
            if (!occupiedTile.isUnderPin() || t.isUnderPin()) {
                Move m = new NormalMove(occupiedTile, t, MoveType.doublePawn);
                moves.add(m);
            }
        }
    }

    private void createPawnMove(List<Move> moves, Tile t) {
        if (GameManager.isCheck && !t.isUnderCheck()) { return; }
        if (occupiedTile.isUnderPin() && !t.isUnderPin()) { return; }
        if (t.getYPosition() == 8 || t.getYPosition() == 1) {
            new PromotingMove(occupiedTile, t, moves); //so it can add all promotions to the list
        }  else {
            Move move = new NormalMove(occupiedTile, t);
            moves.add(move);
        }
    }

    private void enPassantMoveMaker(Tile t, List<Move> moves) {
        if (occupiedTile.isUnderPin() && !t.isUnderPin()) return;
        //if this pawn is on the right go right from this pawn. else go left from this pawn as to not hit the other pawn
        int direction = occupiedTile.getXPosition() - t.getXPosition();
        int x = occupiedTile.getXPosition() + direction;
        Tile checkedTile;
        boolean foundKingFlag = false;
        boolean foundPinningPieceFlag = false;

        while (x>=1 && x<=8){
            checkedTile = Database.getTile(x, occupiedTile.getYPosition());
            x += direction;

            if (checkedTile.getOccupyingPiece() instanceof King){
                foundKingFlag = checkedTile.getOccupyingPiece().isWhite() == isWhite(); //found friendly king
                break;
            }
            if (checkedTile.getOccupyingPiece() instanceof Rook || checkedTile.getOccupyingPiece() instanceof Queen){
                foundPinningPieceFlag = checkedTile.getOccupyingPiece().isWhite() != isWhite(); //found enemy piece
                break;
            }
            if (checkedTile.getOccupyingPiece() != null) { break; }
        }

        x = t.getXPosition() - direction;
        while (x>=1 && x<=8){
            checkedTile = Database.getTile(x, occupiedTile.getYPosition());
            x -= direction;

            if (checkedTile.getOccupyingPiece() instanceof King){
                foundKingFlag = checkedTile.getOccupyingPiece().isWhite() == isWhite(); //found friendly king
                break;
            }
            if (checkedTile.getOccupyingPiece() instanceof Rook || checkedTile.getOccupyingPiece() instanceof Queen){
                foundPinningPieceFlag = checkedTile.getOccupyingPiece().isWhite() != isWhite(); //found enemy piece
                break;
            }
            if (checkedTile.getOccupyingPiece() != null) { break; }
        }

        if (foundPinningPieceFlag && foundKingFlag) { return; }
        Move move = new NormalMove(occupiedTile, t, MoveType.enPassant);
        moves.add(move);
    }
}
