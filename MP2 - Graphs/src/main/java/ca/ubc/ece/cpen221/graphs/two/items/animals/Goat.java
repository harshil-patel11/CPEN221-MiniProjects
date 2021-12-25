package ca.ubc.ece.cpen221.graphs.two.items.animals;

import ca.ubc.ece.cpen221.graphs.two.Food;
import ca.ubc.ece.cpen221.graphs.two.Location;
import ca.ubc.ece.cpen221.graphs.two.Util;
import ca.ubc.ece.cpen221.graphs.two.World;
import ca.ubc.ece.cpen221.graphs.two.ai.AI;
import ca.ubc.ece.cpen221.graphs.two.commands.Command;
import ca.ubc.ece.cpen221.graphs.two.items.Grass;
import ca.ubc.ece.cpen221.graphs.two.items.LivingItem;

import javax.swing.ImageIcon;

/**
 * The {@link Goat} is an {@link ArenaAnimal} that tries to eat {@link Grass}
 */
public class Goat implements ArenaAnimal {

    private static final int INITIAL_ENERGY = 40;
    private static final int MAX_ENERGY = 50;
    private static final int STRENGTH = 30;
    private static final int VIEW_RANGE = 2;
    private static final int MIN_BREEDING_ENERGY = 10;
    private static final int COOLDOWN = 2;
    private static final ImageIcon goatImage = Util.loadImage("goat.gif");

    private final AI ai;

    private Location location;
    private int energy;

    /**
     * Create a new {@link Goat} at
     * <code>initialLocation</code> that eats {@link Grass}. The <code> initialLocation </code> must be
     * valid and empty
     *
     * @param goatAI          : The AI designed for goats
     * @param initialLocation the location where this Goat will be created
     */
    public Goat(AI goatAI, Location initialLocation) {
        ai = goatAI;
        this.location = initialLocation;
        this.energy = INITIAL_ENERGY;
    }

    @Override
    public LivingItem breed() {
        Goat child = new Goat(ai, location);
        child.energy = energy / 2;
        this.energy = energy / 2;
        return child;
    }

    @Override
    public void eat(Food food) {
        // Note that energy does not exceed energy limit.
        energy = Math.min(MAX_ENERGY, energy + food.getMeatCalories());
    }

    @Override
    public int getCoolDownPeriod() {
        return COOLDOWN;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public ImageIcon getImage() {
        return goatImage;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }

    @Override
    public int getMeatCalories() {
        // The amount of meat calories it provides is equal to its current
        // energy.
        return energy;
    }

    @Override
    public int getMinimumBreedingEnergy() {
        return MIN_BREEDING_ENERGY;
    }

    @Override
    public int getMovingRange() {
        return 1; // Can only move to adjacent locations.
    }

    @Override
    public String getName() {
        return "goat";
    }

    @Override
    public Command getNextAction(World world) {
        Command nextAction = ai.getNextAction(world, this);
        this.energy--;
        return nextAction;
    }

    @Override
    public int getPlantCalories() {
        // This goat is not a plant.
        return 0;
    }

    @Override
    public int getStrength() {
        return STRENGTH;
    }

    @Override
    public int getViewRange() {
        return VIEW_RANGE;
    }

    @Override
    public boolean isDead() {
        return energy <= 0;
    }

    @Override
    public void loseEnergy(int energyLoss) {
        this.energy = this.energy - energyLoss;
    }

    @Override
    public void moveTo(Location targetLocation) {
        location = targetLocation;
    }


}
