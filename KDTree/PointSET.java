import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> mainSet;

    // constructs an empty set of points
    public PointSET() {
        mainSet = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return mainSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return mainSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        mainSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return mainSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point2D : mainSet) {
            point2D.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Stack<Point2D> output = new Stack<Point2D>();
        for (Point2D point2D : mainSet) {
            if (rect.contains(point2D)) {
                output.push(point2D);
            }
        }
        return output;
    }

    // a nearest neighbour in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D closest = null;
        double distance = Double.POSITIVE_INFINITY;
        for (Point2D point2D : mainSet) {
            double distanceSquared = point2D.distanceSquaredTo(p);
            if (distanceSquared < distance) {
                closest = point2D;
                distance = distanceSquared;
            }
        }
        return closest;
    }

    public static void main(String[] args) {

    }

}
