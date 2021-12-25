package ca.ubc.ece.cpen221.graphs.one;

import ca.ubc.ece.cpen221.graphs.core.Graph;
import ca.ubc.ece.cpen221.graphs.core.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/******************************************************************************
 *  Dependencies: Graph.java Vertex.java
 *
 *  A data type that represents a Graph using Adjacency Matrices.
 *
 ******************************************************************************/

public class AdjacencyMatrixGraph<T> implements Graph<T> {
    private static final boolean DEBUG = false; //checkRep method moved to checkRep.md

    private List<List<Boolean>> adjacencyMatrix;
    private List<Vertex<T>> vertexList;
    private Set<Edge<T>> edgeSet;

    /*
     * Rep Invariant: edgeSet, vertexList, adjacencyMatrix are not null.
     * vertexList contains no duplicates
     * AdjacencyMatrix.size() == AdjacencyMatrix.get(0).size() == vertexList.size().
     * If edgeSet contains an edge from v1 to v2, then
     * adjacencyMatrix.get(vertexList.indexOf(v1)).get(vertexList.indexOf(v2)) == true.
     *
     * Abstraction Function:
     * Represents a finite graph as an adjacency matrix of booleans (a square matrix of adjacent
     * vertices). Each element (row, col) of the matrix is true if there exists an edge from the
     * vertex with index row in vertexList, to the vertex with index col in vertexList.
     *
     */

    /**
     * Constructor 1: Creates an empty AdjacencyMatrixGraph.
     */
    public AdjacencyMatrixGraph() {
        this.adjacencyMatrix = new ArrayList<>();
        this.vertexList = new ArrayList<>();
        this.edgeSet = new HashSet<>();
    }

    /**
     * Constructor 2: Creates an AdjacencyMatrixGraph, given a list of vertices.
     *
     * @param verticesList the list of vertices present in the finite graph.
     */
    public AdjacencyMatrixGraph(List<Vertex<T>> verticesList) {
        this.adjacencyMatrix = new ArrayList<>();
        this.vertexList = new ArrayList<>();
        this.edgeSet = new HashSet<>();

        for (Vertex<T> v : verticesList) {
            Vertex<T> vCopy = copyOfVertex(v);
            addVertex(vCopy);
        }

    }

    /**
     * Creates an AdjacencyMatrixGraph given the adjacencyMatrix and the list of vertices.
     *
     * @param adjacencyMatrix the adjacencyMatrix
     * @param verticesList    the list of vertices in the graph.
     */
    public AdjacencyMatrixGraph(List<List<Boolean>> adjacencyMatrix, List<Vertex<T>> verticesList) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.vertexList = verticesList;
        this.edgeSet = new HashSet<>();

        for (int i = 0; i < vertexList.size(); i++) {
            List<Boolean> vertexRow = adjacencyMatrix.get(i);
            for (int j = 1; j < vertexList.size(); j++) {
                if (vertexRow.get(j)) {
                    Edge<T> edge = new Edge<>(vertexList.get(i), vertexList.get(j));
                    edgeSet.add(edge);
                }
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
        if (!vertexList.contains(v)) {
            Vertex<T> copyOfV = copyOfVertex(v);
            vertexList.add(copyOfV);

            for (List<Boolean> row : adjacencyMatrix) {
                row.add(false);
            }

            adjacencyMatrix.add(new ArrayList<>());
            for (int i = 0; i < vertexList.size(); i++) {
                adjacencyMatrix.get(vertexList.size() - 1).add(false);
            }
        }
    }

    /**
     * Adds an edge from v1 to v2.
     * <p>
     * Precondition: v1 and v2 are vertices in the graph
     * </p>
     */
    public void addEdge(Vertex<T> v1, Vertex<T> v2) {
        if (vertexList.contains(v1) && vertexList.contains(v2)) {
            Edge<T> edge = new Edge<>(v1, v2);
            edgeSet.add(edge);
            updateMatrix(edge);
        }
    }

    /**
     * Precondition: edge must exist in the graph
     * Modifies: adjacencyMatrix so that it represents the newly
     * added edge in its matrix representation
     *
     * @param edge not null and must exist in the graph
     */
    private void updateMatrix(Edge<T> edge) {
        int v1Index = vertexList.indexOf(edge.getHeadVertex());
        int v2Index = vertexList.indexOf(edge.getTailVertex());
        adjacencyMatrix.get(v1Index).set(v2Index, true);
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
        List<Boolean> vertexRow = adjacencyMatrix.get(vertexList.indexOf(v));

        for (int i = 0; i < vertexRow.size(); i++) {
            if (vertexRow.get(i)) {
                neighbours.add(copyOfVertex(vertexList.get(i)));
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
        List<Vertex<T>> vertices = new ArrayList<>();

        for (Vertex<T> v : vertexList) {
            vertices.add(copyOfVertex(v));
        }

        Algorithms.sortVertexList(vertices);

        return vertices;
    }

    /**
     * Creates a copy of the provided vertex.
     *
     * @param v the vertex to be copied.
     * @return a copy of v
     */
    private Vertex<T> copyOfVertex(Vertex<T> v) {
        return new Vertex<>(v.getLabel(), v.getContent());
    }

    /**
     * Effects: Overrides the equals() method from Object.
     *
     * @param obj the object to be compared for equality.
     * @return true if two AdjacencyMatrixGraph(s) are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AdjacencyMatrixGraph) {
            AdjacencyMatrixGraph other = (AdjacencyMatrixGraph) obj;
            if (this.hashCode() == other.hashCode() &&
                this.adjacencyMatrix.equals(other.adjacencyMatrix)) {
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
        return edgeSet.size() + vertexList.size();
    }

}
