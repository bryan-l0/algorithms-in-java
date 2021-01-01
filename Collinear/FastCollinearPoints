public class FastCollinearPoints {

    private LineSegment[] outputArray = new LineSegment[0];

    // finds all the line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            // For each point, generate array of Points with Slopes
            Point initial = points[i];
            PointWithSlope[] s = new PointWithSlope[points.length - 1];
            int index = 0;
            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    double gradient = points[i].slopeTo(points[j]);
                    s[index++] = new PointWithSlope(gradient, points[j]);
                }
            }
            PointWithSlope[] sortedArray = sortingPoints(
                    s); // Sorts the array of Points with Slopes by Gradient
            LineSegment[] newArray = collinearPoints(sortedArray, initial);
            LineSegment[] tempArray = new LineSegment[outputArray.length + newArray.length];
            System.arraycopy(outputArray, 0, tempArray, 0, outputArray.length);
            System.arraycopy(newArray, 0, tempArray, outputArray.length, newArray.length);
            outputArray = tempArray;
        }


    }

    // the number of line segments
    public int numberOfSegments() {
        return outputArray.length;
    }

    // all the line segments
    public LineSegment[] segments() {
        LineSegment[] output = outputArray;
        return output;
    }

    private class PointWithSlope {

        private final double slope;
        private final Point point;

        private PointWithSlope(double slope, Point point) {
            this.slope = slope;
            this.point = point;
        }

    }

    private PointWithSlope[] sortingPoints(PointWithSlope[] input) {
        int n = input.length;
        for (int k = 0; k < n; k++) {
            for (int m = k; m > 0; m--) {
                if (less(input[m].slope, input[m - 1].slope)) {
                    exch(input, m, m - 1);
                }
                else {
                    break;
                }
            }
        }
        return input;
    }

    private LineSegment[] collinearPoints(PointWithSlope[] sorted, Point initial) {
        LineSegment[] collinearArray = new LineSegment[0];
        int index = 0;
        int u = 0;
        while (u < sorted.length - 2) {
            int adj = 0;
            try {
                while (sorted[u].slope == sorted[u + adj].slope) {
                    adj++;

                }
            }
            catch (IndexOutOfBoundsException e) {
                //
            }
            finally {
                if (adj >= 3) {
                    Point[] collinearPoints = new Point[adj + 1];
                    collinearPoints[0] = initial;
                    for (int z = 0; z < adj; z++) {
                        collinearPoints[z + 1] = sorted[u + z].point;
                    }
                    // We have array of collinear points, need to find the big segment
                    LineSegment collinear = collinearSegment(
                            collinearPoints); // Found the collinear segment
                    boolean exists = false;
                    for (int i = 0; i < outputArray.length; i++) {
                        if (collinear.toString().equals(outputArray[i].toString())) {
                            exists = true;
                        }
                    }
                    for (int i = 0; i < collinearArray.length; i++) {
                        if (collinear.toString().equals(collinearArray[i].toString())) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        LineSegment[] copy = new LineSegment[collinearArray.length + 1];
                        copy[index++] = collinear;
                        System.arraycopy(collinearArray, 0, copy, 0, collinearArray.length);
                        collinearArray = copy;
                    }
                }
                u++;
                if (adj > 1) {
                    u += adj - 1;
                }
            }

        }
        return collinearArray;
    }

    private LineSegment collinearSegment(Point[] input) {
        Point low = input[0];
        Point high = input[0];
        for (int b = 1; b < input.length; b++) {
            if (input[b].compareTo(low) < 0) {
                low = input[b];
            }
            else if (input[b].compareTo(high) > 0) {
                high = input[b];
            }
        }
        LineSegment output = new LineSegment(low, high);
        return output;
    }

    private static boolean less(double slopeA, double slopeB) {
        int lesser = Double.compare(slopeA, slopeB);
        return lesser < 0;
    }

    private static void exch(PointWithSlope[] a, int i, int j) {
        PointWithSlope swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
}
