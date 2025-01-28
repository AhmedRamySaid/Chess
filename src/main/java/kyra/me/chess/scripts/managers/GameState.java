package kyra.me.chess.scripts.managers;

public enum GameState {
    normal("normal"),
    whiteWon("white won!"),
    blackWon("black won!"),
    draw("draw"),;
    private final String state;
    GameState(String state) {
        this.state = state;
    }
    @Override
    public String toString() {
        return state;
    }
}
