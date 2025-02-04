package kyra.me.chess.scripts.players;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.pieces.*;

import java.util.ArrayList;
import java.util.List;

import static kyra.me.chess.scripts.managers.GameManager.isWhiteTurn;
import static kyra.me.chess.scripts.managers.GameManager.moveCreation;

public class LevelTwoAI extends AI {
    static final int pawnValue = 1;
    static final int knightValue = 3;
    static final int bishopValue = 3;
    static final int rookValue = 5;
    static final int queenValue = 9;

    public LevelTwoAI(String imagePath, String name){
        super(imagePath, name);
    }

    @Override
    public Move generateMove(){
        Move move = search(4);
        if (move != null) return move;
        return Database.getMoves().getFirst();
    }

    public static float Evaluate(){
        List<Move> moves = new ArrayList<>();
        moveCreation(moves);
        if (moves.isEmpty()){
            if (GameManager.lastMove.isCheck()){
                if (!isWhiteTurn) {
                    return 10000;
                }
                else {
                    return -10000;
                }
            } else {
                return 0;
            }
        }
        float material = 0;
        for (Piece piece: Database.getPieces()){
            int color = piece.isWhite()? 1: -1;
            switch (piece){
                case Pawn pawn:
                    material += pawnValue * color;
                    break;
                case Knight knight:
                    material += knightValue * color;
                    break;
                case Bishop bishop:
                    material += bishopValue * color;
                    break;
                case Rook rook:
                    material += rookValue * color;
                    break;
                case Queen queen:
                    material += queenValue * color;
                    break;
                default:
                    break;
            }
        }
        return material;
    }

    private Move search(int depth){
        Move[] move = new Move[1];
        search(depth, depth, move, true);
        return move[0];
    }
    private float search(int depth, int origDepth, Move[] bestMove, boolean myChoice){
        if (depth == 0){
            return Evaluate();
        }
        List<Move> moves = new ArrayList<>();
        moveCreation(moves);
        if (moves.isEmpty()){
            if (GameManager.lastMove.isCheck()){
                if (!isWhiteTurn) {
                    return 10000;
                }
                else {
                    return -10000;
                }
            } else {
                return 0;
            }
        }

        List<Float> evaluations = new ArrayList<>();
        float evaluation;
        int pos = 0;

        for (Move move : moves){
            move.doMoveTemporary();
            evaluation = search(depth - 1, origDepth,bestMove, !myChoice);
            evaluations.add(pos++, evaluation);
            move.undoMove();
        }
        evaluation = evaluations.getFirst();
        pos = 0;

        if (isWhite && myChoice){ //my choice
           for (int i = 1; i < evaluations.size(); i++){
               if (evaluations.get(i) > evaluation){
                   evaluation = evaluations.get(i);
                   pos = i;
               }
           }
        }
        else if (isWhite){ //opponent's choice
            for (int i = 1; i < evaluations.size(); i++){
                if (evaluations.get(i) < evaluation){
                    evaluation = evaluations.get(i);
                    pos = i;
                }
            }
        }
        else if (myChoice){ //my choice
            for (int i = 1; i < evaluations.size(); i++){
                if (evaluations.get(i) < evaluation){
                    evaluation = evaluations.get(i);
                    pos = i;
                }
            }
        }
        else { //opponent's choice
            for (int i = 1; i < evaluations.size(); i++){
                if (evaluations.get(i) > evaluation){
                    evaluation = evaluations.get(i);
                    pos = i;
                }
            }
        }

        if (depth == origDepth) {
            bestMove[0] = moves.get(pos);
        }
        return evaluation;
    }
}
