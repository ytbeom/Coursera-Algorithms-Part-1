/* *****************************************************************************
 *  Name: Taekbeom Yoo
 *  Date: 2020.05.18
 *  Description: PercolatoinStats.java
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private final double[] results;
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1)
            throw new IllegalArgumentException();
        this.trials = trials;
        results = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = 0;
                int col = 0;
                do {
                    row = StdRandom.uniform(1, n + 1);
                    col = StdRandom.uniform(1, n + 1);
                } while (p.isOpen(row, col));
                p.open(row, col);
            }
            results[i] = (double) p.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        this.mean = StdStats.mean(results);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        this.stddev = StdStats.stddev(results);
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean - CONFIDENCE_95 * this.stddev / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean + CONFIDENCE_95 * this.stddev / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("mean                     = " + ps.mean());
        StdOut.println("stddev                   = " + ps.stddev());
        StdOut.println("95 % confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi()
                               + "]");
    }
}
