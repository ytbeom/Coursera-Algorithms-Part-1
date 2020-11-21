/* *****************************************************************************
 *  Name: Taekbeom Yoo
 *  Date: 2020.11.21
 *  Description: BruteCollinearPoints.java
 **************************************************************************** */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {
    private List<LineSegment> segmentsList;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        int len = points.length;
        segmentsList = new LinkedList<>();

        for (int i = 0; i < len; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            for (int j = i + 1; j < len; j++) {
                if (points[i].equals(points[j])) {
                    throw new IllegalArgumentException();
                }
            }
        }

        Arrays.sort(points);

        for (int i = 0; i < len - 3; i++) {
            Point p1 = points[i];
            for (int j = i + 1; j < len - 2; j++) {
                Point p2 = points[j];
                for (int k = j + 1; k < len - 1; k++) {
                    Point p3 = points[k];
                    if (p1.slopeTo(p2) == p2.slopeTo(p3)) {
                        double slope = p1.slopeTo(p2);
                        for (int m = k + 1; m < len; m++) {
                            Point p4 = points[m];
                            if (p3.slopeTo(p4) == slope) {
                                segmentsList.add(new LineSegment(p1, p4));
                            }
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentsList.size();
    }

    // the line segments
    public LineSegment[] segments() {
        int size = segmentsList.size();
        return segmentsList.toArray(new LineSegment[size]);
    }
}
