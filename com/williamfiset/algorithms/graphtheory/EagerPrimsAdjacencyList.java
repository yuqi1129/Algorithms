/**
 * An implementation of the eager Prim's algorithm which relies on 
 * an indexed priority queue data structure to query the next best edge.
 *
 *  Time Complexity: O(ElogV)
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 **/
package com.williamfiset.algorithms.graphtheory;

import static java.lang.Math.*;
import java.util.*;

public class EagerPrimsAdjacencyList {

  static class Edge implements Comparable<Edge> {
    int from, to, cost;
    public Edge(int from, int to, int cost) {
      this.from = from;
      this.to = to;
      this.cost = cost;
    }
    @Override public int compareTo(Edge other) {
      return cost - other.cost;
    }
  }

  // Inputs
  private final int n;
  private final List<List<Edge>> graph;

  // Internal
  private boolean solved;
  private boolean mstExists;

  // Outputs
  private long minCostSum;
  private Edge[] mstEdges;

  public EagerPrimsAdjacencyList(List<List<Edge>> graph) {
    if (graph == null) throw new IllegalArgumentException();
    this.n = graph.size();
    this.graph = graph;
  }

  private void relaxEdgesAtNode(int nodeIndex, MinIndexedDHeap<Edge> ipq) {
    // Insert initial set of edges
    for (Edge e : graph.get(nodeIndex)) {
      if (ipq.contains(e.to)) {
        ipq.decrease(e.to, e);
      } else {
        ipq.insert(e.to, e);
      }
    }
  }

  // Returns the edges used in finding the minimum spanning tree,
  // or returns null if no MST exists.
  public Edge[] getMst() {
    solve();
    return mstExists ? mstEdges : null;
  }

  public Long getMstCost() {
    solve();
    return mstExists ? minCostSum : null;
  }

  // Returns the minimum spanning tree cost or null if no MST exists.
  private void solve() {
    if (solved) return;

    boolean[] visited = new boolean[n];
    MinIndexedDHeap<Edge> ipq = new MinIndexedDHeap<>(/*degree=*/2, n);

    relaxEdgesAtNode(0, ipq);
    visited[0] = true;

    int m = n-1, edgeCount = 0;
    mstEdges = new Edge[m];

    for(int i = 0; !ipq.isEmpty() && edgeCount != m;) {
      Edge e = ipq.pollMinValue();
      if (!visited[e.to]) {
        mstEdges[i++] = e;

        edgeCount++;
        minCostSum += e.cost;

        relaxEdgesAtNode(e.to, ipq);
        visited[e.to] = true;
      }
    }

    // Verify MST spans entire graph.
    mstExists = (edgeCount == m);
    solved = true;
  }

  /* Graph construction helpers. */

  static List<List<Edge>> createEmptyGraph(int n) {
    List<List<Edge>> g = new ArrayList<>();
    for(int i = 0; i < n; i++) g.add(new ArrayList<>());
    return g;
  }

  static void addDirectedEdge(List<List<Edge>> g, int from, int to, int cost) {
    g.get(from).add(new Edge(from, to, cost));
  }

  static void addUndirectedEdge(List<List<Edge>> g, int from, int to, int cost) {
    addDirectedEdge(g, from, to, cost);
    addDirectedEdge(g, to, from, cost);
  }

    /* Example usage. */

  public static void main(String[] args) {
    // example1();
    firstGraphFromSlides();
  }

  private static void example1() {
    int n = 10;
    List<List<Edge>> g = createEmptyGraph(n);

    addUndirectedEdge(g, 0, 1, 5);
    addUndirectedEdge(g, 1, 2, 4);
    addUndirectedEdge(g, 2, 9, 2);
    addUndirectedEdge(g, 0, 4, 1);
    addUndirectedEdge(g, 0, 3, 4);
    addUndirectedEdge(g, 1, 3, 2);
    addUndirectedEdge(g, 2, 7, 4);
    addUndirectedEdge(g, 2, 8, 1);
    addUndirectedEdge(g, 9, 8, 0);
    addUndirectedEdge(g, 4, 5, 1);
    addUndirectedEdge(g, 5, 6, 7);
    addUndirectedEdge(g, 6, 8, 4);
    addUndirectedEdge(g, 4, 3, 2);
    addUndirectedEdge(g, 5, 3, 5);
    addUndirectedEdge(g, 3, 6, 11);
    addUndirectedEdge(g, 6, 7, 1);
    addUndirectedEdge(g, 3, 7, 2);
    addUndirectedEdge(g, 7, 8, 6);

    EagerPrimsAdjacencyList solver = new EagerPrimsAdjacencyList(g);
    Long cost = solver.getMstCost();

    if (cost == null) {
      System.out.println("No MST does not exists");
    } else {
      System.out.println("MST cost: " + cost);
      for (Edge e : solver.getMst()) {
        System.out.println(String.format("from: %d, to: %d, cost: %d", e.from, e.to, e.cost));
      }
    }

    // Output:
    // MST cost: 14
    // from: 0, to: 4, cost: 1
    // from: 4, to: 5, cost: 1
    // from: 4, to: 3, cost: 2
    // from: 3, to: 1, cost: 2
    // from: 3, to: 7, cost: 2
    // from: 7, to: 6, cost: 1
    // from: 6, to: 8, cost: 4
    // from: 8, to: 9, cost: 0
    // from: 8, to: 2, cost: 1
  }

  private static void firstGraphFromSlides() {
    int n = 7;
    List<List<Edge>> g = createEmptyGraph(n);

    addUndirectedEdge(g, 0, 1, 9);
    addUndirectedEdge(g, 0, 2, 0);
    addUndirectedEdge(g, 0, 3, 5);
    addUndirectedEdge(g, 0, 5, 7);
    addUndirectedEdge(g, 1, 3, -2);
    addUndirectedEdge(g, 1, 4, 3);
    addUndirectedEdge(g, 1, 6, 4);
    addUndirectedEdge(g, 2, 5, 6);
    addUndirectedEdge(g, 3, 5, 2);
    addUndirectedEdge(g, 3, 6, 3);
    addUndirectedEdge(g, 4, 6, 6);
    addUndirectedEdge(g, 5, 6, 1);

    EagerPrimsAdjacencyList solver = new EagerPrimsAdjacencyList(g);
    Long cost = solver.getMstCost();

    if (cost == null) {
      System.out.println("No MST does not exists");
    } else {
      System.out.println("MST cost: " + cost);
      for (Edge e : solver.getMst()) {
        System.out.println(String.format("from: %d, to: %d, cost: %d", e.from, e.to, e.cost));
      }
    }
  }

    /* Supporting indexed priority queue implementation. */

  private static class MinIndexedDHeap <T extends Comparable<T>> {

    // Current number of elements in the heap.
    private int sz;

    // Maximum number of elements in the heap.
    private final int N;

    // The degree of every node in the heap.
    private final int D;

    // Lookup arrays to track the child/parent indexes of each node.
    private final int[] child, parent;

    // The Position Map (pm) maps Key Indexes (ki) to where the position of that 
    // key is represented in the priority queue in the domain [0, sz).
    public final int[] pm;

    // The Inverse Map (im) stores the indexes of the keys in the range
    // [0, sz) which make up the priority queue. It should be noted that
    // 'im' and 'pm' are inverses of each other, so: pm[im[i]] = im[pm[i]] = i
    public final int[] im;

    // The values associated with the keys. It is very important  to note 
    // that this array is indexed by the key indexes (aka 'ki').
    public final Object[] values;

    // Initializes a D-ary heap with a maximum capacity of maxSize.
    public MinIndexedDHeap(int degree, int maxSize) {
      if (maxSize <= 0) throw new IllegalArgumentException("maxSize <= 0");

      D = max(2, degree);
      N = max(D+1, maxSize);

      im = new int[N];
      pm = new int[N];
      child = new int[N];
      parent = new int[N];
      values = new Object[N];

      for (int i = 0; i < N; i++) {
        parent[i] = (i-1) / D;
        child[i] = i*D + 1;
        pm[i] = im[i] = -1;
      }
    }

    public int size() {
      return sz;
    }

    public boolean isEmpty() {
      return sz == 0;
    }

    public boolean contains(int ki) {
      keyInBoundsOrThrow(ki);
      return pm[ki] != -1;
    }

    public int peekMinKeyIndex() {
      isNotEmptyOrThrow();
      return im[0];
    }

    public int pollMinKeyIndex() {
      int minki = peekMinKeyIndex();
      delete(minki);
      return minki;
    }

    @SuppressWarnings("unchecked")
    public T peekMinValue() {
      isNotEmptyOrThrow();
      return (T) values[im[0]];
    }

    public T pollMinValue() {
      T minValue = peekMinValue();
      delete(peekMinKeyIndex());
      return minValue;
    }

    public void insert(int ki, T value) {
      if (contains(ki))
        throw new IllegalArgumentException("index already exists; received: " + ki);
      valueNotNullOrThrow(value);
      pm[ki] = sz;
      im[sz] = ki;
      values[ki] = value;
      swim(sz++);
    }

    @SuppressWarnings("unchecked")
    public T valueOf(int ki) {
      keyExistsOrThrow(ki);
      return (T) values[ki];
    }

    @SuppressWarnings("unchecked")
    public T delete(int ki) {
      keyExistsOrThrow(ki);
      final int i = pm[ki];
      swap(i, --sz);
      sink(i);
      swim(i);
      T value = (T) values[ki];
      values[ki] = null;
      pm[ki] = -1;
      im[sz] = -1;
      return value;
    }

    @SuppressWarnings("unchecked")
    public T update(int ki, T value) {
      keyExistsAndValueNotNullOrThrow(ki, value);
      final int i = pm[ki];
      T oldValue = (T) values[ki];
      values[ki] = value;
      sink(i);
      swim(i);
      return oldValue;
    }

    // Strictly decreases the value associated with 'ki' to 'value'
    public void decrease(int ki, T value) {
      keyExistsAndValueNotNullOrThrow(ki, value);
      if (less(value, values[ki])) {
        values[ki] = value;
        swim(pm[ki]);
      }
    }

    // Strictly increases the value associated with 'ki' to 'value'
    public void increase(int ki, T value) {
      keyExistsAndValueNotNullOrThrow(ki, value);
      if (less(values[ki], value)) {
        values[ki] = value;
        sink(pm[ki]);
      }
    }

      /* Helper functions */

    private void sink(int i) {
      for(int j = minChild(i); j != -1;) {
        swap(i, j);
        i = j;
        j = minChild(i);
      }
    }

    private void swim(int i) {
      while(less(i, parent[i])) {
        swap(i, parent[i]);
        i = parent[i];
      }
    }

    // From the parent node at index i find the minimum child below it
    private int minChild(int i) {
      int index = -1, from = child[i], to = min(sz, from + D);
      for(int j = from; j < to; j++)
        if (less(j, i))
          index = i = j;
      return index;
    }

    private void swap(int i, int j) {
      pm[im[j]] = i;
      pm[im[i]] = j;
      int tmp = im[i];
      im[i] = im[j];
      im[j] = tmp;
    }

    // Tests if the value of node i < node j
    @SuppressWarnings("unchecked")
    private boolean less(int i, int j) {
      return ((Comparable<? super T>) values[im[i]]).compareTo((T) values[im[j]]) < 0;
    }

    @SuppressWarnings("unchecked")
    private boolean less(Object obj1, Object obj2) {
      return ((Comparable<? super T>) obj1).compareTo((T) obj2) < 0;
    }

    @Override
    public String toString() {
      List<Integer> lst = new ArrayList<>(sz);
      for(int i = 0; i < sz; i++) lst.add(im[i]);
      return lst.toString();
    }

      /* Helper functions to make the code more readable. */

    private void isNotEmptyOrThrow() {
      if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
    }

    private void keyExistsAndValueNotNullOrThrow(int ki, Object value) {
      keyExistsOrThrow(ki);
      valueNotNullOrThrow(value);
    }

    private void keyExistsOrThrow(int ki) {
      if (!contains(ki)) 
        throw new NoSuchElementException("Index does not exist; received: " + ki);
    }

    private void valueNotNullOrThrow(Object value) {
      if (value == null) 
        throw new IllegalArgumentException("value cannot be null");
    }

    private void keyInBoundsOrThrow(int ki) {
      if (ki < 0 || ki >= N) 
        throw new IllegalArgumentException("Key index out of bounds; received: " + ki);
    }

      /* Test functions */

    // Recursively checks if this heap is a min heap. This method is used
    // for testing purposes to validate the heap invariant.
    public boolean isMinHeap() {
      return isMinHeap(0);
    }

    private boolean isMinHeap(int i) {
      int from = child[i], to = min(sz, from + D);
      for(int j = from; j < to; j++) {
        if (!less(i, j)) return false;
        if (!isMinHeap(j)) return false;
      }
      return true;
    }

  }

}


