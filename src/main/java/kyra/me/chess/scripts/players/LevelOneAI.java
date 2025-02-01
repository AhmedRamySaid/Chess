package kyra.me.chess.scripts.players;

import kyra.me.chess.scripts.managers.Database;
import kyra.me.chess.scripts.move.Move;
import kyra.me.chess.scripts.pieces.Rook;
import kyra.me.chess.scripts.tile.Tile;

import java.util.Random;

public class LevelOneAI extends AI{
    public LevelOneAI(String imagePath, String name) {
        super(imagePath, name);
    }

    @Override
    public Move generateMove() {
        Random rand = new Random();
        return Database.getMoves().get(rand.nextInt(Database.getMoves().size()));
    }
}
