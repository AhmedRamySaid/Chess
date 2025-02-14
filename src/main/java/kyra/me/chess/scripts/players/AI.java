package kyra.me.chess.scripts.players;

import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.move.Move;

import java.util.ArrayList;
import java.util.List;

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
            if (GameManager.isCheck) {
                //turn changes after a move is played so if isWhiteTurn is true it means black is the winner
                return !GameManager.isWhiteTurn ? 10000 * myColor : -10000 * myColor;
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
