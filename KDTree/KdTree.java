import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    private int size = 0;
    private Node root = new Node();

    private static class Node {
        private Point2D p;
        private Node lb;
        private Node rt;
        private int height;
        private double distance;
    }


    // constructs a root node
    public KdTree() {
        root.p = null;
        root.lb = null;
        root.rt = null;
        root.height = -1;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root.p == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root.p == null) {
            root.p = p;
            size++;
        }
        else {
            int height = -1;
            Node pointer = root;
            int size1 = size;
            while (size == size1 && pointer != null) {
                height *= -1;
                if (pointer.p.equals(p)) {
                    break;
                }
                else if (pointer.height == -1) {
                    boolean xsame = pointer.p.x() == p.x();
                    if (xsame && pointer.p.y() > p.y() && pointer.lb == null) {
                        pointer.lb = new Node();
                        pointer.lb.p = p;
                        pointer.lb.height = height;
                        size++;
                    }
                    else if (xsame && pointer.p.y() > p.y()) {
                        pointer = pointer.lb;
                    }
                    else if (xsame && pointer.rt == null) {
                        pointer.rt = new Node();
                        pointer.rt.p = p;
                        pointer.rt.height = height;
                        size++;
                    }
                    else if (xsame) {
                        pointer = pointer.rt;
                    }
                    else if (pointer.p.x() > p.x() && pointer.lb == null) {
                        pointer.lb = new Node();
                        pointer.lb.p = p;
                        pointer.lb.height = height;
                        size++;
                    }
                    else if (pointer.p.x() > p.x()) {
                        pointer = pointer.lb;
                    }
                    else if (pointer.rt == null) {
                        pointer.rt = new Node();
                        pointer.rt.p = p;
                        pointer.rt.height = height;
                        size++;
                    }
                    else {
                        pointer = pointer.rt;
                    }
                }
                else if (pointer.height == 1) {
                    if (pointer.p.y() > p.y() && pointer.lb == null) {
                        pointer.lb = new Node();
                        pointer.lb.p = p;
                        pointer.lb.height = height;
                        size++;
                    }
                    else if (pointer.p.y() > p.y()) {
                        pointer = pointer.lb;
                    }
                    else if (pointer.rt == null) {
                        pointer.rt = new Node();
                        pointer.rt.p = p;
                        pointer.rt.height = height;
                        size++;
                    }
                    else {
                        pointer = pointer.rt;
                    }
                }
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node pointer = root;
        while (pointer != null && pointer.p != null) {
            if (pointer.p.equals(p)) {
                return true;
            }
            else if (pointer.height == -1) {
                if (pointer.p.x() == p.x()) {
                    if (pointer.p.y() > p.y()) {
                        pointer = pointer.lb;
                    }
                    else {
                        pointer = pointer.rt;
                    }
                }
                else if (pointer.p.x() > p.x()) {
                    pointer = pointer.lb;
                }
                else {
                    pointer = pointer.rt;
                }
            }
            else if (pointer.height == 1) {
                if (pointer.p.y() > p.y()) {
                    pointer = pointer.lb;
                }
                else {
                    pointer = pointer.rt;
                }
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        else if (root.p == null) {
            return null;
        }
        Stack<Node> stackOfNodes = new Stack<Node>();
        Stack<Point2D> output = new Stack<Point2D>();
        stackOfNodes.push(root);
        while (!stackOfNodes.isEmpty()) {
            Node pointer = stackOfNodes.pop();
            while (pointer != null) {
                if (rect.contains(pointer.p)) {
                    output.push(pointer.p);
                    stackOfNodes.push(pointer.rt);
                    pointer = pointer.lb;
                }
                else if (pointer.height == -1) {
                    RectHV line = new RectHV(pointer.p.x(), 0, pointer.p.x(), 1);
                    if (rect.intersects(line)) {
                        if (pointer.rt != null) {
                            stackOfNodes.push(pointer.rt);
                        }
                        pointer = pointer.lb;
                    }
                    else if (rect.xmax() < pointer.p.x()) {
                        pointer = pointer.lb;
                    }
                    else {
                        pointer = pointer.rt;
                    }
                }
                else if (pointer.height == 1) {
                    RectHV line = new RectHV(0, pointer.p.y(), 1, pointer.p.y());
                    if (rect.intersects(line)) {
                        if (pointer.rt != null) {
                            stackOfNodes.push(pointer.rt);
                        }
                        pointer = pointer.lb;
                    }
                    else if (rect.ymax() < pointer.p.y()) {
                        pointer = pointer.lb;
                    }
                    else {
                        pointer = pointer.rt;
                    }
                }
            }

        }
        return output;
    }

    // a nearest neighbour in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Stack<Node> stackOfNodes = new Stack<Node>();
        Point2D output = null;
        double distance = Double.POSITIVE_INFINITY;
        if (root.p != null) {
            stackOfNodes.push(root);
        }
        while (!stackOfNodes.isEmpty()) {
            Node pointer = stackOfNodes.pop();
            if (pointer.distance > distance) {
                continue;
            }
            while (pointer != null) {
                double temp = pointer.p.distanceSquaredTo(p);
                if (temp < distance) {
                    output = pointer.p;
                    distance = temp;
                }
                if (pointer.height == -1) {
                    RectHV line = new RectHV(pointer.p.x(), 0, pointer.p.x(), 1);
                    double lineDistance = line.distanceSquaredTo(p);
                    if (lineDistance <= distance) {
                        if (pointer.p.x() > p.x()) {
                            if (pointer.rt != null) {
                                pointer.rt.distance = lineDistance;
                                stackOfNodes.push(pointer.rt);
                            }
                            pointer = pointer.lb;
                        }
                        else {
                            if (pointer.lb != null) {
                                pointer.lb.distance = lineDistance;
                                stackOfNodes.push(pointer.lb);
                            }
                            pointer = pointer.rt;
                        }
                    }
                    else if (pointer.p.x() > p.x()) {
                        pointer = pointer.lb;
                    }
                    else {
                        pointer = pointer.rt;
                    }
                }
                else if (pointer.height == 1) {
                    RectHV line = new RectHV(0, pointer.p.y(), 1, pointer.p.y());
                    double lineDistance = line.distanceSquaredTo(p);
                    if (lineDistance <= distance) {
                        if (pointer.p.y() > p.y()) {
                            if (pointer.rt != null) {
                                pointer.rt.distance = lineDistance;
                                stackOfNodes.push(pointer.rt);
                            }
                            pointer = pointer.lb;
                        }
                        else {
                            if (pointer.lb != null) {
                                pointer.lb.distance = lineDistance;
                                stackOfNodes.push(pointer.lb);
                            }
                            pointer = pointer.rt;
                        }
                    }
                    else if (pointer.p.y() > p.y()) {
                        pointer = pointer.lb;
                    }
                    else {
                        pointer = pointer.rt;
                    }

                }
            }

        }
        return output;
    }


    public static void main(String[] args) {

    }

}
