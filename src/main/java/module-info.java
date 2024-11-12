module com.finlayprojects.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.finlayprojects.minesweeper;
    opens com.finlayprojects.minesweeper to javafx.fxml;
}