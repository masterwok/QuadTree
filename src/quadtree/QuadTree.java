package quadtree;

import java.util.ArrayList;
import javax.media.opengl.GL2;
import quadtree.objects.Particle;

/**
 *
 * @author Jonathan Trowbridge
 */
public class QuadTree {

    private int capacity;
    private BoundingBox boundary;
    private ArrayList<Particle> particles = new ArrayList<>(); // Points in this quad tree
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

    public QuadTree(int width, int height, int maxLevel, int capacity) {
        this.boundary = new BoundingBox(0, height, width, height);
        this.level = 0;
        this.maxLevel = maxLevel;
        this.capacity = capacity;
    }

    private QuadTree(BoundingBox parent, Region region, int level, int maxLevel, int capacity) {
        float x = 0, y = 0;

        this.level = level + 1;
        this.maxLevel = maxLevel;
        this.capacity = capacity;

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

    public boolean insert(Particle p) {

        // Ignore points that are not contained in this bounding box
        if (!boundary.contains(p))
            return false;

        // If still below capacity add it to this quadtree
        if (particles.size() < capacity || level == maxLevel) {
            particles.add(p);
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
        northEast = new QuadTree(boundary, Region.NORTH_EAST, level, maxLevel, capacity);
        northWest = new QuadTree(boundary, Region.NORTH_WEST, level, maxLevel, capacity);
        southWest = new QuadTree(boundary, Region.SOUTH_WEST, level, maxLevel, capacity);
        southEast = new QuadTree(boundary, Region.SOUTH_EAST, level, maxLevel, capacity);

        // Put the points that exist in this node into the children
        for (Particle p : particles) {
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
        particles.clear();
    }
}
