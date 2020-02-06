package com.bgrummitt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GUI extends JFrame{

    private GridPanel gridPanel;
    private JButton solveButton;
    private JButton clearButton;

    public static void main(String[] args) {

        JFrame frame = new GUI("A* Visualiser");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public GUI(String Name){
        super(Name);

        // Layout Buttons In JPanel
        JPanel buttonLayout = new JPanel();
        buttonLayout.setLayout(new BorderLayout());
        solveButton = new JButton("Solve");
        buttonLayout.add(solveButton, BorderLayout.EAST);
        clearButton = new JButton("Clear");
        buttonLayout.add(clearButton, BorderLayout.WEST);

        // Create new 10x10 grid of maze
        gridPanel = new GridPanel(10, 10);

        // Add buttons and maze to JFrame
        setLayout(new BorderLayout());
        add(gridPanel, BorderLayout.NORTH);
        add(buttonLayout, BorderLayout.SOUTH);

        // Set solve button to solve maze display it and then disable solve button until cleared
        solveButton.addActionListener((ActionEvent e) -> {
            AStar solver = new AStar(gridPanel.getMaze());
            int[][] solvedMaze = solver.solveMaze(new int[]{0, 0}, new int[]{9, 9});
            gridPanel.setSolvedMaze(solvedMaze);
            solveButton.setEnabled(false);
        });

        // Set clear button to reset the maze to original state and allow solve button
        clearButton.addActionListener((ActionEvent e) -> {
            gridPanel.clearMaze();
            solveButton.setEnabled(true);
        });
    }

}
