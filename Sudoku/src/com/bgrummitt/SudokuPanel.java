package com.bgrummitt;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

class SudokuPanel extends JPanel {

    public static final Border defaultBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
    public static final Border solvedBorder = BorderFactory.createLineBorder(Color.GREEN, 1);
    public static final Border solvingBorder = BorderFactory.createLineBorder(Color.RED, 1);
    public static final Color defaultBackground = Color.WHITE;
    public static final Color solvedBackground = Color.GREEN;
    public static final Color solvingBackground = Color.RED;

    private JTextField[][] grid;
    private JPanel[][] subSquarePanels;

    private Board sudoku;
    private JButton solveButton;
    private JButton resetButton;
    private JPanel gridPanel;
    private JPanel buttonPanel;

    public SudokuPanel(Board sudokuBoard){
        grid = new JTextField[9][9];
        sudoku = sudokuBoard;

        // For every row in the board
        for(int i = 0; i < 9; i++) {
            int[] rowNumbs = sudoku.getRow(i);
            // For every column in the board
            for(int j = 0; j < 9; j++) {
                // Create new text field and assign it to position in grid and centre text
                grid[i][j] = new JTextField();
                grid[i][j].setHorizontalAlignment(JLabel.CENTER);
                grid[i][j].setBorder(defaultBorder);
                grid[i][j].setPreferredSize(new Dimension(50, 50));
                if(rowNumbs[j] != 0){
                    grid[i][j].setText(Integer.toString(rowNumbs[j]));
                    grid[i][j].setEditable(false);
                }
            }
        }

        // Create a panel for the full 9x9 sudoku board and buttons below
        gridPanel = new JPanel();
        buttonPanel = new JPanel();

        // Set the grid to be 3x3 for each mini box
        gridPanel.setLayout(new GridLayout(3, 3));

        subSquarePanels = new JPanel[3][3];

        // For every sub square (contains the 3x3 squares with numbers)
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                subSquarePanels[i][j] = new JPanel();
                subSquarePanels[i][j].setLayout(new GridLayout(3, 3));
                subSquarePanels[i][j].setBorder(defaultBorder);
                gridPanel.add(subSquarePanels[i][j]);
            }
        }

        // Assign correct JPanel to sub square position
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                subSquarePanels[i / 3][j / 3].add(grid[i][j]);
            }
        }

        // Set the outline border and add buttons
        gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        resetButton = new JButton("Clear");
        solveButton = new JButton("Solve");

        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(resetButton, BorderLayout.WEST);
        buttonPanel.add(solveButton, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(gridPanel,   BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        resetButton.addActionListener((ActionEvent e) -> {
            solveButton.setEnabled(true);
            clearAll();
        });

        solveButton.addActionListener((ActionEvent e) -> {
            SudokuSolver sudokuSolver = new SudokuSolver(sudokuBoard, this);
            // Create new runnable to run asynchronously
            Runnable solvePuzzle = () -> {
                if(sudokuSolver.solveBoard()){
                    resetButton.setEnabled(true);
                }
            };
            // Start thread
            Thread thread = new Thread(solvePuzzle);
            thread.start();
            resetButton.setEnabled(false);
            solveButton.setEnabled(false);
        });
    }

    /**
     * Function to reset the board to it's original state
     */
    void clearAll(){
        sudoku.resetBoard();
        for(int i = 0; i < 9; i++) {
            int[] rowNumbs = sudoku.getRow(i);
            for(int j = 0; j < 9; j++) {
                if(rowNumbs[j] == 0){
                    grid[i][j].setText("");
                    grid[i][j].setBackground(defaultBackground);
                    grid[i][j].setEditable(true);
                }
            }
        }
    }

    public JTextField getPanel(int row, int col){
        return grid[row][col];
    }

}