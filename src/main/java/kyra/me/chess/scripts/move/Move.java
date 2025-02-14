package kyra.me.chess.scripts.move;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.pieces.Pawn;
import kyra.me.chess.scripts.pieces.Piece;
import kyra.me.chess.scripts.tile.Tile;

public abstract class Move {
    final Piece movingPiece;
    final Piece capturedPiece;
    final Tile startingSquare;
    final Tile endingSquare;
    final MoveType moveType;
    final boolean isCapture;

    //stores the variables in GameManager so it can do and undo moves
    Move lastMove;
    int turnCount;
    boolean movingPieceHasMoved;
    boolean kingInCheck;
    boolean kingInDoubleCheck;

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

        //saves values so it can reset them later
        this.movingPieceHasMoved = movingPiece.getHasMoved();
        this.kingInCheck = GameManager.isCheck;
        this.kingInDoubleCheck = GameManager.isDoubleCheck;
        this.lastMove = GameManager.lastMove;

        movingPiece.setHasMoved(true);
        startingSquare.setOccupyingPiece(null);
        movingPiece.setOccupiedTile(endingSquare);
        endingSquare.setOccupyingPiece(movingPiece);

        GameManager.isWhiteTurn = !GameManager.isWhiteTurn;
        GameManager.isCheck = false;
        GameManager.isDoubleCheck = false;
        GameManager.lastMove = this;

        this.turnCount = GameManager.drawTurnCount;
        if (this.isCapture() || this.getMovingPiece() instanceof Pawn){
            GameManager.drawTurnCount = 0;
        }
        else { GameManager.drawTurnCount++; }
    }
    public void undoMove(){
        if (isCapture){
            Database.addPiece(capturedPiece);
        }

        movingPiece.setHasMoved(this.movingPieceHasMoved);
        GameManager.isCheck = this.kingInCheck;
        GameManager.isDoubleCheck = this.kingInDoubleCheck;
        GameManager.lastMove = this.lastMove;

        startingSquare.setOccupyingPiece(movingPiece);
        endingSquare.setOccupyingPiece(capturedPiece);
        movingPiece.setOccupiedTile(startingSquare);

        GameManager.isWhiteTurn = !GameManager.isWhiteTurn;
        GameManager.drawTurnCount = this.turnCount;
    }

    @Override
    public String toString(){
        return "moving piece: " + movingPiece + " starting square: " + startingSquare.getXPosition() + " " + startingSquare.getYPosition() + " ending square: " + endingSquare.getXPosition() + " " + endingSquare.getYPosition();
    }
    @Override
    public boolean equals(Object obj){
        if (obj.getClass() != this.getClass()){
            return false;
        }
        Move m = (Move)obj;
        if (!this.movingPiece.equals(m.movingPiece)) {
            return false;
        }
        if (!this.startingSquare.equals(m.startingSquare)) {
            return false;
        }
        if (!this.endingSquare.equals(m.endingSquare)) {
            return false;
        }
        return true;
    }

    public boolean isCapture() { return isCapture; }
    public Tile getStartingSquare() { return startingSquare; }
    public Tile getEndingSquare() { return endingSquare; }
    public Piece getMovingPiece() { return movingPiece; }
    public Piece getCapturedPiece() { return capturedPiece; }
    public MoveType getType() { return moveType; }
}
