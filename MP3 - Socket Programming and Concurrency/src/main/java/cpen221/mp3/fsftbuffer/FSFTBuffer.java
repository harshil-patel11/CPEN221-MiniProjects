package cpen221.mp3.fsftbuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FSFTBuffer<T extends Bufferable> {
    private static final boolean DEBUG = false;

    /* the default buffer size is 32 objects */
    public static final int DSIZE = 32;

    /* the default timeout value is 3600s */
    public static final int DTIMEOUT = 3600;

    public static final long SECONDS_TO_MILLIS = 1000;

    private final int capacity;
    private final long timeout;
    private final Map<String, T> buffer;
    private final Map<String, Long> timeMap;

    /*
     * RI:
     * buffer.keySet().equals(timeMap.keySet()) -> keySet of buffer and timeMap must match
     * buffer.keySet().size() <= capacity -> buffer and timeMap never have more keys than capacity
     *
     * AF:
     * The keys of buffer and timeMap contain the ids of various bufferables in the FSFTBuffer.
     * Each key in buffer maps to an object in the FSFTBuffer with that id()
     * Each key in timeMap maps to a Long representing the time (in ms) at which the object
     * mapped to by that key in timeMap will become stale (no longer in the FSFTBuffer)
     * capacity represents the number of bufferables allowed in the FSFTBuffer
     * timeout represents the amount of time (in ms) that a bufferable can last in the FSFTBuffer
     * without being accessed, updated, or touched
     *
     * Thread Safety Conditions:
     * Used synchronization to safeguard all mutable fields (ie. buffer and timeMap). Also used
     * thread safe Maps for buffer and timeMap
     */

    /**
     * Create a buffer with a fixed capacity and a timeout value.
     * Objects in the buffer that have not been refreshed within the
     * timeout period are removed from the cache.
     *
     * @param capacity the number of objects the buffer can hold. capacity > 0.
     * @param timeout  the duration, in seconds, an object should
     *                 be in the buffer before it times out. timeout > 0
     */
    public FSFTBuffer(int capacity, int timeout) {
        this.capacity = capacity;
        this.timeout = timeout * SECONDS_TO_MILLIS;
        buffer = Collections.synchronizedMap(new HashMap<>());
        timeMap = Collections.synchronizedMap(new HashMap<>());

        if (DEBUG) {
            checkRep();
        }
    }

    public FSFTBuffer(Map<T, Long> initialTimes, int capacity, int timeout) {
        this.capacity = capacity;
        this.timeout = timeout * SECONDS_TO_MILLIS;
        buffer = Collections.synchronizedMap(new HashMap<>());
        timeMap = Collections.synchronizedMap(new HashMap<>());

        for (T obj : initialTimes.keySet()) {
            buffer.put(obj.id(), obj);
            timeMap.put(obj.id(), initialTimes.get(obj));
        }

        if (DEBUG) {
            checkRep();
        }
    }

    /**
     * Create a buffer with default capacity and timeout values.
     */
    public FSFTBuffer() {
        this(DSIZE, DTIMEOUT);
    }

    /**
     * Effects: mutates the timeMap set
     * <p>
     * Add a value to the buffer, unless a bufferable with the same id is already in the buffer
     * If the buffer is full then remove the least recently accessed
     * object to make room for the new object.
     */
    synchronized public boolean put(T t) {
        if (buffer.containsKey(t.id()) && !isStale(t.id())) {
            if (DEBUG) {
                System.out.println("Did not put " + t.id());
            }

            return false;
        }

        if (buffer.keySet().size() == capacity) {
            updateBuffer();
        }

        if (buffer.keySet().size() == capacity) {

            List<String> list = new ArrayList<>(timeMap.keySet());

            String id = timeMap.keySet().parallelStream()
                .reduce(list.get(0), (x, y) -> (timeMap.get(x) < timeMap.get(y)) ? x : y);

            timeMap.remove(id);
            buffer.remove(id);
        }

        buffer.put(t.id(), t);
        timeMap.put(t.id(), System.currentTimeMillis() + timeout);

        if (DEBUG) {
            checkRep();
            System.out.println("put " + t.id());
        }
        return true;
    }

    /**
     * @param id the identifier of the object to be retrieved
     * @return the object that matches the identifier from the
     * buffer
     * Throws IDNotFoundException if no bufferable in the buffer has t.id() equal to id
     */
    public T get(String id) throws IDNotFoundException {

        if (touch(id)) {
            return buffer.get(id);
        }

        throw new IDNotFoundException("id not found in the buffer");

        /* Do not return null. Throw a suitable checked exception when an object
            is not in the cache. You can add the checked exception to the method
            signature. */
    }

    /**
     * Update the last refresh time for the object with the provided id.
     * This method is used to mark an object as "not stale" so that its
     * timeout is delayed.
     *
     * @param id the identifier of the object to "touch"
     * @return true if successful and false otherwise (if bufferable with the id is not found)
     */
    public boolean touch(String id) {
        synchronized (this) {
            long currentTime = System.currentTimeMillis();
            boolean isValid = buffer.containsKey(id) && !isStale(id);

            if (!isValid) {
                buffer.remove(id);
                timeMap.remove(id);
                return false;
            }

            timeMap.replace(id, currentTime + timeout);
        }

        if (DEBUG) {
            checkRep();
        }
        return true;
    }

    /**
     * Gets a timeMap, which maps a bufferable to the last time it was accessed
     *
     * @return a timeMap representing the current state of the buffer
     */
    synchronized public Map<T, Long> getObjectTimeMap() {
        Map<T, Long> objectTimeMap = Collections.synchronizedMap(new HashMap<>());

        for (String s : buffer.keySet()) {
            objectTimeMap.put(buffer.get(s), timeMap.get(s));
        }

        return objectTimeMap;
    }

    /**
     * Update an object in the buffer.
     * This method updates an object and acts like a "touch" to
     * renew the object in the cache.
     *
     * @param t the object to update.
     * @return true if successful and false otherwise (if no object in this buffer has the same id
     * as t)
     */
    synchronized public boolean update(T t) {
        if (DEBUG) {
            checkRep();
        }
        buffer.replace(t.id(), t);
        return touch(t.id());
    }

    /**
     * Checks if the object with a certain id inside the buffer is stale or not.
     * Returns true if the object is stale (timed out), or not in the buffer.
     *
     * @param id : id of an object
     * @return true if the object is stale (timed out) and false otherwise.
     */
    synchronized public boolean isStale(String id) {
        long currentTime = System.currentTimeMillis();
        boolean mapHasId = !(timeMap.get(id) == null);


        return mapHasId && timeMap.get(id) < currentTime;
    }

    /**
     * Updates buffer and returns a set containing all nonstale items in the buffer
     *
     * @return the set of nonstale items in the buffer
     */
    synchronized public Set<T> bufferableSet() {
        updateBuffer();

        return new HashSet<>(buffer.values());
    }

    /**
     * Updates the buffer and timeMap by removing objects that have timed out.
     * modifies: <strong>timeMap</strong> and <strong>buffer</strong>
     *
     * @return true if the size of the buffer is less than the initial size,
     * false otherwise
     */
    synchronized private boolean updateBuffer() {
        long currentTime = System.currentTimeMillis();
        int prevBufferSize = buffer.size();

        Set<String> validKeys =
            timeMap.keySet().parallelStream().filter(x -> timeMap.get(x) > currentTime)
                .collect(Collectors.toSet());

        timeMap.keySet().removeIf(x -> !validKeys.contains(x));
        buffer.keySet().removeIf(x -> !validKeys.contains(x));

        if (DEBUG) {
            checkRep();
        }
        return prevBufferSize > buffer.size();
    }

    /**
     * This method verifies that the representation invariant is not violated
     */
    synchronized private void checkRep() {
        assert buffer.size() == timeMap.size() :
            "buffer.size() and timeMap.size() are not equal";

        assert buffer.keySet().equals(timeMap.keySet()) :
            "buffer.keySet and timeMap.keySet are not equal";

        assert buffer.size() <= capacity : "buffer size exceeds capacity";
    }
}
