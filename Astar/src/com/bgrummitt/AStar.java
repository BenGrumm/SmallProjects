package com.bgrummitt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AStar {

    private Node[][] nodeMaze;
    private int[][] baseMaze;
    private List<Node> openNodes;
    private List<Node> closedNodes;

    public AStar(int[][] basicMaze){
        this.nodeMaze = new Node[basicMaze.length][basicMaze[0].length];
        this.baseMaze = basicMaze;
    }

    /**
     * Function to solve a maze give a start and end point using A* algorithm
     * @param startPoint coordinates of the start point in an array of form {x, y}
     * @param endPoint coordinates of the end point in an array of form {x, y}
     * @return an array of the original maze with the route shown with 5 TODO update how route is displayed
     */
    public int[][] solveMaze(int[] startPoint, int[] endPoint){
        int[][] path = new int[nodeMaze.length][nodeMaze[0].length];
        openNodes = new ArrayList<>();
        closedNodes = new ArrayList<>();

        // Initialise end node
        Node endNode = new Node(endPoint, endPoint);
        nodeMaze[endPoint[1]][endPoint[0]] = endNode;

        // Initialise start node
        openNodes.add(new Node(startPoint, endPoint, Node.OPEN));
        nodeMaze[startPoint[1]][startPoint[0]] = openNodes.get(0);

        // While the end node has not been moved to the closed list and the open list is not empty
        while(endNode.getType() != Node.CLOSED && openNodes.size() != 0){
            // Move the node with the smallest f value (list is sorted) to the closed list and then test its surrounding
            // nodes
            closedNodes.add(openNodes.get(0));
            openNodes.get(0).updateType(Node.CLOSED);
            openNodes.remove(0);
            testSurroundingNodes(closedNodes.get(closedNodes.size() - 1), endPoint);
        }

        if(endNode.getParent() != null){
            showRoute(endNode, startPoint, baseMaze);
        }else{
            System.out.println("BROKEN");
        }

        return baseMaze;
    }

    /**
     * Function to backtrack from the end node through all the parent nodes and set the route in the given map
     * @param node Node of the element to set the coordinates of
     * @param base the start point to stop at
     * @param map to draw route on
     */
    public void showRoute(Node node, int[] base, int[][] map){
        if(node.getCoordinates() == base){
            map[base[1]][base[0]] = Node.PATH_NODE_ELEMENT;
        }else{
            int[] coords = node.getCoordinates();
            showRoute(node.getParent(), base, map);
            map[coords[1]][coords[0]] = Node.PATH_NODE_ELEMENT;
        }
    }

    /**
     * Function to go through the children of the given node and update F/G of children and move to open list
     * @param currentNode parent node of which to find surrounding child nodes
     * @param endPoint coordinates of the destination
     */
    private void testSurroundingNodes(Node currentNode, int[] endPoint){
        // Get all the valid children of the given node
        Node[] children = getChildren(currentNode, endPoint);

        // For every valid child
        for(Node child : children){

            // If the node is closed or blocked do nothing
            if(child.getType() != Node.CLOSED){

                // If the is open and its G value is smaller than this route continue to next child
                if(child.getType() == Node.OPEN && child.getG() < currentNode.getG() + 1){
                    continue;
                }

                // Else update the nodes info
                child.setParent(currentNode);
                child.setG(currentNode.getG() + 1);
                child.recalculateF();

                // If the node is not already in the open list add it and set type
                if(child.getType() != Node.OPEN){
                    child.updateType(Node.OPEN);
                    openNodes.add(child);
                }

                // Resort the list
                openNodes.sort(new SortNodesByG());

            }

        }
    }


    /**
     * Function to retrieve all the valid children of a given node
     * @param mainNode node to find children of
     * @param endPoint coordinates of the destination
     * @return array of all children nodes
     */
    public Node[] getChildren(Node mainNode, int[] endPoint){
        List<Node> children = new ArrayList<>();
        int[] mainCoordinates = mainNode.getCoordinates();
        // The coordinates for every surrounding square
        int[][] surroundings = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        int[] newCoordinates = new int[2];

        // For every surrounding position
        for(int[] position : surroundings){
            // Get the coordinates
            newCoordinates[0] = mainCoordinates[0] + position[0];
            newCoordinates[1] = mainCoordinates[1] + position[1];

            // If the within bounds add to the children if node not already created create it
            if(withinBounds(newCoordinates[0], newCoordinates[1]) && baseMaze[newCoordinates[1]][newCoordinates[0]] != Node.BLOCKED_NODE_ELEMENT){

                if(nodeMaze[newCoordinates[1]][newCoordinates[0]] == null){
                    initialiseNode(newCoordinates[0], newCoordinates[1], endPoint, mainNode);
                }

                children.add(nodeMaze[newCoordinates[1]][newCoordinates[0]]);
            }

        }

        // Return an array not list.
        return Arrays.copyOf(children.toArray(), children.size(), Node[].class);
    }

    /**
     * Function to initialise a node given some info
     * @param x x coordinate as int
     * @param y y coordinate as int
     * @param endPoint coordinates of the end point int array of form {x, y} for calculating h
     * @param parent parent node
     */
    public void initialiseNode(int x, int y, int[] endPoint, Node parent){
        Node nodeToAdd = new Node(new int[]{x,y}, endPoint);
        if(baseMaze[y][x] == Node.BLOCKED_NODE_ELEMENT){
            nodeToAdd.updateType(Node.BLOCKED);
        }
        nodeMaze[y][x] = nodeToAdd;
        nodeMaze[y][x].setParent(parent);
    }

    /**
     * Function to check whether to coordinates are within the bounds of the maze
     * @param x x coordinate
     * @param y y coordinate
     * @return true / false depending on coordinates validity
     */
    public boolean withinBounds(int x, int y){
        return x >= 0 && y >= 0 && x < baseMaze[0].length && y < baseMaze.length;
    }

}
