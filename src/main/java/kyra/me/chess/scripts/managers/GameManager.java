package kyra.me.chess.scripts.managers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import kyra.me.chess.Chess;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.pieces.*;
import kyra.me.chess.scripts.players.AI;
import kyra.me.chess.scripts.players.HumanPlayer;
import kyra.me.chess.scripts.players.Player;
import kyra.me.chess.scripts.tile.Tile;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    public static String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w";
    public static boolean isWhiteTurn = true;
    public static boolean isWhiteTurnTemp = true;
    public static Clip[] audio = new Clip[3];

    public static GameState gameState;
    public static Move lastMove;
    public static Player playerOne;
    public static Player playerTwo;

    public static int turnCount;
    public static int drawTurnCount; //used to decide if the game is a draw
    public static boolean isCheck;
    public static boolean isDoubleCheck;
    public static boolean isEarlyGame;

    public static void gameStart(){
        gameState = GameState.normal;
        lastMove = null;
        turnCount = 0;
        drawTurnCount = 0;
        isCheck = false;
        isDoubleCheck = false;
        isEarlyGame = true;
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

        if (playerOne instanceof AI) {
            playerOne.setIsWhite(true);
            if (playerTwo instanceof HumanPlayer) {
                rotateProfilesAndTimer();
                Chess.board.setRotate(180);
                for (Node node: Chess.board.getChildren()){
                    node.setRotate(-180);
                }
            }
            ((AI) playerOne).setTightOnTime(false);
        }
        if (playerTwo instanceof AI) {
            playerTwo.setIsWhite(false);
            ((AI) playerTwo).setTightOnTime(false);
        }

        turnStart();
    }
    public static void turnStart(){
        isCheck = false;
        isDoubleCheck = false;
        if (lastMove != null) {
            isWhiteTurn = !isWhiteTurn;
            turnCount++;
            if (lastMove.isCapture() || lastMove.getMovingPiece() instanceof Pawn){
                drawTurnCount = 0;
            }
            else { drawTurnCount++; }
            if (playerOne instanceof HumanPlayer && playerTwo instanceof HumanPlayer) {
                rotateProfilesAndTimer();
            }
        }

        isWhiteTurnTemp = isWhiteTurn;
        isEarlyGame = turnCount <= 15;
        Database.clearMoves();
        moveCreation(Database.getMoves());
        playAudio();
        checkWinner();

        if (gameState != GameState.normal) { return; }
        if (isWhiteTurn){
            if (playerOne instanceof AI) {
                new Thread(() -> {
                    Move bestMove = ((AI)playerOne).generateMove();  // AI move calculation (long process)

                    // Update UI safely
                    Platform.runLater(bestMove::doMove);
                }).start();
                return;
            }

            Chess.board.setRotate(0);
            for (Node node: Chess.board.getChildren()){
                node.setRotate(0);
            }
        }
        else
        {
            if (playerTwo instanceof AI) {
                new Thread(() -> {
                    Move bestMove = ((AI)playerTwo).generateMove();  // AI move calculation (long process)

                    // Update UI safely
                    Platform.runLater(bestMove::doMove);
                }).start();
                return;
            }

            Chess.board.setRotate(180);
            for (Node node: Chess.board.getChildren()){
                node.setRotate(-180);
            }
        }
    }

    public static void checkWinner(){
        //it is the turn of the losing player
        if (Database.getMoves().isEmpty()){
            if (!isCheck){
                gameState = GameState.draw;
            }
            else if (isWhiteTurn){
                gameState = GameState.blackWon;
            }
            else { gameState = GameState.whiteWon; }
        }
        else if (drawTurnCount >= 50){
            gameState = GameState.draw;
        }
        else {
            return;
        }

        Database.emptyAll();
        endGame();
    }

    public static void endGame(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game ended");
        alert.setContentText(gameState.toString());
        alert.show();

        alert.setOnHidden(event -> {
            Stage primaryStage = Chess.primaryStage;
            FXMLLoader mainMenu = new FXMLLoader(GameManager.class.getResource("/kyra/me/chess/scenes/main-menu.fxml"));
            Scene scene = null;
            try {
                scene = mainMenu.load(); }
            catch (IOException ex) { System.out.println(ex); }
            primaryStage.setScene(scene);
        });
    }

    public static void playAudio(){
        if (lastMove == null) { return; }

        if (isCheck) {
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

    public static void moveCreation(List<Move> moveList){
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                Tile t = Database.getTile(i,j);
                t.toggleAllOff();
            }
        }
        for (Piece piece: Database.getPieces()){
            piece.setPinDirection(0);
        }
        for (Piece piece: Database.getPieces()){
            if (isWhiteTurn != piece.isWhite()){
                piece.createAttacks();
            }
        }
        for (Piece piece: Database.getPieces()){
            if (isWhiteTurn == piece.isWhite()){
                piece.createMoves(moveList);
            }
        }
    }

    public static void moveGeneration(int depth){
        long t1 = System.currentTimeMillis();
        BigInteger n = moveGeneration(depth, depth);
        System.out.print("Depth: " + depth + " positions: " + n);
        long t2 = System.currentTimeMillis();
        System.out.println(" in: " + (t2 - t1) + "ms");
    }
    //also prints out the number of positions that appears after each move is played
    private static BigInteger moveGeneration(int depth, int og){
        if (depth == 0){
            return BigInteger.ONE;
        }
        BigInteger numOfPositions = BigInteger.ZERO;
        List<Move> moves = new ArrayList<>();
        if (depth == og) {
            moves = Database.getMoves();
        } else {
            moveCreation(moves);
        }

        for (Move move : moves){
            move.doMoveTemporary();
            BigInteger n = moveGeneration(depth - 1, og);
            if (depth == og) {
                char c1 = (char)(move.getStartingSquare().getXPosition() + 'a' - 1);
                char c2 = (char)(move.getEndingSquare().getXPosition() + 'a' - 1);
                System.out.println( c1 + "" + move.getStartingSquare().getYPosition() + c2 + move.getEndingSquare().getYPosition() + ": " + n);
            }
            numOfPositions = numOfPositions.add(n);
            move.undoMove();
        }
        return numOfPositions;
    }

    private static void rotateProfilesAndTimer(){
        Chess.playersProfiles.getChildren().getFirst().toFront();

        Chess.sceneVBox.getChildren().getFirst().toFront();
        Chess.sceneVBox.getChildren().get(1).toBack();
    }
}
