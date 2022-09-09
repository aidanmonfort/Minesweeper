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

    private Texture[] numTiles;

    private static final int BOMB = 9, EMPTY_TILE = 10, FLAGGED_TILE = 20, QUESTION_TILE = 30;

    private boolean isValidLoc(Location loc){
        return (loc.row >= 0 && loc.row < board.length) && (loc.col >= 0 && loc.col < board[0].length);
    }

    private ArrayList<Location> getNeigh(Location loc){
        ArrayList<Location> neigh = new ArrayList<>();
        for(int i = loc.row - 1; i <= loc.row+1; i++){
            for(int j = loc.col-1; j <= loc.col+1;j++){
                if(isValidLoc(new Location(i, j))){
                    if((i != loc.row || loc.col != j) && board[i][j] != 0) neigh.add(new Location(i, j));
                }
            }
        }
        return neigh;
    }

    private int countBombs(Location loc){
        ArrayList<Location> neighbors = getNeigh(loc);
        int count = 0;
        for(Location l : neighbors){
            if(board[l.row][l.col]%10 == BOMB){
                count++;
            }
        }
        return count;
    }

    private void reveal(Location loc){
        board[loc.row][loc.col] %= 10;
        if(board[loc.row][loc.col] < 9 && board[loc.row][loc.col] != 0){
            return;
        }
        else if(board[loc.row][loc.col] == 0){
            ArrayList<Location> neighs = getNeigh(loc);
            for (int i = 0; i < neighs.size(); i++) {
                reveal(neighs.get(i));
            }
        }
    }

    public void handleClick(int x, int y){
        int row = (y-10)/25;
        int col = (x-10)/25;
        Location loc = new Location(row, col);
        if(isValidLoc(loc)){
            if(!clicked){
                clicked = true;
                placeBomb(loc);
                generateNumbers();
            }
            if(board[loc.row][loc.col] > BOMB) {
                reveal(loc);
            }
        }
    }

    public GameBoard(float height, float width){
        this.width = width;
        this.height = height;
        board = new int[16][30];
        initEmptyBoard();

        emptyTile = new Texture("emptyTile.jpeg");
        bomb = new Texture("bomb.jpg");
        numTiles = new Texture[8];
        numTiles[0] = new Texture("oneTile.jpg");
        numTiles[1] = new Texture("twoTile.jpg");
        numTiles[2] = new Texture("threeTile.jpg");
        numTiles[3] = new Texture("fourTile.jpg");
        numTiles[4] = new Texture("fiveTile.jpg");
        numTiles[5] = new Texture("sixTile.jpg");
        numTiles[6] = new Texture("sevenTile.jpg");
        numTiles[7] = new Texture("eightTile.jpg");
    }

    private void initEmptyBoard(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = 10;
            }
        }
    }

    private void generateNumbers(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j]%10 != BOMB){
                    int numBombs = countBombs(new Location(i, j));
                    board[i][j] = numBombs + 10;
                }
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
                else if (board[i][j] > 0 && board[i][j] < 9){
                    spriteBatch.draw(numTiles[board[i][j]-1], 10 + (j * 25), height - 35 - (i * 25));
                }
            }
        }
    }
}
