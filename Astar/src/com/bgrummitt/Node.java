package com.bgrummitt;

import java.util.Comparator;

public class Node {

    public static final int OPEN = 14;
    public static final int CLOSED = 33;
    public static final int BLOCKED = 37;
    public static final int UNASSIGNED = -1;
    public static final int OPEN_NODE_ELEMENT = 0;
    public static final int BLOCKED_NODE_ELEMENT = 1;
    public static final int PATH_NODE_ELEMENT = 5;

    private int[] coordinates;
    private Node parentNode;
    private int type = UNASSIGNED;
    // F = total predicted distance to goal
    private int f;
    // Shortest found distance from home to current node
    private int g;
    // Estimated distance from current node to finish
    private int h;

    public Node(int[] position, int[] end){
        coordinates = position;
        calculateH(end);
    }

    public Node(int[] position, int[] end, int type){
        coordinates = position;
        calculateH(end);
        this.type = type;
    }

    public void recalculateF(){
        f = g + h;
    }

    private void calculateH(int[] endPos){
        // Use pythagoras to get estimate of distance to end
        int xChange =  coordinates[0] - endPos[0];
        int yChange = coordinates[1] - endPos[1];
        h = (xChange * xChange) + (yChange * yChange);
    }

    public int getF(){
        return f;
    }

    public void setParent(Node node){
        parentNode = node;
    }

    public void updateType(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

    public int[] getPosition(){
        return coordinates;
    }

    public void setG(int g){
        this.g = g;
    }

    public int getG(){
        return g;
    }

    public int[] getCoordinates(){
        return coordinates;
    }

    public Node getParent(){
        return parentNode;
    }

}

class SortNodesByG implements Comparator<Node>{
    @Override
    public int compare(Node node, Node t1) {
        return  node.getF() - t1.getF();
    }
}