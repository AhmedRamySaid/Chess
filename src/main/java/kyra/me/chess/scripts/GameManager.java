package kyra.me.chess.scripts;

import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.pieces.*;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    public static String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    public static boolean isWhiteTurn = true; //will change to true on start
    public static Clip[] audio = new Clip[2];

    public static void gameStart(){
        int col = 1, row = 8;
        for (char c: FEN.toCharArray()) {
            switch (c) {
                case 'r':
                    new Rook(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'R':
                    new Rook(Database.getTile(col, row), true);
                    col++;
                    break;
                case 'n':
                    new Knight(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'N':
                    new Knight(Database.getTile(col, row), true);
                    col++;
                    break;
                case 'b':
                    new Bishop(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'B':
                    new Bishop(Database.getTile(col, row), true);
                    col++;
                    break;
                case 'q':
                    new Queen(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'Q':
                    new Queen(Database.getTile(col, row), true);
                    col++;
                    break;
                case 'k':
                    new King(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'K':
                    new King(Database.getTile(col, row), true);
                    col++;
                    break;
                case 'p':
                    new Pawn(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'P':
                    new Pawn(Database.getTile(col, row), true);
                    col++;
                    break;
                case '/':
                    col = 1;
                    row--;
                    break;
                default:
                    col += c - '0';
                    break;
            }
        }
        try {
            BufferedInputStream moveStream = new BufferedInputStream(
                    GameManager.class.getResourceAsStream("/kyra/me/chess/assets/audio/move.wav")
            );
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(moveStream);
            Clip moveClip = AudioSystem.getClip();
            moveClip.open(audioInputStream);
            audio[0] = moveClip;

            BufferedInputStream captureStream = new BufferedInputStream(
                    GameManager.class.getResourceAsStream("/kyra/me/chess/assets/audio/capture.wav")
            );
            audioInputStream = AudioSystem.getAudioInputStream(captureStream);
            Clip captureClip = AudioSystem.getClip();
            captureClip.open(audioInputStream);
            audio[1] = captureClip;
        }
        catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {System.out.println(ex);}
        turnStart();
    }
    public static void turnStart(){
        Database.clearMoves();
        for (Piece piece: Database.getPieces()){ //move creation
            if (isWhiteTurn == piece.isWhite()){
                piece.createMoves(Database.getMoves(), true);
            }
        }

        List<Move> moves = new ArrayList<>(); //move validation
        Iterator<Move> iterator = Database.getMoves().iterator();
        while (iterator.hasNext()){
            Move move = iterator.next();

            move.doMoveTemporary();
            for (Piece piece: Database.getPieces()){
                if (isWhiteTurn == piece.isWhite()){
                    piece.createMoves(moves, false);
                }
            }
            moves.stream().filter(t -> t.getCapturedPiece() instanceof King).findAny().ifPresent(
                    badMove -> { iterator.remove(); }
            );
            move.undoMove();
            moves.clear();
        }
        System.out.println(Database.getMoves().size());
    }
}
