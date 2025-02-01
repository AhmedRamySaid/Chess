package kyra.me.chess.scripts.move;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.pieces.King;
import kyra.me.chess.scripts.pieces.Piece;
import kyra.me.chess.scripts.pieces.Queen;
import kyra.me.chess.scripts.pieces.Rook;
import kyra.me.chess.scripts.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public abstract class Move {
    final Piece movingPiece;
    final Piece capturedPiece;
    final Tile startingSquare;
    final Tile endingSquare;
    final MoveType moveType;
    final boolean isCapture;
    boolean isCheck;

    boolean movingPieceHasMoved;
    Move lastMove;

    public Move(Tile start, Tile end){
        this(start, end, MoveType.normal);
    }
    public Move (Tile start, Tile end, MoveType moveType){
        if (start.getOccupyingPiece() == null){
            throw new IllegalArgumentException("starting piece is null");
        }

        this.movingPiece = start.getOccupyingPiece();
        this.startingSquare = start;
        this.endingSquare = end;
        this.moveType = moveType;

        if (moveType == MoveType.enPassant){
            capturedPiece = GameManager.lastMove.getMovingPiece();
            isCapture = true;
        } else {
            capturedPiece = end.getOccupyingPiece();
            isCapture = capturedPiece != null;
        }
    }

    public void doMove(){
        if (isCapture) { capturedPiece.destroy(); }

        movingPiece.setHasMoved(true);
        movingPiece.getStackPane().getChildren().remove(movingPiece);

        startingSquare.setOccupyingPiece(null);
        movingPiece.setOccupiedTile(endingSquare);
        endingSquare.setOccupyingPiece(movingPiece);
        Database.setSelectedPiece(null);

        endingSquare.getStackPane().getChildren().add(movingPiece);

        for (Move m: movingPiece.getMoves()){
            m.getEndingSquare().togglePlayableMoveOff();
        }
        GameManager.lastMove = this;
    }

    public void doMoveTemporary(){
        if (isCapture){
            Database.removePiece(capturedPiece);
        }

        this.movingPieceHasMoved = movingPiece.getHasMoved();
        this.lastMove = GameManager.lastMove;

        movingPiece.setHasMoved(true);
        startingSquare.setOccupyingPiece(null);
        movingPiece.setOccupiedTile(endingSquare);
        endingSquare.setOccupyingPiece(movingPiece);

        GameManager.isWhiteTurn = !GameManager.isWhiteTurn;
    }
    public void undoMove(){
        if (isCapture){
            Database.addPiece(capturedPiece);
        }

        movingPiece.setHasMoved(this.movingPieceHasMoved);
        GameManager.lastMove = this.lastMove;

        startingSquare.setOccupyingPiece(movingPiece);
        endingSquare.setOccupyingPiece(capturedPiece);
        movingPiece.setOccupiedTile(startingSquare);

        GameManager.isWhiteTurn = !GameManager.isWhiteTurn;
    }
    public void initializeIsCheck(){
        List<Move> m = new ArrayList<>();
        movingPiece.createMoves(m);
        isCheck = m.stream().anyMatch(t -> t.capturedPiece instanceof King);
    }

    public boolean isCheck() { return isCheck; }
    public boolean isCapture() { return isCapture; }
    public Tile getStartingSquare() { return startingSquare; }
    public Tile getEndingSquare() { return endingSquare; }
    public Piece getMovingPiece() { return movingPiece; }
    public Piece getCapturedPiece() { return capturedPiece; }
    public MoveType getType() { return moveType; }
}
