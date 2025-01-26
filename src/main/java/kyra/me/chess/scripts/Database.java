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
    public static Tile getTile(int x, int y) {
        if (x <= 0 || x > 8 || y <= 0 || y > 8) { return null; }
        return tiles[x-1][y-1];
    }
    public static void addPiece(Piece piece) { pieces.add(piece); }
    public static void removePiece(Piece piece) { pieces.remove(piece); }
    public static List<Piece> getPieces() { return pieces; }
    public static void addMove(Move move) { moves.add(move); }
    public static void removeMove(Move move) { moves.remove(move); }
    public static List<Move> getMoves() { return moves; }
    public static void clearMoves() { moves.clear(); }
    public static void setSelectedPiece(Piece piece) {
        if (selectedPiece != null) {
            for (Move move: selectedPiece.getMoves()) {
                move.getEndingSquare().togglePlayableMoveOff();
            }
        }
        if (piece != null) {
            for (Move move: piece.getMoves()) {
                move.getEndingSquare().togglePlayableMoveOn();
            }
        }
        selectedPiece = piece;
    }
    public static Piece getSelectedPiece() { return selectedPiece; }
}
