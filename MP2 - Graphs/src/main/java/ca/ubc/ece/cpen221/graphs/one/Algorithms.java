package ca.ubc.ece.cpen221.graphs.one;

import ca.ubc.ece.cpen221.graphs.core.Graph;
import ca.ubc.ece.cpen221.graphs.core.Vertex;

import java.util.*;

public class Algorithms {
    public static final int INFINITE_DISTANCE = Integer.MAX_VALUE;

    /**
     * *********************** Algorithms ****************************
     *
     * Please see the README for this machine problem for a more detailed
     * specification of the behavior of each method that one should
     * implement.
     */

    /**
     * This is provided as an example to indicate that this method and
     * other methods should be implemented here.
     * <p>
     * You should write the specs for this and all other methods.
     * <p>
     * To find the shortest distance between two vertices in a given finite graph.
     *
     * @param graph the graph to be analyzed.
     * @param a     The start vertex
     * @param b     The end vertex
     * @return the minimum number of edges needed to traverse to go from a to b.
     */
    public static <T> int distance(Graph<T> graph, Vertex<T> a, Vertex<T> b) {
        // Note that this method can be invoked as follows:
        //      Algorithms.<String>distance(g, a, b)
        // when the graph contains vertices of type Vertex<String>.
        // The compiler can also perform type inference so that we can simply use:
        //      Algorithms.distance(g, a, b)


        // some of the code in this method was used from this website
        // https://www.baeldung.com/java-breadth-first-search

        if (a.equals(b)) {
            return 0;
        }

        int shortestDist = 0;

        Set<Vertex<T>> visitedVertices = new HashSet<>();
        visitedVertices.add(a);
        Queue<Vertex<T>> queue = new LinkedList<>();
        queue.add(a);

        while (!queue.isEmpty()) {
            int nextDepthSize = queue.size();

            for (int i = nextDepthSize; i > 0; i--) {
                Vertex<T> vertex = queue.remove();
                List<Vertex<T>> neighbors = graph.getNeighbors(vertex);

                for (Vertex<T> v : neighbors) {
                    if (!visitedVertices.contains(v)) {
                        queue.add(v);
                        visitedVertices.add(v);
                    }
                    if (v.equals(b)) {
                        return shortestDist + 1;
                    }
                }
            }
            shortestDist++;
        }

        return INFINITE_DISTANCE;
    }

    /**
     * To find the common upstream vertices for two vertices in a graph.
     *
     * @param graph the graph containing the vertices.
     * @param a     the first Vertex
     * @param b     the second Vertex
     * @return a list of all vertices u such that there is an edge from u to a and an edge from u to b.
     */
    public static <T> List<Vertex<T>> commonUpstreamVertices(Graph<T> graph, Vertex<T> a,
                                                             Vertex<T> b) {
        List<Vertex<T>> upstreamVertexList = new ArrayList<>();

        for (Vertex<T> u : graph.getVertices()) {
            if (graph.edgeExists(u, a) && graph.edgeExists(u, b)) {
                upstreamVertexList.add(copyOfVertex(u));
            }
        }

        return upstreamVertexList;
    }

    /**
     * To find the common downstream vertices for two vertices in a graph.
     *
     * @param graph the graph containing the vertices.
     * @param a     the first vertex
     * @param b     the second vertex
     * @return a list of all vertices v such that there is an edge from a to v and an edge from b to v.
     */
    public static <T> List<Vertex<T>> commonDownstreamVertices(Graph<T> graph, Vertex<T> a,
                                                               Vertex<T> b) {
        List<Vertex<T>> bNeighbours = graph.getNeighbors(b);
        List<Vertex<T>> aNeighbours = graph.getNeighbors(a);

        return cloneVertexList(aNeighbours.stream().filter
            (x -> bNeighbours.contains(x)).collect(java.util.stream.Collectors.toList()));
    }

    /**
     * Creates a deep copy of a Vertex List
     *
     * @param list the list to be copied
     * @return a deep copy of a Vertex List
     */
    private static <T> List<Vertex<T>> cloneVertexList(List<Vertex<T>> list) {
        List<Vertex<T>> clone = new ArrayList<Vertex<T>>();

        for (int i = 0; i < list.size(); i++) {
            Vertex<T> v = copyOfVertex(list.get(i));
            clone.add(v);
        }

        return clone;
    }

    /**
     * Perform a complete depth first search of the given
     * graph. Start with the search at each vertex of the
     * graph and create a list of the vertices visited.
     * Return a set where each element of the set is a list
     * of elements seen by starting a DFS at a specific
     * vertex of the graph (the number of elements in the
     * returned set should correspond to the number of graph
     * vertices).
     *
     * @param graph to be analyzed.
     * @return a set where each element of the set is a list of elements seen by
     * starting a DFS at a specific vertex of the graph.
     */
    public static <T> Set<List<Vertex<T>>> depthFirstSearch(Graph<T> graph) {
        List<Vertex<T>> vertexList = graph.getVertices();
        Set<List<Vertex<T>>> dfsSet = new HashSet<>();

        for (Vertex<T> v : vertexList) {
            Stack<Vertex<T>> stack = new Stack<>();
            Set<Vertex<T>> visitedVertices = new HashSet<>();
            List<Vertex<T>> neighbors = graph.getNeighbors(v);
            List<Vertex<T>> searchedList = new ArrayList<>();

            stack.push(v);
            visitedVertices.add(v);
            searchedList.add(v);

            while (!stack.empty()) {
                v = DFSVertex(v, stack, visitedVertices, neighbors, searchedList);
                neighbors = graph.getNeighbors(v);
            }

            dfsSet.add(searchedList);
        }

        return dfsSet;
    }

    /**
     * Computes the vertex where the current DFS search should continue
     * searching from.
     *
     * @param v               the current vertex where DFS if being performed
     * @param stack           stack of vertices that can be popped/pushed
     * @param visitedVertices Set of previously visited vertices by DFS
     * @param neighbors       neighbors of Vertex v
     * @param searchedList    list of vertices that have already been searched
     * @param <T>             represents the type of Vertex v
     * @return the next vertex to perform DFS on
     */
    private static <T> Vertex<T> DFSVertex(Vertex<T> v, Stack<Vertex<T>> stack,
                                           Set<Vertex<T>> visitedVertices,
                                           List<Vertex<T>> neighbors,
                                           List<Vertex<T>> searchedList) {
        boolean neighborFlag = false;
        sortVertexList(neighbors);
        for (int i = 0; i < neighbors.size(); i++) {
            Vertex<T> vertex = neighbors.get(i);
            if (!visitedVertices.contains(vertex)) {
                v = vertex;
                searchedList.add(vertex);
                visitedVertices.add(vertex);
                stack.push(vertex);
                neighborFlag = true;
                break;
            }
        }
        if (!neighborFlag) {
            stack.pop();
            if (!stack.empty()) {
                v = stack.peek();
            }
        }

        return v;
    }

    /**
     * Perform a complete breadth first search of the given
     * graph. Start with the search at each vertex of the
     * graph and create a list of the vertices visited.
     * Return a set where each element of the set is a list
     * of elements seen by starting a BFS at a specific
     * vertex of the graph (the number of elements in the
     * returned set should correspond to the number of graph
     * vertices).
     *
     * @param graph to be analyzed
     * @return a set where each element of the set is a list of elements seen by
     * starting a BFS at a specific vertex of the graph.
     */
    public static <T> Set<List<Vertex<T>>> breadthFirstSearch(Graph<T> graph) {
        Set<List<Vertex<T>>> searchSet = new HashSet<List<Vertex<T>>>();
        List<Vertex<T>> vertexList = graph.getVertices();

        for (Vertex<T> v : vertexList) {
            searchSet.add(BFSFromNode(graph, v));
        }

        return searchSet;
    }

    /**
     * Performs a breadth-first search from a specified node.
     *
     * @param graph the graph to perform the search in
     * @param node the node from which to start the search
     * @param <T> the type of the vertex
     * @return a list containing the vertices reached in the breadth-first search, where vertices appear in the order
     * they were traversed
     */
    private static <T> List<Vertex<T>> BFSFromNode(Graph<T> graph, Vertex<T> node) {
        List<Vertex<T>> visitedVertices = new ArrayList<>();
        visitedVertices.add(node);
        List<Vertex<T>> neighbourList = graph.getNeighbors(node);
        Queue<Vertex<T>> neighbourQueue = new LinkedList<>();
        HashSet<Vertex<T>> neighbourSet = new HashSet<>();
        sortVertexList(neighbourList);

        while (neighbourList.size() > 0) {
            for (int i = 0; i < neighbourList.size(); i++) {
                neighbourQueue.add(neighbourList.get(i));
            }

            visitedVertices.addAll(neighbourList);
            neighbourList.clear();
            neighbourSet.clear();

            while (neighbourQueue.size() > 0) {
                neighbourSet.addAll(graph.getNeighbors(neighbourQueue.remove()));
            }

            neighbourSet.removeIf(v -> visitedVertices.contains(v));
            neighbourList.addAll(neighbourSet);

            sortVertexList(neighbourList);
        }

        return visitedVertices;
    }

    /**
     * Takes in a list of vertices of type T and sorts them based on lexicographical order.
     * Modifies: the input vertexList to be sorted in lexicographical order
     *
     * @param vertexList list of vertices.
     */
    public static <T> void sortVertexList(List<Vertex<T>> vertexList) {
        List<String> labelList = new ArrayList<>();
        List<Vertex<T>> vertexListCopy = new ArrayList<>();

        for (Vertex<T> v : vertexList) {
            vertexListCopy.add(v);
        }

        Comparator<String> stringComparator = Comparator.naturalOrder();

        for (Vertex<T> v : vertexList) {
            labelList.add(v.getLabel());
        }

        labelList.sort(stringComparator);

        for (int i = 0; i < labelList.size(); i++) {
            vertexList.set(i,
                vertexListCopy.get(indexOfVertexWithLabel(labelList.get(i), vertexListCopy)));
        }
    }

    /**
     * @param label the label of the vertex
     * @param vertexList the list of vertices to seach
     * @param <T> the type of the vertices in the list
     * @return the value of the first index with label equal to label
     */
    private static <T> int indexOfVertexWithLabel(String label, List<Vertex<T>> vertexList) {
        for (int i = 0; i < vertexList.size(); i++) {
            if (vertexList.get(i).getLabel().equals(label)) {
                return i;
            }
        }

        throw new IllegalArgumentException();
    }

    /**
     * To return the diameter of a finite graph.
     * Precondition:
     * exclude the case in which all distances are infinite.
     *
     * @param graph to be analyzed.
     * @returns the maximum finite distance among pairs of vertices
     */
    public static <T> int diameter(Graph<T> graph) {
        int max = 0;
        int currentDistance;
        boolean allInfinite = true;
        List<Vertex<T>> vertexList = graph.getVertices();

        for (int i = 0; i < vertexList.size() - 1; i++) {
            for (int j = i + 1; j < vertexList.size(); j++) {
                currentDistance = distance(graph, vertexList.get(i), vertexList.get(j));

                if (currentDistance != INFINITE_DISTANCE) {
                    allInfinite = false;

                    if (currentDistance > max) {
                        max = currentDistance;
                    }
                }
            }
        }

        if (allInfinite) {
            return INFINITE_DISTANCE;
        }

        return max;
    }

    /**
     * Creates a copy of the provided vertex.
     *
     * @param v the vertex to be copied.
     * @return a copy of v
     */
    private static <T> Vertex<T> copyOfVertex(Vertex<T> v) {
        return new Vertex<>(v.getLabel(), v.getContent());
    }

}
