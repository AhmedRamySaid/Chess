package kyra.me.chess.scripts.managers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import kyra.me.chess.Chess;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.move.MoveType;
import kyra.me.chess.scripts.pieces.*;
import kyra.me.chess.scripts.players.AI;
import kyra.me.chess.scripts.players.Player;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    public static String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w";
    public static boolean isWhiteTurn = true; //will change to true on start
    public static Clip[] audio = new Clip[3];
    public static GameState gameState;
    public static Move lastMove;
    public static Player playerOne;
    public static Player playerTwo;
    public static int turnCount; //used to decide if the game is a draw

    public static void gameStart(){
        gameState = GameState.normal;
        lastMove = null;
        turnCount = 0;
        int col = 1, row = 8;

        for (int i = 0; i < FEN.length(); i++) {
            char c = FEN.charAt(i);
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
                case 'w':
                    isWhiteTurn = true;
                    break;
                case ' ':
                    if (FEN.charAt(i+1) == 'w') {
                        isWhiteTurn = true;
                    } else if (FEN.charAt(i+1) == 'b') {
                        isWhiteTurn = false; }
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

            BufferedInputStream checkStream = new BufferedInputStream(
                    GameManager.class.getResourceAsStream("/kyra/me/chess/assets/audio/check.wav")
            );
            audioInputStream = AudioSystem.getAudioInputStream(checkStream);
            Clip checkClip = AudioSystem.getClip();
            checkClip.open(audioInputStream);
            audio[2] = checkClip;
        }
        catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {System.out.println(ex);}
        turnStart();
    }
    public static void turnStart(){
        if (lastMove != null) {
            if (lastMove.isCapture() || lastMove.getMovingPiece() instanceof Pawn){
                turnCount = 0;
            } else { turnCount++; }
        }

        Database.clearMoves();
        for (Piece piece: Database.getPieces()){ //move creation
            if (isWhiteTurn == piece.isWhite()){
                piece.createMoves(Database.getMoves());
            }
        }

        List<Move> moves = new ArrayList<>(); //move validation
        Iterator<Move> iterator = Database.getMoves().iterator();
        while (iterator.hasNext()){
            Move move = iterator.next();

            if (move.getType() != MoveType.normal) { continue; }
            move.doMoveTemporary();
            for (Piece piece: Database.getPieces()){
                if (isWhiteTurn == piece.isWhite()){
                    piece.createMoves(moves);
                }
            }
            moves.stream().filter(t -> t.getCapturedPiece() instanceof King).findAny().ifPresentOrElse(
                    badMove -> iterator.remove(), move::initializeIsCheck
            );
            move.undoMove();
            moves.clear();
        }
        playAudio();
        checkWinner();

        if (gameState == GameState.normal){
            if (isWhiteTurn){
                if (playerOne instanceof AI) {
                    ((AI)playerOne).generateMove().doMove();
                }
            } else {
                if (playerTwo instanceof AI) {
                    ((AI)playerTwo).generateMove().doMove();
                }
            }
        }
    }

    public static void checkWinner(){
        if (Database.getMoves().isEmpty()){
            isWhiteTurn = !isWhiteTurn;
            for (Piece piece: Database.getPieces()){
                if (isWhiteTurn == piece.isWhite()){
                    piece.createMoves(Database.getMoves());
                }
            }

            if (!lastMove.isCheck()){
                gameState = GameState.draw;
            }
            else if (isWhiteTurn){
                gameState = GameState.whiteWon;
            }
            else { gameState = GameState.blackWon; }

        }
        else if (turnCount >= 50){
            gameState = GameState.draw;
        }
        else {
            return;
        }

        Database.emptyAll();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game ended");
        alert.setContentText(gameState.toString());
        alert.showAndWait();

        Stage primaryStage = Chess.primaryStage;
        FXMLLoader mainMenu = new FXMLLoader(GameManager.class.getResource("/kyra/me/chess/scenes/main-menu.fxml"));
        Scene scene = null;
        try {
            scene = mainMenu.load(); }
        catch (IOException ex) { System.out.println(ex); }
        primaryStage.setScene(scene);
    }

    public static void playAudio(){
        if (lastMove == null) { return; }

        if (lastMove.isCheck()) {
            GameManager.audio[2].stop(); //resets the audio clip so it can be replayed
            GameManager.audio[2].setFramePosition(0);
            GameManager.audio[2].start();
            return;
        }
        if (lastMove.isCapture()){
            GameManager.audio[1].stop();
            GameManager.audio[1].setFramePosition(0);
            GameManager.audio[1].start();
            return;
        }
        GameManager.audio[0].stop();
        GameManager.audio[0].setFramePosition(0);
        GameManager.audio[0].start();
    }
}
