package com.finlayprojects.minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MinesweeperController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}