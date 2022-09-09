package com.monfort;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

class Location{
    int row, col;

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public String toString(){
        String s = "Row: " + row + " Col: " + col;
        return s;
    }
}

public class GameBoard {
    private int[][] board;
    private boolean clicked = false;
    private final float height;
    private final float width;
    private Texture emptyTile;
    private Texture questionTile;
    private Texture bombTile;
    private Texture emptyFloor;
    private Texture bomb;

    private Texture oneTile, twoTile, threeTile, fourTile, fiveTile, sixTile, sevenTile, eightTile;

    private static final int BOMB = 9, EMPTY_TILE = 10, FLAGGED_TILE = 20, QUESTION_TILE = 30;

    private boolean isValidLoc(Location loc){
        return (loc.row >= 0 && loc.row < board.length) && (loc.col >= 0 && loc.col < board[0].length);
    }

    private ArrayList<Location> getNeigh(Location loc){
        ArrayList<Location> neigh = new ArrayList<>();
        for(int i = loc.row - 1; i <= loc.row+1; i++){
            for(int j = loc.col-1; j <= loc.col+1;j++){
                if(isValidLoc(new Location(i, j))){
                    if(i != loc.row || loc.col != j) neigh.add(new Location(i, j));
                }
            }
        }
        return neigh;
    }

    public void handleClick(int x, int y){
        int row = (y-10)/25;
        int col = (x-10)/25;
        Location loc = new Location(row, col);

        if(isValidLoc(loc)){
            board[row][col] %= 10;
        }
        if(!clicked){
            clicked = true;
            placeBomb(loc);
            getNeigh(loc);
        }
    }

    public GameBoard(float height, float width){
        this.width = width;
        this.height = height;
        board = new int[16][30];
        initEmptyBoard();

        emptyTile = new Texture("emptyTile.jpeg");
        bomb = new Texture("bomb.jpg");
        oneTile = new Texture("oneTile.jpg");
        twoTile = new Texture("twoTile.jpg");
        threeTile = new Texture("threeTile.jpg");
        fourTile = new Texture("fourTile.jpg");
        fiveTile = new Texture("fiveTile.jpg");
        sixTile = new Texture("sixTile.jpg");
        sevenTile = new Texture("sevenTile.jpg");
        eightTile = new Texture("eightTile.jpg");
    }

    private void initEmptyBoard(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = 10;
            }
        }
    }



    private void placeBomb(Location loc){
        int bombCount = 0;
        while(bombCount < 99){
            int randRow = (int)(Math.random() * board.length);
            int randCol = (int)(Math.random() * board[0].length);

            if(randRow != loc.row && randCol != loc.col){
                if(board[randRow][randCol] == EMPTY_TILE){
                    board[randRow][randCol] = BOMB + 10;
                    bombCount++;
                }
            }
        }

        System.out.println("Bombs Placed: " + bombCount);
    }

    public void draw(SpriteBatch spriteBatch){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] >= EMPTY_TILE && board[i][j] < FLAGGED_TILE){
                    spriteBatch.draw(emptyTile, 10 + (j * 25), height - 35 - (i * 25));
                } else if (board[i][j] == BOMB) {
                    spriteBatch.draw(bomb, 10 + (j * 25), height - 35 - (i * 25));
                }
            }
        }
    }
}
