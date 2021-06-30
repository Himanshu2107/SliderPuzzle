/**********************************************************************************************************************
 * Compilation: javac Solver.java
 * Execution: java Solver filename.txt
 * Dependencies: Board.java
 *
 * This program creates a solution to a sliding board puzzle specified on the command line using A* Search with priority
 * function equaling manhattan distance + number of moves at the point. Contains methods that return the minimum number
 * moves required to solve the board with exact moves and tells if the board is solvable.
 **********************************************************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Deque;
import java.util.ArrayDeque;

public class Solver {

    // is the board solvable?
    private final boolean solvable;
    // number of moves to solve the board
    private final int moves;
    // SearchNode corresponding to final solved node
    private final SearchNode answer;

    // implements SearchNode class that goes into PQ
    private class SearchNode implements Comparable<SearchNode> {
        // number of moves made to reach the node
        int moves;
        // the board corresponding to the SearchNode
        Board board;
        // previous SearchNode
        SearchNode previous;

        int manhattan;

        // return this board
        Board getBoard() {
            return board;
        }

        SearchNode(Board a, SearchNode previous) {
            board = a;

            if (previous != null) this.moves = previous.moves + 1;
            else this.moves = 0;
            this.previous = previous;
            this.manhattan = board.manhattan();
        }

        // the priority function
        public int compareTo(SearchNode o) {
            return Integer.compare(this.moves + this.manhattan, o.moves + o.manhattan);
        }

    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) throw new IllegalArgumentException("null argument");

        //temp variable for moves
        int moves1 = -1;

        // Min Priority Queue to contain SearchNodes
        PriorityQueue<SearchNode> pq = new PriorityQueue<>();

        // insert first search node
        pq.offer(new SearchNode(initial, null));

        // PQ to solve for a twin of initial board to check if board is unsolvable
        PriorityQueue<SearchNode> twin = new PriorityQueue<>();
        twin.offer(new SearchNode(initial.twin(), null));

        while (true) {
            SearchNode deque = pq.poll();
            Board minP = deque.getBoard();
            // check if Goal is reached, if not add all neighbors with one extra move otherwise solved
            if (minP.isGoal()) {
                answer = deque;
                solvable = true;
                break;
            } else {
                for (Board i : minP.neighbors()) {
                    // check if this board is already present
                    if (deque.previous != null) {
                        if (!i.equals(deque.previous.board)) pq.offer(new SearchNode(i, deque));
                    } else {
                        pq.offer(new SearchNode(i, deque));
                    }
                }
            }

            // repeat above process for twin board
            SearchNode twinDeque = twin.poll();
            Board minTwin = twinDeque.getBoard();
            if (minTwin.isGoal()) {
                // if found the board is unsolvable
                answer = null;
                solvable = false;
                break;
            } else {
                for (Board i : minTwin.neighbors()) {
                    if (twinDeque.previous != null) {
                        if (!i.equals(twinDeque.previous.board)) twin.offer(new SearchNode(i, twinDeque));
                    } else {
                        twin.offer(new SearchNode(i, twinDeque));
                    }
                }
            }
        }

        // we have found moves
        if (solvable) moves1 = answer.moves;
        moves = moves1;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return this.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {

        // not solvable
        if (!solvable) return null;

        // create an empty stack to hold all boards in order
        Deque<Board> iterate = new ArrayDeque<>();
        // create a pointer to answer SearchNode
        SearchNode point = answer;
        // repeat until first node is reached
        while (point != null) {
            iterate.push(point.getBoard());
            point = point.previous;
        }
        return iterate;
    }


    // test client
    public static void main(String[] args) throws FileNotFoundException {

        String directory = System.getProperty("user.dir");
        File file = new File(directory + File.separator + args[0]);

        // create initial board from file
        Scanner in = new Scanner(file);
        int n = in.nextInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.nextInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            System.out.println("No solution possible");
        } else {
            System.out.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                System.out.println(board);
            }
        }
    }
}
