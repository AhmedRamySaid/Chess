package kyra.me.chess.scripts;

import kyra.me.chess.scripts.pieces.Piece;

public class GameManager {
    public static String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    public static boolean isWhiteTurn = false; //will change to true on start

    public static void gameStart(){
        int col = 1, row = 8;
        Piece p;
        for (char c: FEN.toCharArray()) {
            switch (c) {
                case 'r':
                    p = new Piece(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'R':
                    p = new Piece(Database.getTile(col, row), true);
                    col++;
                    break;
                case 'n':
                    p = new Piece(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'N':
                    p = new Piece(Database.getTile(col, row), true);
                    col++;
                    break;
                case 'b':
                    p = new Piece(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'B':
                    p = new Piece(Database.getTile(col, row), true);
                    col++;
                    break;
                case 'q':
                    p = new Piece(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'Q':
                    p = new Piece(Database.getTile(col, row), true);
                    col++;
                    break;
                case 'k':
                    p = new Piece(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'K':
                    p = new Piece(Database.getTile(col, row), true);
                    col++;
                    break;
                case 'p':
                    p = new Piece(Database.getTile(col, row), false);
                    col++;
                    break;
                case 'P':
                    p = new Piece(Database.getTile(col, row), true);
                    col++;
                    break;
                case '/':
                    col = 1;
                    row--;
                    break;
                default:
                    col += c;
                    break;
            }
        }
        turnStart();
    }
    public static void turnStart(){
        Database.clearMoves();
        isWhiteTurn = !isWhiteTurn;
        for (Piece piece: Database.getPieces()){
            if (isWhiteTurn == piece.isWhite()){
                piece.createMoves();
            }
        }
    }
}
