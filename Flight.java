import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Flight {
    Graph airportsGraph;
    private final String LINE_SEPARATOR = "--------------------------------";
    private final double KNOT_CONVERSION = 1.852;

    public void planFlight(ArrayList<Airport> destinationAirports, Airplane selectedPlane,
            ArrayList<Airport> airportList) {
        airportsGraph = new Graph(airportList);
        ArrayList<Edge> allFlightLegs = new ArrayList<Edge>();
        for (int i = 0; i < destinationAirports.size() - 1; i++) {
            ArrayList<Edge> currentLegs = findRoute(destinationAirports.get(i), destinationAirports.get(i + 1),
                    selectedPlane);
            allFlightLegs.addAll(currentLegs);
        } // End of for
        System.out.println(displayFlightPlan(allFlightLegs, selectedPlane));
    } // End of method planFlight

    private ArrayList<Edge> findRoute(Airport fromAirport, Airport toAirport, Airplane selectedAirplane) {
        ArrayList<Edge> route = new ArrayList<>();
        HashMap<Airport, Double> distanceMap = new HashMap<>();
        PriorityQueue<Airport> queue = new PriorityQueue<>(
        Comparator.comparingDouble(a -> distanceMap.getOrDefault(a, Double.MAX_VALUE)));
        HashMap<Airport, Edge> previousEdges = new HashMap<>();
        HashMap<Airport, Double> fuelRemaining = new HashMap<>();

        distanceMap.put(fromAirport, 0.0);
        fuelRemaining.put(fromAirport, selectedAirplane.getTankSize()); // Start with full tank
        queue.add(fromAirport);

        while (!queue.isEmpty()) {
            Airport current = queue.poll();

            if (current.equals(toAirport))
                break;

            for (Edge edge : airportsGraph.getListings().getOrDefault(current, new ArrayList<>())) {
                Airport nextAirport = edge.getDestinationNode();
                double legDistance = edge.getDistance();
                double fuelRequired = legDistance * selectedAirplane.getFuelBurnRate();
                double currentFuel = fuelRemaining.get(current);

                String requiredFuel = (selectedAirplane.getType() == 3) ? "AVGAS" : "JA-a";
                boolean canRefuel = Arrays.asList(nextAirport.getFuelTypes()).contains(requiredFuel);

                if (currentFuel < fuelRequired) {
                    if (!canRefuel)
                        continue;
                    currentFuel = selectedAirplane.getTankSize(); // Refuel because we can
                    if (currentFuel < fuelRequired)
                        continue;
                }

                double remainingFuelAfterLeg = currentFuel - fuelRequired;
                double nextFuelLevel = canRefuel ? selectedAirplane.getTankSize() : remainingFuelAfterLeg;
                double newDistance = distanceMap.get(current) + legDistance;

                if (newDistance < distanceMap.getOrDefault(nextAirport, Double.MAX_VALUE)) {
                    distanceMap.put(nextAirport, newDistance);
                    fuelRemaining.put(nextAirport, nextFuelLevel);
                    previousEdges.put(nextAirport, edge);
                    queue.add(nextAirport);
                }
            }
        }

        Airport step = toAirport;
        while (previousEdges.containsKey(step)) {
            route.add(0, previousEdges.get(step));
            step = previousEdges.get(step).getOriginNode();
        }

        return route;
    }

    private double calculateLegFlightTime(Edge leg, Airplane selectedAirplane) {
        double distanceInKm = leg.getDistance();
        double airspeed = selectedAirplane.getAirspeed();
        double timeInHours = (distanceInKm / KNOT_CONVERSION) / airspeed;
        return timeInHours;

    } // End of method calculateLegFlightTime

    private String displayFlightPlan(ArrayList<Edge> flightLegs, Airplane selectedAirplane) {
        StringBuilder flightPlan = new StringBuilder();
        flightPlan.append("Flight Plan:\n\n");

        for (int i = 0; i < flightLegs.size(); i++) {
            Edge leg = flightLegs.get(i);
            String from = leg.getOriginNode().getName();
            String to = leg.getDestinationNode().getName();
            double distance = leg.getDistance();
            double heading = leg.getHeading();
            double time = calculateLegFlightTime(leg, selectedAirplane);

            flightPlan.append((i + 1) + ". " + from + " to " + to + "\n" +
                    "   Distance: " + String.format("%.2f km", distance) + "\n" +
                    "   Heading: " + String.format("%.1f°", heading) + "\n" +
                    "   Time Taken: " + String.format("%.2f hours", time) + "\n" + LINE_SEPARATOR + "\n");
        }
        return flightPlan.toString();
    } // End of method displayFlightPlan
}
