/* *****************************************************************************
 *  Name: Taekbeom Yoo
 *  Date: 2020.12.12
 *  Description: KdTree.java
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private class Node {
        private Point2D point;
        private Node left;
        private Node right;
        private final boolean isVertical;

        public Node(Point2D p, boolean isVertical) {
            if (p == null) {
                throw new IllegalArgumentException();
            }

            this.point = new Point2D(p.x(), p.y());
            left = null;
            right = null;
            this.isVertical = isVertical;
        }
    }

    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        this.root = insert(this.root, p, true);
    }

    private Node insert(Node n, Point2D p, boolean isVertical) {
        if (n == null) {
            this.size++;
            return new Node(p, isVertical);
        }

        if (n.point.x() == p.x() && n.point.y() == p.y()) {
            return n;
        }

        if (n.isVertical) {
            if (p.x() < n.point.x()) {
                n.left = insert(n.left, p, !isVertical);
            }
            else {
                n.right = insert(n.right, p, !isVertical);
            }
        }

        else {
            if (p.y() < n.point.y()) {
                n.left = insert(n.left, p, !isVertical);
            }
            else {
                n.right = insert(n.right, p, !isVertical);
            }
        }

        return n;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(this.root, p);
    }

    private boolean contains(Node n, Point2D p) {
        if (n == null) {
            return false;
        }

        if (n.point.x() == p.x() && n.point.y() == p.y()) {
            return true;
        }

        if (n.isVertical) {
            if (p.x() < n.point.x()) {
                return contains(n.left, p);
            }
            else {
                return contains(n.right, p);
            }
        }

        else {
            if (p.y() < n.point.y()) {
                return contains(n.left, p);
            }
            else {
                return contains(n.right, p);
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(this.root, new RectHV(0.0, 0.0, 1.0, 1.0));
    }

    private void draw(Node n, RectHV rectHV) {
        if (n == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.point.draw();

        if (n.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            RectHV rect = new RectHV(n.point.x(), rectHV.ymin(), n.point.x(), rectHV.ymax());
            rect.draw();
            draw(n.left, new RectHV(rectHV.xmin(), rectHV.ymin(), n.point.x(), rectHV.ymax()));
            draw(n.right, new RectHV(n.point.x(), rectHV.ymin(), rectHV.xmax(), rectHV.ymax()));
        }

        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            RectHV rect = new RectHV(rectHV.xmin(), n.point.y(), rectHV.xmax(), n.point.y());
            rect.draw();
            draw(n.left, new RectHV(rectHV.xmin(), rectHV.ymin(), rectHV.xmax(), n.point.y()));
            draw(n.right, new RectHV(rectHV.xmin(), n.point.y(), rectHV.xmax(), rectHV.ymax()));
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> list = new LinkedList<>();
        range(rect, new RectHV(0.0, 0.0, 1.0, 1.0), list, this.root);
        return list;
    }

    private void range(RectHV rect, RectHV nodeRect, List<Point2D> list, Node n) {
        if (n == null) {
            return;
        }

        if (!nodeRect.intersects(rect)) {
            return;
        }

        if (rect.contains(n.point)) {
            list.add(n.point);
        }

        if (n.isVertical) {
            range(rect, new RectHV(nodeRect.xmin(), nodeRect.ymin(), n.point.x(), nodeRect.ymax()),
                  list, n.left);
            range(rect, new RectHV(n.point.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax()),
                  list, n.right);
        }
        else {
            range(rect, new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), n.point.y()),
                  list, n.left);
            range(rect, new RectHV(nodeRect.xmin(), n.point.y(), nodeRect.xmax(), nodeRect.ymax()),
                  list, n.right);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }

        Node nearestNode = new Node(root.point, true);
        nearestNode.left = root.left;
        nearestNode.right = root.right;
        nearest(root, nearestNode, new RectHV(0.0, 0.0, 1.0, 1.0), p);
        return nearestNode.point;
    }

    private void nearest(Node node, Node nearestNode, RectHV r, Point2D p) {
        if (node == null) {
            return;
        }

        if (p.distanceSquaredTo(node.point) < p.distanceSquaredTo(nearestNode.point)) {
            nearestNode.point = node.point;
        }

        if (node.isVertical) {
            RectHV lRect = new RectHV(r.xmin(), r.ymin(), node.point.x(), r.ymax());
            RectHV rRect = new RectHV(node.point.x(), r.ymin(), r.xmax(), r.ymax());

            if (p.x() <= node.point.x()) {
                nearest(node.left, nearestNode, lRect, p);
                if (rRect.distanceSquaredTo(p) < p.distanceSquaredTo(nearestNode.point)) {
                    nearest(node.right, nearestNode, rRect, p);
                }
            }
            else {
                nearest(node.right, nearestNode, rRect, p);
                if (lRect.distanceSquaredTo(p) < p.distanceSquaredTo(nearestNode.point)) {
                    nearest(node.left, nearestNode, lRect, p);
                }
            }
        }

        else {
            RectHV lRect = new RectHV(r.xmin(), r.ymin(), r.xmax(), node.point.y());
            RectHV rRect = new RectHV(r.xmin(), node.point.y(), r.xmax(), r.ymax());

            if (p.y() <= node.point.y()) {
                nearest(node.left, nearestNode, lRect, p);
                if (rRect.distanceSquaredTo(p) < p.distanceSquaredTo(nearestNode.point)) {
                    nearest(node.right, nearestNode, rRect, p);
                }
            }
            else {
                nearest(node.right, nearestNode, rRect, p);
                if (lRect.distanceSquaredTo(p) < p.distanceSquaredTo(nearestNode.point)) {
                    nearest(node.left, nearestNode, lRect, p);
                }
            }
        }
    }

    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        kdtree.draw();
        StdDraw.show();

        RectHV rect = new RectHV(0.7, 0.7,
                                 0.9, 0.9);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        rect.draw();

        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (Point2D p : kdtree.range(rect))
            p.draw();
    }
}
