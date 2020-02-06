package com.bgrummitt;

import javax.swing.*;

public class SudokuSolver{

	private Board sudoku;
	private SudokuPanel sudokuBoardUI;

	public SudokuSolver(Board board, SudokuPanel boardPanel){
		sudoku = board;
		sudokuBoardUI = boardPanel;
	}

	/**
	 * Function to solve the board passed to the object when created
	 * @return the solved board
	 */
	public boolean solveBoard(){
		// Initialise first position to solve at 0,0
		int[] pos = {0, 0};

		// If that position is not empty find the next position to solve
		if(sudoku.getCell(0, 0) != 0){
			pos = findNextPos(0, 0);
		}

		// Begin solving the puzzle with the first unknown cell

		return solveCell(pos[0], pos[1]);
	}

	/**
	 * Recursive function to solve an individual cell then move on to the next cell until full board solved
	 * @param row of cell solve
	 * @param col umn of cell to solve
	 * @return if the cell was successfully solved
	 */
	public boolean solveCell(int row, int col){

		// If the row is outside of the puzzle max then return true
		if(row > 8){
			return true;
		}

		// Get the number at position row, col and the find the next position to solve after current cell
		int number = sudoku.getCell(row, col);
		int[] nextPos = findNextPos(row, col);
		JTextField cellField = sudokuBoardUI.getPanel(row, col);

		// While the next cell is not successfully solved repeat trying to find a new valid number for current cell
		do {
			cellField.setBackground(SudokuPanel.solvingBackground);
			sudokuBoardUI.revalidate();
			// The number should be increased and the cell updated while the number in that cell is not valid
			do {
				number++;
				// If the number goes outside of max range reset cell to 0 and return false as solve has been
				// unsuccessful
				if(number == 10){
					sudoku.updateCell(row, col, 0);
					cellField.setText("0");
					cellField.setBackground(SudokuPanel.solvingBackground);
					sudokuBoardUI.revalidate();
					return false;
				}
				sudoku.updateCell(row, col, number);
				cellField.setText(Integer.toString(number));
				sudokuBoardUI.revalidate();
				try{
					Thread.sleep( 25);
				}catch (InterruptedException err){
					System.out.println(err);
				}
			} while (!checkIfValid(row, col));

			cellField.setBackground(SudokuPanel.solvedBackground);
			sudokuBoardUI.revalidate();

		}while(!solveCell(nextPos[0], nextPos[1]));

		return true;
	}

	/**
	 * Function to check if the cell is valid by checking row, column and box for duplicate numbers
	 * @param row of cell solve
	 * @param col umn of cell to solve
	 * @return if the cell is valid
	 */
	private boolean checkIfValid(int row, int col){
		return checkSection(sudoku.getRow(row)) && checkSection(sudoku.getColumn(col)) && checkSection(sudoku.getBox(row, col));
	}

	/**
	 * Function that checks if the numbers supplied are all different (excluding 0's)
	 * @param section an integer array of all the numbers in a section (row / column / box)
	 * @return true if the numbers supplied are all different else false
	 */
	public static boolean checkSection(int[] section){
		boolean[] numCheck = new boolean[]{false, false, false, false, false, false, false, false, false};

		boolean rowPass = true;

		// For every number in the section
		for(int i = 0; i < 9; i++){
			// If the number if not = 0 (the cell is not empty)
			if(section[i] != 0){
				// If the number has already been seen (the position in the array = true) set rowPass and exit for loop
				// Else set the position to true and continue checking the rest of the numbers
				if(numCheck[section[i] - 1]){
					rowPass = false;
					break;
				}else{
					numCheck[section[i] - 1] = true;
				}
			}
		}

		return rowPass;
	}

	/**
	 * Function to find the next empty position in the array
	 * @param row row of start position
	 * @param col column of start position
	 * @return integer array of x and y position on next empty cell
	 */
	private int[] findNextPos(int row, int col){
		// Set position to given values
		int[] position = {row, col};

		// Try to find the position but if the row overflows past 9 and produces and err set position manually to 9,0
		// which gets checked in the solveCell function to return true
		try {

			// Increase the position while the current position is not = 0 (empty cell)
			do {
				// If the column is the last in the row increase the row and reset column to 0
				// Else increase column position
				if (position[1] == 8) {
					position[0]++;
					position[1] = 0;
				} else {
					position[1]++;
				}
			} while (sudoku.getCell(position[0], position[1]) != 0);

		}catch (ArrayIndexOutOfBoundsException err){
			position[0] = 9;
			position[1] = 0;
		}

		return position;
	}

}
