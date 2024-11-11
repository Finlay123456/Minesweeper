package minesweeper.minesweeper;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperApplication extends Application {

    private static final int TILE_SIZE = 40;
    private static final int width = 800;
    private static final int height = 600;
    private static final int X_TILES = width/TILE_SIZE;
    private static final int Y_TILES = height/TILE_SIZE;

    private final Tile[][] grid = new Tile[X_TILES][Y_TILES];
    private Scene scene;
    private boolean firstClick = true;

    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(width, height);

        firstClick = true;

        for(int y = 0; y < Y_TILES; y++){
            for(int x = 0; x < X_TILES; x++){
                Tile tile = new Tile(x, y, false);
                grid[x][y] = tile;
                root.getChildren().add(tile);
            }
        }
        return root;
    }

    private List<Tile> getNeighbours(Tile tile) {
        List<Tile> neighbours = new ArrayList<>();

        // Surrounding tiles (tile is X):
        // abc
        // dXe
        // fgh

        int[] points = new int[] {
                -1, 1,  // a
                0, 1,   // b
                1, 1,   // c
                -1, 0,  // d
                1, 0,   // e
                -1, -1, // f
                0, -1,  // g
                1, -1   // h
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
        int totalBombs = (int) (X_TILES * Y_TILES * 0.2);

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

            border.setStroke(isMarked ? Color.ORANGERED : Color.LIGHTGRAY);
        }

        public void open() {
            if (isOpen || isMarked) return;

            //Handle first click to ensure it is an empty tile
            if (firstClick) {
                firstClick = false;
                placeBombs(this);
            }

            isOpen = true;
            text.setVisible(true);
            border.setFill(null);

            if (hasBomb) {
                System.out.println("Game Over.");
                scene.setRoot(createContent());
                return;
            }

            if (text.getText().isEmpty()) {
                getNeighbours(this).forEach(Tile::open);
            }
        }
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