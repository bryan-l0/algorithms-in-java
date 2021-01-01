public class BruteCollinearPoints {

    private Point pqhigh, pqlow, rshigh, rslow, high, low;
    private int cumulative;
    private LineSegment[] collinearPoints;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        if (points == null) {
            throw new IllegalArgumentException();
        }

        cumulative = 0;
        collinearPoints = new LineSegment[0];

        for (int i = 0; i < points.length - 3; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            Point p = points[i];
            for (int j = i + 1; j < points.length - 2; j++) {
                if (points[j] == null) {
                    throw new IllegalArgumentException();
                }
                Point q = points[j];
                double pq = p.slopeTo(q);
                for (int k = j + 1; k < points.length - 1; k++) {
                    if (points[k] == null) {
                        throw new IllegalArgumentException();
                    }
                    Point r = points[k];
                    double pr = p.slopeTo(r);
                    if (pq == pr) {
                        for (int m = k + 1; m < points.length; m++) {
                            if (points[m] == null) {
                                throw new IllegalArgumentException();
                            }
                            Point s = points[m];
                            double ps = p.slopeTo(s);
                            if (pq == ps) {
                                LineSegment[] copy = new LineSegment[collinearPoints.length + 1];
                                System.arraycopy(collinearPoints, 0, copy, 0,
                                                 collinearPoints.length);
                                collinearPoints = copy;
                                if (p.compareTo(q) == 0) {
                                    pqlow = p;
                                    pqhigh = q;
                                }
                                else if (p.compareTo(q) < 0) {
                                    pqlow = q;
                                    pqhigh = p;
                                }
                                else {
                                    pqlow = p;
                                    pqhigh = q;
                                }
                                if (r.compareTo(s) == 0) {
                                    rslow = r;
                                    rshigh = s;
                                }
                                else if (r.compareTo(s) < 0) {
                                    rslow = s;
                                    rshigh = r;
                                }
                                else {
                                    rslow = r;
                                    rshigh = s;
                                }
                                if (pqhigh.compareTo(rshigh) > 0) {
                                    high = rshigh;
                                }
                                else {
                                    high = pqhigh;
                                }
                                if (pqlow.compareTo(rslow) > 0) {
                                    low = pqlow;
                                }
                                else {
                                    low = rslow;
                                }
                                LineSegment x = new LineSegment(low, high);
                                collinearPoints[collinearPoints.length - 1] = x;
                                cumulative++;
                            }
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return cumulative;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] output = collinearPoints;
        return output;
    }

}
