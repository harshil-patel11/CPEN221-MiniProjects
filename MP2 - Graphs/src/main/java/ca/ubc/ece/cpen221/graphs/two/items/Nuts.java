package ca.ubc.ece.cpen221.graphs.two.items;

import ca.ubc.ece.cpen221.graphs.two.Location;
import ca.ubc.ece.cpen221.graphs.two.Util;

import javax.swing.ImageIcon;

/**
 * Nuts will be grown by the {@link Gardener} every step at an empty location
 * if fewer than half of all locations in the world are occupied.
 */
public class Nuts implements Item {
    private final static ImageIcon nutsImage = Util.loadImage("nuts.gif");

    private Location location;
    private boolean isDead;

    /**
     * Produce Nuts at <code> location </code>. The location must be valid and
     * empty
     *
     * @param location : the location where nuts will be created
     */
    public Nuts(Location location) {
        this.location = location;
        this.isDead = false;
    }

    @Override
    public ImageIcon getImage() {
        return nutsImage;
    }

    @Override
    public String getName() {
        return "nuts";
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getPlantCalories() {
        return 20;
    }

    @Override
    public int getMeatCalories() {
        return 0;
    }

    @Override
    public void loseEnergy(int energy) {
        // Dies if loses energy.
        isDead = true;
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    public int getStrength() {
        return 10;
    }

}