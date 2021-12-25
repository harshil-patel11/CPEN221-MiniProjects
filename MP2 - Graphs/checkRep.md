The checkRep methods for AdjacencyMatrixGraph and AdjacencyListGraph have been moved to this markdown file as suggested by post #2379 on campuswire.

```java
/**
  checkRep method for AdjacencyMatrixGraph
  */
private void checkRep() {
        if (adjacencyMatrix.size() >= 1) {
            assert adjacencyMatrix.size() == adjacencyMatrix.get(0).size() :
                "adjacencyMatrix.size() is not equal to adjacencyMatrix.get(0).size()";
        }
        assert adjacencyMatrix.size() == vertexList.size() :
            "adjacencyMatrix.size() is not equal to vertexList.size()";

        for (Edge<T> e : edgeSet) {
            Vertex<T> v1 = e.getHeadVertex();
            Vertex<T> v2 = e.getTailVertex();
            int indexV1 = vertexList.indexOf(v1);
            int indexV2 = vertexList.indexOf(v2);
            assert adjacencyMatrix.get(indexV1).get(indexV2) :
                "adjacencyMatrix doesn't contain an edge that is in the edgeSet";
        }

        for (int i = 0; i < adjacencyMatrix.size(); i++) {
            for (int j = 0; j < adjacencyMatrix.size(); j++) {
                if (adjacencyMatrix.get(i).get(j)) {
                    Vertex<T> v1 = vertexList.get(i);
                    Vertex<T> v2 = vertexList.get(j);
                    Edge<T> e = new Edge<>(v1, v2);
                    assert edgeSet.contains(e) :
                     "edgeSet doesn't contain an edge that exists in adjacencyMatrix"; 
                }
            }
        }
}

/**
  checkRep method for AdjacencyListGraph
  */
private void checkRep() {
    assert adjacencyMap.keySet().equals(vertexSet) :
        "vertexSet not equal to adjacencyMap.keySet()";

    for (Edge<T> e : edgeSet) {
        Vertex<T> v1 = e.getHeadVertex();
        Vertex<T> v2 = e.getTailVertex();
        assert adjacencyMap.get(v1).contains(v2) :
            "adjacencyMap doesn't contain an edge that is in the edgeSet";
    }

    for (Vertex<T> v1 : adjacencyMap.keySet()) {
        for (Vertex<T> v2 : adjacencyMap.get(v1)) {
            Edge<T> e = new Edge<>(v1, v2);
            assert edgeSet.contains(e) :
                "edgeSet doesn't contain an edge that exists in the adjacencyMap";
        }
    }
}  
    