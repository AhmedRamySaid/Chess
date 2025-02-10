package kyra.me.chess.scripts.players;

import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.move.Move;

import java.util.ArrayList;
import java.util.List;

import static kyra.me.chess.scripts.managers.GameManager.isWhiteTurn;
import static kyra.me.chess.scripts.managers.GameManager.moveCreation;

public abstract class AI extends Player {
    boolean isTightOnTime;

    protected AI(String imagePath, String name) {
        super(imagePath, name);
        isTightOnTime = false;
    }

    public abstract Move generateMove();

    float checkGameEnded(){
        List<Move> moves = new ArrayList<>();
        moveCreation(moves);
        return checkGameEnded(moves);
    }
    float checkGameEnded(List<Move> moves){
        int myColor = this.isWhite? 1 : -1; //make result always positive if in AI's favour

        if (moves.isEmpty()) {
            if (GameManager.lastMove.isCheck()) {
                return isWhiteTurn ? Float.NEGATIVE_INFINITY * myColor : Float.POSITIVE_INFINITY * myColor;
            }
            return 0;
        }
        if (GameManager.drawTurnCount >= 50) { return 0; }
        return -1; //game isn't over
    }
    public void setTightOnTime(boolean tightOnTime) {
        this.isTightOnTime = tightOnTime;
    }
}
