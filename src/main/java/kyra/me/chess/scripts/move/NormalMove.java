package kyra.me.chess.scripts.move;

import kyra.me.chess.scripts.managers.GameManager;
import kyra.me.chess.scripts.tile.Tile;

public class NormalMove extends Move {
    public NormalMove(Tile start, Tile end){
        super(start, end, MoveType.normal);
    }
    public NormalMove(Tile start, Tile end, MoveType moveType){
        super(start, end, moveType);
    }

    @Override
    public void doMove() {
        super.doMove();
        GameManager.turnStart();
    }
}
