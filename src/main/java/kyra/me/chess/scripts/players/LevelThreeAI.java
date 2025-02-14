package kyra.me.chess.scripts.players;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.pieces.*;

import java.util.ArrayList;
import java.util.List;

import static kyra.me.chess.scripts.managers.GameManager.isWhiteTurn;
import static kyra.me.chess.scripts.managers.GameManager.moveCreation;

public class LevelThreeAI extends AI {
    static final int pawnValue = 1;
    static final int knightValue = 3;
    static final int bishopValue = 3;
    static final int rookValue = 5;
    static final int queenValue = 9;

    public LevelThreeAI(String imagePath, String name){
        super(imagePath, name);
    }

    @Override
    public Move generateMove(){
        Move m;
        //make it so it is 4 when early-game as well
        if (isTightOnTime || GameManager.isEarlyGame) m = search(4);
        else m = search(5);
        if (m == null) {
            System.out.println("Error. could not retrieve a move");
            return Database.getMoves().getFirst();
        }
        return m;
    }

    public float Evaluate(int depth){
        int myColor = this.isWhite? 1 : -1; //make result always positive if in AI's favour

        float evaluation = 0;
        for (Piece piece: Database.getPieces()){
            int color = piece.isWhite()? 1: -1;
            switch (piece){
                case Pawn pawn:
                    evaluation += pawnValue * color;
                    break;
                case Knight knight:
                    evaluation += knightValue * color;
                    break;
                case Bishop bishop:
                    evaluation += bishopValue * color;
                    break;
                case Rook rook:
                    evaluation += rookValue * color;
                    break;
                case Queen queen:
                    evaluation += queenValue * color;
                    break;
                default:
                    break;
            }
            evaluation += evaluatePiece(piece) * color;
        }
        evaluation += forceKingToCornerEndgameEval() * myColor;
        return evaluation * myColor;
    }

    private Move search(int depth) {
        Move[] move = new Move[1];
        search(depth, depth, move, true, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        return move[0];
    }

    private float search(int depth, int origDepth, Move[] bestMove, boolean myChoice, float alpha, float beta) {
        List<Move> moves = new ArrayList<>();
        moveCreation(moves);

        float gameEnded = super.checkGameEnded(moves);

        if (gameEnded != -1) {
            return gameEnded * (depth + 1);
        }

        if (depth == 0) {
            return Evaluate(depth);
        }

        float bestEvaluation = myChoice ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
        int bestMoveIndex = -1;

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
                }
                beta = Math.min(beta, bestEvaluation);
            }

            if (beta <= alpha) {  // Prune remaining branches
                break;
            }
        }

        if (depth == origDepth && bestMoveIndex != -1) {
            bestMove[0] = moves.get(bestMoveIndex);
            int myColor = this.isWhite? 1 : -1;
            //System.out.println("Evaluation by the Colette bot: " + bestEvaluation * myColor);
        }

        return bestEvaluation;
    }

    private float evaluatePiece(Piece piece){
        float evaluation = 0;

        int tileX = piece.getOccupiedTile().getXPosition();
        int tileY = piece.getOccupiedTile().getYPosition();

        switch(piece){
            case Pawn pawn:
                int startY = piece.isWhite()? 2 : 7;
                if (tileX == 4 || tileX == 5) {
                    if (tileY == startY){
                        evaluation += -0.3F;
                    }
                    else if (Math.abs(tileY - startY) == 1){
                        evaluation -= 0.1F;
                    }
                    else evaluation += 0.3F;
                }

                break;
            case Knight knight:
                if (tileY == 1 || tileY == 8) { break; }
                else if (tileX == 1 || tileX == 8) { break; }
                evaluation += 0.3F;
                break;
            case Bishop bishop:
                if (tileY == 1 || tileY == 8) { break; }
                else if (tileX == 1 || tileX == 8) { break; }
                evaluation += 0.3F;
                break;
            case Rook rook:
                if (tileX == 1 || tileX == 2 || tileX == 7 || tileX == 8) { evaluation -= -0.2F; break; }
                if (tileX == 3 || tileX == 6) { break; }
                evaluation += 0.5F;
                break;
            case Queen queen:
                break;
            case King king:
                int startYPos = piece.isWhite()? 1 : 8;
                if (Database.getPieces().size() > 10) {
                    if (tileX == 1 || tileX == 8) { evaluation += 0.3F; }
                    else if (tileX == 2 || tileX == 7) { evaluation += 0.5F; }

                    if (Math.abs(tileY - startYPos) > 1) {
                        evaluation -= 1;
                    }
                }
                break;
            default:
                break;
        }

        return evaluation;
    }

    private float forceKingToCornerEndgameEval(){
        float evaluation = 0;
        float endgameWeight = (float) 2 / Database.getPieces().size();

        Piece myKing = Database.getPieces().stream().filter(t -> t instanceof King && t.isWhite() == this.isWhite).findFirst().get();
        Piece opponentKing = Database.getPieces().stream().filter(t -> t instanceof King && t.isWhite() != this.isWhite).findFirst().get();

        int opponentKingXPos = opponentKing.getOccupiedTile().getXPosition();
        int opponentKingYPos = opponentKing.getOccupiedTile().getYPosition();

        //forces opponent king into the corner
        int opponentKingDstToCenterX = Math.max(3 - opponentKingXPos, opponentKingXPos - 4);
        int opponentKingDstToCenterY = Math.max(3 - opponentKingYPos, opponentKingYPos - 4);
        int opponentKingDstFromCenter = opponentKingDstToCenterX + opponentKingDstToCenterY;
        evaluation += opponentKingDstFromCenter;

        //gets my king next to the opponent's king
        int myKingXPos = myKing.getOccupiedTile().getXPosition();
        int myKingYPos = myKing.getOccupiedTile().getYPosition();

        int xDstBetweenKings = Math.abs(opponentKingXPos - myKingXPos);
        int yDstBetweenKings = Math.abs(opponentKingYPos - myKingYPos);
        int dstBetweenKings = xDstBetweenKings + yDstBetweenKings;
        evaluation += 14 - dstBetweenKings;

        return evaluation * endgameWeight;
    }
}
