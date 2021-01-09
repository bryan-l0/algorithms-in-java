import edu.princeton.cs.algs4.MinPQ;

public class Solver {

    private int output = 0;
    private Stack<Board> iterated = new Stack<Board>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<Node> initialBoard = new MinPQ<Node>();
        MinPQ<Node> twinBoard = new MinPQ<Node>();
        Node firstNode = new Node(0, initial);
        initialBoard.insert(firstNode);
        Node twinFirstNode = new Node(0, initial.twin());
        twinBoard.insert(twinFirstNode);
        while (!initialBoard.min().board.isGoal() && !twinBoard.min().board.isGoal()) {
            Iterable<Board> neighbouring = initialBoard.min().board
                    .neighbors();
            Iterable<Board> twinNeighbours = twinBoard.min().board.neighbors();
            Node prevNode = initialBoard.delMin();
            Node prevTwin = twinBoard.delMin();
            for (Board i : neighbouring) {
                try {
                    if (!prevNode.prev.board.equals(i)) {
                        Node temp = new Node(prevNode.moves + 1, i);
                        temp.prev = prevNode;
                        initialBoard.insert(temp);
                    }
                }
                catch (NullPointerException e) {
                    Node temp = new Node(1, i);
                    temp.prev = prevNode;
                    initialBoard.insert(temp);
                }
            }
            for (Board i : twinNeighbours) {
                try {
                    if (!prevTwin.prev.board.equals(i)) {
                        Node temp = new Node(prevTwin.moves + 1, i);
                        temp.prev = prevTwin;
                        twinBoard.insert(temp);
                    }
                }
                catch (NullPointerException e) {
                    Node temp = new Node(1, i);
                    temp.prev = prevTwin;
                    twinBoard.insert(temp);
                }
            }
        }
        Node current = initialBoard.min();
        if (current.board.isGoal()) {
            while (current.prev != null) {
                output++;
                iterated.push(current.board);
                current = current.prev;
            }
            iterated.push(current.board);

        }
        else {
            output = -1;
        }


    }

    private class Node implements Comparable<Node> {

        private Node prev;
        private final Board board;
        private final int moves;
        private final int priority;

        private Node(int moves, Board s) {
            int manhattan = s.manhattan();
            this.prev = null;
            this.board = s;
            this.moves = moves;
            this.priority = manhattan + moves;
        }

        private boolean equals(Node that) {
            return this.priority == that.priority;
        }

        public int compareTo(Node that) {
            if (this.equals(that)) {
                return 0;
            }
            else if (this.priority > that.priority) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (output == -1) {
            return false;
        }
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return output;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (output == -1) {
            return null;
        }
        else {
            return iterated;
        }
    }

}
