import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class PointST<Value> {
    // size of the tree
    private int size = 0;
    // RBBST data structure of PointST class
    private RedBlackBST<Point2D, Value> pointTree;

    // construct an empty symbol table of points
    public PointST() {
        pointTree = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // number of points
    public int size() {
        return size;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) throw new IllegalArgumentException("null args");
        if (!contains(p)) size++;
        pointTree.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null args");
        return pointTree.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null args");
        return pointTree.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return pointTree.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("null args");
        Stack<Point2D> pointsRect;
        pointsRect = new Stack<Point2D>();
        for (Point2D point : pointTree.keys()) {
            if (rect.contains(point)) pointsRect.push(point);
        }
        return pointsRect;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null args");
        Point2D nearest = null;
        double min = Double.POSITIVE_INFINITY;
        for (Point2D point : pointTree.keys()) {
            if (p.distanceSquaredTo(point) < min) {
                nearest = point;
                min = p.distanceSquaredTo(point);
            }
        }
        return nearest;
    }

    // unit testing (required)
    public static void main(String[] args) {

        // this is the unit testing required in readme for reading the
        // input1M.txt file and calculating the time elapsed.
        // I put it as a commentary in order for allowing the unit testing
        // of all methods as shown right after this commentary.

        /*
        PointST<Integer> brute = new PointST<>();
        String filename = args[0];
        In in = new In(filename);

        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.put(p, i);
        }
        Stopwatch timer = new Stopwatch();
        for (int m = 0; m < 60; m++) {
            double xRand = StdRandom.uniformDouble(0.0, 1.0);
            double yRand = StdRandom.uniformDouble(0.0, 1.0);
            Point2D p = new Point2D(xRand, yRand);
            Point2D champ = brute.nearest(p);
            StdOut.println(champ.toString());

        }
        StdOut.println("elapsed time =     " + timer.elapsedTime());
        */
        PointST<Integer> pointST = new PointST<>();

        if (pointST.isEmpty()) StdOut.println("It is initially empty");
        Point2D point4 = new Point2D(2, 7);

        Point2D point1 = new Point2D(2, 3);
        Point2D point2 = new Point2D(3, 3);
        Point2D point3 = new Point2D(4, 4);

        pointST.put(point1, 0);
        pointST.put(point2, 0);
        pointST.put(point3, 0);
        pointST.put(point4, 0);

        StdOut.println(pointST.get(point1));
        Point2D pointX = new Point2D(6, 4);

        if (!pointST.isEmpty()) StdOut.println(
                "Now it has " + pointST.size() + " keys ");
        StdOut.println("closer to (6,4) is " + pointST.nearest(pointX));

        RectHV rect = new RectHV(0, 0, 6, 6);
        Iterable<Point2D> range = pointST.range(rect);
        StdOut.println("These are the points the rectangle contains:");
        for (Point2D point : range) {
            StdOut.println(point.toString());
        }
        StdOut.println("These are all points:");
        for (Point2D point : pointST.points()) {
            StdOut.println(point.toString());
        }
        Point2D point5 = new Point2D(7, 8);
        Point2D point6 = new Point2D(3, 3);
        StdOut.println("Does point (7,8) exists? ");
        if (pointST.contains(point5)) StdOut.println("Yes");
        else StdOut.println("No");
        StdOut.println("Does point (3,3) exists? ");
        if (pointST.contains(point6)) StdOut.println("Yes");
        else StdOut.println("No");
    }
    
}

