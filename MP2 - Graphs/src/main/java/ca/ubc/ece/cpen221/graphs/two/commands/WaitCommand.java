package ca.ubc.ece.cpen221.graphs.two.commands;

import ca.ubc.ece.cpen221.graphs.two.World;

/**
 * A WaitCommand is a {@link Command} which represents doing nothing
 */
public final class WaitCommand implements Command {

    @Override
    public void execute(World w) {
        // Do nothing.
    }

}
