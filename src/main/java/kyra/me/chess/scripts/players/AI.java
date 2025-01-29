package kyra.me.chess.scripts.players;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.move.Move;

import java.util.Random;

public abstract class AI extends Player {
    protected AI(String imagePath, String name) {
        super(imagePath, name);
    }

    public abstract Move generateMove();
}
