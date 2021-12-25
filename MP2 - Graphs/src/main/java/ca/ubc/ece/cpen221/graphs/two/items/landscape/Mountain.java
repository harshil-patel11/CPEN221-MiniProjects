package ca.ubc.ece.cpen221.graphs.two.items.landscape;

import ca.ubc.ece.cpen221.graphs.two.Location;
import ca.ubc.ece.cpen221.graphs.two.Util;
import ca.ubc.ece.cpen221.graphs.two.items.Item;
import javax.swing.ImageIcon;

public class Mountain implements Item {
    private static final int STRENGTH = Integer.MAX_VALUE;
    private static final ImageIcon mountainImage = Util.loadImage("mountain.gif");

    private final Location location;

    /*
     * RI:
     * Nothing (all fields are final and immutable already)
     *
     * AF:
     * location represents the location of the mountain in the world, and STRENGTH represents how
     * strong it is, which is practically infinite since... well... its a mountain!
     *
     */

    public Mountain (Location location) {
        this.location = location;
    }

    public ImageIcon getImage() {
        return mountainImage;
    }

    public String getName(){
        return "mountain";
    }

    public Location getLocation() {
        return location;
    }

    public int getStrength() {
        return STRENGTH;
    }

    public void loseEnergy(int energy){
        // a Mountain is not a living thing so it doesn't have energy
        return;
    }

    public boolean isDead() {
        // a Mountain is not a living thing and can never be destroyed
        return true;
    }

    @Override
    public int getPlantCalories() {
        // a Mountain is not a plant
        return 0;
    }

    @Override
    public int getMeatCalories() {
        // a Mountain is not an animal
        return 0;
    }
}
