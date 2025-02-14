package kyra.me.chess.scripts.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.move.PromotingMove;
import kyra.me.chess.scripts.players.HumanPlayer;
import kyra.me.chess.scripts.tile.Tile;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Piece extends ImageView {
    Tile occupiedTile;
    boolean isWhite;
    boolean hasMoved;
    //this tells the piece in which direction a DiagonalMovingPiece is staring it down, pinning it to the king
    //0 means it is not pinned
    //1 means it is pinned horizontally
    //2 means it is pinned vertically
    //3 means it is pinned north-east to south-west
    //4 means it is pinned north-west to south-east
    int pinDirection;
    //white pawn  (0), black pawn (1), white knight (2), black knight (3), white bishop (4), black bishop (5),
    // white rook (6), black rook (7), white queen (8), black queen (9), white king (10), black king (11)
    static Image[] models = {new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/white pawn.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/black pawn.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/white knight.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/black knight.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/white bishop.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/black bishop.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/white rook.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/black rook.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/white queen.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/black queen.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/white king.png")), new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/models/pieces/black king.png"))};

    public Piece(Tile tile, boolean isWhite) {
        this.isWhite = isWhite;
        this.setPreserveRatio(true);
        this.setFitHeight(40);
        occupiedTile = tile;
        pinDirection = 0;

        if (occupiedTile != null){
            initialize(occupiedTile);
        }
    }

    public void moveTo(Tile newTile){
        Move move = Database.getMoves().stream().filter(t -> t.getMovingPiece() == this &&
                t.getEndingSquare().equals(newTile)).findFirst().orElse(null);
        if (move == null) {
            return;
        }

        if (GameManager.playerOne instanceof HumanPlayer && GameManager.isWhiteTurn ||
                GameManager.playerTwo instanceof HumanPlayer && !GameManager.isWhiteTurn){
            if (move instanceof PromotingMove){
                ((PromotingMove)move).showSelection();
                return;
            }
        }

        move.doMove();
    }

    public abstract void createMoves(List<Move> moves);
    public abstract void createAttacks();

    public void destroy() {
        Database.removePiece(this);
        occupiedTile.setOccupyingPiece(null);
        getStackPane().getChildren().remove(this);
    }

    public void initialize(Tile tile){
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

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Piece p = (Piece) obj;
        if (p.isWhite != this.isWhite) {
            return false;
        }
        if (!p.occupiedTile.equals(occupiedTile)) {
            return false;
        }
        return true;
    }

    public List<Move> getMoves() { return Database.getMoves().stream().filter(t -> t.getMovingPiece() == this).collect(Collectors.toList()); }
    public StackPane getStackPane() { return (StackPane)getParent(); }
    public Tile getOccupiedTile() { return occupiedTile; }
    public void setOccupiedTile(Tile occupiedTile) { this.occupiedTile = occupiedTile; }
    public boolean isWhite() { return isWhite; }
    public void setHasMoved(boolean moved) { this.hasMoved = moved; }
    public boolean getHasMoved() { return hasMoved; }
    public void setPinDirection(int pinDirection) { this.pinDirection = pinDirection; }
    public int getPinDirection() { return pinDirection; }
}
