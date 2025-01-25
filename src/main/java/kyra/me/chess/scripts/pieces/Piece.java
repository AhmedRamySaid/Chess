package kyra.me.chess.scripts.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import kyra.me.chess.scripts.Database;
import kyra.me.chess.scripts.GameManager;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Piece extends ImageView {
    Tile occupiedTile;
    boolean isWhite;
    Image model;
    StackPane stackPane;
    List<Move> moves;

    public Piece(Tile tile, boolean isWhite) {
        super(new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/colette.jpg")));
        this.isWhite = isWhite;
        this.setPreserveRatio(true);
        this.setFitHeight(40);
        occupiedTile = tile;
        moves = new ArrayList<>();
        this.stackPane = tile.getStackPane();

        setOnMouseClicked(event -> {
            occupiedTile.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED,0, 0, 0, 0, MouseButton.PRIMARY, 1,
                    true, true, true, true, true, true, true, true, true, true, null));
        });
        tile.setOccupyingPiece(this);
        Database.addPiece(this);

        this.fitWidthProperty().bind(stackPane.prefWidthProperty());
        this.fitHeightProperty().bind(stackPane.prefHeightProperty());
        stackPane.getChildren().add(this);
    }

    public void moveTo(Tile newTile){
        Move move = moves.stream().filter(t -> t.getStartingSquare().equals(occupiedTile) && t.getEndingSquare().equals(newTile)).findFirst().orElse(null);
        if (move == null) {
            return;
        }
        move.doMove();
    }

    public void addMove(Move move){ moves.add(move); }
    public List<Move> getMoves() { return moves; }
    public void createMoves(){
        Tile[][] tiles = Database.getTiles();
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 8; j++) {
                if (tiles[i][j].equals(occupiedTile)) { continue; }
                Move move = new Move(occupiedTile, tiles[i][j]);
                moves.add(move);
                Database.addMove(move);
            }
        }
    }
    public void destroy() {
        Database.removePiece(this);
        occupiedTile.setOccupyingPiece(null);
        stackPane.getChildren().remove(this);
    }
    public StackPane getStackPane() { return stackPane; }
    public void setStackPane(StackPane stackPane) { this.stackPane = stackPane; }
    public void setOccupiedTile(Tile occupiedTile) { this.occupiedTile = occupiedTile; }
    public boolean isWhite() { return isWhite; }
}
