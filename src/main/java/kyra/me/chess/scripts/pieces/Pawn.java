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
        if (isWhite && occupiedTile.getYPosition() == 2) { hasMoved = false; }
        else if (!isWhite && occupiedTile.getYPosition() == 7) { hasMoved = false; }
        else { hasMoved = true; }
    }
    @Override
    public void createMoves(List<Move> moves){
        int color = isWhite? 1: -1; //if white, the pawn goes up. if black, the pawn goes down

        Tile t = Database.getTile(occupiedTile.getXPosition(), occupiedTile.getYPosition() + color);
        if (t != null) {
            if (t.getOccupyingPiece() == null) {
                //checks if the move is normal or a promotion
                createPawnMove(moves, t);

                if (!hasMoved){
                    t = Database.getTile(occupiedTile.getXPosition(), occupiedTile.getYPosition() + 2 * color);
                    if (t != null) {
                        if (t.getOccupyingPiece() == null) {
                            Move m = new NormalMove(occupiedTile, t, MoveType.doublePawn);
                            moves.add(m);
                        }
                    }
                }
            }
        }

        t = Database.getTile(occupiedTile.getXPosition()+1, occupiedTile.getYPosition() + color);
        if (t != null) {
            if (t.getOccupyingPiece() != null) {
                if (t.getOccupyingPiece().isWhite() != isWhite()) {
                    createPawnMove(moves, t);
                }
            }
        }

        t = Database.getTile(occupiedTile.getXPosition()-1, occupiedTile.getYPosition() + color);
        if (t != null) {
            if (t.getOccupyingPiece() != null) {
                if (t.getOccupyingPiece().isWhite() != isWhite()) {
                    createPawnMove(moves, t);
                }
            }
        }

        //check if last move is a double pawn move
        if (GameManager.lastMove == null){ return; }
        if (GameManager.lastMove.getType() != MoveType.doublePawn){ return; }
        //en passant is possible if they are on the same y and the x difference is 1
        if (GameManager.lastMove.getEndingSquare().getYPosition() == this.occupiedTile.getYPosition() &&
                Math.abs(GameManager.lastMove.getEndingSquare().getXPosition() - this.occupiedTile.getXPosition()) == 1) {
            t = Database.getTile(GameManager.lastMove.getEndingSquare().getXPosition(), occupiedTile.getYPosition() + color);
            Move move = new NormalMove(occupiedTile, t, MoveType.enPassant);
            moves.add(move);
        }
    }

    private void createPawnMove(List<Move> moves, Tile t) {
        if (t.getYPosition() == 8 || t.getYPosition() == 1) {
            new PromotingMove(occupiedTile, t, moves); //so it can add all promotions to the list
        }  else {
            Move move = new NormalMove(occupiedTile, t);
            moves.add(move);
        }
    }
}
