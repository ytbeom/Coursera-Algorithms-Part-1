/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

public class Solver {
    private class Move implements Comparable<Move> {
        private Move previous;
        private final Board board;
        private int moveCount = 0;

        public Move(Board board) {
            this.board = board;
        }

        public Move(Board board, Move previous) {
            this.board = board;
            this.previous = previous;
            this.moveCount = previous.moveCount + 1;
        }

        public int compareTo(Move move) {
            return (this.board.manhattan() - move.board.manhattan()) +
                    (this.moveCount - move.moveCount);
        }
    }

    private Move lastMove;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<Move> moves = new MinPQ<>();
        moves.insert(new Move(initial));

        MinPQ<Move> twinMoves = new MinPQ<>();
        twinMoves.insert(new Move(initial.twin()));

        while (true) {
            lastMove = expand(moves);
            if (lastMove != null || expand(twinMoves) != null) {
                return;
            }
        }
    }

    private Move expand(MinPQ<Move> moves) {
        if (moves.isEmpty()) {
            return null;
        }
        Move bestMove = moves.delMin();

        if (bestMove.board.isGoal()) {
            return bestMove;
        }
        for (Board neighbor : bestMove.board.neighbors()) {
            if (bestMove.previous == null || !neighbor.equals(bestMove.previous.board)) {
                moves.insert(new Move(neighbor, bestMove));
            }
        }

        return null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return lastMove != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? lastMove.moveCount : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }

        List<Board> boardList = new LinkedList<>();
        Move itr = lastMove;
        while (itr != null) {
            boardList.add(0, itr.board);
            itr = itr.previous;
        }

        return boardList;
    }


    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
