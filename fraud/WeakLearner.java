import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class WeakLearner {
    // number of inputs
    private int n;
    // dimension of inputs
    private int k;
    private int bestDim; // bestDim
    private double bestVal; // bestVal
    private int bestSgn; // bestSgn

    // Pair helper class
    private static class Pair implements Comparable<Pair> {
        private double coord; // cord
        private int idx; // idx

        // constructor
        public Pair(double coord, int idx) {
            this.coord = coord;
            this.idx = idx;
        }

        // compareTo
        public int compareTo(Pair other) {
            return Double.compare(this.coord, other.coord);
        }
    }

    // train the weak learner
    public WeakLearner(double[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null) {
            throw new IllegalArgumentException();
        }
        this.n = input.length;
        if (weights.length != this.n) throw new IllegalArgumentException();
        if (labels.length != this.n) throw new IllegalArgumentException();
        for (int i = 0; i < n; i++) {
            if (input[i] == null || weights[i] < 0)
                throw new IllegalArgumentException();
            if (labels[i] < 0 || labels[i] > 1) throw new IllegalArgumentException();
        }
        this.k = input[0].length;

        // iterate through all dimensions
        double curBest = 0;

        for (int i = 0; i < k; i++) {
            Pair[] pairs = new Pair[n];
            for (int j = 0; j < n; j++) pairs[j] = new Pair(input[j][i], j);
            Arrays.sort(pairs);

            // sgn = 0
            double val = 0;
            for (int j = 0; j < n; j++) {
                if (labels[j] == 1) val += weights[j];
            }
            if (val > curBest) {
                curBest = val;
                bestDim = i;
                bestVal = -1;
                bestSgn = 0;
            }
            for (int j = 0; j < n; j++) {
                double key = pairs[j].coord;
                int idx = pairs[j].idx;
                if (labels[idx] == 1) val -= weights[idx];
                else val += weights[idx];
                if ((j < n - 1) && pairs[j].coord == pairs[j + 1].coord) continue;
                if (val > curBest) {
                    curBest = val;
                    bestDim = i;
                    bestVal = key;
                    bestSgn = 0;
                }
            }

            // sgn = 1
            val = 0;
            for (int j = 0; j < n; j++) {
                if (labels[j] == 0) val += weights[j];
            }
            if (val > curBest) {
                curBest = val;
                bestDim = i;
                bestVal = -1;
                bestSgn = 1;
            }
            for (int j = 0; j < n; j++) {
                double key = pairs[j].coord;
                int idx = pairs[j].idx;
                if (labels[idx] == 1) val += weights[idx];
                else val -= weights[idx];
                if ((j < n - 1) && pairs[j].coord == pairs[j + 1].coord) continue;
                if (val > curBest) {
                    curBest = val;
                    bestDim = i;
                    bestVal = key;
                    bestSgn = 1;
                }
            }
        }

    }

    // return the prediction of the learner for a new sample
    public int predict(double[] sample) {
        if (sample == null) throw new IllegalArgumentException();
        if (sample.length != this.k) throw new IllegalArgumentException();
        if (bestSgn == 0) {
            if (sample[bestDim] > bestVal) return 1;
            else return 0;
        }
        else {
            if (sample[bestDim] > bestVal) return 0;
            else return 1;
        }

    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return bestDim;
    }

    // return the value the learner uses to separate the data
    public double valuePredictor() {
        return bestVal;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return bestSgn;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet test = new DataSet(args[1]);

        double[] weights = new double[training.n];
        for (int i = 0; i < training.n; i++) weights[i] = 1.0;

        // train the model
        WeakLearner model = new WeakLearner(training.input, weights, training.labels);

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.n; i++)
            if (model.predict(training.input[i]) == training.labels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.n;

        // calculate the test data set accuracy
        double testAccuracy = 0;
        for (int i = 0; i < test.n; i++)
            if (model.predict(test.input[i]) == test.labels[i])
                testAccuracy += 1;
        testAccuracy /= test.n;

        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model:     " + testAccuracy);

        StdOut.println(model.dimensionPredictor());
        StdOut.println(model.signPredictor());
        StdOut.println(model.valuePredictor());
    }

}

