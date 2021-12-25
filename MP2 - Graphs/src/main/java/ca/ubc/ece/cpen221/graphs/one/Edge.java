package ca.ubc.ece.cpen221.graphs.one;

import ca.ubc.ece.cpen221.graphs.core.Vertex;

public class Edge<T> {
    private final Vertex<T> v1;
    private final Vertex<T> v2;
    private final int hashCode;

    /*
     * Rep Invariants:
     * Vertices v1, v2 are non-null
     *
     * Abstraction Function:
     * Represents an edge from v1 to v2.
     */

    /**
     * @param v1 The vertex that defines the head of the edge
     * @param v2 The vertex that defines the tail of the edge
     */
    public Edge(Vertex<T> v1, Vertex<T> v2) {
        this.v1 = new Vertex<>(v1.getLabel(), v1.getContent());
        this.v2 = new Vertex<>(v2.getLabel(), v2.getContent());
        hashCode = (37 + v1.getLabel().hashCode() + 11 * v2.getLabel().hashCode()) % 6321;
    }

    /**
     * A method that returns the head vertex of an edge
     *
     * @return the label and value of the head vertex.
     */
    public Vertex<T> getHeadVertex() {
        return new Vertex<>(v1.getLabel(), v1.getContent());
    }

    /**
     * A method that returns the tail vertex of an edge
     *
     * @return the label and value of the tail vertex.
     */
    public Vertex<T> getTailVertex() {
        return new Vertex<>(v2.getLabel(), v2.getContent());
    }

    /**
     * For fast equality checking. This method overrides hashCode() in Object.
     *
     * @return a hash code for this edge
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * Effects: Overrides the equals method from Object to conduct behavioural equality analysis.
     *
     * @param obj
     * @return true if two edges are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge other = (Edge) obj;
            return other.hashCode() == hashCode && v1.equals(other.getHeadVertex()) &&
                v2.equals(other.getTailVertex());
        }

        return false;
    }

}
