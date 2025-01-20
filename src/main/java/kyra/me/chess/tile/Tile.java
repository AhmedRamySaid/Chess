package kyra.me.chess.tile;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import kyra.me.chess.pieces.Piece;

public class Tile {
    private final int xPosition;
    private final int yPosition;
    private Piece occupyingPiece;
    private Color tileColor;
    private Rectangle tile;

    public Tile(int x, int y, Stage stage) {
        this(x, y, null, Color.WHITE, Color.BLACK, stage);
    }
    public Tile(int x, int y, Piece piece, Stage stage) {
        this(x, y, piece, Color.WHITE, Color.BLACK, stage);
    }
    public Tile(int x, int y, Color mainColor, Color offsetColor, Stage stage) {
        this(x, y,null, mainColor, offsetColor, stage);
    }
    public Tile(int x, int y, Piece piece, Color mainColor, Color offsetColor, Stage stage) {
        xPosition = x;
        yPosition = y;
        occupyingPiece = piece;
        tile = new Rectangle();

        sizeSetter(stage);
        locationSetter(stage);
        colorSetter(mainColor, offsetColor);

        tile.setOnMouseEntered(e -> {
            tile.setFill(tileColor.brighter());
        });
        tile.setOnMouseExited(e -> {
            tile.setFill(tileColor);
        });
    }

    public Rectangle getTile() {
        return tile;
    }

    public void colorSetter(Color mainColor, Color offsetColor) {
        if (xPosition%2 == yPosition%2){
            tileColor = offsetColor;
        }
        else{
            tileColor = mainColor;
        }
        tile.setFill(tileColor);
    }

    public void sizeSetter(Stage stage) {
        if (stage.getWidth() < stage.getHeight()) {
            tile.widthProperty().bind(stage.widthProperty().divide(10));
            tile.heightProperty().bind(stage.widthProperty().divide(10));
        }
        else{
            tile.widthProperty().bind(stage.heightProperty().divide(10));
            tile.heightProperty().bind(stage.heightProperty().divide(10));
        }
    }

    public void locationSetter(Stage stage){
        //default: x = 5, y = 3 measured from the top left
        tile.setX(stage.getWidth()/2 + ( (xPosition-5) * tile.getWidth() ));
        tile.setY(stage.getHeight()/2 + ( (yPosition-5) * tile.getHeight() ));
    }
}
