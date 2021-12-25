package ca.ubc.ece.cpen221.graphs.two.ai;

import ca.ubc.ece.cpen221.graphs.two.ArenaWorld;
import ca.ubc.ece.cpen221.graphs.two.commands.Command;
import ca.ubc.ece.cpen221.graphs.two.items.animals.ArenaAnimal;

/**
 * The AI interface for all animal AIs. Your implementations of FoxAI and
 * RabbitAI must implement this interface, with no constructor.
 */
public interface AI {

    /**
     * Decides the next action to be taken, given the state of the World and the
     * animal.
     *
     * @param world  the current World
     * @param animal the animal waiting for the next action
     * @return the next action for animal
     */
    Command getNextAction(ArenaWorld world, ArenaAnimal animal);

}
