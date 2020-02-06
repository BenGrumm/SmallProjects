package com.bgrummitt;

import java.util.Arrays;

public class Board {

    private int[][] board;

    private int[][] usedBoard;

    public Board(){
        board = new int[][]{{7, 8, 0, 4, 0, 0, 1, 2, 0},
                            {6, 0, 0, 0, 7, 5, 0, 0, 9},
                            {0, 0, 0, 6, 0, 1, 0, 7, 8},
                            {0, 0, 7, 0, 4, 0, 2, 6, 0},
                            {0, 0, 1, 0, 5, 0, 9, 3, 0},
                            {9, 0, 4, 0, 6, 0, 0, 0, 5},
                            {0, 7, 0, 3, 0, 0, 0, 1, 2},
                            {1, 2, 0, 0, 0, 7, 4, 0, 0},
                            {0, 4, 9, 2, 0, 6, 0, 0, 7}};

        usedBoard = cloneBoard(board);
    }

    public Board(int[][] unsolvedBoard){
        board = unsolvedBoard;
        usedBoard = cloneBoard(board);
    }

    /**
     * Function to print board in a formatted way
     */
    public void printBoard(){

        System.out.println("-------------------------------------");
        for(int[] row : usedBoard){

            System.out.print("|");

            for(int column : row){
                System.out.printf(" %d |", column);
            }

            System.out.print("\n");
            System.out.println("-------------------------------------");

        }

    }

    /**
     * Function to retrieve value in cell
     * @param row y of cell
     * @param column x of cell
     * @return value in cell
     */
    public int getCell(int row, int column){
        return usedBoard[row][column];
    }

    /**
     * Function to update the value in the cell
     * @param row y of cell
     * @param column x of cell
     * @param value value to give to cell
     */
    public void updateCell(int row, int column, int value){
        usedBoard[row][column] = value;
    }

    /**
     * Function to get the row
     * @param rowNo row to retrieve
     * @return an int array of numbers in row
     */
    public int[] getRow(int rowNo){
        return usedBoard[rowNo];
    }

    /**
     * Function to get the numbers in a given column
     * @param colNo column number of column to retrieve
     * @return int array of all numbers in column
     */
    public int[] getColumn(int colNo){
        int[] col = new int[9];

        for(int i = 0; i < 9; i++){
            col[i] = usedBoard[i][colNo];
        }

        return col;
    }

    /**
     * Function to get an array of all numbers in a box
     * @param row row of cell in box
     * @param col column of cell in box
     * @return an int array of all numbers in the box
     */
    public int[] getBox(int row, int col){
        int[] box = new int[9];

        // Get coordinates of box (in 3x3) from 0-2
        row = (int) Math.floor((float)row / 3);
        col = (int) Math.floor((float)col / 3);

        int boxArrPos = 0;
        // For every row in the box
        for(int i = row * 3; i < (row + 1) * 3; i++){
            // For every column in the box
            for(int j = col * 3; j < (col + 1) * 3; j++){
                // Add the integer at the box position and then increase position in array
                box[boxArrPos] = usedBoard[i][j];
                boxArrPos++;
            }
        }

        return box;
    }

    public void resetBoard(){
        usedBoard = cloneBoard(board);
    }

    /**
     * Function to clone a 2d array
     * @param board array to clone
     * @return return array as separate object
     */
    public static int[][] cloneBoard(int[][] board){
        int[][] clonedBoard = new int[board.length][board[0].length];

        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                clonedBoard[i][j] = board[i][j];
            }
        }

        return clonedBoard;
    }

}
