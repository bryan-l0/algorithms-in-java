Solves the 8 puzzle problem. Size of grid of n x n is limited by memory usage. 

Board.java creates a Board object for use in Solver.java.  
It takes in n x n array of arrays as input.

Solver.java takes a Board object and finds the quickest solution is by the A* algorithm. It also finds whether it is solvable or not via computing simultaneously, a twin of the board.

This was written for an assignment for the Algorithms, Part I course, on Coursera, by Princeton.
