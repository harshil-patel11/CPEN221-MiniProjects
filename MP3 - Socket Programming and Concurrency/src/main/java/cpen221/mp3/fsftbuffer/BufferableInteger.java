package cpen221.mp3.fsftbuffer;

public class BufferableInteger implements Bufferable {
    int val;

    /*
     * RI:
     * val is not null
     *
     * AF:
     * val corresponds to a BufferableInteger with a value equal to val and an id that is the string
     * representation of the integer val.
     *
     * Thread Safety Condition:
     * This datatype is immutable
     */

    /**
     * Creates a new Bufferable integer with value = val
     *
     * @param val the value to assign to the bufferable integer
     */
    public BufferableInteger(int val) {
        this.val = val;
    }

    /**
     * Gives the id of the bufferable integer
     *
     * @return the string representation of the value of the bufferable integer
     */
    @Override
    public String id() {
        return Integer.toString(val);
    }
}
