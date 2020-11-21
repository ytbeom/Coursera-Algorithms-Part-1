/* *****************************************************************************
 *  Name: Taekbeom Yoo
 *  Date: 2020.11.21
 *  Description: FastCollinearPoints.java
 **************************************************************************** */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> segmentsList;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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

        for (int i = 0; i < len; i++) {
            // swap two points
            Point tmp = points[i];
            points[i] = points[0];
            points[0] = tmp;

            // sort the points except points[0];
            Arrays.sort(points, 1, len, points[0].slopeOrder());

            Point p = points[0];
            int idx = 1;
            int count = 0;
            while (idx < len) {
                double slope = p.slopeTo(points[idx]);
                if (p.compareTo(points[idx]) > 0) {
                    while (idx < len && p.slopeTo(points[idx]) == slope) {
                        idx++;
                    }
                    continue;
                }

                while (idx < len && p.slopeTo(points[idx]) == slope) {
                    count++;
                    idx++;
                }

                if (count >= 3) {
                    segmentsList.add(new LineSegment(p, points[idx - 1]));
                }
                count = 0;
            }

            // recover the original order
            Arrays.sort(points);
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
