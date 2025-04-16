import java.util.*;

/**
 * Represents a weighted undirected graph where nodes are airports and
 * edges represent connections between them.
 */
public class Graph {
    private HashMap<Airport, ArrayList<Edge>> nodeListings;

    /**
     * Constructs a new Graph with the given list of airports.
     * Each airport is added as a node, and edges are initialized between
     * each pair of airports.
     *
     * @param airports the initial list of airports to include in the graph
     */
    public Graph(ArrayList<Airport> airports) {
        nodeListings = new HashMap<>();
        for (Airport airport : airports) {
            addNode(airport);
        }
    }

    /**
     * Returns the adjacency list of the graph.
     *
     * @return a HashMap where keys are Airport nodes and values are lists of outgoing Edges
     */
    public HashMap<Airport, ArrayList<Edge>> getListings() {
        return nodeListings;
    }

    /**
     * Adds a new airport node to the graph if it does not already exist.
     * Automatically creates edges between the new node and all existing nodes,
     * forming an undirected connection.
     *
     * @param newAirport the Airport to be added as a node
     */
    public void addNode(Airport newAirport) {
        if (!nodeListings.containsKey(newAirport)) {
            nodeListings.put(newAirport, new ArrayList<>());
            for (Airport airport : nodeListings.keySet()) {
                if (!airport.equals(newAirport)) {
                    Edge edge1 = new Edge(newAirport, airport); // edge from new to existing
                    Edge edge2 = new Edge(airport, newAirport); // edge from existing to new
                    nodeListings.get(newAirport).add(edge1);
                    nodeListings.get(airport).add(edge2);
                }
            }
        }
    }

    /**
     * Adds a directed edge from one airport to another.
     * This does not add a reverse edge, so it represents a one-way connection.
     * Useful for customizing connectivity beyond the default full-mesh behavior.
     *
     * @param fromAirport the origin Airport
     * @param toAirport   the destination Airport
     */
    public void addEdge(Airport fromAirport, Airport toAirport) {
        if (nodeListings.containsKey(fromAirport) && nodeListings.containsKey(toAirport)) {
            Edge edge = new Edge(fromAirport, toAirport);
            nodeListings.get(fromAirport).add(edge);
        }
    }
}