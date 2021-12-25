package ca.ubc.ece.cpen221.graphs.two.items.vehicles;

import ca.ubc.ece.cpen221.graphs.two.Actor;
import ca.ubc.ece.cpen221.graphs.two.Direction;
import ca.ubc.ece.cpen221.graphs.two.items.MoveableItem;

public interface ArenaVehicle extends MoveableItem, Actor {

    /**
     * Accelerates a vehicle based on its maximum acceleration
     *
     * @return true if the vehicle accelerated, false otherwise
     */
    boolean accelerate();

    /**
     * Decelerates a vehicle based on its maximum deceleration
     *
     * @return true if the vehicle was able to brake (lose kinetic energy),
     * false if it did not brake (kinetic energy did not change)
     */
    boolean brake();

    /**
     * Returns true if the vehicle is able to turn at its current speed (kinetic energy)
     *
     * @return true if the vehicle is able to turn, false otherwise
     */
    boolean canTurn();

    /**
     * Returns ture if the vehicle turns to the given direction
     *
     * @param direction the direction to turn to
     * @return true if the vehicle turned into the direction, false otherwise
     */
    boolean turn(Direction direction);
}
