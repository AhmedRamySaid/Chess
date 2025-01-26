package kyra.me.chess.scripts.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import kyra.me.chess.scripts.Database;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Piece extends ImageView {
    Tile occupiedTile;
    boolean isWhite;
    boolean hasMoved;
    //white pawn  (0), black pawn (1), white knight (2), black knight (3), white bishop (4), black bishop (5),
    // white rook (6), black rook (7), white queen (8), black queen (9), white king (10), black king (11)
    static Image[] models = {new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/white pawn.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/black pawn.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/white knight.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/black knight.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/white bishop.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/black bishop.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/white rook.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/black rook.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/white queen.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/black queen.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/white king.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/black king.png"))};

    public Piece(Tile tile, boolean isWhite) {
        this.isWhite = isWhite;
        this.setPreserveRatio(true);
        this.setFitHeight(40);
        occupiedTile = tile;
        tile.getStackPane().getChildren().add(this);

        setOnMouseClicked(event -> {
            occupiedTile.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED,0, 0, 0, 0, MouseButton.PRIMARY, 1,
                    true, true, true, true, true, true, true, true, true, true, null));
        });
        setOnMouseEntered(event -> {
            occupiedTile.fireEvent(new MouseEvent(MouseEvent.MOUSE_ENTERED,0, 0, 0, 0, MouseButton.PRIMARY, 1,
                    true, true, true, true, true, true, true, true, true, true, null));
        });
        setOnMouseExited(event -> {
            occupiedTile.fireEvent(new MouseEvent(MouseEvent.MOUSE_EXITED,0, 0, 0, 0, MouseButton.PRIMARY, 1,
                    true, true, true, true, true, true, true, true, true, true, null));
        });
        tile.setOccupyingPiece(this);
        Database.addPiece(this);

        this.fitWidthProperty().bind(getStackPane().prefWidthProperty());
        this.fitHeightProperty().bind(getStackPane().prefHeightProperty());
    }

    public void moveTo(Tile newTile){
        Move move = Database.getMoves().stream().filter(t -> t.getMovingPiece() == this &&
                t.getEndingSquare().equals(newTile)).findFirst().orElse(null);
        if (move == null) {
            return;
        }
        move.doMove();
    }

    abstract public void createMoves(List<Move> moves, boolean isMoveCreation);

    public void destroy() {
        Database.removePiece(this);
        occupiedTile.setOccupyingPiece(null);
        getStackPane().getChildren().remove(this);
    }

    //to prevent the method from changing the list when validating legal moves
    //validation is done to remove moves that puts the king in check
    public void addMove(List<Move> moves, Tile endTile){
        if (endTile == null) { return; }
        if (endTile.getOccupyingPiece() != null) {
            if (endTile.getOccupyingPiece().isWhite() == isWhite()) {
                return;
            }
        }
        Move move = new Move(occupiedTile, endTile);
        moves.add(move);
    }

    public List<Move> getMoves() { return Database.getMoves().stream().filter(t -> t.getMovingPiece() == this).collect(Collectors.toList()); }
    public StackPane getStackPane() { return (StackPane)getParent(); }
    public Tile getOccupiedTile() { return occupiedTile; }
    public void setOccupiedTile(Tile occupiedTile) { this.occupiedTile = occupiedTile; }
    public boolean isWhite() { return isWhite; }
    public void setHasMoved(boolean moved) { this.hasMoved = moved; }
    public boolean getHasMoved() { return hasMoved; }
}
