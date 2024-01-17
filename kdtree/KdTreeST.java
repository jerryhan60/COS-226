import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class KdTreeST<Value> {
    // size of KdTree
    private int size = 0;
    // root node
    private Node root;
    // rect at root
    private RectHV rectRoot = new RectHV(Double.NEGATIVE_INFINITY,
                                         Double.NEGATIVE_INFINITY,
                                         Double.POSITIVE_INFINITY,
                                         Double.POSITIVE_INFINITY);

    private class Node {
        // value
        Value val;
        // point2D key
        Point2D point2D;
        // corresponding rect
        RectHV rect;
        // pointer to left node
        Node left;
        // pointer to right node
        Node right;

        // constructor
        private Node(Point2D point2D, Value val, RectHV rect) {
            this.point2D = point2D;
            this.val = val;
            this.rect = rect;
        }
    }

    // construct an empty symbol table of points
    public KdTreeST() {
        size = 0;
        root = null;
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
        if (p == null || val == null) throw new IllegalArgumentException();
        if (!contains(p)) size++;
        root = insert(root, p, val, rectRoot, false);
    }

    // insert node - similar to implementation of BST insert method
    private Node insert(Node x, Point2D p, Value val, RectHV rect,
                        boolean isHorizontal) {
        Node newNode = new Node(p, val, rect);
        if (x == null) {
            return newNode;
        }
        if (x.point2D.equals(p)) {
            x.val = val;
            return x;
        }
        // if current node is horizontal
        else if (isHorizontal) {
            if (p.y() < x.point2D.y()) {
                RectHV newRec = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(),
                                           x.point2D.y());
                x.left = insert(x.left, p, val, newRec, !isHorizontal);
            }

            else {
                RectHV newRec = new RectHV(x.rect.xmin(), x.point2D.y(), x.rect.xmax(),
                                           x.rect.ymax());
                x.right = insert(x.right, p, val, newRec, !isHorizontal);
            }
        }
        // if current node is vertical
        else {
            if (p.x() < x.point2D.x()) {
                RectHV newRec = new RectHV(x.rect.xmin(), x.rect.ymin(),
                                           x.point2D.x(), x.rect.ymax());
                x.left = insert(x.left, p, val, newRec, !isHorizontal);
            }
            else {
                RectHV newRec = new RectHV(x.point2D.x(), x.rect.ymin(),
                                           x.rect.xmax(), x.rect.ymax());
                x.right = insert(x.right, p, val, newRec, !isHorizontal);
            }
        }
        return x;
    }

    // helper get function
    private Value get(Node x, Point2D p, boolean isHorizontal) {
        if (x == null) return null;
        if (x.point2D.equals(p)) return x.val;
        if (isHorizontal) {
            if (p.y() < x.point2D.y()) return get(x.left, p, !isHorizontal);
            else return get(x.right, p, !isHorizontal);
        }
        else {
            if (p.x() < x.point2D.x()) return get(x.left, p, !isHorizontal);
            else return get(x.right, p, !isHorizontal);
        }
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p, false);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        return get(p) != null;
    }


    // all points in the symbol table
    public Iterable<Point2D> points() {
        // this queue stores the nodes we are going iterating over
        Queue<Node> queueNodes = new Queue<Node>();
        // this queue stores the points we are iterating over
        Queue<Point2D> queuePoints = new Queue<Point2D>();

        queueNodes.enqueue(root);
        while (!queueNodes.isEmpty()) {
            Node x = queueNodes.dequeue();
            if (x != null) {
                queuePoints.enqueue(x.point2D);
                queueNodes.enqueue(x.left);
                queueNodes.enqueue(x.right);
            }
        }

        return queuePoints;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        // queue for iterating over the nodes
        Queue<Node> queue = new Queue<Node>();
        // queue that contains all point2D nodes inside the rectangle
        Queue<Point2D> queueIn = new Queue<Point2D>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            // below corresponds to the prunning condition
            if (!rect.intersects(x.rect)) continue;
            if (rect.contains(x.point2D)) queueIn.enqueue(x.point2D);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return queueIn;
    }

    // this is a nearestNeighbor helper function
    private Point2D nearestNeighbor(Node u, Point2D p, Point2D best,
                                    boolean isHorizontal) {
        if (u == null) return best;
        double dist = best.distanceSquaredTo(p);

        // if current rectangle no chance the best point is inside the
        // current rectangle, return the best. So we can prune.
        if (u.rect.distanceSquaredTo(p) >= dist) return best;

        // updates best
        if (u.point2D.distanceSquaredTo(p) < dist) {
            best = u.point2D;
        }
        // these represent the first and second Node we will recurrsing later
        Node lNode, rNode;
        // split is equal to the current point's  x or y coordinate depending on
        // the parity. Simlarly, coord is equal to query point's x or y coordinate.
        double split, coord;
        if (isHorizontal) {
            split = u.point2D.y();
            coord = p.y();
        }
        else {
            split = u.point2D.x();
            coord = p.x();
        }

        if (coord < split) {
            lNode = u.left;
            rNode = u.right;
        }
        else {
            lNode = u.right;
            rNode = u.left;
        }

        if (lNode != null) {
            best = nearestNeighbor(lNode, p, best, !isHorizontal);
        }
        if (rNode != null) {
            best = nearestNeighbor(rNode, p, best, !isHorizontal);
        }
        return best;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (size == 0) return null;
        return nearestNeighbor(root, p, root.point2D, false);
    }

    // unit testing (required)
    public static void main(String[] args) {

        KdTreeST<Character> kdTree = new KdTreeST<Character>();
        if (kdTree.isEmpty()) StdOut.println("Empty");
        Point2D p1 = new Point2D(0.0, 1.0);
        Point2D p2 = new Point2D(0.0, 0.0);
        kdTree.put(p2, 'A');
        kdTree.put(p2, 'B');
        kdTree.put(p2, 'C');
        StdOut.println(kdTree.size());
        kdTree.put(p1, 'D');
        StdOut.println(kdTree.size());
        StdOut.println(kdTree.get(p1));

        kdTree.put(p1, 'E');
        StdOut.println(kdTree.size());

        kdTree.put(p1, 'A');
        StdOut.println(kdTree.contains(p2));
        StdOut.println(kdTree.size());
        StdOut.println(kdTree.get(p2));
        Iterable<Point2D> q = kdTree.points();
        for (Point2D i : q) StdOut.println(i);
        Iterable<Point2D> q2 = kdTree.range(new RectHV(0, 0, 1, 1));
        for (Point2D i : q2) StdOut.println(i);
        Point2D neighbor = kdTree.nearest(p2);
        StdOut.println(neighbor);
    }


}
