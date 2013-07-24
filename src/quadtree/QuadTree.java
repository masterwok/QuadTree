package quadtree;

import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL2;

/**
 *
 * @author Jonathan Trowbridge
 */
public class QuadTree {

    private static final int MAX_LEVEL = 2;
    private static final int MAX_CAPACITY = 1;
    private BoundingBox boundary;
    public List<BoundingBox> objects;
    private int level;
    private QuadTree NORTH_EAST, NORTH_WEST, SOUTH_WEST, SOUTH_EAST;

    public QuadTree(int level, BoundingBox boundary) {
        objects = new ArrayList();
        this.level = level;
        this.boundary = boundary;
    }

    public void draw(GL2 gl) {
        boundary.draw(gl);
        if (NORTH_EAST != null) {
            NORTH_EAST.draw(gl);
            NORTH_WEST.draw(gl);
            SOUTH_WEST.draw(gl);
            SOUTH_EAST.draw(gl);
        }
    }

    public boolean insert(BoundingBox b) {

        if (!boundary.contains(b.x, b.y, b.width, b.height))
            return false;

        if (objects.size() < MAX_CAPACITY && NORTH_EAST == null) {
            objects.add(b);
            return true;
        }

        if (level < MAX_LEVEL) {
            if (objects.size() == MAX_CAPACITY && NORTH_EAST == null)
                split();

            if (NORTH_EAST.insert(b))
                return true;
            else if (NORTH_WEST.insert(b))
                return true;
            else if (SOUTH_WEST.insert(b))
                return true;
            else if (SOUTH_EAST.insert(b))
                return true;

        }

        objects.add(b);
        return true;

    }

    public List retrieve(List returnObjects, BoundingBox b) {
        return null;
    }

    public void split() {
        float subWidth = boundary.width / 2;
        float subHeight = boundary.height / 2;
        NORTH_EAST = new QuadTree(level + 1, new BoundingBox(boundary.x + subWidth, boundary.y, subWidth, subHeight));
        NORTH_WEST = new QuadTree(level + 1, new BoundingBox(boundary.x, boundary.y, subWidth, subHeight));
        SOUTH_WEST = new QuadTree(level + 1, new BoundingBox(boundary.x, boundary.y - subHeight, subWidth, subHeight));
        SOUTH_EAST = new QuadTree(level + 1, new BoundingBox(boundary.x + subWidth, boundary.y - subHeight, subWidth, subHeight));

        // Bump the particles down to the children
        for (BoundingBox b : objects) {
            if (NORTH_EAST.insert(b))
                continue;
            if (NORTH_WEST.insert(b))
                continue;
            if (SOUTH_WEST.insert(b))
                continue;
            if (SOUTH_EAST.insert(b))
                continue;
        }

        // There should be no particles in current node at this point
        objects.clear();
    }

    public void clear() {
        objects.clear();
        NORTH_EAST = NORTH_WEST = SOUTH_EAST = SOUTH_WEST = null;
    }

    @Override
    public String toString() {
        if (NORTH_EAST != null)
            return String.format("ROOT = %d, NE = %d, NW = %d, SW = %d, SE = %d", objects.size(), NORTH_EAST.objects.size(), NORTH_WEST.objects.size(), SOUTH_WEST.objects.size(), SOUTH_EAST.objects.size())
                    + String.format("\n  %s", NORTH_EAST.toString())
                    + String.format("\n  %s", NORTH_WEST.toString())
                    + String.format("\n  %s", SOUTH_WEST.toString())
                    + String.format("\n  %s", SOUTH_EAST.toString());
        //else
        //     return String.format("(%f, %f) = %d", boundary.x, boundary.y, objects.size());
        return "";
    }
}