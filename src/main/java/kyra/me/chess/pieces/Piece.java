package kyra.me.chess.pieces;

import kyra.me.chess.tile.Tile;

public abstract class Piece {
    int xPosition;
    int yPosition;
    Tile occupiedTile;

    Piece(int xPos, int yPos, Tile tile){
        occupiedTile = tile;
        xPosition = xPos;
        yPosition = yPos;
    }

}
