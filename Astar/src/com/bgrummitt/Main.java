package com.bgrummitt;

public class Main {

    private static final int[][] maze = {{0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                                         {0, 1, 1, 1, 1, 1, 0, 0, 0, 0},
                                         {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                                         {0, 1, 0, 0, 1, 1, 1, 0, 0, 0},
                                         {0, 1, 0, 0, 1, 0, 0, 0, 1, 1},
                                         {0, 1, 0, 0, 1, 0, 1, 1, 1, 0},
                                         {0, 1, 1, 0, 1, 0, 0, 0, 0, 0},
                                         {0, 0, 1, 0, 1, 1, 1, 1, 1, 0},
                                         {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                                         {0, 1, 1, 0, 1, 0, 0, 0, 0, 0}};

    public static void main(String[] args) {

        AStar test = new AStar(maze);

        int[][] mazeSolved = test.solveMaze(new int[]{0, 0}, new int[]{9, 9});

        // Print the solved maze
        for(int[] row : mazeSolved){
            for(int node : row){
                String print;
                switch(node){
                    case Node.BLOCKED_NODE_ELEMENT:
                        print = "\u2588\u2588\u2588";
                        break;
                    case Node.PATH_NODE_ELEMENT:
                        print = " x ";
                        break;
                    default:
                        print = "   ";
                }
                System.out.print(print);
            }
            System.out.println("");
        }

    }
}
