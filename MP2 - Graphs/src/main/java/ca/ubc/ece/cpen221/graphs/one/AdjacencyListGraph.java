package ca.ubc.ece.cpen221.graphs.one;

import ca.ubc.ece.cpen221.graphs.core.Graph;
import ca.ubc.ece.cpen221.graphs.core.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/******************************************************************************
 *  Dependencies: Graph.java Vertex.java
 *
 *  A data type that represents a Graph using Adjacency Lists.
 *
 ******************************************************************************/


public class AdjacencyListGraph<T> implements Graph<T> {
    private static final boolean DEBUG = false; //checkRep method moved to checkRep.md

    private Map<Vertex<T>, List<Vertex<T>>> adjacencyMap;
    private Set<Edge<T>> edgeSet;
    private Set<Vertex<T>> vertexSet;

    /*
     * Rep Invariant: edgeSet, vertexSet, adjacencyMap are not null.
     * If adjacencyMap.get(v1).contains(v2), then edgeSet must contain an edge from v1 to v2. The
     * reverse holds true as well.
     * adjacencyMap.keySet.equals(vertexSet) is true.
     *
     * Abstraction Function:
     * Represents a finite graph as an adjacency list (a collection of unordered lists).
     * adjacencyMap.get(v1) is a list of vertices to which there exists an edge from v1.
     * Each edge in edgeSet represents an edge in the graph.
     * vertexSet represents the set of vertices in the graph.
     */

    /**
     * Constructor 1: Creates an empty AdjacencyListGraph.
     */
    public AdjacencyListGraph() {
        this.adjacencyMap = new HashMap<>();
        this.edgeSet = new HashSet<>();
        this.vertexSet = new HashSet<>();
    }

    /**
     * Constructor 2: Creates an AdjacencyListGraph, given a list of vertices.
     *
     * @param verticesList the list of vertices present in the finite graph.
     */
    public AdjacencyListGraph(List<Vertex<T>> verticesList) {
        this.adjacencyMap = new HashMap<>();
        this.edgeSet = new HashSet<>();
        this.vertexSet = new HashSet<>();

        for (Vertex<T> v : verticesList) {
            Vertex<T> vCopy = copyOfVertex(v);
            this.adjacencyMap.put(vCopy, new ArrayList<>());
            this.vertexSet.add(vCopy);
        }
    }

    /**
     * Constructor 3: Creates an AdjacencyListGraph given the adjacencyMap
     *
     * @param adjacencyMap A HashMap with key: Vertex Labels and Value: List of vertices
     */
    public AdjacencyListGraph(HashMap<Vertex<T>, List<Vertex<T>>> adjacencyMap) {
        this.adjacencyMap = new HashMap<>(adjacencyMap);
        this.edgeSet = new HashSet<>();
        this.vertexSet = new HashSet<>();

        this.vertexSet.addAll(this.adjacencyMap.keySet());
        for (Vertex<T> v : vertexSet) {
            List<Vertex<T>> list = this.adjacencyMap.get(v);
            for (Vertex<T> connectedVertex : list) {
                Edge<T> edge = new Edge<>(v, connectedVertex);
                this.edgeSet.add(edge);
            }
        }
    }


    /**
     * Adds a vertex to the graph.
     * <p>
     * Precondition: v is not already a vertex in the graph
     * </p>
     */
    public void addVertex(Vertex<T> v) {
        if (!adjacencyMap.containsKey(v)) {
            Vertex<T> vCopy = new Vertex<>(v.getLabel(), v.getContent());
            adjacencyMap.put(vCopy, new ArrayList<>(0));
            vertexSet.add(vCopy);
        }
    }

    /**
     * Adds an edge from v1 to v2.
     * <p>
     * Precondition: v1 and v2 are vertices in the graph
     * </p>
     */
    public void addEdge(Vertex<T> v1, Vertex<T> v2) {
        if (adjacencyMap.containsKey(v1) && adjacencyMap.containsKey((v2))) {
            Edge<T> edge = new Edge<>(v1, v2);
            edgeSet.add(edge);
            adjacencyMap.get(v1).add(copyOfVertex(v2));
        }
    }

    /**
     * Check if there is an edge from v1 to v2.
     * <p>
     * Precondition: v1 and v2 are vertices in the graph
     * </p>
     * <p>
     * Postcondition: return true iff an edge from v1 connects to v2
     * </p>
     */
    public boolean edgeExists(Vertex<T> v1, Vertex<T> v2) {
        Edge<T> edge = new Edge<>(v1, v2);
        return edgeSet.contains(edge);
    }

    /**
     * Get an array containing all vertices adjacent to v.
     * <p>
     * Precondition: v is a vertex in the graph
     * </p>
     * <p>
     * Postcondition: returns a list containing each vertex w such that there is
     * an edge from v to w. The size of the list must be as small as possible
     * (No trailing null elements). This method should return a list of size 0
     * iff v has no downstream neighbors.
     * </p>
     */
    public List<Vertex<T>> getNeighbors(Vertex<T> v) {
        List<Vertex<T>> neighbours = new ArrayList<>();

        if (adjacencyMap.containsKey(v)) {
            for (Vertex<T> v1 : adjacencyMap.get(v)) {
                neighbours.add(copyOfVertex(v1));
            }
        }

        return neighbours;
    }

    /**
     * Get all vertices in the graph.
     * <p>
     * Postcondition: returns a list containing all vertices in the graph,
     * sorted by label in non-descending order.
     * This method should return a list of size 0 iff the graph has no vertices.
     * </p>
     */
    public List<Vertex<T>> getVertices() {
        List<Vertex<T>> vertices = new ArrayList<>(0);

        for (Vertex<T> v : vertexSet) {
            vertices.add(copyOfVertex(v));
        }

        Algorithms.sortVertexList(vertices);

        return vertices;
    }

    /**
     * Returns a copy of a given Vertex
     * Precondition: the vertex must be valid, it must exist in the graph and be non-null.
     *
     * @param v non-null Vertex<T> v
     * @return a copy of v
     */
    private Vertex<T> copyOfVertex(Vertex<T> v) {
        return new Vertex<>(v.getLabel(), v.getContent());
    }

    /**
     * Overrides the equals method to provide behavioural equality
     *
     * @param obj
     * @return true if the graphs are equal, otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AdjacencyListGraph) {
            AdjacencyListGraph other = (AdjacencyListGraph) obj;
            if (this.hashCode() == other.hashCode() &&
                this.adjacencyMap.equals(other.adjacencyMap)) {
                return true;
            }
        }
        return false;
    }

    /**
     * For fast equality checking. This method overrides hashCode() in Object.
     *
     * @return a hash code for adjacencyMatrixGraph
     */
    @Override
    public int hashCode() {
        return edgeSet.size() + vertexSet.size();
    }

}
