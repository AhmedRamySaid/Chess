package kyra.me.chess.scripts.move;

import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.tile.Tile;

public class NormalMove extends Move {
    private Tile enpassantedPieceTile;
    public NormalMove(Tile start, Tile end){
        super(start, end, MoveType.normal);
    }
    public NormalMove(Tile start, Tile end, MoveType moveType){
        super(start, end, moveType);
        if (moveType == MoveType.enPassant){
            enpassantedPieceTile = GameManager.lastMove.getEndingSquare();
        }
    }

    @Override
    public void doMove() {
        super.doMove();
        GameManager.turnStart();
    }

    @Override
    public void doMoveTemporary(){
        super.doMoveTemporary();
        if (moveType == MoveType.enPassant){
            enpassantedPieceTile.setOccupyingPiece(null);
        }
    }

    @Override
    public void undoMove() {
        super.undoMove();
        if (moveType == MoveType.enPassant){
            endingSquare.setOccupyingPiece(null);
            capturedPiece.setOccupiedTile(enpassantedPieceTile);
            enpassantedPieceTile.setOccupyingPiece(capturedPiece);
        }
    }
}
