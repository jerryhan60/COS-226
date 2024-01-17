import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private WeightedQuickUnionUF dsu;
    private int N, opCnt;
    private boolean percolates;
    private byte[][] state;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n out of range");
        }
        else {
            N = n;
        }
        int sz = N * N;
        dsu = new WeightedQuickUnionUF(sz);
        state = new byte[N][N];
        opCnt = 0;
    }

    // Check that arguments are in range, else throw exception
    private void check(int row, int col) {
        if (row < 0 || row >= N) {
            throw new IllegalArgumentException("Row out of range");
        }
        else if (col < 0 || col >= N) {
            throw new IllegalArgumentException("Col out of range");
        }
        else return;
    }

    // compress 2d coordinates into 1d index
    private int compress(int row, int col) {
        check(row, col);
        return row * N + col;
    }

    // get the root state corresponding to the site (row, col)
    private byte getRootState(int row, int col) {
        check(row, col);
        int idx = compress(row, col);
        int root = dsu.find(idx);
        int rootrow = root / N;
        int rootcol = root % N;
        return state[rootrow][rootcol];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        check(row, col);
        if (state[row][col] > 0) return;
        opCnt++;
        if (row == 0) state[row][col] |= 5;
        if (row == N - 1) state[row][col] |= 3;
        state[row][col] |= 1;

        int idx = compress(row, col);
        if (row != 0) if (isOpen(row - 1, col)) {
            state[row][col] |= getRootState(row - 1, col);
            dsu.union(idx, compress(row - 1, col));
        }

        if (row != N - 1) if (isOpen(row + 1, col)) {
            state[row][col] |= getRootState(row + 1, col);
            dsu.union(idx, compress(row + 1, col));
        }

        if (col != 0) if (isOpen(row, col - 1)) {
            state[row][col] |= getRootState(row, col - 1);
            dsu.union(idx, compress(row, col - 1));
        }

        if (col != N - 1) if (isOpen(row, col + 1)) {
            state[row][col] |= getRootState(row, col + 1);
            dsu.union(idx, compress(row, col + 1));
        }

        int root = dsu.find(idx);
        int rootrow = root / N;
        int rootcol = root % N;
        state[rootrow][rootcol] |= state[row][col];
        if (state[rootrow][rootcol] == 7) percolates = true;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        check(row, col);
        return (state[row][col] > 0);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        check(row, col);
        int rootState = getRootState(row, col);
        if (rootState == 5 || rootState == 7) return true;
        else return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opCnt;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolates;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation p = new Percolation(2);
        p.open(0, 1);
        p.open(1, 1);
        StdOut.print(p.percolates());
    }

}

// implementation without backfill
/*
public class Percolation {
    private WeightedQuickUnionUF dsu;
    private boolean[][] op;
    private int N, source, sink, opCnt;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n out of range");
        }
        else {
            N = n;
        }
        int sz = N * N;
        // 0 to (sz-1) for the grid, sz for the source, sz+1 for the sink
        dsu = new WeightedQuickUnionUF(sz + 2);
        source = sz;
        sink = sz + 1;
        op = new boolean[N][N];
        opCnt = 0;
    }

    private void check(int row, int col) {
        if (row < 0 || row >= N) {
            throw new IllegalArgumentException("Row out of range");
        }
        else if (col < 0 || col >= N) {
            throw new IllegalArgumentException("Col out of range");
        }
        else return;
    }

    private int compress(int row, int col) {
        check(row, col);
        return row * N + col;
    }

    private boolean connected(int x, int y) {
        return dsu.find(x) == dsu.find(y);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        check(row, col);
        if (op[row][col]) return;
        op[row][col] = true;
        opCnt++;
        int idx = compress(row, col);

        if (row == 0) dsu.union(idx, source);
        else if (isOpen(row - 1, col)) dsu.union(idx, compress(row - 1, col));

        if (row == N - 1) dsu.union(idx, sink);
        else if (isOpen(row + 1, col)) dsu.union(idx, compress(row + 1, col));

        if (col != 0) if (isOpen(row, col - 1)) dsu.union(idx, compress(row, col - 1));
        if (col != N - 1) if (isOpen(row, col + 1)) dsu.union(idx, compress(row, col + 1));

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        check(row, col);
        return op[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) return false;
        return connected(compress(row, col), source);

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opCnt;
    }

    // does the system percolate?
    public boolean percolates() {
        return connected(source, sink);
    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation p = new Percolation(2);
        p.open(0, 1);
        p.open(1, 1);
        StdOut.print(p.percolates());
    }

}
*/
