package com.finlayprojects.minesweeper;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class MinesweeperApplication extends Application {

    private static final int TILE_SIZE = 40;
    private static final int width = 800;
    private static final int height = 600;
    private static final int X_TILES = width/TILE_SIZE;
    private static final int Y_TILES = height/TILE_SIZE;

    private final Tile[][] grid = new Tile[X_TILES][Y_TILES];
    private Scene scene;
    private boolean firstClick = true;

    private int totalBombs;
    private int remainingBombs;
    private final Text bombCounter = new Text();

    private final Text timerText = new Text();
    private Timeline timeline;
    private int secondsElapsed;

    private int openedTiles;

    private Parent createContent(){
        VBox layout = new VBox();
        Pane root = new Pane();
        root.setPrefSize(width, height);

        // Initialize bomb count and counter display
        totalBombs = (int) (X_TILES * Y_TILES * 0.2);
        remainingBombs = totalBombs;
        bombCounter.setFont(Font.font(18));
        bombCounter.setText("Bombs: " + remainingBombs);

        // Initialize timer
        secondsElapsed = 0;
        timerText.setFont(Font.font(18));
        timerText.setText("Time: " + secondsElapsed);

        HBox header = new HBox();
        header.setSpacing(10);
        header.setPrefWidth(width);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(bombCounter, spacer, timerText);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> {
            secondsElapsed++;
            timerText.setText("Time: " + secondsElapsed);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        firstClick = true;
        openedTiles = 0;

        for(int y = 0; y < Y_TILES; y++){
            for(int x = 0; x < X_TILES; x++){
                Tile tile = new Tile(x, y, false);
                grid[x][y] = tile;
                root.getChildren().add(tile);
            }
        }

        layout.getChildren().addAll(header, root);

        return layout;
    }

    private List<Tile> getNeighbours(Tile tile) {
        List<Tile> neighbours = new ArrayList<>();

        // Surrounding tiles
        // 123
        // 4X5
        // 678

        int[] points = new int[] {
                -1, 1,  // 1
                0, 1,   // 2
                1, 1,   // 3
                -1, 0,  // 4
                1, 0,   // 5
                -1, -1, // 6
                0, -1,  // 7
                1, -1   // 8
        };

        for(int i = 0; i < points.length; i++){
            int dx = points[i];
            int dy = points[++i];

            int newX = tile.x + dx;
            int newY = tile.y + dy;

            if (newX >= 0 && newX < X_TILES && newY >= 0 && newY < Y_TILES){
                neighbours.add(grid[newX][newY]);
            }
        }
        return neighbours;
    }

    private void placeBombs(Tile firstClicked){
        int bombsPlaced = 0;

        while(bombsPlaced < totalBombs){
            int x = (int) (Math.random() * X_TILES);
            int y = (int) (Math.random() * Y_TILES);
            Tile tile = grid[x][y];

            // Ensure the bomb is not on the first clicked tile or its neighbours
            if(tile != firstClicked && !tile.hasBomb && !getNeighbours(firstClicked).contains(tile)){
                tile.hasBomb = true;
                bombsPlaced++;
            }
        }

        //Calculate bomb counts for all tiles after placing bombs
        for(int y = 0; y < Y_TILES; y++){
            for(int x = 0; x < X_TILES; x++){
                Tile tile = grid[x][y];

                if(tile.hasBomb) continue;

                long bombs = getNeighbours(tile).stream().filter(t -> t.hasBomb).count();
                if(bombs > 0){
                    tile.text.setText(String.valueOf(bombs));
                }
            }
        }
    }

    private class Tile extends StackPane {
        private final int x, y;
        private boolean hasBomb;
        private boolean isOpen = false;
        private boolean isMarked = false;

        private final Rectangle border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);
        private final Text text = new Text();
        private final Text markText = new Text("!");

        public Tile(int x, int y, boolean hasBomb) {
            this.x = x;
            this.y = y;
            this.hasBomb = hasBomb;

            border.setStroke(Color.LIGHTGRAY);

            text.setFont(Font.font(18));
            text.setText(hasBomb ? "X" : "");
            text.setVisible(false);

            markText.setFont(Font.font(18));
            markText.setFill(Color.RED);
            markText.setVisible(false);

            getChildren().addAll(border, text, markText);

            setTranslateX(x * TILE_SIZE);
            setTranslateY(y * TILE_SIZE);

            setOnMouseClicked(event -> {
                if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                    open();
                } else if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                    toggleMark();
                }
            });
        }

        private void toggleMark(){
            if(isOpen) return;

            isMarked = !isMarked;
            markText.setVisible(isMarked);

            if(isMarked){
               remainingBombs--;
            } else {
                remainingBombs++;
            }
            bombCounter.setText("Bombs: " + remainingBombs);

            border.setStroke(isMarked ? Color.ORANGERED : Color.LIGHTGRAY);
        }

        public void open() {
            if (isOpen || isMarked) return;

            //Handle first click to ensure it is an empty tile
            if (firstClick) {
                firstClick = false;
                placeBombs(this);
                timeline.play();
            }

            isOpen = true;
            openedTiles++;
            text.setVisible(true);
            border.setFill(null);

            if (hasBomb) {
                timeline.stop();
                showEndGameMessage("Game Over! You hit a bomb.", false);
                return;
            }

            if (openedTiles == X_TILES * Y_TILES - totalBombs) {
                timeline.stop();
                showEndGameMessage("Congratulations! You won!", true);
                return;
            }

            if (text.getText().isEmpty()) {
                getNeighbours(this).forEach(tile -> {
                    if(!tile.isOpen && !tile.hasBomb){
                        tile.open();
                    }
                });
            }
        }
    }

    private void showEndGameMessage(String message, boolean isWin) {
        // Stop the timer if it's running
        if (timeline != null) {
            timeline.stop();
        }

        // Create a VBox to hold the message and play again button
        VBox endGameLayout = new VBox(20); // 20 is the spacing between elements
        endGameLayout.setPrefSize(width, height);
        endGameLayout.setAlignment(Pos.CENTER); // Center elements in VBox
        endGameLayout.setStyle("-fx-background-color: white;"); // Set background color

        // Message text
        String fullMessage = message;
        if (isWin) {
            fullMessage += "\nTime Taken: " + secondsElapsed + " seconds";
        }

        Text endMessage = new Text(fullMessage);
        endMessage.setFont(Font.font(24));
        endMessage.setFill(isWin ? Color.GREEN : Color.RED);

        // Play Again button
        Button playAgainButton = new Button("Play Again");
        playAgainButton.setOnAction(_ -> {
            // Reset the game
            scene.setRoot(createContent());
        });

        // Add the message and button to the VBox
        endGameLayout.getChildren().addAll(endMessage, playAgainButton);

        // Set the VBox as the new root of the scene
        scene.setRoot(endGameLayout);
    }


    @Override
    public void start(Stage stage){
        scene = new Scene(createContent());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}