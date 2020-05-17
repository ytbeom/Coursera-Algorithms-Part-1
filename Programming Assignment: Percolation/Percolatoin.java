/* *****************************************************************************
 *  Name: Taekbeom Yoo
 *  Date: 2020.05.18
 *  Description: Percolatoin.java
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int DIR_NUM = 4;
    // false: closed, true: opened;
    private boolean[][] grid;
    private int openSitesNum;
    private final int top;
    private final int bottom;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf2;
    private final int length;
    private final int[] dx;
    private final int[] dy;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1)
            throw new IllegalArgumentException();

        grid = new boolean[n][n];

        openSitesNum = 0;
        top = n * n;
        bottom = n * n + 1;
        uf = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF((n * n + 1));
        length = n;
        dx = new int[] { 0, 0, -1, 1 };
        dy = new int[] { -1, 1, 0, 0 };
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!checkIndex(row, col))
            throw new IllegalArgumentException();
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            openSitesNum++;
            if (row == 1) {
                uf.union(col - 1, top);
                uf2.union(col - 1, top);
            }
            if (row == length) {
                uf.union(length * (row - 1) + col - 1, bottom);
            }

            for (int i = 0; i < DIR_NUM; i++) {
                if (checkIndex(row + dy[i], col + dx[i]) && isOpen(row + dy[i], col + dx[i])) {
                    uf.union(length * (row - 1) + col - 1,
                             length * (row + dy[i] - 1) + col + dx[i] - 1);
                    uf2.union(length * (row - 1) + col - 1,
                              length * (row + dy[i] - 1) + col + dx[i] - 1);
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!checkIndex(row, col))
            throw new IllegalArgumentException();
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!checkIndex(row, col))
            throw new IllegalArgumentException();
        return uf2.find(length * (row - 1) + col - 1) == uf2.find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesNum;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(top) == uf.find(bottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        // test client
    }

    private boolean checkIndex(int row, int col) {
        if (row < 1 || row > length)
            return false;
        if (col < 1 || col > length)
            return false;
        return true;
    }
}
