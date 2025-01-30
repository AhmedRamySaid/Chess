package kyra.me.chess.scripts.managers;

import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.pieces.Piece;
import kyra.me.chess.scripts.players.Player;
import kyra.me.chess.scripts.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static List<Player> players = new ArrayList<>();

    private Tile[][] tiles = new Tile[8][8];
    private List<Piece> pieces = new ArrayList<>();
    private List<Move> moves = new ArrayList<>();
    private Piece selectedPiece;

    public void addTile(Tile tile) { tiles[tile.getXPosition()-1][tile.getYPosition()-1] = tile; }
    public Tile[][] getTiles() { return tiles; }
    public Tile getTile(int x, int y) {
        if (x <= 0 || x > 8 || y <= 0 || y > 8) { return null; }
        return tiles[x-1][y-1];
    }

    public void addPiece(Piece piece) { pieces.add(piece); }
    public void removePiece(Piece piece) { pieces.remove(piece); }
    public List<Piece> getPieces() { return pieces; }

    public static void addPlayer(Player player) { players.add(player); }
    public static List<Player> getPlayers() { return players; }

    public void addMove(Move move) { moves.add(move); }
    public void removeMove(Move move) { moves.remove(move); }
    public List<Move> getMoves() { return moves; }
    public void clearMoves() { moves.clear(); }

    public void setSelectedPiece(Piece piece) {
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
    public Piece getSelectedPiece() { return selectedPiece; }

    void emptyAll(){
        tiles = new Tile[8][8];
        pieces = new ArrayList<>();
        moves = new ArrayList<>();
        selectedPiece = null;
    }
}
