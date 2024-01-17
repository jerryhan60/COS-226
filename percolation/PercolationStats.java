import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    // array that stores results of trials
    private double[] x;
    private int N, T;
    private double elapsed;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("Inputs out of range");
        N = n;
        T = trials;
        x = new double[trials];
        Stopwatch timer = new Stopwatch();
        for (int i = 0; i < trials; i++) PercolationTrial(n, i);
        elapsed = timer.elapsedTime();
    }

    // run one percolation trial on n by n grid with index idx
    private void PercolationTrial(int n, int idx) {
        Percolation P = new Percolation(n);
        int[] order = StdRandom.permutation(n * n);
        for (int i = 0; i < n * n; i++) {
            int row = order[i] / n;
            int col = order[i] - row * n;
            P.open(row, col);
            if (P.percolates()) {
                x[idx] = (double) P.numberOfOpenSites() / (n * n);
                return;
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(x);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(x);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n, T;
        n = Integer.parseInt(args[0]);
        T = Integer.parseInt(args[1]);
        PercolationStats PS = new PercolationStats(n, T);
        StdOut.println("mean()           = " + PS.mean());
        StdOut.println("stddev()         = " + PS.stddev());
        StdOut.println("confidenceLow()  = " + PS.confidenceLow());
        StdOut.println("confidenceHigh() = " + PS.confidenceHigh());
        StdOut.println("elapsed time     = " + PS.elapsed);
    }

}
