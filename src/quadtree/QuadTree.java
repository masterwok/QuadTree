package quadtree;

import java.awt.Point;
import java.util.ArrayList;
import javax.media.opengl.GL2;

/**
 *
 * @author Jonathan Trowbridge
 */
public class QuadTree {

    private static final int CAPACITY = 6;       // Max objects in node
    private BoundingBox boundary;
    private ArrayList<Point> points = new ArrayList<>(); // Points in this quad tree
    private int pointIndex = 0;
    private int level;
    private int maxLevel;
    private QuadTree northWest;
    private QuadTree northEast;
    private QuadTree southWest;
    private QuadTree southEast;

    public static enum Region {

        NORTH_EAST, NORTH_WEST, SOUTH_WEST, SOUTH_EAST
    }

    public QuadTree(int width, int height, int maxLevel) {
        this.boundary = new BoundingBox(0, height, width, height);
        this.level = 0;
        this.maxLevel = maxLevel;
    }

    private QuadTree(BoundingBox parent, Region region, int level, int maxLevel) {
        float x = 0, y = 0;

        this.level = level + 1;
        this.maxLevel = maxLevel;

        switch (region) {
            case NORTH_EAST:
                x = parent.getX() + (parent.getWidth() / 2);
                y = parent.getY();
                break;
            case NORTH_WEST:
                x = parent.getX();
                y = parent.getY();
                break;
            case SOUTH_WEST:
                x = parent.getX();
                y = parent.getY() - (parent.getWidth() / 2);
                break;
            case SOUTH_EAST:
                x = parent.getX() + (parent.getWidth() / 2);
                y = parent.getY() - (parent.getWidth() / 2);
                break;
        }

        this.boundary = new BoundingBox(x, y, parent.getWidth() / 2, parent.getHeight() / 2);
    }

    public void draw(GL2 gl) {

        // Draw the bounding box
        boundary.draw(gl);

        // Check to see if there's children
        if (northEast == null)
            return;

        // Draw the children recursively
        northEast.draw(gl);
        northWest.draw(gl);
        southWest.draw(gl);
        southEast.draw(gl);
    }

    public boolean insert(Point p) {

        // Ignore points that are not contained in this bounding box
        if (!boundary.contains(p))
            return false;

        // If still below capacity add it to this quadtree
        if (points.size() < CAPACITY || level == maxLevel) {
            points.add(p);
            return true;
        }

        // Above capacity so subdivide and add it to accepting child node
        if (northEast == null)
            subdivide();

        if (northEast.insert(p) == true)
            return true;
        if (northWest.insert(p) == true)
            return true;
        if (southWest.insert(p) == true)
            return true;
        if (southEast.insert(p) == true)
            return true;

        // Something went wrong if we got this far
        return false;
    }

    public void subdivide() {
        // Create children of this quad tree
        northEast = new QuadTree(boundary, Region.NORTH_EAST, level, maxLevel);
        northWest = new QuadTree(boundary, Region.NORTH_WEST, level, maxLevel);
        southWest = new QuadTree(boundary, Region.SOUTH_WEST, level, maxLevel);
        southEast = new QuadTree(boundary, Region.SOUTH_EAST, level, maxLevel);

        // Put the points that exist in this node into the children
        for (Point p : points) {
            if (northEast.insert(p))
                continue;
            if (northWest.insert(p))
                continue;
            if (southWest.insert(p))
                continue;
            if (southEast.insert(p))
                continue;
        }

        // Since we put the points into the children this node is said to not
        // contain any points
        points.clear();
    }
}
