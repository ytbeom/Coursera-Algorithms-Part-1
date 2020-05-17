/* *****************************************************************************
 *  Name: Taekbeom Yoo
 *  Date: 2020.05.18
 *  Description: Percolatoin.java
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // -1: closed, 0: open, 1: full
    private int[][] grid;
    private int openSitesNum;
    private int top;
    private int bottom;
    private WeightedQuickUnionUF uf;
    private int length;
    private int[] dx;
    private int[] dy;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1)
            throw new IllegalArgumentException();

        grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = -1;
            }
        }
        openSitesNum = 0;
        top = n * n;
        bottom = n * n + 1;
        uf = new WeightedQuickUnionUF(n * n + 2);
        length = n;
        dx = new int[] { 0, 0, -1, 1 };
        dy = new int[] { -1, 1, 0, 0 };
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!checkIndex(row, col))
            throw new IllegalArgumentException();
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = 0;
            openSitesNum++;
            if (row == 1) {
                uf.union(col - 1, top);
            }
            else if (row == length) {
                uf.union(col - 1, bottom);
            }

            for (int i = 0; i < 4; i++) {
                if (checkIndex(row + dy[i], col + dx[i]) && isOpen(row + dy[i], col + dx[i])) {
                    uf.union(length * (row - 1) + col - 1,
                             length * (row + dy[i] - 1) + col + dx[i] - 1);
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!checkIndex(row, col))
            throw new IllegalArgumentException();
        return grid[row - 1][col - 1] == 0;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!checkIndex(row, col))
            throw new IllegalArgumentException();
        return grid[row - 1][col - 1] == 1;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesNum;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(top, bottom);
    }

    // test client (optional)
    public static void main(String[] args) {

    }

    public boolean checkIndex(int row, int col) {
        if (row < 1 || row > length)
            return false;
        if (col < 1 || col > length)
            return false;
        return true;
    }
}
