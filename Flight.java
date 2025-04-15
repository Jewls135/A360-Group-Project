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

        ArrayList<Edge> allFlightLegs = new ArrayList<>();

        for (int i = 0; i < destinationAirports.size() - 1; i++) {
            ArrayList<Edge> currentLegs = findRoute(destinationAirports.get(i), destinationAirports.get(i + 1),
                    selectedPlane);

            if (currentLegs == null) { // Should only return null above when the destination/origin are the same so return early
                return;
            }

            if (currentLegs.isEmpty()) {
                System.out.println("Flight plan not possible between " +
                        destinationAirports.get(i).getName() + " and " +
                        destinationAirports.get(i + 1).getName());
                return;
            }

            allFlightLegs.addAll(currentLegs);
        }

        System.out.println(displayFlightPlan(allFlightLegs, selectedPlane));
    }

    private ArrayList<Edge> findRoute(Airport fromAirport, Airport toAirport, Airplane selectedAirplane) {

        if (fromAirport.equals(toAirport)) {
            System.out.println("Origin and destination are the same, routing is not needed!");
            return null;
        }

        ArrayList<Edge> route = new ArrayList<>();
        HashMap<Airport, Double> distanceMap = new HashMap<>();
        PriorityQueue<Airport> queue = new PriorityQueue<>(
                Comparator.comparingDouble(a -> distanceMap.getOrDefault(a, Double.MAX_VALUE)));
        HashMap<Airport, Edge> previousEdges = new HashMap<>();
        HashMap<Airport, Double> fuelRemaining = new HashMap<>();

        distanceMap.put(fromAirport, 0.0);
        fuelRemaining.put(fromAirport, selectedAirplane.getTankSize());
        queue.add(fromAirport);

        System.out.println("Starting route search from " + fromAirport.getName() + " to " + toAirport.getName());
        System.out.println("Selected airplane: " + selectedAirplane.displayInfo() + "\n");

        while (!queue.isEmpty()) {
            Airport current = queue.poll();

            if (current.equals(toAirport)) {
                break;
            }

            for (Edge edge : airportsGraph.getListings().getOrDefault(current, new ArrayList<>())) {
                Airport nextAirport = edge.getDestinationNode();
                double legDistance = edge.getDistance() * KNOT_CONVERSION;

                double airspeed = selectedAirplane.getAirspeed(); // in km/h
                double fuelBurnRate = selectedAirplane.getFuelBurnRate(); // in gallons/hour

                double flightTimeHours = legDistance / airspeed;
                double fuelRequired = flightTimeHours * fuelBurnRate;

                double currentFuel = fuelRemaining.getOrDefault(current, 0.0);

                String requiredFuel = (selectedAirplane.getType() == 3) ? "AVGAS" : "JA-a";
                boolean canRefuel = Arrays.asList(nextAirport.getFuelTypes()).contains(requiredFuel);

                double usableFuel = currentFuel;

                if (usableFuel < fuelRequired) {
                    if (!canRefuel) {
                        continue;
                    }
                    usableFuel = selectedAirplane.getTankSize();
                    if (usableFuel < fuelRequired) {
                        continue;
                    }
                }

                double remainingFuelAfterLeg = usableFuel - fuelRequired;
                double newFuelLevel = canRefuel ? selectedAirplane.getTankSize() : remainingFuelAfterLeg;
                double newDistance = distanceMap.get(current) + legDistance;

                if (newDistance < distanceMap.getOrDefault(nextAirport, Double.MAX_VALUE)) {
                    distanceMap.put(nextAirport, newDistance);
                    fuelRemaining.put(nextAirport, newFuelLevel);
                    previousEdges.put(nextAirport, edge);
                    queue.add(nextAirport);

                }
            }
        }

        Airport step = toAirport;
        while (previousEdges.containsKey(step)) {
            Edge edge = previousEdges.get(step);
            route.add(0, edge);
            step = edge.getOriginNode();
        }

        if (route.isEmpty()) {
            System.out.println("No route found.");
        } else {
            System.out.println("Route successfully found.");
        }

        return route;
    }

    private double calculateLegFlightTime(Edge leg, Airplane selectedAirplane) {
        double distanceInKnots = leg.getDistance();
        double distanceInKm = distanceInKnots * KNOT_CONVERSION;
        double airspeedInKmh = selectedAirplane.getAirspeed();
        double timeInHours = distanceInKm / airspeedInKmh;
        return timeInHours;
    }

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
                    "   Distance: " + String.format("%.2f Knots", distance) + "\n" +
                    "   Heading: " + String.format("%.1f°", heading) + "\n" +
                    "   Time Taken: " + String.format("%.2f Hours", time) + "\n" + LINE_SEPARATOR + "\n");
        }
        return flightPlan.toString();
    } // End of method displayFlightPlan
}
