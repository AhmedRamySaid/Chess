package kyra.me.chess.scripts.move;

import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.pieces.Piece;
import kyra.me.chess.scripts.pieces.Rook;
import kyra.me.chess.scripts.tile.Tile;

public class CastlingMove extends Move{
    private final Rook castlingRook;
    private final Tile castlingRookStartTile;
    private final Tile castlingRookEndTile;

    public CastlingMove(Tile start, Tile end, Rook rook, Tile rookEndTile){
        super(start, end, MoveType.castling);
        this.castlingRook = rook;
        this.castlingRookStartTile = rook.getOccupiedTile();
        this.castlingRookEndTile = rookEndTile;
    }

    @Override
    public void doMove() {
        super.doMove();

        castlingRook.setHasMoved(true);
        castlingRook.getStackPane().getChildren().remove(castlingRook);

        castlingRook.getOccupiedTile().setOccupyingPiece(null);
        castlingRook.setOccupiedTile(castlingRookEndTile);
        castlingRookEndTile.setOccupyingPiece(castlingRook);

        castlingRookEndTile.getStackPane().getChildren().add(castlingRook);
        GameManager.turnStart();
    }

    @Override
    public void doMoveTemporary(){
        super.doMoveTemporary();

        castlingRook.setHasMoved(true);
        castlingRookStartTile.setOccupyingPiece(null);
        castlingRook.setOccupiedTile(castlingRookEndTile);
        castlingRookEndTile.setOccupyingPiece(castlingRook);
    }

    @Override
    public void undoMove() {
        super.undoMove();

        castlingRook.setHasMoved(false);
        castlingRook.setOccupiedTile(castlingRookStartTile);
        castlingRookStartTile.setOccupyingPiece(castlingRook);
        castlingRookEndTile.setOccupyingPiece(null);
    }
}
