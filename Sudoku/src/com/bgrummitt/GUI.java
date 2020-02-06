package com.bgrummitt;

import javax.swing.*;

public class GUI {

    private final static JFrame frame = new JFrame("Sudoku solver");

    public static void main(String[] args) {

        frame.getContentPane().add(new SudokuPanel(new Board()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
