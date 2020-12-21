import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int arrayDimension;
    private boolean[][] siteOpen;
    private final WeightedQuickUnionUF arraySize;
    private int openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        siteOpen = new boolean[n][n];
        arrayDimension = n;
        arraySize = new WeightedQuickUnionUF(n * n + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > arrayDimension || row <= 0 || col > arrayDimension || col <= 0) {
            throw new IllegalArgumentException();
        }
        else {
            if (!siteOpen[row - 1][col - 1]) {
                openSites += 1;
            }
            siteOpen[row - 1][col - 1] = true;
            try {
                if (siteOpen[row][col - 1]) {
                    arraySize.union(arrayDimension * (row - 1) + col,
                                    arrayDimension * (row) + col);
                }
            }
            catch (IndexOutOfBoundsException e) {
                arraySize.union(arrayDimension * (row - 1) + col,
                                arrayDimension * arrayDimension + 1);
            }
            try {
                if (siteOpen[row - 2][col - 1]) {
                    arraySize.union(arrayDimension * (row - 1) + col,
                                    arrayDimension * (row - 2) + col);
                }
            }
            catch (IndexOutOfBoundsException e) {
                arraySize.union(arrayDimension * (row - 1) + col, 0);
            }
            try {
                if (siteOpen[row - 1][col]) {
                    arraySize.union(arrayDimension * (row - 1) + col,
                                    arrayDimension * (row - 1) + col + 1);
                }
            }
            catch (IndexOutOfBoundsException e) {
                // caught already!
            }
            try {
                if (siteOpen[row - 1][col - 2]) {
                    arraySize.union(arrayDimension * (row - 1) + col,
                                    arrayDimension * (row - 1) + col - 1);
                }
            }
            catch (IndexOutOfBoundsException e) {
                // caught already!
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > arrayDimension || row <= 0 || col > arrayDimension || col <= 0) {
            throw new IllegalArgumentException();
        }
        else {
            return siteOpen[row - 1][col - 1];
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > arrayDimension || row <= 0 || col > arrayDimension || col <= 0) {
            throw new IllegalArgumentException();
        }
        else {
            return arraySize.find(0) == arraySize.find(arrayDimension * (row - 1) + col);
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return arraySize.find(0) == arraySize.find(arrayDimension * arrayDimension + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        // optional!
    }
}
