import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] attempts;
    private final int runTrials;
    private static double confidence95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        runTrials = trials;
        attempts = new double[trials];
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < trials; i++) {
            Percolation percolate = new Percolation(n);
            while (!percolate.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int column = StdRandom.uniform(1, n + 1);
                percolate.open(row, column);
            }
            double openSites = percolate.numberOfOpenSites();
            attempts[i] = (openSites / (n * n));
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(attempts);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(attempts);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (this.mean() - (confidence95 * this.stddev() / Math.sqrt(runTrials)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (this.mean() + (confidence95 * this.stddev() / Math.sqrt(runTrials)));
    }

    // test client (see below)
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats output = new PercolationStats(x, t);
        System.out
                .printf("%s\t\t\t\t\t%s%.10f\n%s\t\t\t\t\t%s%.10f\n%s\t%s%.10f%s%.10f%s", "mean",
                        "= ",
                        output.mean(), "stddev",
                        "= ", output.stddev(), "95% confidence interval", "= [",
                        output.confidenceLo(), ", ", output.confidenceHi(), "]");
    }
}
