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
        Move move = search(5);
        if (move != null) return move;
        return Database.getMoves().getFirst();
    }

    public float Evaluate(){
        int myColor = this.isWhite? 1 : -1; //make result always positive if in AI's favour
        List<Move> moves = new ArrayList<>();
        moveCreation(moves);

        if (moves.isEmpty()) {
            if (GameManager.lastMove.isCheck()) {
                return isWhiteTurn ? Float.NEGATIVE_INFINITY * myColor : Float.POSITIVE_INFINITY * myColor;
            }
            return 0;
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
        return material * myColor;
    }

    private Move search(int depth) {
        Move[] move = new Move[1];
        search(depth, depth, move, true, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        return move[0];
    }

    private float search(int depth, int origDepth, Move[] bestMove, boolean myChoice, float alpha, float beta) {
        if (depth == 0) {
            return Evaluate();
        }

        List<Move> moves = new ArrayList<>();
        moveCreation(moves);
        int myColor = this.isWhite? 1 : -1; //make result always positive if in AI's favour

        if (moves.isEmpty()) {
            if (GameManager.lastMove.isCheck()) {
                return isWhiteTurn ? Float.NEGATIVE_INFINITY * myColor : Float.POSITIVE_INFINITY * myColor;
            }
            return 0;
        }

        float bestEvaluation = myChoice ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
        int bestMoveIndex = 0;

        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            move.doMoveTemporary();
            float evaluation = search(depth - 1, origDepth, bestMove, !myChoice, alpha, beta);
            move.undoMove();

            if (myChoice) {  // Maximizing player (AI)
                if (evaluation > bestEvaluation) {
                    bestEvaluation = evaluation;
                    bestMoveIndex = i;
                }
                alpha = Math.max(alpha, bestEvaluation);
            } else {  // Minimizing player (Opponent)
                if (evaluation < bestEvaluation) {
                    bestEvaluation = evaluation;
                    bestMoveIndex = i;
                }
                beta = Math.min(beta, bestEvaluation);
            }

            if (beta <= alpha) {  // Prune remaining branches
                break;
            }
        }

        if (depth == origDepth) {
            bestMove[0] = moves.get(bestMoveIndex);
        }

        return bestEvaluation;
    }
}
