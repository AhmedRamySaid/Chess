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

public class Move {
    private final Piece movingPiece;
    private final Piece capturedPiece;
    private final Tile startingSquare;
    private final Tile endingSquare;
    private final MoveType moveType;
    private final boolean isCapture;
    private boolean isCheck;

    private final Rook castlingRook;
    private final Tile castlingRookEndTile;

    public Move(Tile start, Tile end){
        this(start, end, MoveType.normal);
    }
    public Move (Tile start, Tile end, MoveType moveType){
        this(start, end, moveType, null, null);
    }
    public Move(Tile start, Tile end, MoveType moveType, Rook rook, Tile rookEndTile){
        if (start.getOccupyingPiece() == null){
            throw new IllegalArgumentException("starting piece is null");
        }

        this.castlingRook = rook;
        this.castlingRookEndTile = rookEndTile;
        this.movingPiece = start.getOccupyingPiece();
        this.startingSquare = start;
        this.endingSquare = end;
        this.moveType = moveType;

        switch(this.moveType){
            case normal, promotion:
                capturedPiece = end.getOccupyingPiece();
                isCapture = capturedPiece != null;
                break;
            case enPassant:
                capturedPiece = GameManager.lastMove.getMovingPiece();
                isCapture = true;
                break;
            case castling:
                capturedPiece = null;
                isCapture = false;
                break;
            default:
                capturedPiece = null;
                isCapture = false;
                break;
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
        GameManager.isWhiteTurn = !GameManager.isWhiteTurn;
        GameManager.lastMove = this;

        switch(moveType) {
            case promotion:
                movingPiece.destroy();
                new Queen(endingSquare, !GameManager.isWhiteTurn);
                break;
            case castling:
                castlingRook.setHasMoved(true);
                castlingRook.getStackPane().getChildren().remove(castlingRook);

                castlingRook.getOccupiedTile().setOccupyingPiece(null);
                castlingRook.setOccupiedTile(castlingRookEndTile);
                castlingRookEndTile.setOccupyingPiece(castlingRook);

                castlingRookEndTile.getStackPane().getChildren().add(castlingRook);
                break;
            default:
                break;
        }

        GameManager.turnStart();
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

    private boolean movingPieceHasMoved;
    private Move lastMove;
}
