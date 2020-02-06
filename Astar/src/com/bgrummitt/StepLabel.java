package com.bgrummitt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StepLabel extends JLabel implements MouseListener {

    private static final int PAINT_PRESS = 32;
    private static final int CLEAR_PRESS = 321;
    private static final Color DEFAULT_BACKGROUND = new Color(238, 238, 238);
    private static final Color SELECTED_BACKGROUND = Color.RED;
    private static final Color PATH_BACKGROUND = Color.BLACK;

    private static Boolean mousePressed = false;
    private static int pressType;

    private int xPosition;
    private int yPosition;
    private boolean isSelected = false;

    public StepLabel(int x, int y){
        super();
        xPosition = x;
        yPosition = y;
        this.setOpaque(true);
        this.addMouseListener(this);
    }

    // Set the static var mousePressed to true then set type of pressDepending on beginning square
    // If square is already a wall all squares moved to with mouse while button held will be reset to basic
    // If square is basic all squares moved to become walls
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        mousePressed = true;
        if(isSelected){
            deselectSquare();
            pressType = CLEAR_PRESS;
        }else{
            selectSquare();
            pressType = PAINT_PRESS;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if(mousePressed){
            if(pressType == PAINT_PRESS){
                selectSquare();
            }else if(pressType == CLEAR_PRESS){
                deselectSquare();
            }
        }
    }

    private void selectSquare(){
        this.setBackground(SELECTED_BACKGROUND);
        isSelected = true;
    }

    private void deselectSquare(){
        this.setBackground(DEFAULT_BACKGROUND);
        isSelected = false;
    }

    public boolean isBarrier(){
        return isSelected;
    }

    public void setState(int squareType, boolean editable){
        if(!editable){
            this.removeMouseListener(this);
        }
        switch(squareType){
            case Node.BLOCKED_NODE_ELEMENT:
                this.setBackground(SELECTED_BACKGROUND);
                break;
            case Node.PATH_NODE_ELEMENT:
                this.setBackground(PATH_BACKGROUND);
                break;
            default:
                this.setBackground(DEFAULT_BACKGROUND);
        }
    }

    public void reset(){
        isSelected = false;
        this.setBackground(DEFAULT_BACKGROUND);
        this.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) { }

    @Override
    public void mouseExited(MouseEvent mouseEvent) { }

}
