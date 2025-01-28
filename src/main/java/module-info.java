module kyra.me.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens kyra.me.chess to javafx.fxml;
    exports kyra.me.chess;
    exports kyra.me.chess.scripts.controllers;
    opens kyra.me.chess.scripts.controllers to javafx.fxml;
    exports kyra.me.chess.scripts.move;
    opens kyra.me.chess.scripts.move to javafx.fxml;
    exports kyra.me.chess.scripts.managers;
    opens kyra.me.chess.scripts.managers to javafx.fxml;
}