package kyra.me.chess.scripts;

import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.pieces.Piece;
import kyra.me.chess.scripts.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Tile[][] tiles = new Tile[8][8];
    private static List<Piece> pieces = new ArrayList<>();
    private static List<Move> moves = new ArrayList<>();
    private static Piece selectedPiece;

    public static void addTile(Tile tile) { tiles[tile.getXPosition()-1][tile.getYPosition()-1] = tile; }
    public static Tile[][] getTiles() { return tiles; }
    public static Tile getTile(int x, int y) { return tiles[x-1][y-1]; }
    public static void addPiece(Piece piece) { pieces.add(piece); }
    public static void removePiece(Piece piece) { pieces.remove(piece); }
    public static List<Piece> getPieces() { return pieces; }
    public static void addMove(Move move) { moves.add(move); }
    public static void clearMoves() { moves.clear(); }
    public static void setSelectedPiece(Piece piece) {
        selectedPiece = piece;
        if (piece == null) { return; }
        for (Move move: selectedPiece.getMoves()) {
            move.getEndingSquare().togglePlayableMoveOn();
        }
    }
    public static Piece getSelectedPiece() { return selectedPiece; }
}
