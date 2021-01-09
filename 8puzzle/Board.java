import java.util.Arrays;

public class Board {

    private final int[][] board2D;
    private int blankRow, blankCol;
    private boolean twinned;
    private int x1, x2, y1, y2;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        int[][] copy = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                copy[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    blankCol = i;
                    blankRow = j;
                }
            }
        }
        board2D = copy;
    }

    // string representation of this board
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(board2D.length);
        output.append('\n');
        for (int[] i : board2D) {
            for (int j = 0; j < board2D.length; j++) {
                output.append(i[j]);
                if (j == board2D.length - 1) {
                    output.append('\n');
                }
                else {
                    output.append(' ');
                }
            }
        }
        String finalOutput = output.toString();
        return finalOutput;
    }

    // board dimension n
    public int dimension() {
        return board2D.length;
    }

    // number of tiles out of place
    public int hamming() {
        int output = 0;
        for (int i = 0; i < board2D.length; i++) {
            for (int j = 0; j < board2D.length; j++) {
                if (board2D[i][j] != i * board2D.length + j + 1 && board2D[i][j] != 0) {
                    output++;
                }
            }
        }
        return output;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int output = 0;
        for (int i = 0; i < board2D.length; i++) {
            for (int j = 0; j < board2D.length; j++) {
                if (board2D[i][j] != 0) {
                    int inter1 = board2D[i][j] % board2D.length;
                    if (inter1 == 0) {
                        inter1 = board2D.length;
                    }
                    int inter2 = (i * board2D.length + j + 1) % board2D.length;
                    if (inter2 == 0) {
                        inter2 = board2D.length;
                    }
                    output += Math.abs(inter1 - inter2);
                    output += Math.abs((board2D[i][j] - 1) / board2D.length
                                               - (i * board2D.length + j) / board2D.length);
                }
            }
        }
        return output;
    }

    // is this board the goal board?
    public boolean isGoal() {
        boolean output = (manhattan() == 0) && (hamming() == 0);
        return output;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        else if (y == null) {
            return false;
        }
        else if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        return Arrays.deepEquals(that.board2D, this.board2D);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbourBoards = new Stack<Board>();
        for (int s = 0; s < 4; s++) {
            int[][] medium = addNeighbourToStack(s, this);
            if (!Arrays.deepEquals(medium, board2D)) {
                Board output = new Board(medium);
                neighbourBoards.push(output);
            }
        }
        return neighbourBoards;
    }

    private int[][] addNeighbourToStack(int s, Board p) {
        int[][] tempBoard = new int[board2D.length][board2D.length];
        for (int i = 0; i < board2D.length; i++) {
            for (int j = 0; j < board2D.length; j++) {
                tempBoard[i][j] = p.board2D[i][j];
            }
        }
        try {
            if (s == 0) {
                tempBoard[blankCol][blankRow] = tempBoard[blankCol - 1][blankRow];
                tempBoard[blankCol - 1][blankRow] = 0;
            }
            else if (s == 1) {
                tempBoard[blankCol][blankRow] = tempBoard[blankCol + 1][blankRow];
                tempBoard[blankCol + 1][blankRow] = 0;
            }
            else if (s == 2) {
                tempBoard[blankCol][blankRow] = tempBoard[blankCol][blankRow - 1];
                tempBoard[blankCol][blankRow - 1] = 0;
            }
            else {
                tempBoard[blankCol][blankRow] = tempBoard[blankCol][blankRow + 1];
                tempBoard[blankCol][blankRow + 1] = 0;
            }

        }
        catch (ArrayIndexOutOfBoundsException e) {
            return board2D;
        }
        return tempBoard;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] tempBoard = new int[board2D.length][board2D.length];
        for (int i = 0; i < board2D.length; i++) {
            for (int j = 0; j < board2D.length; j++) {
                tempBoard[i][j] = board2D[i][j];
            }
        }
        if (!twinned) {
            int[] first = { StdRandom.uniform(board2D.length), StdRandom.uniform(board2D.length) };
            int[] second = { StdRandom.uniform(board2D.length), StdRandom.uniform(board2D.length) };
            int[] empty = { blankCol, blankRow };
            while (Arrays.equals(first, empty)) {
                for (int i = 0; i < 2; i++) {
                    first[i] = StdRandom.uniform(board2D.length);
                }
            }
            while (Arrays.equals(second, empty) || Arrays.equals(first, second)) {
                for (int i = 0; i < 2; i++) {
                    second[i] = StdRandom.uniform(board2D.length);
                }
            }
            y1 = first[0];
            x1 = first[1];
            y2 = second[0];
            x2 = second[1];
            twinned = true;
        }
        int copy = tempBoard[y1][x1];
        tempBoard[y1][x1] = tempBoard[y2][x2];
        tempBoard[y2][x2] = copy;
        return new Board(tempBoard);
    }
}
