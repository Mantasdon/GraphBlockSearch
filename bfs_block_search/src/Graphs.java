import java.io.*;
import java.util.*;

// This class represents a directed graph using adjacency list representation
class Graphs {
    private int V, E; // Number of vertices & edges respectively
    private LinkedList<Integer> adj[]; // Adjacency List

    // Count is the number of biconnected components. time is used to find discovery times
    static int count = 0, time = 0;

    class Edge {
        int u, v;
        Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    };

    // Constructor
    Graphs(int v) {
        V = v;
        E = 0;
        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i)
            adj[i] = new LinkedList<>();
    }

    void addEdge(int v, int w) {
        adj[v].add(w);
        E++;
    }

    void BCCUtil(int u, int disc[], int low[], LinkedList<Edge> st,
                 int parent[], List<List<Integer>> biconnectedComponents) {

        disc[u] = low[u] = ++time;
        int children = 0;

        Iterator<Integer> it = adj[u].iterator();
        while (it.hasNext()) {
            int v = it.next();

            if (disc[v] == -1) {
                children++;
                parent[v] = u;

                st.add(new Edge(u, v));
                BCCUtil(v, disc, low, st, parent, biconnectedComponents);

                if (low[u] > low[v])
                    low[u] = low[v];

                if ((disc[u] == 1 && children > 1) || (disc[u] > 1 && low[v] >= disc[u])) {
                    List<Integer> component = new ArrayList<>();
                    //System.out.print("Articulation point = " + u + " ");
                    while (st.getLast().u != u || st.getLast().v != v) {
                        Edge edge = st.removeLast();
                        //System.out.print(edge.u + "--" + edge.v + " ");
                        if (!component.contains(edge.u)) component.add(edge.u);
                        if (!component.contains(edge.v)) component.add(edge.v);
                    }
                    Edge edge = st.removeLast();
                    //System.out.println(edge.u + "--" + edge.v + " ");
                    if (!component.contains(edge.u)) component.add(edge.u);
                    if (!component.contains(edge.v)) component.add(edge.v);

                    biconnectedComponents.add(component);
                    count++;
                }
            } else if (v != parent[u] && disc[v] < disc[u]) {
                if (low[u] > disc[v])
                    low[u] = disc[v];

                st.add(new Edge(u, v));
            }
        }
    }

    void BFS() {
        int disc[] = new int[V];
        int low[] = new int[V];
        int parent[] = new int[V];
        LinkedList<Edge> st = new LinkedList<Edge>();
        List<List<Integer>> biconnectedComponents = new ArrayList<>();

        List<Integer> bfsOrder = getBFSTraversalOrder();

        // Initialize disc and low, and parent arrays
        for (int i = 0; i < V; i++) {
            disc[i] = -1;
            low[i] = -1;
            parent[i] = -1;
        }

        for (int i = 0; i < V; i++) {
            if (disc[i] == -1)
                BCCUtil(i, disc, low, st, parent, biconnectedComponents);

            // If stack is not empty, pop all edges from stack
            if (!st.isEmpty()) {
                List<Integer> component = new ArrayList<>();
                while (!st.isEmpty()) {
                    Edge edge = st.removeLast();
                    //System.out.print(edge.u + "--" + edge.v + " ");
                    if (!component.contains(edge.u)) component.add(edge.u);
                    if (!component.contains(edge.v)) component.add(edge.v);
                }
                System.out.println();

                biconnectedComponents.add(component);
                count++;
            }
        }

        System.out.println("Biconnected Components:");
        for (List<Integer> component : biconnectedComponents) {
            // Sort each component according to the BFS traversal order
            List<Integer> sortedComponent = sortComponentByBFSOrder(component, bfsOrder);
            System.out.println(sortedComponent);
        }
    }

    List<Integer> getBFSTraversalOrder() {
        boolean visited[] = new boolean[V];
        List<Integer> bfsOrder = new ArrayList<>();

        for (int start = 0; start < V; start++) {
            if (!visited[start]) {
                LinkedList<Integer> queue = new LinkedList<>();
                visited[start] = true;
                queue.add(start);

                while (!queue.isEmpty()) {
                    int u = queue.poll();
                    bfsOrder.add(u);

                    Iterator<Integer> i = adj[u].listIterator();
                    while (i.hasNext()) {
                        int n = i.next();
                        if (!visited[n]) {
                            visited[n] = true;
                            queue.add(n);
                        }
                    }
                }
            }
        }

        //System.out.println("BFS Traversal Order: " + bfsOrder);
        return bfsOrder;
    }

    List<Integer> sortComponentByBFSOrder(List<Integer> component, List<Integer> bfsOrder) {
        Map<Integer, Integer> bfsIndexMap = new HashMap<>();
        for (int i = 0; i < bfsOrder.size(); i++) {
            bfsIndexMap.put(bfsOrder.get(i), i);
        }

        component.sort(Comparator.comparingInt(bfsIndexMap::get));
        return component;
    }

    void printBFSTraversal() {
        System.out.println("BFS Traversal:");
        boolean visited[] = new boolean[V];

        for (int start = 0; start < V; start++) {
            if (!visited[start]) {
                LinkedList<Integer> queue = new LinkedList<>();
                visited[start] = true;
                queue.add(start);

                while (!queue.isEmpty()) {
                    int u = queue.poll();
                    System.out.print(u + " ");

                    Iterator<Integer> i = adj[u].listIterator();
                    while (i.hasNext()) {
                        int n = i.next();
                        if (!visited[n]) {
                            visited[n] = true;
                            queue.add(n);
                        }
                    }
                }
                System.out.println();
            }
        }
    }

    void printDFSTraversal() {
        System.out.println("DFS Traversal:");
        boolean visited[] = new boolean[V];

        for (int i = 0; i < V; ++i)
            visited[i] = false;

        for (int i = 0; i < V; ++i)
            if (!visited[i])
                DFSUtil(i, visited);
        System.out.println();
    }

    void DFSUtil(int v, boolean visited[]) {
        visited[v] = true;
        System.out.print(v + " ");

        Iterator<Integer> i = adj[v].listIterator();
        while (i.hasNext()) {
            int n = i.next();
            if (!visited[n])
                DFSUtil(n, visited);
        }
    }

    public static void main(String args[]) {
        int v = 13;
        Graphs g = new Graphs(v);
        int[][] matrix = {
                {0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0}
        };

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                if (matrix[i][j] == 1) {
                    g.addEdge(i, j);
                }
            }
        }

        g.BFS();
    }
}
