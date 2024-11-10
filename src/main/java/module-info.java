module minesweeper.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;


    opens minesweeper.minesweeper to javafx.fxml;
    exports minesweeper.minesweeper;
}