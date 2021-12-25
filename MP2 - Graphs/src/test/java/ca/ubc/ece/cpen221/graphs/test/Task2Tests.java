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

public class Task2Tests {

    @Test
    public void testDFS_1() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);

        Set<List<Vertex<Integer>>> actual = Algorithms.depthFirstSearch(adjList);
        Set<List<Vertex<Integer>>> expected = new HashSet<List<Vertex<Integer>>>();

        ArrayList<Vertex<Integer>> v1List = new ArrayList<>();
        ArrayList<Vertex<Integer>> v2List = new ArrayList<>();
        ArrayList<Vertex<Integer>> v3List = new ArrayList<>();

        v1List.addAll(List.of(v1, v2, v3));
        v2List.addAll(List.of(v2));
        v3List.addAll(List.of(v3));

        expected.addAll(Set.of(v1List, v2List, v3List));

        assertEquals(actual, expected);
    }

    @Test
    public void testDFS2() {
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
        adjList.addEdge(v2, v4);

        Set<List<Vertex<Integer>>> actual = Algorithms.depthFirstSearch(adjList);
        Set<List<Vertex<Integer>>> expected = new HashSet<List<Vertex<Integer>>>();

        ArrayList<Vertex<Integer>> v1List = new ArrayList<>();
        ArrayList<Vertex<Integer>> v2List = new ArrayList<>();
        ArrayList<Vertex<Integer>> v3List = new ArrayList<>();
        ArrayList<Vertex<Integer>> v4List = new ArrayList<>();

        v1List.addAll(List.of(v1, v2, v4, v3));
        v2List.addAll(List.of(v2, v4));
        v3List.addAll(List.of(v3));
        v4List.addAll(List.of(v4));

        expected.addAll(Set.of(v1List, v2List, v3List, v4List));

        assertEquals(actual, expected);
    }

    @Test
    public void testDFS3() {
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

        Set<List<Vertex<Integer>>> actual = Algorithms.depthFirstSearch(adjList);
        Set<List<Vertex<Integer>>> expected = new HashSet<>();

        expected.add(List.of(vertexList.get(0), vertexList.get(1), vertexList.get(4),
            vertexList.get(8), vertexList.get(5), vertexList.get(2), vertexList.get(6),
            vertexList.get(3), vertexList.get(7), vertexList.get(9)));
        expected.add(List.of(vertexList.get(1), vertexList.get(4), vertexList.get(8),
            vertexList.get(5)));
        expected.add(List.of(vertexList.get(2), vertexList.get(6)));
        expected.add(List.of(vertexList.get(3), vertexList.get(7), vertexList.get(9)));
        expected.add(List.of(vertexList.get(4), vertexList.get(8)));
        expected.add(List.of(vertexList.get(5)));
        expected.add(List.of(vertexList.get(6)));
        expected.add(List.of(vertexList.get(7), vertexList.get(9)));
        expected.add(List.of(vertexList.get(8)));
        expected.add(List.of(vertexList.get(9)));

        assertEquals(actual, expected);
    }

    @Test
    public void testDFS4() {
        List<Vertex<Integer>> vertexList = new ArrayList<>();
        int size = 10;

        StringBuilder label = new StringBuilder();

        for (int i = 0; i < size; i++) {
            label.append(i);
            vertexList.add(new Vertex<Integer>(label.toString(), i));
            label.delete(0, label.length());
        }

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(vertexList.get(7), vertexList.get(9));
        adjList.addEdge(vertexList.get(0), vertexList.get(1));
        adjList.addEdge(vertexList.get(0), vertexList.get(3));
        adjList.addEdge(vertexList.get(3), vertexList.get(7));
        adjList.addEdge(vertexList.get(1), vertexList.get(5));
        adjList.addEdge(vertexList.get(2), vertexList.get(6));
        adjList.addEdge(vertexList.get(0), vertexList.get(2));
        adjList.addEdge(vertexList.get(4), vertexList.get(8));
        adjList.addEdge(vertexList.get(1), vertexList.get(4));

        Set<List<Vertex<Integer>>> actual = Algorithms.depthFirstSearch(adjList);
        Set<List<Vertex<Integer>>> expected = new HashSet<>();

        expected.add(List.of(vertexList.get(0), vertexList.get(1), vertexList.get(4),
            vertexList.get(8), vertexList.get(5), vertexList.get(2), vertexList.get(6),
            vertexList.get(3), vertexList.get(7), vertexList.get(9)));
        expected.add(List.of(vertexList.get(1), vertexList.get(4), vertexList.get(8),
            vertexList.get(5)));
        expected.add(List.of(vertexList.get(2), vertexList.get(6)));
        expected.add(List.of(vertexList.get(3), vertexList.get(7), vertexList.get(9)));
        expected.add(List.of(vertexList.get(4), vertexList.get(8)));
        expected.add(List.of(vertexList.get(5)));
        expected.add(List.of(vertexList.get(6)));
        expected.add(List.of(vertexList.get(7), vertexList.get(9)));
        expected.add(List.of(vertexList.get(8)));
        expected.add(List.of(vertexList.get(9)));

        assertEquals(actual, expected);
    }

    @Test
    public void testDFS5() {
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

        Set<List<Vertex<Integer>>> actual = Algorithms.depthFirstSearch(adjMatrix);
        Set<List<Vertex<Integer>>> expected = new HashSet<>();

        expected.add(List.of(vertexList.get(0), vertexList.get(1), vertexList.get(2),
            vertexList.get(3), vertexList.get(5), vertexList.get(7), vertexList.get(8),
            vertexList.get(9), vertexList.get(6), vertexList.get(4)));
        expected.add(List.of(vertexList.get(1), vertexList.get(0), vertexList.get(2),
            vertexList.get(3), vertexList.get(5), vertexList.get(7), vertexList.get(8),
            vertexList.get(9), vertexList.get(6), vertexList.get(4)));
        expected.add(List.of(vertexList.get(2), vertexList.get(3), vertexList.get(5),
            vertexList.get(7), vertexList.get(8), vertexList.get(9), vertexList.get(6)));
        expected.add(List.of(vertexList.get(3), vertexList.get(5), vertexList.get(7),
            vertexList.get(8), vertexList.get(9)));
        expected.add(List.of(vertexList.get(4), vertexList.get(3), vertexList.get(5),
            vertexList.get(7), vertexList.get(8), vertexList.get(9)));
        expected.add(List.of(vertexList.get(5), vertexList.get(7),
            vertexList.get(8), vertexList.get(9)));
        expected.add(List.of(vertexList.get(6)));
        expected.add(List.of(vertexList.get(7)));
        expected.add(List.of(vertexList.get(8), vertexList.get(9)));
        expected.add(List.of(vertexList.get(9)));

        assertEquals(actual, expected);
    }

    @Test
    public void testBFS1() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v1);
        vertexList.add(v3);
        vertexList.add(v2);
        vertexList.add(v3);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);

        Set<List<Vertex<Integer>>> actual = Algorithms.breadthFirstSearch(adjList);
        Set<List<Vertex<Integer>>> expected = new HashSet<List<Vertex<Integer>>>();

        ArrayList<Vertex<Integer>> v1List = new ArrayList<>();
        ArrayList<Vertex<Integer>> v2List = new ArrayList<>();
        ArrayList<Vertex<Integer>> v3List = new ArrayList<>();

        v1List.addAll(List.of(v1, v2, v3));
        v2List.addAll(List.of(v2));
        v3List.addAll(List.of(v3));

        expected.addAll(Set.of(v1List, v2List, v3List));

        assertEquals(actual, expected);
    }

    @Test
    public void testBFS2() {
        Vertex<Integer> v1 = new Vertex<Integer>("v1", 1);
        Vertex<Integer> v2 = new Vertex<Integer>("v2", 2);
        Vertex<Integer> v3 = new Vertex<Integer>("v3", 3);
        Vertex<Integer> v4 = new Vertex<Integer>("v4", 4);

        List<Vertex<Integer>> vertexList = new ArrayList<>();
        vertexList.add(v4);
        vertexList.add(v1);
        vertexList.add(v2);
        vertexList.add(v3);

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(v1, v2);
        adjList.addEdge(v1, v3);
        adjList.addEdge(v2, v4);

        Set<List<Vertex<Integer>>> actual = Algorithms.breadthFirstSearch(adjList);
        Set<List<Vertex<Integer>>> expected = new HashSet<List<Vertex<Integer>>>();

        ArrayList<Vertex<Integer>> v1List = new ArrayList<>();
        ArrayList<Vertex<Integer>> v2List = new ArrayList<>();
        ArrayList<Vertex<Integer>> v3List = new ArrayList<>();
        ArrayList<Vertex<Integer>> v4List = new ArrayList<>();

        v1List.addAll(List.of(v1, v2, v3, v4));
        v2List.addAll(List.of(v2, v4));
        v3List.addAll(List.of(v3));
        v4List.addAll(List.of(v4));

        expected.addAll(Set.of(v1List, v2List, v3List, v4List));

        assertEquals(expected, actual);
    }

    @Test
    public void testBFS3() {
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

        Set<List<Vertex<Integer>>> actual = Algorithms.breadthFirstSearch(adjList);
        Set<List<Vertex<Integer>>> expected = new HashSet<>();

        expected.add(List.of(vertexList.get(0), vertexList.get(1), vertexList.get(2),
            vertexList.get(3), vertexList.get(4), vertexList.get(5), vertexList.get(6),
            vertexList.get(7), vertexList.get(8), vertexList.get(9)));
        expected.add(List.of(vertexList.get(1), vertexList.get(4), vertexList.get(5),
            vertexList.get(8)));
        expected.add(List.of(vertexList.get(2), vertexList.get(6)));
        expected.add(List.of(vertexList.get(3), vertexList.get(7), vertexList.get(9)));
        expected.add(List.of(vertexList.get(4), vertexList.get(8)));
        expected.add(List.of(vertexList.get(5)));
        expected.add(List.of(vertexList.get(6)));
        expected.add(List.of(vertexList.get(7), vertexList.get(9)));
        expected.add(List.of(vertexList.get(8)));
        expected.add(List.of(vertexList.get(9)));

        assertEquals(actual, expected);
    }

    @Test
    public void testBFS4() {
        List<Vertex<Integer>> vertexList = new ArrayList<>();
        int size = 10;

        StringBuilder label = new StringBuilder();

        for (int i = 0; i < size; i++) {
            label.append(i);
            vertexList.add(new Vertex<Integer>(label.toString(), i));
            label.delete(0, label.length());
        }

        AdjacencyListGraph<Integer> adjList = new AdjacencyListGraph<>(vertexList);
        adjList.addEdge(vertexList.get(7), vertexList.get(9));
        adjList.addEdge(vertexList.get(0), vertexList.get(1));
        adjList.addEdge(vertexList.get(0), vertexList.get(3));
        adjList.addEdge(vertexList.get(3), vertexList.get(7));
        adjList.addEdge(vertexList.get(1), vertexList.get(5));
        adjList.addEdge(vertexList.get(2), vertexList.get(6));
        adjList.addEdge(vertexList.get(0), vertexList.get(2));
        adjList.addEdge(vertexList.get(4), vertexList.get(8));
        adjList.addEdge(vertexList.get(1), vertexList.get(4));

        Set<List<Vertex<Integer>>> actual = Algorithms.breadthFirstSearch(adjList);
        Set<List<Vertex<Integer>>> expected = new HashSet<>();

        expected.add(List.of(vertexList.get(0), vertexList.get(1), vertexList.get(2),
            vertexList.get(3), vertexList.get(4), vertexList.get(5), vertexList.get(6),
            vertexList.get(7), vertexList.get(8), vertexList.get(9)));
        expected.add(List.of(vertexList.get(1), vertexList.get(4), vertexList.get(5),
            vertexList.get(8)));
        expected.add(List.of(vertexList.get(2), vertexList.get(6)));
        expected.add(List.of(vertexList.get(3), vertexList.get(7), vertexList.get(9)));
        expected.add(List.of(vertexList.get(4), vertexList.get(8)));
        expected.add(List.of(vertexList.get(5)));
        expected.add(List.of(vertexList.get(6)));
        expected.add(List.of(vertexList.get(7), vertexList.get(9)));
        expected.add(List.of(vertexList.get(8)));
        expected.add(List.of(vertexList.get(9)));

        assertEquals(actual, expected);
    }

    @Test
    public void testBFS5() {
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

        Set<List<Vertex<Integer>>> actual = Algorithms.breadthFirstSearch(adjMatrix);
        Set<List<Vertex<Integer>>> expected = new HashSet<>();

        expected.add(List.of(vertexList.get(0), vertexList.get(1), vertexList.get(2),
            vertexList.get(3), vertexList.get(4), vertexList.get(5), vertexList.get(6),
            vertexList.get(8), vertexList.get(7), vertexList.get(9)));
        expected.add(List.of(vertexList.get(1), vertexList.get(0), vertexList.get(2),
            vertexList.get(5), vertexList.get(3), vertexList.get(4), vertexList.get(6),
            vertexList.get(7),
            vertexList.get(8), vertexList.get(9)));
        expected.add(List.of(vertexList.get(2), vertexList.get(3), vertexList.get(6),
            vertexList.get(5), vertexList.get(7), vertexList.get(8), vertexList.get(9)));
        expected.add(List.of(vertexList.get(3), vertexList.get(5), vertexList.get(7),
            vertexList.get(8), vertexList.get(9)));
        expected.add(List.of(vertexList.get(4), vertexList.get(3), vertexList.get(8),
            vertexList.get(5), vertexList.get(9), vertexList.get(7)));
        expected.add(List.of(vertexList.get(5), vertexList.get(7),
            vertexList.get(8), vertexList.get(9)));
        expected.add(List.of(vertexList.get(6)));
        expected.add(List.of(vertexList.get(7)));
        expected.add(List.of(vertexList.get(8), vertexList.get(9)));
        expected.add(List.of(vertexList.get(9)));

        assertEquals(actual, expected);
    }

    private void concatLists(List<Vertex<Integer>> l1, List<Vertex<Integer>> l2) {
        for (int i = 0; i < l2.size(); i++) {
            l1.add(l2.get(i));
        }
    }

}
