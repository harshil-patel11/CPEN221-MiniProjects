package cpen221.mp3.wikimediator;

import cpen221.mp3.fsftbuffer.Bufferable;

public class Query implements Bufferable, Comparable<Query> {
    private final String query;
    private int searches;

    /*
     * Immutable datatype that represents a query
     *
     * RI: all fields are not null
     *
     * AF:
     * query represents a search criteria
     * searches represents the number of times a query has been used
     */

    /**
     * Default constructor that creates a Query object
     *
     * @param query a search criteria
     */
    public Query(String query) {
        this.query = query;
        searches = 1;
    }

    /**
     * @return the number of searches that one Query object was used for
     */
    public int numSearches() {
        return searches;
    }

    /**
     * Adds a search to a Query object
     *
     * @modifies searches: adds a search
     */
    public synchronized void addSearch() {
        searches++;
    }

    /**
     * @return the name of the query
     */
    @Override
    public String toString() {
        return query;
    }

    /**
     * @return the Query id, which is simply the name (query)
     */
    @Override
    public String id() {
        return query;
    }

    /**
     * Compares one Query object to another
     *
     * @param other
     * @return 1 if this.numSearches > other.numSearches,
     * 0 if this.numSearches = other.numSearches,
     * -1 if this.numSearches < other.numSearches.
     */
    @Override
    public synchronized int compareTo(Query other) {
        if (other.numSearches() == searches) {
            return 0;
        } else if (other.numSearches() < searches) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Checks behavioural equality of two Query objects
     *
     * @param other the second Query object
     * @return true if they are equal, false otherwise
     */
    @Override
    public synchronized boolean equals(Object other) {
        if (other instanceof Query) {
            return query.equals((other).toString());
        }

        return false;
    }

    /**
     * hashCode for a Query object
     *
     * @return query.length() as the hash code
     */
    @Override
    public synchronized int hashCode() {
        return query.length();
    }

}
