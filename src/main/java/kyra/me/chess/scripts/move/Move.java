package kyra.me.chess.scripts.move;

import kyra.me.chess.scripts.Database;
import kyra.me.chess.scripts.GameManager;
import kyra.me.chess.scripts.pieces.Piece;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;

public class Move {
    private Piece movingPiece;
    private Piece capturedPiece;
    private Tile startingSquare;
    private Tile endingSquare;
    private MoveType moveType;
    private boolean movingPieceHasMoved;

    public Move(Tile start, Tile end){
        if (start.getOccupyingPiece() == null){
            throw new IllegalArgumentException("starting piece is null");
        }
        this.movingPiece = start.getOccupyingPiece();
        this.startingSquare = start;
        this.endingSquare = end;
        this.movingPieceHasMoved = movingPiece.getHasMoved();

        if (end.getOccupyingPiece() == null){
            capturedPiece = null;
            moveType = MoveType.normal;
        }
        else {
            capturedPiece = end.getOccupyingPiece();
            moveType = MoveType.capture;
        }
    }
    public void doMove(){
        switch (moveType){
            case normal:
                GameManager.audio[0].stop(); //resets the audio clip so it can be replayed
                GameManager.audio[0].setFramePosition(0);
                GameManager.audio[0].start();
                break;
            case capture:
                capturedPiece.destroy();
                GameManager.audio[1].stop();
                GameManager.audio[1].setFramePosition(0);
                GameManager.audio[1].start();
                break;
        }
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
        GameManager.turnStart();
    }

    public void doMoveTemporary(){
        switch (moveType){
            case normal:
                break;
            case capture:
                Database.removePiece(capturedPiece);
                break;
        }

        movingPiece.setHasMoved(true);
        startingSquare.setOccupyingPiece(null);
        movingPiece.setOccupiedTile(endingSquare);
        endingSquare.setOccupyingPiece(movingPiece);

        GameManager.isWhiteTurn = !GameManager.isWhiteTurn;
    }
    public void undoMove(){
        switch (moveType){
            case normal:
                break;
            case capture:
                Database.addPiece(capturedPiece);
                break;
        }

        movingPiece.setHasMoved(movingPieceHasMoved);
        startingSquare.setOccupyingPiece(movingPiece);
        endingSquare.setOccupyingPiece(capturedPiece);
        movingPiece.setOccupiedTile(startingSquare);

        GameManager.isWhiteTurn = !GameManager.isWhiteTurn;
    }

    public Tile getStartingSquare() { return startingSquare; }
    public Tile getEndingSquare() { return endingSquare; }
    public Piece getMovingPiece() { return movingPiece; }
    public Piece getCapturedPiece() { return capturedPiece; }
}
