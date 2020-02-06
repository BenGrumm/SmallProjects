package com.bgrummitt;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GridPanel extends JPanel {

    private static final Border defaultBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

    private StepLabel[][] jLabelGrid;
    private int xSize;
    private int ySize;

    public GridPanel(int numberOfSquaresX, int numberOfSquaresY){

        jLabelGrid = new StepLabel[numberOfSquaresY][numberOfSquaresX];
        xSize = numberOfSquaresX;
        ySize = numberOfSquaresY;

        this.setLayout(new GridLayout(numberOfSquaresY, numberOfSquaresX));

        // For every label in the grid set default attributes and add to array
        for(int i = 0; i < numberOfSquaresY; i++){
            for(int j = 0; j < numberOfSquaresX; j++){
                jLabelGrid[i][j] = new StepLabel(j, i);
                jLabelGrid[i][j].setBorder(defaultBorder);
                jLabelGrid[i][j].setPreferredSize(new Dimension(50, 50));
                this.add(jLabelGrid[i][j]);
            }
        }

    }

    /**
     * Function to retrieve a valid maze from the JLabels
     * @return a 2D integer array of the maze
     */
    public int[][] getMaze(){
        int[][] maze = new int[jLabelGrid.length][jLabelGrid[0].length];

        int elementType;

        for(int i = 0; i < ySize; i++){
            for(int j = 0; j < xSize; j++){

                if(jLabelGrid[i][j].isBarrier()){
                    elementType = Node.BLOCKED_NODE_ELEMENT;
                }else{
                    elementType = Node.OPEN_NODE_ELEMENT;
                }

                maze[i][j] = elementType;

            }
        }

        return maze;
    }

    public void clearMaze(){
        for(StepLabel[] row : jLabelGrid){
            for(StepLabel label : row){
                label.reset();
            }
        }
    }

    public void setSolvedMaze(int[][] solvedMaze){
        for(int i = 0; i < ySize; i++){
            for(int j = 0; j < xSize; j++){
                jLabelGrid[i][j].setState(solvedMaze[i][j], false);
            }
        }
    }

}
