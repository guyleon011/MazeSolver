//NAME: Guy Leonard KAMGA FOTSO WAFO
//STUDENT ID: 240ADM005

import java.io.*;
import java.util.*;

class MazeSolver {
    // Node class representing each cell in the maze
    static class Node implements Comparable<Node> {
        int x, y, coins, steps;

        // Constructor to initialize a node
        Node(int x, int y, int coins, int steps) {
            this.x = x;
            this.y = y;
            this.coins = coins;
            this.steps = steps;
        }

        // Compare nodes based on the number of steps (for priority queue)
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.steps, other.steps);
        }
    }

    // Possible movement directions including diagonals
    static int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    // Method to read the maze from a file and return it as a 2D character array
    public static char[][] readMaze(String filePath) throws IOException {
        List<char[]> maze = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            maze.add(line.toCharArray());
        }
        reader.close();
        return maze.toArray(new char[0][]);
    }

    // Method to find the start ('S') and goal ('G') positions in the maze
    public static int[] findStartAndGoal(char[][] maze) {
        int[] startGoal = new int[4]; // startX, startY, goalX, goalY
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 'S') {
                    startGoal[0] = i;
                    startGoal[1] = j;
                } else if (maze[i][j] == 'G') {
                    startGoal[2] = i;
                    startGoal[3] = j;
                }
            }
        }
        return startGoal;
    }

    // Breadth-first search (BFS) to find the shortest path and collect coins
    public static int[] bfs(char[][] maze, int startX, int startY, int goalX, int goalY) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(startX, startY, 0, 0)); // Initialize queue with the start node
        boolean[][] visited = new boolean[maze.length][maze[0].length];
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            Node current = queue.poll(); // Get the node with the fewest steps

            // Check if we have reached the goal
            if (current.x == goalX && current.y == goalY) {
                return new int[]{current.coins, current.steps};
            }

            // Explore all possible movements
            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                // Check if the new position is valid and not yet visited
                if (newX >= 0 && newX < maze.length && newY >= 0 && newY < maze[0].length && !visited[newX][newY] && maze[newX][newY] != 'X') {
                    int newCoins = Character.isDigit(maze[newX][newY]) ? Character.getNumericValue(maze[newX][newY]) : 0;
                    queue.add(new Node(newX, newY, current.coins + newCoins, current.steps + 1));
                    visited[newX][newY] = true;
                }
            }
        }
        return new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE}; // If no path is found
    }

    // Method to solve the maze by reading it, finding the start/goal, and performing BFS
    public static void solveMaze(String filePath) throws IOException {
        char[][] maze = readMaze(filePath); // Read the maze from the file
        int[] startGoal = findStartAndGoal(maze); // Find start and goal positions
        int[] result = bfs(maze, startGoal[0], startGoal[1], startGoal[2], startGoal[3]); // Perform BFS to solve the maze
        System.out.println("Maze: " + filePath);
        System.out.println("Coins collected: " + result[0] + ", Steps: " + result[1]);
    }

    // Main method to solve all the provided mazes
    public static void main(String[] args) {
        // List of maze files to solve
        String[] mazeFiles = {"data/maze_11x11.txt", "data/maze_31x31.txt", "data/maze_101x101.txt"};
        try {
            for (String filePath : mazeFiles) {
                solveMaze(filePath); // Solve each maze file
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle any IO exceptions
        }
    }
}
