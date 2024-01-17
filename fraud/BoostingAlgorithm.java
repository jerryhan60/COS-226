import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class BoostingAlgorithm {
    private double[][] reduced; // reduced
    private double[] weights; // weights
    private int[] labels; // labels
    private int n; // n
    private int k; // k
    private int m; // m
    private Queue<WeakLearner> q; // queue
    private Clustering cluster; // cluster

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(double[][] input, int[] labels,
                             Point2D[] locations, int k) {
        if (input == null || labels == null || locations == null)
            throw new IllegalArgumentException();
        n = input.length;
        if (labels.length != n) throw new IllegalArgumentException();
        for (int i = 0; i < n; i++) {
            if (input[i] == null) throw new IllegalArgumentException();
            if (labels[i] != 0 && labels[i] != 1) throw new IllegalArgumentException();
        }

        m = input[0].length;
        if (k < 1 || k > locations.length) throw new IllegalArgumentException();

        cluster = new Clustering(locations, k);
        this.k = k;
        reduced = new double[n][k];
        for (int i = 0; i < n; i++) {
            reduced[i] = cluster.reduceDimensions(input[i]);
        }
        weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = 1.0 / n;
        }
        this.labels = labels.clone();
        q = new Queue<WeakLearner>();
    }

    // return the current weights
    public double[] weights() {
        return weights.clone();
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        WeakLearner model = new WeakLearner(reduced, weights, labels);
        q.enqueue(model);
        for (int i = 0; i < n; i++) {
            int pred = model.predict(reduced[i]);
            if (pred != labels[i]) weights[i] *= 2;
        }
        double sumWeights = 0;
        for (int i = 0; i < n; i++) sumWeights += weights[i];
        for (int i = 0; i < n; i++) {
            weights[i] = weights[i] / sumWeights;
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(double[] sample) {
        if (sample == null) throw new IllegalArgumentException();
        if (sample.length != m) throw new IllegalArgumentException();
        int countOne = 0;
        int countZero = 0;
        double[] red = cluster.reduceDimensions(sample);
        for (WeakLearner model : q) {
            int pred = model.predict(red);
            if (pred == 1) countOne++;
            else countZero++;
        }
        if (countOne > countZero) return 1;
        return 0;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet test = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int iterations = Integer.parseInt(args[3]);

        // train the model
        Stopwatch timer = new Stopwatch();
        BoostingAlgorithm model = new BoostingAlgorithm(
                training.input, training.labels, training.locations, k);
        for (int t = 0; t < iterations; t++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.n; i++)
            if (model.predict(training.input[i]) == training.labels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.n;

        double[] w = model.weights();
        StdOut.println(w.length);

        // calculate the test data set accuracy
        double testAccuracy = 0;
        for (int i = 0; i < test.n; i++)
            if (model.predict(test.input[i]) == test.labels[i])
                testAccuracy += 1;
        testAccuracy /= test.n;
        StdOut.println("elapsed time =     " + timer.elapsedTime());

        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model:     " + testAccuracy);
    }
}

