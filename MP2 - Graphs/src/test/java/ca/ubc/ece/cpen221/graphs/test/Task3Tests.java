package ca.ubc.ece.cpen221.graphs.test;

import ca.ubc.ece.cpen221.graphs.core.Vertex;
import ca.ubc.ece.cpen221.graphs.one.AdjacencyListGraph;
import ca.ubc.ece.cpen221.graphs.one.AdjacencyMatrixGraph;
import ca.ubc.ece.cpen221.graphs.one.Algorithms;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class Task3Tests {

    @Test
    public void test_commonUpstream1() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);
        Vertex<Integer> v4 = new Vertex<Integer>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v4, v2);
        adjList.addEdge(v4, v3);

        List<Vertex<Integer>> expectedList = new ArrayList<>();
        expectedList.addAll(List.of(v1, v4));

        assertEquals(expectedList, Algorithms.commonUpstreamVertices(adjList, v2, v3));
    }

    @Test
    public void test_commonUpstream2() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);
        Vertex<Integer> v4 = new Vertex<Integer>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v4, v2);
        adjList.addEdge(v4, v1);

        List<Vertex<Integer>> expectedList = new ArrayList<>();
        expectedList.addAll(List.of(v1));

        assertEquals(expectedList, Algorithms.commonUpstreamVertices(adjList, v2, v3));
    }

    @Test
    public void test_commonUpstream3() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);
        Vertex<Integer> v4 = new Vertex<Integer>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v4);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v4, v2);
        adjList.addEdge(v4, v1);

        List<Vertex<Integer>> expectedList = new ArrayList<>(0);

        assertEquals(expectedList, Algorithms.commonUpstreamVertices(adjList, v2, v3));
    }

    @Test
    public void test_commonUpstream4() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);
        Vertex<Integer> v4 = new Vertex<Integer>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v4);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v4, v1);

        List<Vertex<Integer>> expectedList = new ArrayList<>(0);

        assertEquals(expectedList, Algorithms.commonUpstreamVertices(adjList, v2, v3));
    }

    @Test
    public void test_commonDownstream1() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);
        Vertex<Integer> v4 = new Vertex<Integer>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v4, v2);
        adjList.addEdge(v4, v3);

        List<Vertex<Integer>> expectedList = new ArrayList<>();
        expectedList.addAll(List.of(v2, v3));

        assertEquals(expectedList, Algorithms.commonDownstreamVertices(adjList, v1, v4));
    }

    @Test
    public void test_commonDownstream2() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);
        Vertex<Integer> v4 = new Vertex<Integer>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v4);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v4, v2);
        adjList.addEdge(v4, v3);

        List<Vertex<Integer>> expectedList = new ArrayList<>();
        expectedList.addAll(List.of(v3));

        assertEquals(expectedList, Algorithms.commonDownstreamVertices(adjList, v1, v4));
    }

    @Test
    public void test_commonDownstream3() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);
        Vertex<Integer> v4 = new Vertex<Integer>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v4);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v4, v2);
        adjList.addEdge(v4, v1);

        List<Vertex<Integer>> expectedList = new ArrayList<>(0);

        assertEquals(expectedList, Algorithms.commonDownstreamVertices(adjList, v1, v4));
    }

    @Test
    public void test_commonDownstream4() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);
        Vertex<Integer> v4 = new Vertex<Integer>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);

        List<Vertex<Integer>> expectedList = new ArrayList<>(0);

        assertEquals(expectedList, Algorithms.commonDownstreamVertices(adjList, v1, v4));
    }

    @Test
    public void test_distance() {
        List<Vertex<Integer>> vertexList = new ArrayList<>();
        int size = 10;

        StringBuilder label = new StringBuilder();

        for (int i = 0; i < size; i++) {
            label.append(i);
            vertexList.add(new Vertex<Integer>(label.toString(), i));
            label.delete(0, label.length());
        }

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(vertexList.get(0), vertexList.get(1));
        adjList.addEdge(vertexList.get(0), vertexList.get(2));
        adjList.addEdge(vertexList.get(0), vertexList.get(3));
        adjList.addEdge(vertexList.get(1), vertexList.get(4));
        adjList.addEdge(vertexList.get(1), vertexList.get(5));
        adjList.addEdge(vertexList.get(2), vertexList.get(6));
        adjList.addEdge(vertexList.get(3), vertexList.get(7));
        adjList.addEdge(vertexList.get(4), vertexList.get(8));
        adjList.addEdge(vertexList.get(7), vertexList.get(9));
        adjList.addEdge(vertexList.get(6), vertexList.get(8));
        adjList.addEdge(vertexList.get(8), vertexList.get(9));

        assertEquals(2, Algorithms.distance(adjList, vertexList.get(0), vertexList.get(4)));
        assertEquals(2, Algorithms.distance(adjList, vertexList.get(0), vertexList.get(7)));
        assertEquals(1, Algorithms.distance(adjList, vertexList.get(7), vertexList.get(9)));
        assertEquals(3, Algorithms.distance(adjList, vertexList.get(2), vertexList.get(9)));
        assertEquals(2, Algorithms.distance(adjList, vertexList.get(4), vertexList.get(9)));
        assertEquals(2, Algorithms.distance(adjList, vertexList.get(0), vertexList.get(6)));
        assertEquals(0, Algorithms.distance(adjList, vertexList.get(0), vertexList.get(0)));
        assertEquals(Algorithms.INFINITE_DISTANCE, Algorithms.distance(adjList, vertexList.get(9), vertexList.get(8)));
        assertEquals(Algorithms.INFINITE_DISTANCE,
            Algorithms.distance(adjList, vertexList.get(7), new Vertex<Integer>("10", 10)));
        assertEquals(Algorithms.INFINITE_DISTANCE,
            Algorithms.distance(adjList, new Vertex<Integer>("10", 10), vertexList.get(9)));
        assertEquals(Algorithms.INFINITE_DISTANCE, Algorithms
            .distance(adjList, new Vertex<Integer>("10", 10),
                new Vertex<Integer>("11", 11)));
    }

    @Test
    public void test_diameter1() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);
        Vertex<Integer> v4 = new Vertex<Integer>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v3, v4);

        assertEquals(2, Algorithms.diameter(adjList));
    }

    @Test
    public void test_diameter2() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);
        Vertex<Integer> v4 = new Vertex<Integer>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);
        vertexList.add(v4);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);

        assertEquals(1, Algorithms.diameter(adjList));
    }

    @Test
    public void test_diameter3() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);

        assertEquals(Algorithms.INFINITE_DISTANCE, Algorithms.diameter(adjList));
    }

    @Test
    public void test_diameter4() {
        List<Vertex<Integer>> vertexList = new ArrayList<>();
        int size = 10;

        StringBuilder label = new StringBuilder();

        for (int i = 0; i < size; i++) {
            label.append(i);
            vertexList.add(new Vertex<Integer>(label.toString(), i));
            label.delete(0, label.length());
        }

        AdjacencyMatrixGraph<Integer> adjMatrix = new AdjacencyMatrixGraph<Integer>(vertexList);
        adjMatrix.addEdge(vertexList.get(0), vertexList.get(1));
        adjMatrix.addEdge(vertexList.get(0), vertexList.get(2));
        adjMatrix.addEdge(vertexList.get(0), vertexList.get(3));
        adjMatrix.addEdge(vertexList.get(0), vertexList.get(4));
        adjMatrix.addEdge(vertexList.get(1), vertexList.get(2));
        adjMatrix.addEdge(vertexList.get(1), vertexList.get(0));
        adjMatrix.addEdge(vertexList.get(2), vertexList.get(3));
        adjMatrix.addEdge(vertexList.get(4), vertexList.get(3));
        adjMatrix.addEdge(vertexList.get(1), vertexList.get(5));
        adjMatrix.addEdge(vertexList.get(2), vertexList.get(6));
        adjMatrix.addEdge(vertexList.get(3), vertexList.get(5));
        adjMatrix.addEdge(vertexList.get(5), vertexList.get(7));
        adjMatrix.addEdge(vertexList.get(5), vertexList.get(8));
        adjMatrix.addEdge(vertexList.get(8), vertexList.get(9));
        adjMatrix.addEdge(vertexList.get(4), vertexList.get(8));

        assertEquals(4, Algorithms.diameter(adjMatrix));
    }

}
