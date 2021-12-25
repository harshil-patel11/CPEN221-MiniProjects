package ca.ubc.ece.cpen221.graphs.test;

import ca.ubc.ece.cpen221.graphs.core.Vertex;
import ca.ubc.ece.cpen221.graphs.one.AdjacencyListGraph;
import ca.ubc.ece.cpen221.graphs.one.AdjacencyMatrixGraph;
import ca.ubc.ece.cpen221.graphs.one.Edge;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class Task1Tests {

    @Test
    public void testList_addVertex1() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>();
        adjList.addVertex(v1);
        assertTrue(adjList.getVertices().contains(v1));
    }

    @Test
    public void testList_addVertex2() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>();
        adjList.addVertex(v1);
        adjList.addVertex(v1);
        assertTrue(adjList.getVertices().contains(v1));
        assertEquals(1, adjList.getVertices().size());
    }

    @Test
    public void testList_Edge1() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v2", 3);
        Vertex<Integer> v4 = new Vertex<>("v1", 4);

        Edge<Integer> e1 = new Edge<>(v1, v2);
        Edge<Integer> e2 = new Edge<>(v2, v1);
        Edge<Integer> e3 = new Edge<>(v2, v3);
        Edge<Integer> e4 = new Edge<>(v4, v1);
        Edge<Integer> e5 = new Edge<>(v3, v4);
        Edge<Integer> e6 = new Edge<>(v1, v3);

        assertEquals(e1, e1);
        assertNotEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertNotEquals(e1, e4);
        assertNotEquals(e2, e1);
        assertNotEquals(e2, e3);
        assertNotEquals(e2, e4);
        assertNotEquals(e3, e4);
        assertNotEquals(e4, e1);
        assertNotEquals(e3, e2);
        assertNotEquals(e2, e5);
        assertNotEquals(e1, e6);
        assertNotEquals(e1, "Sathish");
    }

    @Test
    public void testList_addEdge1() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>();
        adjList.addVertex(v1);
        adjList.addVertex(v2);
        adjList.addEdge(v1, v2);

        assertTrue(adjList.edgeExists(v1, v2));
    }

    @Test
    public void testList_addEdge2() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>();
        adjList.addVertex(v1);
        adjList.addVertex(v2);
        adjList.addEdge(v1, v3);

        assertFalse(adjList.edgeExists(v1, v3));
    }

    @Test
    public void testList_addEdge3() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>();
        adjList.addVertex(v1);
        adjList.addVertex(v2);
        adjList.addEdge(v3, v2);

        assertFalse(adjList.edgeExists(v3, v2));
    }

    @Test
    public void testList_addEdge4() {   //testing with parameterized constructor
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);

        assertTrue(adjList.edgeExists(v1, v2));
    }

    @Test
    public void testList_getVertices1() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v2);
        vertexList.add(v4);
        vertexList.add(v1);
        vertexList.add(v3);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);

        List<Vertex<Integer>> vertexListSorted = new ArrayList<>();
        vertexListSorted.add(v1);
        vertexListSorted.add(v2);
        vertexListSorted.add(v3);
        vertexListSorted.add(v4);

        assertEquals(vertexListSorted, adjList.getVertices());
    }

    @Test
    public void testList_getVertices2() {
        List<Vertex<Integer>> vertexList = new ArrayList<>(0);
        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);

        assertEquals(vertexList, adjList.getVertices());
    }

    @Test
    public void testList_getNeighbours1() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v1, v4);

        List<Vertex<Integer>> v1Neighbors = new ArrayList<>();
        v1Neighbors.add(v2);
        v1Neighbors.add(v3);
        v1Neighbors.add(v4);

        assertEquals(v1Neighbors, adjList.getNeighbors(v1));
    }

    @Test
    public void testList_getNeighbours2() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);

        assertEquals(new ArrayList<>(0), adjList.getNeighbors(v2));
    }

    @Test
    public void testList_getAdjacencyMap1() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v1, v4);

        HashMap<Vertex<Integer>, List<Vertex<Integer>>> adjacencyMap = new HashMap<>();
        List<Vertex<Integer>> v1List = new ArrayList<>();
        v1List.add(v2);
        v1List.add(v3);
        v1List.add(v4);

        adjacencyMap.put(v1, v1List);
        adjacencyMap.put(v2, new ArrayList<>(0));
        adjacencyMap.put(v3, new ArrayList<>(0));
        adjacencyMap.put(v4, new ArrayList<>(0));

        AdjacencyListGraph<Integer> adjListEqual = new AdjacencyListGraph<>(adjacencyMap);

        assertTrue(adjListEqual.equals(adjList));
    }

    @Test
    public void testList_getAdjacencyMap2() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v1, v4);

        HashMap<Vertex<Integer>, List<Vertex<Integer>>> adjacencyMap = new HashMap<>();
        adjacencyMap.put(v1, new ArrayList<>(0));
        adjacencyMap.put(v2, new ArrayList<>(0));
        adjacencyMap.put(v3, new ArrayList<>(0));
        adjacencyMap.put(v4, new ArrayList<>(0));

        AdjacencyListGraph<Integer> adjListNotEqual = new AdjacencyListGraph<>(adjacencyMap);

        assertFalse(adjListNotEqual.equals(adjList));
    }

    @Test
    public void testList_getAdjacencyMap3() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v2, v1);
        adjList.addEdge(v1, v3);

        HashMap<Vertex<Integer>, List<Vertex<Integer>>> adjacencyMap = new HashMap<>();
        List<Vertex<Integer>> v1List = new ArrayList<>();
        v1List.add(v2);
        v1List.add(v3);
        v1List.add(v4);

        adjacencyMap.put(v1, v1List);
        adjacencyMap.put(v2, new ArrayList<>(0));
        adjacencyMap.put(v3, new ArrayList<>(0));
        adjacencyMap.put(v4, new ArrayList<>(0));

        AdjacencyListGraph<Integer> adjListNotEqual = new AdjacencyListGraph<>(adjacencyMap);

        assertFalse(adjListNotEqual.equals(adjList));
    }


    @Test
    public void testMatrix_addVertex1() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>();
        adjMatrix.addVertex(v1);
        assertTrue(adjMatrix.getVertices().contains(v1));
    }

    @Test
    public void testMatrix_addVertex2() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>();
        adjMatrix.addVertex(v1);
        adjMatrix.addVertex(v1);
        assertTrue(adjMatrix.getVertices().contains(v1));
        assertEquals(1, adjMatrix.getVertices().size());
    }

    @Test
    public void testMatrix_addEdge1() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v1", 2);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>();
        adjMatrix.addVertex(v1);
        adjMatrix.addVertex(v2);
        adjMatrix.addEdge(v1, v2);

        assertTrue(adjMatrix.edgeExists(v1, v2));
    }

    @Test
    public void testMatrix_addEdge2() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>();
        adjMatrix.addVertex(v1);
        adjMatrix.addVertex(v2);
        adjMatrix.addEdge(v1, v3);

        assertFalse(adjMatrix.edgeExists(v1, v3));
    }

    @Test
    public void testMatrix_addEdge3() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>();
        adjMatrix.addVertex(v1);
        adjMatrix.addVertex(v2);
        adjMatrix.addEdge(v3, v1);

        assertFalse(adjMatrix.edgeExists(v3, v1));
    }

    @Test
    public void testMatrix_addEdge4() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>(vertexList);
        adjMatrix.addEdge(v3, v1);

        assertTrue(adjMatrix.edgeExists(v3, v1));
    }

    @Test
    public void testMatrix_getVertices1() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v2);
        vertexList.add(v4);
        vertexList.add(v1);
        vertexList.add(v3);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>(vertexList);

        List<Vertex<Integer>> vertexListSorted = new ArrayList<>();
        vertexListSorted.add(v1);
        vertexListSorted.add(v2);
        vertexListSorted.add(v3);
        vertexListSorted.add(v4);

        assertEquals(vertexListSorted, adjMatrix.getVertices());
    }

    @Test
    public void testMatrix_getVertices2() {
        List<Vertex<Integer>> vertexList = new ArrayList<>(0);
        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>(vertexList);

        assertEquals(vertexList, adjMatrix.getVertices());
    }

    @Test
    public void testMatrix_getNeighbours1() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>(vertexList);
        adjMatrix.addEdge(v1, v2);
        adjMatrix.addEdge(v1, v3);
        adjMatrix.addEdge(v1, v4);

        List<Vertex<Integer>> v1Neighbors = new ArrayList<>();
        v1Neighbors.add(v2);
        v1Neighbors.add(v3);
        v1Neighbors.add(v4);

        assertEquals(v1Neighbors, adjMatrix.getNeighbors(v1));
        //assertEquals(List.of(v1), adjMatrix.getUpwardNeighbors(v3));
    }

    @Test
    public void testMatrix_getNeighbours2() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>(vertexList);

        assertEquals(new ArrayList<>(0), adjMatrix.getNeighbors(v2));
    }

    @Test
    public void testMatrix_getNeighbours3() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>(vertexList);
        adjMatrix.addEdge(v1, v2);
        adjMatrix.addEdge(v1, v3);
        adjMatrix.addEdge(v1, v4);

        List<List<Boolean>> adjacencyMatrix = new ArrayList<>();
        adjacencyMatrix.add(List.of(false, true, true, true));
        adjacencyMatrix.add(List.of(false, false, false, false));
        adjacencyMatrix.add(List.of(false, false, false, false));
        adjacencyMatrix.add(List.of(false, false, false, false));

        AdjacencyMatrixGraph<Integer> adjMatrixEqual =
            new AdjacencyMatrixGraph<>(adjacencyMatrix, vertexList);

        assertEquals(adjMatrix.getNeighbors(v1), adjMatrixEqual.getNeighbors(v1));
    }

    @Test
    public void testMatrix_getAdjacencyMap1() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>(vertexList);
        adjMatrix.addEdge(v1, v2);
        adjMatrix.addEdge(v1, v3);
        adjMatrix.addEdge(v1, v4);

        List<List<Boolean>> adjacencyMatrix = new ArrayList<>();
        adjacencyMatrix.add(List.of(false, true, true, true));
        adjacencyMatrix.add(List.of(false, false, false, false));
        adjacencyMatrix.add(List.of(false, false, false, false));
        adjacencyMatrix.add(List.of(false, false, false, false));

        AdjacencyMatrixGraph<Integer> adjMatrixEqual =
            new AdjacencyMatrixGraph<>(adjacencyMatrix, vertexList);

        assertTrue(adjMatrixEqual.equals(adjMatrix));
    }

    @Test
    public void testMatrix_getAdjacencyMap2() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>(vertexList);
        adjMatrix.addEdge(v1, v2);
        adjMatrix.addEdge(v1, v3);
        adjMatrix.addEdge(v1, v4);

        List<List<Boolean>> adjacencyMatrix = new ArrayList<>();
        adjacencyMatrix.add(List.of(false, false, false, false));
        adjacencyMatrix.add(List.of(false, false, false, false));
        adjacencyMatrix.add(List.of(false, false, false, false));
        adjacencyMatrix.add(List.of(false, false, false, false));

        AdjacencyMatrixGraph<Integer> adjMatrixNotEqual =
            new AdjacencyMatrixGraph<>(adjacencyMatrix, vertexList);

        assertFalse(adjMatrixNotEqual.equals(adjMatrix));
    }

    @Test
    public void testMatrix_getAdjacencyMap3() {
        Vertex<Integer> v1 = new Vertex<>("v1", 1);
        Vertex<Integer> v2 = new Vertex<>("v2", 2);
        Vertex<Integer> v3 = new Vertex<>("v3", 3);
        Vertex<Integer> v4 = new Vertex<>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<>(vertexList);
        adjMatrix.addEdge(v1, v2);
        adjMatrix.addEdge(v1, v3);
        adjMatrix.addEdge(v1, v4);

        List<List<Boolean>> adjacencyMatrix = new ArrayList<>();
        adjacencyMatrix.add(List.of(false, true, false, false));
        adjacencyMatrix.add(List.of(false, false, false, true));
        adjacencyMatrix.add(List.of(false, false, true, false));
        adjacencyMatrix.add(List.of(false, false, false, false));

        AdjacencyMatrixGraph<Integer> adjMatrixNotEqual =
            new AdjacencyMatrixGraph<>(adjacencyMatrix, vertexList);

        assertFalse(adjMatrixNotEqual.equals(adjMatrix));
    }

}
