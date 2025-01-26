module kyra.me.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens kyra.me.chess to javafx.fxml;
    exports kyra.me.chess;
    exports kyra.me.chess.scripts.Controllers;
    opens kyra.me.chess.scripts.Controllers to javafx.fxml;
    exports kyra.me.chess.scripts;
    opens kyra.me.chess.scripts to javafx.fxml;
    exports kyra.me.chess.scripts.move;
    opens kyra.me.chess.scripts.move to javafx.fxml;
}