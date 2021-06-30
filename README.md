# SliderPuzzle

The 8-puzzle is a sliding puzzle that is played on a 3-by-3 grid with 8 square tiles labeled 1 through 8, plus a blank square. The goal is to rearrange the tiles so that they are in row-major order, using as few moves as possible. You are permitted to slide tiles either horizontally or vertically into the blank square.

Extending the 8-puzzle on boards of arbitrary sizes, we can call it SliderPuzzle. The SliderPuzzle problem can be efficiently solved using the A* algorithm. The A* is a graph searching algorithm that employs heuristics to guide its search. Like Dijkstra's algorithm, A* uses a priority queue to find the next node in the path. If the heuristic function that we use for determining priority is admissible, i.e. if it never overestimates the cost of reaching the goal, then the path found by A* is optimal. 

We can model the SliderPuzzle problem as a graph problem. Our goal node is the desired position of the board and our start node is the given intitial position of the board. The adjacent nodes will be all of the positions of the Board that can be found using legal moves. Using Manhattan distance as our heuristic, we can employ A* to guide our search for the steps to solve the puzzle. As our heuristic is admissible the sequence of steps given by the program is optimal, i.e. it takes the least number of moves. We can also find if a puzzle is unsolvable. Employ the same algorithm on a twin board, i.e. a board that is same as the given board with two tiles swapped. If a solution to the twin board for a given position is found, then the position is unsolvable.

This project consists of three different java classes `Board`, `Solver` and `PuzzleChecker`. `Board` models an *n-by-n* board with sliding tiles. It represents the board as a 2D integer array, with numbers describing the tiles and `0` describing an empty tile. It also contains many methods that are useful for this problem.
`Solver` is the crux of the program. It implements the A* search algorithm and provides us with a step-by-step optimal solution to the puzzle. If the initial board position is unsolvable it reports 'No Solution Possible'. It takes as input the `Board` to be solved. `PuzzleChecker` is a helping class that reports the number of moves required to solve a board. It can take multiple inputs. If a board is unsolvable it reports -1.

## Compilation
Run 
`javac Board.java`
`javac Solver.java`
`javac PuzzleChecker.java`
in order from the folder that contains these files. It is important to follow the order as the next file is dependent on the previous one.

## Usage
To find the solution to a board use `java Solver puzzle.txt`. <br>
To find the number of moves required by a number of boards use `java PuzzleChecker puzzle1.txt puzzle2.txt ...... puzzleN.txt`. <br>
The files puzzle.txt should contain the board position as an *n-by-n* matrix of integers ranging from 0 to n - 1, with 0 representing the empty tile. 

Note :- This was an assignment for Princeton's Coursera MOOC, Algorithms 1. All the test files are provided by the authors of the course.
