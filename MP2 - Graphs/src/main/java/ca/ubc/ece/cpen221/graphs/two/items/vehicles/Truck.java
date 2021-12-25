package ca.ubc.ece.cpen221.graphs.two.items.vehicles;

import ca.ubc.ece.cpen221.graphs.two.Direction;
import ca.ubc.ece.cpen221.graphs.two.Location;
import ca.ubc.ece.cpen221.graphs.two.Util;
import ca.ubc.ece.cpen221.graphs.two.World;
import ca.ubc.ece.cpen221.graphs.two.commands.Command;
import ca.ubc.ece.cpen221.graphs.two.commands.MoveCommand;

import javax.swing.ImageIcon;
import java.util.Random;

public class Truck implements ArenaVehicle {

    private static final int INITIAL_ENERGY = 100;
    private static final int STRENGTH = 300;
    private static final int INITIAL_KINETIC_ENERGY = 10;
    private static final int KINETIC_ENERGY_DIVISOR = 10;
    private static final int MAX_COOLDOWN = 11;
    private static final int MAX_MOVING_RANGE = 1;
    private static final int MAX_TURN_ENERGY = 30;

    private static final int ACCELERATION_ENERGY = 5;
    private static final ImageIcon truckImage = Util.loadImage("truck.gif");

    private Location location;
    private Direction direction;
    private int energy;
    private int kineticEnergy;
    private int potentialEnergy;
    private boolean crashed;

    /*
     * Representation Invariant:
     * kineticEnergy + potentialEnergy == energy
     * 0 <= INITIAL_KINETIC_ENERGY <= ENERGY
     * energy / KINETIC_ENERGY_DIVISOR < MAX_COOLDOWN
     *
     *
     * Abstraction Function:
     * location represents the location of the vehicle in the world
     * direction represents the direction of travel of the vehicle
     * energy is the total energy a vehicle has at any given time
     * MAX_MOVING_RANGE is the farthest a vehicle can move in any given time step
     * kineticEnergy is proportional to the speed of the vehicle
     * potentialEnergy is proportional to how much faster the vehicle can get
     * MAX_TURN_ENERGY is the highest kinetic energy at which a vehicle can turn
     * cooldown is how long the vehicle needs to wait before making another move. The faster the
     * vehicle is going (higher kineticEnergy) the lower the cooldown should be.
     */

    /**
     * Initializes the location and instance variables of a Truck object
     *
     * @param initialLocation the initial location where the truck
     *                        will be created
     */
    public Truck(Location initialLocation) {
        location = initialLocation;
        energy = INITIAL_ENERGY;
        potentialEnergy = energy;
        kineticEnergy = 0;
        crashed = false;
        direction = Direction.NORTH;
    }

    public Command getNextAction(World world) {
        Random rand = new Random();
        int action = rand.nextInt(6);

        switch(action) {
            case 0:
                accelerate();
            case 1:
                brake();
            case 2:
                turn(Direction.EAST);
            case 3:
                turn(Direction.WEST);
            case 4:
                turn(Direction.NORTH);
            case 5:
                turn(Direction.SOUTH);
        }

        return new MoveCommand(this, new Location(this.location, this.direction));
    }

    public int getCoolDownPeriod() {
        return MAX_COOLDOWN - kineticEnergy / KINETIC_ENERGY_DIVISOR;
    }

    @Override
    public void moveTo(Location targetLocation) {
        location = targetLocation;
    }

    @Override
    public int getMovingRange() {
        return MAX_MOVING_RANGE;
    }

    @Override
    public ImageIcon getImage() {
        return truckImage;
    }

    @Override
    public String getName() {
        return "truck";
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void loseEnergy(int energy) {
        this.kineticEnergy -= energy;
    }

    public boolean accelerate() {
        if (kineticEnergy < energy) {
            kineticEnergy = Math.min(kineticEnergy + ACCELERATION_ENERGY, energy);
            potentialEnergy = energy - kineticEnergy;
            return true;
        }
        return false;
    }

    public boolean brake() {
        if (kineticEnergy > 0) {
            kineticEnergy = Math.max(kineticEnergy - ACCELERATION_ENERGY, 0);
            potentialEnergy = energy - kineticEnergy;
            return true;
        }
        return false;
    }

    public boolean canTurn() {
        return kineticEnergy <= MAX_TURN_ENERGY;
    }

    public boolean turn(Direction direction) {
        if (canTurn()) {
            this.direction = direction;
            return true;
        }
        return false;
    }

    @Override
    public boolean isDead() {
        return crashed;
    }

    @Override
    public int getStrength() {
        return STRENGTH;
    }

    @Override
    public int getPlantCalories() {
        // A truck is not a plant
        return 0;
    }

    @Override
    public int getMeatCalories() {
        // A truck is not meat
        return 0;
    }
}
