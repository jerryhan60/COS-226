import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

public class ShortestCommonAncestor {
    private Digraph dG; // digraph
    private Integer n; // number of nodes

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        dG = new Digraph(G);
        n = dG.V();
        Topological topo = new Topological(dG);
        if (!topo.hasOrder()) throw new IllegalArgumentException();
        int roots = 0;
        for (int i = 0; i < n; i++) {
            if (dG.outdegree(i) == 0) roots++;
        }
        if (roots != 1) throw new IllegalArgumentException();
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v < 0 || v >= n || w < 0 || w >= n) throw new IllegalArgumentException();
        Queue<Integer> q1 = new Queue<Integer>();
        q1.enqueue(v);
        Queue<Integer> q2 = new Queue<Integer>();
        q2.enqueue(w);
        return lengthSubset(q1, q2);
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (v < 0 || v >= n || w < 0 || w >= n) throw new IllegalArgumentException();
        Queue<Integer> q1 = new Queue<Integer>();
        q1.enqueue(v);
        Queue<Integer> q2 = new Queue<Integer>();
        q2.enqueue(w);
        return ancestorSubset(q1, q2);
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) throw new IllegalArgumentException();
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext())
            throw new IllegalArgumentException();
        for (Integer i : subsetA) {
            if (i == null) throw new IllegalArgumentException();
        }
        for (Integer i : subsetB) {
            if (i == null) throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(dG, subsetA);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(dG, subsetB);
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (!bfs1.hasPathTo(i)) continue;
            if (!bfs2.hasPathTo(i)) continue;
            ans = Integer.min(ans, bfs1.distTo(i) + bfs2.distTo(i));
        }
        return ans;
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) throw new IllegalArgumentException();
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext())
            throw new IllegalArgumentException();
        for (Integer i : subsetA) {
            if (i == null) throw new IllegalArgumentException();
        }
        for (Integer i : subsetB) {
            if (i == null) throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(dG, subsetA);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(dG, subsetB);
        int best = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < n; i++) {
            if (!bfs1.hasPathTo(i)) continue;
            if (!bfs2.hasPathTo(i)) continue;
            int tmp = bfs1.distTo(i) + bfs2.distTo(i);
            if (tmp < best) {
                ancestor = i;
                best = tmp;
            }
        }
        return ancestor;
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
        StdOut.println(sca.lengthSubset(null, null));
        StdOut.println(sca.ancestorSubset(null, null));
    }

}

