package kyra.me.chess.scripts;

import kyra.me.chess.scripts.pieces.Piece;
import kyra.me.chess.scripts.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Tile[][] tiles = new Tile[8][8];
    private static List<Piece> pieces = new ArrayList<>();
    private static Piece selectedPiece;

    public static void addTile(Tile tile) { tiles[tile.getXPosition()-1][tile.getYPosition()-1] = tile; }
    public static void addPiece(Piece piece) { pieces.add(piece); }
    public static void setSelectedPiece(Piece piece) {
        selectedPiece = piece;

    }
    public static Piece getSelectedPiece() { return selectedPiece; }
}
