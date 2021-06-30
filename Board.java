/***********************************************************************************************************************
 * Compilation: javac Board.java
 * Execution: java Board
 * Dependencies: None
 *
 * This program implements a board of the slider puzzle abstractly. It takes a 2-D int array describing the board as
 * argument to the constructor and then creates the data type. Used in Solver so we can solve these puzzles. 
 ******************************************************************************************************************** */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Deque;
import java.util.ArrayDeque;

public class Board {

    // 2D array to store the board instance
    private final int[][] board;
    // dimension of board
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        board = copy(tiles);
        n = tiles.length;
    }

    //Create a copy of a 2D array
    private int[][] copy(int[][] tiles) {
        // number of rows
        int rn = tiles.length;
        // number of columns
        int cn = tiles[1].length;
        int[][] arr = new int[rn][];
        for (int i = 0; i < rn; i++) {
            arr[i] = Arrays.copyOf(tiles[i], cn);
        }
        return arr;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        // add elements in proper format
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // the dimension of the board
    public int dimension() {
        return n;
    }

    /*
     * Uses a modified index i which assumes the array to be 1-D only. Returns board value at that index.
     */
    private int boardValueAt(int i) {
        int a = i / n;
        int b = i % n;
        return board[a][b];
    }

    // number of tiles out of place
    public int hamming() {
        int hamDist = 0;
        // check every index (except last one) if it contains its goal number and increase hamDist by 1 if not so
        for (int i = 0; i < n * n - 1; i++) {
            if (boardValueAt(i) != i + 1) hamDist++;
        }
        return hamDist;
    }

    // The sum of manhattan distances
    public int manhattan() {
        int manDist = 0;
        // add all manhattan distances to result
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // for all blocks except blank one
                if (board[i][j] != 0) {
                    int x = (board[i][j] - 1) / n;
                    int y = (board[i][j] - 1) % n;
                    manDist += Math.abs(i - x) + Math.abs(j - y);
                }
            }
        }
        return manDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        // check all positions
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // do not check last block
                if (i != n - 1 || j != n - 1)
                    // return false if not expected number (computed from function)
                    if (n * i + j + 1 != board[i][j]) return false;
            }
        }
        // all are checked earlier
        return true;
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;
        if (this.n != that.n) return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) if (board[i][j] != that.board[i][j]) return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        // create a stack to add all boards to
        Deque<Board> iterator = new ArrayDeque<>();

        // store the modified index of blank square
        int j = find();

            /*
            This part checks if exchange is possible using checkIndex method
            For true it creates a new board that is a copy of given board with the exchange performed
            Then the new board is pushed into the stack iterator
             */
        // move blank square up
        if (checkIndex(j, j - n)) {
            Board a = copyOf();
            a.exch(j, j - n);
            iterator.push(a);
        }
        // move blank square down
        if (checkIndex(j, j + n)) {
            Board a = copyOf();
            a.exch(j, j + n);
            iterator.push(a);
        }
        // moves blank square to the left
        if (checkIndex(j, j - 1)) {
            Board a = copyOf();
            a.exch(j, j - 1);
            iterator.push(a);
        }
        // move blank square to the right
        if (checkIndex(j, j + 1)) {
            Board a = copyOf();
            a.exch(j, j + 1);
            iterator.push(a);
        }

        // return stack iterator
        return iterator;
    }


    // returns the index associated with the blank square
    private int find() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) return (n * i + j);
            }
        }
        return -1;
    }

    // check if exchanging a to b is allowed
    private boolean checkIndex(int a, int b) {
        // check if index of a is out of bounds
        if (a < 0 || a >= n * n) return false;
        // check if index of b is out of bounds
        if (b < 0 || b >= n * n) return false;
        // convert a to (ai, aj)
        int ai = a / n;
        int aj = a % n;
        // convert b to (bi, bj)
        int bi = b / n;
        int bj = b % n;
        // return true only if exactly one of the coordinates change (because of blank square can only go in some directions
        return ai == bi || aj == bj;
    }


    // copy the board and return a new copy
    private Board copyOf() {
        int[][] arr = copy(board);
        return new Board(arr);
    }


    // exchange values at index a and b
    private void exch(int a, int b) {
        // store i coordinates
        int i1 = a / n;
        int i2 = b / n;
        // store j coordinates
        int j1 = a % n;
        int j2 = b % n;

        // exchange function
        int temp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = temp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // indexes to be exchanged
        int a, b;
        // a = 0 and b = 1 only if none of 0 or 1 equals zero
        if (board[0][0] != 0 && board[0][1] != 0) {
            a = 0;
            b = 1;
        } else {
            a = 2;
            b = 3;
        }

        Board s = copyOf();
        s.exch(a, b);
        // return twin board
        return s;
    }
}

