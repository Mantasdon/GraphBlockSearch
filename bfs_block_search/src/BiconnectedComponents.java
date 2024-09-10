import java.util.*;

public class BiconnectedComponents {
    private int V;
    private LinkedList<Integer> adj[];

    class Edge {
        int u, v;

        Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
    }

    public static void addEdge(ArrayList<Integer>[] adj, int u, int v) {
        adj[u].add(v);
        adj[v].add(u);
    }

    public static void findCycles(ArrayList<Integer>[] adj, int V) {
        boolean[] visited = new boolean[V];
        List<List<Integer>> allCycles = new ArrayList<>();
        Set<String> cycleSet = new HashSet<>(); // To avoid duplicate cycles

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                detectCycleDFS(adj, i, -1, visited, new ArrayList<>(), allCycles, cycleSet);
            }
        }

        // Print the second detected cycle if it exists
        if (allCycles.size() > 1) {
            //System.out.println(allCycles.get(0));
            System.out.println(allCycles.get(1));
            //System.out.println(allCycles.get(2));
            //System.out.println(allCycles.get(3));

        } else {
            System.out.println("No second cycle found");
        }
    }

    private static void detectCycleDFS(ArrayList<Integer>[] adj, int curr, int parent, boolean[] visited, List<Integer> path, List<List<Integer>> allCycles, Set<String> cycleSet) {
        visited[curr] = true;
        path.add(curr);

        for (int neighbor : adj[curr]) {
            if (!visited[neighbor]) {
                detectCycleDFS(adj, neighbor, curr, visited, path, allCycles, cycleSet);
            } else if (neighbor != parent && path.contains(neighbor)) {
                // Cycle detected, construct the cycle
                List<Integer> cycle = new ArrayList<>(path.subList(path.indexOf(neighbor), path.size()));
                cycle.add(neighbor);

                // Ensure unique cycles
                String cycleStr = cycle.toString();
                if (!cycleSet.contains(cycleStr)) {
                    allCycles.add(cycle);
                    cycleSet.add(cycleStr);
                }
            }
        }

        path.remove(path.size() - 1);
    }

    public static void main(String[] args) {
        int V = 7;
        BiconnectedComponents g = new BiconnectedComponents();
        g.V = V;
        g.adj = new LinkedList[V];
        for (int i = 0; i < V; i++) {
            g.adj[i] = new LinkedList<>();
        }

        ArrayList<Integer>[] adj = new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<>();
        }

        int[][] matrix = {

               //0  1  2  3  4  5  6  7
                {0, 1, 0, 0, 0, 0, 1},//0
                {1, 0, 1, 0, 0, 0, 0},//1
                {0, 1, 0, 1, 0, 0, 1},//2
                {0, 0, 1, 0, 1, 1, 0},//3
                {0, 0, 0, 1, 0, 1, 0},//4
                {0, 0, 0, 1, 1, 0, 6},//5
                {1, 0, 1, 0, 0, 1, 0},//6


        };

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (matrix[i][j] == 1) {
                    addEdge(adj, i, j);
                    g.addEdge(i, j);
                }
            }
        }

        findCycles(adj, V);
    }
}
