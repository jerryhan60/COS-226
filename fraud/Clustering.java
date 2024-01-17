import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;

import java.util.Arrays;

public class Clustering {
    // number of vertices/points
    private int m;
    // the number of clusters
    private int k;

    // a cluster object for storing the cluster indices
    private CC clusters;

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        if (locations == null) throw new IllegalArgumentException();
        if (k < 1 || k > locations.length) throw new IllegalArgumentException();
        this.k = k;
        this.m = locations.length;
        EdgeWeightedGraph G = new EdgeWeightedGraph(m);
        for (int i = 0; i < m; i++) {
            if (locations[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < m; j++) {
                if (locations[j] == null) throw new IllegalArgumentException();
                Edge e = new Edge(i, j, locations[i].distanceTo(locations[j]));
                G.addEdge(e);
            }
        }
        // kruskal takes m^2 log m because we sort the edges of G.
        KruskalMST kruskal = new KruskalMST(G);

        Edge[] mstE = new Edge[m - 1];
        int idx = 0;
        for (Edge e : kruskal.edges()) {
            mstE[idx] = e;
            idx++;
        }
        // sort by edge weight
        Arrays.sort(mstE);
        // create graph with only the m-k edges
        EdgeWeightedGraph clusterG = new EdgeWeightedGraph(m);
        for (int i = 0; i < m - k; i++) {
            clusterG.addEdge(mstE[i]);
        }
        this.clusters = new CC(clusterG);
    }

    // return the cluster of the ith point
    public int clusterOf(int i) {
        if (i < 0 || i >= m) throw new IllegalArgumentException();
        return clusters.id(i);
    }

    // use the clusters to reduce the dimensions of an input
    public double[] reduceDimensions(double[] input) {
        if (input == null) throw new IllegalArgumentException();
        if (input.length != m) throw new IllegalArgumentException();
        double[] clusteredArr = new double[k];
        for (int i = 0; i < input.length; i++) {
            clusteredArr[clusterOf(i)] += input[i];
        }
        return clusteredArr;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        int k = Integer.parseInt(args[2]);
        Clustering cluster = new Clustering(training.locations, k);
        cluster.reduceDimensions(training.input[0]);
        cluster.clusterOf(0);
    }
}
