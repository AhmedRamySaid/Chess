package kyra.me.chess.scripts.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import kyra.me.chess.scripts.Database;
import kyra.me.chess.scripts.tile.Tile;

public class Piece extends ImageView {
    Tile occupiedTile;
    Image model;
    StackPane stackPane;

    public Piece(Tile tile) {
        super(new Image(Piece.class.getResourceAsStream("/kyra/me/chess/assets/colette.jpg")));
        this.setPreserveRatio(true);
        this.setFitHeight(40);
        occupiedTile = tile;
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
        stackPane.getChildren().remove(this);

        occupiedTile.setOccupyingPiece(null);
        occupiedTile = newTile;
        occupiedTile.setOccupyingPiece(this);
        Database.setSelectedPiece(null);

        stackPane = occupiedTile.getStackPane();
        stackPane.getChildren().add(this);
    }
}
