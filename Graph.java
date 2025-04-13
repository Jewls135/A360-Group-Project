import java.util.*;

public class Graph {
    private HashMap<Airport, ArrayList<Edge>> nodeListings;

    public Graph(ArrayList<Airport> airports) {
        nodeListings = new HashMap<>();
        for (Airport airport : airports) {
            addNode(airport);
        }
    }

    public HashMap<Airport, ArrayList<Edge>> getListings() {
        return nodeListings;
    }

    public void addNode(Airport newAirport) {
        if (!nodeListings.containsKey(newAirport)) {
            nodeListings.put(newAirport, new ArrayList<>());
            for (Airport airport : nodeListings.keySet()) {
                if (!airport.equals(newAirport)) {
                    Edge edge1 = new Edge(newAirport, airport);
                    Edge edge2 = new Edge(airport, newAirport);
                    nodeListings.get(newAirport).add(edge1);
                    nodeListings.get(airport).add(edge2);
                }
            }
        }
    }

    public void addEdge(Airport fromAirport, Airport toAirport) {
        if (nodeListings.containsKey(fromAirport) && nodeListings.containsKey(toAirport)) {
            Edge edge = new Edge(fromAirport, toAirport);
            nodeListings.get(fromAirport).add(edge);
        }
    }
}