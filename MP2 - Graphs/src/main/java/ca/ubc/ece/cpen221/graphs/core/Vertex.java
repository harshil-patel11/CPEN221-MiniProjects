package ca.ubc.ece.cpen221.graphs.core;

/**
 * This class models a vertex of the graph.
 * DO NOT MODIFY THIS FILE.
 */
public class Vertex<T> {

    private String label;
    private T content;

    /**
     * Create a new vertex with a given label and content string
     *
     * @param label with which to identify the vertex
     */
    public Vertex(String label, T content) {
        this.label = label;
        this.content = content;
    }

    /**
     * Create a new vertex with a label only.
     * The label is the vertex content too.
     *
     * @param label with which to identify the vertex
     */
    /*public Vertex(String label) {
        this.label = label;
    }*/

    /**
     * Obtain the label associated with a vertex
     *
     * @return label associated with this vertex
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label for a vertex
     *
     * @param label to set for the vertex
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Obtain the content associated with a vertex
     *
     * @return content associated with this vertex
     */
    public T getContent() {
        return content;
    }

    /**
     * Set the content associated with this vertex
     *
     * @param content to be used to set the content associated with the vertex
     */
    public void setContent(T content) {
        this.content = content;
    }


    /**
     * Check equality of vertices. This method overrides equals( ) in Object.
     *
     * @return true if this vertex is equal to the obj otherwise return false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Vertex)) {
            return false;
        }
        Vertex other = (Vertex) obj;
        return (label.equals(other.label) && content.equals(other.content));
    }

    /**
     * For fast equality checking. This method overrides hashCode() in Object.
     *
     * @return a hash code for this vertex
     */
    @Override
    public int hashCode() {
        return label.hashCode();
    }

    /**
     * Obtain a string representation of the vertex
     *
     * @return the label associated with the vertex as its string
     * representation.
     */
    @Override
    public String toString() {
        return label;
    }
}
