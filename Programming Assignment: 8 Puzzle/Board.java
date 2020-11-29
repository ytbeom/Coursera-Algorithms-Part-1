/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

public class Board {
    private final int size;
    private int[][] tiles;
    private int emptyRow;
    private int emptyCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles.length == 0 || tiles.length != tiles[0].length) {
            throw new IllegalArgumentException();
        }

        size = tiles.length;
        this.tiles = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size);
        for (int i = 0; i < size; i++) {
            sb.append("\n");
            for (int j = 0; j < size; j++) {
                sb.append(tiles[i][j]);
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingDistance = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != i * size + j + 1) {
                    hammingDistance++;
                }
            }
        }

        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDistance = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != 0 && (tiles[i][j] - 1) != i * size + j) {
                    int rowDiff = Math.abs(i - ((tiles[i][j] - 1) / size));
                    int colDiff = Math.abs(j - ((tiles[i][j] - 1) % size));
                    manhattanDistance += (rowDiff + colDiff);
                }
            }
        }

        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != i * size + j + 1) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }

        if (y == null) {
            return false;
        }

        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y;
        if (this.size != that.size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighborBoardList = new LinkedList<>();
        int[] dx = { -1, 1, 0, 0 };
        int[] dy = { 0, 0, -1, 1 };
        for (int i = 0; i < 4; i++) {
            int newRow = emptyRow + dy[i];
            int newCol = emptyCol + dx[i];
            if (newRow >= 0 && newRow < size &&
                    newCol >= 0 && newCol < size) {
                Board neighborBoard = new Board(tiles);
                int temp = neighborBoard.tiles[emptyRow][emptyCol];
                neighborBoard.tiles[emptyRow][emptyCol] = neighborBoard.tiles[newRow][newCol];
                neighborBoard.tiles[newRow][newCol] = temp;
                neighborBoard.emptyRow = newRow;
                neighborBoard.emptyCol = newCol;
                neighborBoardList.add(neighborBoard);
            }
        }

        return neighborBoardList;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twinBoard = new Board(tiles);

        int row = 0;
        int col = 0;
        int changeRow = 0;
        int changeCol = 1;

        if (emptyCol == col && emptyRow == row) {
            row = 1;
            col = 1;
        }
        else if (emptyCol == changeCol && emptyRow == changeRow) {
            changeRow = 1;
            changeCol = 0;
        }

        int temp = twinBoard.tiles[row][col];
        twinBoard.tiles[row][col] = twinBoard.tiles[changeRow][changeCol];
        twinBoard.tiles[changeRow][changeCol] = temp;

        return twinBoard;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        StdOut.println(initial.twin().toString());

        for (Board b : initial.neighbors()) {
            for (Board bb : b.neighbors()) {
                StdOut.println(bb.toString());
            }
        }

    }
}
