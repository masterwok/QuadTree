package quadtree;

import java.awt.Color;
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
    private int level;
    private int maxLevel;
    private QuadTree northWest;
    private QuadTree northEast;
    private QuadTree southWest;
    private QuadTree southEast;
    private Region region;

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

        this.region = region;
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

    public void clear() {
        particles.clear();
        northWest = northEast = southWest = southEast = null;
    }

    public void checkForCollisions() {
        Particle p1, p2;
        boolean hadCollision = false;

        // Only run check if there is more than one particle in the node
        if (particles.size() > 1) {

            // Check for collision between each particle
            for (int i = 0; i < particles.size() - 1; i++) {
                p1 = particles.get(i);
                for (int n = i + 1; n < particles.size(); n++) {
                    p2 = particles.get(n);
                    p1.handlePossibleCollision(p2);
                }
            }
        }

        // If no children then return
        if (northEast == null)
            return;

        // Recursively run same check on the children
        northEast.checkForCollisions();
        northWest.checkForCollisions();
        southWest.checkForCollisions();
        southEast.checkForCollisions();

    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public ArrayList<QuadTree> getQuads() {
        ArrayList<QuadTree> quads = new ArrayList<>();

        //System.out.println("[Region " + region + "] Number of particles = " + particles.size());

        if (particles.size() > 0)
            quads.add(this);

        // Return if we reached a leaf node
        if (northEast == null)
            return quads;

        quads.addAll(northEast.getQuads());
        quads.addAll(northWest.getQuads());
        quads.addAll(southWest.getQuads());
        quads.addAll(southEast.getQuads());

        return quads;
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

        // Cannot be added if outside of boundary
        if (!boundary.contains(p))
            return false;

        // If below capacity and a leaf node add to node
        if (particles.size() < capacity && northEast == null) {
            particles.add(p);
            return true;
        }

        if (level < maxLevel) {
            // If at capacity and a leaf node subdivide
            if (particles.size() == capacity && northEast == null)
                subdivide();

            if (northEast.insert(p))
                return true;
            if (northWest.insert(p))
                return true;
            if (southWest.insert(p))
                return true;
            if (southEast.insert(p))
                return true;

        } else {
            particles.add(p);
            return true;
        }
        return false;
    }

    public void subdivide() {

        // Create children of this quad tree
        northEast = new QuadTree(boundary, Region.NORTH_EAST, level, maxLevel, capacity);
        northWest = new QuadTree(boundary, Region.NORTH_WEST, level, maxLevel, capacity);
        southWest = new QuadTree(boundary, Region.SOUTH_WEST, level, maxLevel, capacity);
        southEast = new QuadTree(boundary, Region.SOUTH_EAST, level, maxLevel, capacity);

        // Bump the particles down to the children
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

        // There should be no particles in current node at this point
        particles.clear();
    }
}
